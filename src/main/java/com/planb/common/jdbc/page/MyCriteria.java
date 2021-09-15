/*
 * Copyright 2019-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.planb.common.jdbc.page;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.data.relational.core.query.CriteriaDefinition.Combinator;
import org.springframework.data.relational.core.query.CriteriaDefinition.Comparator;
import org.springframework.data.relational.core.sql.IdentifierProcessing;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.data.util.Pair;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Central class for creating queries. It follows a fluent API style so that you can easily chain together multiple
 * criteria. Static import of the {@code Criteria.property(…)} method will improve readability as in
 * {@code where(property(…).is(…)}.
 * <p>
 * The Criteria API supports composition with a {@link #empty() NULL object} and a {@link #from(List) static factory
 * method}. Example usage:
 *
 * <pre class="code">
 * Criteria.from(Criteria.where("name").is("Foo"), Criteria.from(Criteria.where("age").greaterThan(42)));
 * </pre>
 *
 * rendering:
 *
 * <pre class="code">
 * WHERE name = 'Foo' AND age > 42
 * </pre>
 *
 * @author Mark Paluch
 * @author Oliver Drotbohm
 * @author Roman Chigvintsev
 * @author Jens Schauder
 * @since 2.0
 */
public class MyCriteria implements CriteriaDefinition {
	
	// dengxz
	private Map<String, Object> paramMap = new HashMap<String, Object>();

	public Map<String, Object> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<String, Object> paramMap) {
		this.paramMap = paramMap;
	}
	
	private boolean beginWhere = true;

	public boolean getBeginWhere() {
		return beginWhere;
	}

	public void setBeginWhere(boolean beginWhere) {
		this.beginWhere = beginWhere;
	}
	
	public boolean isBeginWhere() {
    	MyCriteria pre = this.getPrevious();
    	boolean isBegin = this.getBeginWhere();
    	while (pre != null) {
    		isBegin = pre.isBeginWhere();
    		pre = pre.getPrevious();
    	}
    	return isBegin;
    }

	static final MyCriteria EMPTY = new MyCriteria(SqlIdentifier.EMPTY, Comparator.INITIAL, null);

	private final @Nullable MyCriteria previous;
	private final Combinator combinator;
	private final List<CriteriaDefinition> group;

	private final @Nullable SqlIdentifier column;
	private final @Nullable Comparator comparator;
	private final @Nullable Object value;
	private final boolean ignoreCase;

	private MyCriteria(SqlIdentifier column, Comparator comparator, @Nullable Object value) {
		this(null, Combinator.INITIAL, Collections.emptyList(), column, comparator, value, false);
	}

	private MyCriteria(@Nullable MyCriteria previous, Combinator combinator, List<CriteriaDefinition> group,
			@Nullable SqlIdentifier column, @Nullable Comparator comparator, @Nullable Object value) {
		this(previous, combinator, group, column, comparator, value, false);
	}

	private MyCriteria(@Nullable MyCriteria previous, Combinator combinator, List<CriteriaDefinition> group,
			@Nullable SqlIdentifier column, @Nullable Comparator comparator, @Nullable Object value, boolean ignoreCase) {

		this.previous = previous;
		this.combinator = previous != null && previous.isEmpty() ? Combinator.INITIAL : combinator;
		this.group = group;
		this.column = column;
		this.comparator = comparator;
		this.value = value;
		this.ignoreCase = ignoreCase;
	}

	private MyCriteria(@Nullable MyCriteria previous, Combinator combinator, List<CriteriaDefinition> group) {

		this.previous = previous;
		this.combinator = previous != null && previous.isEmpty() ? Combinator.INITIAL : combinator;
		this.group = group;
		this.column = null;
		this.comparator = null;
		this.value = null;
		this.ignoreCase = false;
	}

	/**
	 * Static factory method to create an empty Criteria.
	 *
	 * @return an empty {@link MyCriteria}.
	 */
	public static MyCriteria empty() {
		MyCriteria myCriteria = EMPTY;
		myCriteria.setBeginWhere(false);
		return myCriteria;
	}

	/**
	 * Create a new {@link MyCriteria} and combine it as group with {@code AND} using the provided {@link List Criterias}.
	 *
	 * @return new {@link MyCriteria}.
	 */
	public static MyCriteria from(MyCriteria... criteria) {

		Assert.notNull(criteria, "Criteria must not be null");
		Assert.noNullElements(criteria, "Criteria must not contain null elements");

		return from(Arrays.asList(criteria));
	}

	/**
	 * Create a new {@link MyCriteria} and combine it as group with {@code AND} using the provided {@link List Criterias}.
	 *
	 * @return new {@link MyCriteria}.
	 */
	public static MyCriteria from(List<MyCriteria> criteria) {

		Assert.notNull(criteria, "Criteria must not be null");
		Assert.noNullElements(criteria, "Criteria must not contain null elements");

		if (criteria.isEmpty()) {
			return EMPTY;
		}

		if (criteria.size() == 1) {
			return criteria.get(0);
		}

		return EMPTY.and(criteria);
	}

	/**
	 * Static factory method to create a Criteria using the provided {@code column} name.
	 *
	 * @param column Must not be {@literal null} or empty.
	 * @return a new {@link CriteriaStep} object to complete the first {@link MyCriteria}.
	 */
	public static CriteriaStep where(String column) {

		Assert.hasText(column, "Column name must not be null or empty!");

		CriteriaStep criteriaStep = new DefaultCriteriaStep(SqlIdentifier.unquoted(column));
		
		
		return criteriaStep;
	}

	/**
	 * Create a new {@link MyCriteria} and combine it with {@code AND} using the provided {@code column} name.
	 *
	 * @param column Must not be {@literal null} or empty.
	 * @return a new {@link CriteriaStep} object to complete the next {@link MyCriteria}.
	 */
	public CriteriaStep and(String column) {

		Assert.hasText(column, "Column name must not be null or empty!");

		SqlIdentifier identifier = SqlIdentifier.unquoted(column);
		return new DefaultCriteriaStep(identifier) {
			@Override
			protected MyCriteria createCriteria(Comparator comparator, @Nullable Object value) {
				return new MyCriteria(MyCriteria.this, Combinator.AND, Collections.emptyList(), identifier, comparator, value);
			}
		};
	}

	/**
	 * Create a new {@link MyCriteria} and combine it as group with {@code AND} using the provided {@link MyCriteria} group.
	 *
	 * @param criteria criteria object.
	 * @return a new {@link MyCriteria} object.
	 * @since 1.1
	 */
	public MyCriteria and(CriteriaDefinition criteria) {

		Assert.notNull(criteria, "Criteria must not be null!");

		return and(Collections.singletonList(criteria));
	}

	/**
	 * Create a new {@link MyCriteria} and combine it as group with {@code AND} using the provided {@link MyCriteria} group.
	 *
	 * @param criteria criteria objects.
	 * @return a new {@link MyCriteria} object.
	 */
	@SuppressWarnings("unchecked")
	public MyCriteria and(List<? extends CriteriaDefinition> criteria) {

		Assert.notNull(criteria, "Criteria must not be null!");

		return new MyCriteria(MyCriteria.this, Combinator.AND, (List<CriteriaDefinition>) criteria);
	}

	/**
	 * Create a new {@link MyCriteria} and combine it with {@code OR} using the provided {@code column} name.
	 *
	 * @param column Must not be {@literal null} or empty.
	 * @return a new {@link CriteriaStep} object to complete the next {@link MyCriteria}.
	 */
	public CriteriaStep or(String column) {

		Assert.hasText(column, "Column name must not be null or empty!");

		SqlIdentifier identifier = SqlIdentifier.unquoted(column);
		return new DefaultCriteriaStep(identifier) {
			@Override
			protected MyCriteria createCriteria(Comparator comparator, @Nullable Object value) {
				return new MyCriteria(MyCriteria.this, Combinator.OR, Collections.emptyList(), identifier, comparator, value);
			}
		};
	}

	/**
	 * Create a new {@link MyCriteria} and combine it as group with {@code OR} using the provided {@link MyCriteria} group.
	 *
	 * @param criteria criteria object.
	 * @return a new {@link MyCriteria} object.
	 * @since 1.1
	 */
	public MyCriteria or(CriteriaDefinition criteria) {

		Assert.notNull(criteria, "Criteria must not be null!");

		return or(Collections.singletonList(criteria));
	}

	/**
	 * Create a new {@link MyCriteria} and combine it as group with {@code OR} using the provided {@link MyCriteria} group.
	 *
	 * @param criteria criteria object.
	 * @return a new {@link MyCriteria} object.
	 * @since 1.1
	 */
	@SuppressWarnings("unchecked")
	public MyCriteria or(List<? extends CriteriaDefinition> criteria) {

		Assert.notNull(criteria, "Criteria must not be null!");

		return new MyCriteria(MyCriteria.this, Combinator.OR, (List<CriteriaDefinition>) criteria);
	}

	/**
	 * Creates a new {@link MyCriteria} with the given "ignore case" flag.
	 *
	 * @param ignoreCase {@literal true} if comparison should be done in case-insensitive way
	 * @return a new {@link MyCriteria} object
	 */
	public MyCriteria ignoreCase(boolean ignoreCase) {
		if (this.ignoreCase != ignoreCase) {
			return new MyCriteria(previous, combinator, group, column, comparator, value, ignoreCase);
		}
		return this;
	}

	/**
	 * @return the previous {@link MyCriteria} object. Can be {@literal null} if there is no previous {@link MyCriteria}.
	 * @see #hasPrevious()
	 */
	@Nullable
	public MyCriteria getPrevious() {
		return previous;
	}

	/**
	 * @return {@literal true} if this {@link MyCriteria} has a previous one.
	 */
	public boolean hasPrevious() {
		return previous != null;
	}

	/**
	 * @return {@literal true} if this {@link MyCriteria} is empty.
	 * @since 1.1
	 */
	@Override
	public boolean isEmpty() {

		if (!doIsEmpty()) {
			return false;
		}

		MyCriteria parent = this.previous;

		while (parent != null) {

			if (!parent.doIsEmpty()) {
				return false;
			}

			parent = parent.previous;
		}

		return true;
	}

	private boolean doIsEmpty() {

		if (this.comparator == Comparator.INITIAL) {
			return true;
		}

		if (this.column != null) {
			return false;
		}

		for (CriteriaDefinition criteria : group) {

			if (!criteria.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * @return {@literal true} if this {@link MyCriteria} is empty.
	 */
	public boolean isGroup() {
		return !this.group.isEmpty();
	}

	/**
	 * @return {@link Combinator} to combine this criteria with a previous one.
	 */
	public Combinator getCombinator() {
		return combinator;
	}

	@Override
	public List<CriteriaDefinition> getGroup() {
		return group;
	}

	/**
	 * @return the column/property name.
	 */
	@Nullable
	public SqlIdentifier getColumn() {
		return column;
	}

	/**
	 * @return {@link Comparator}.
	 */
	@Nullable
	public Comparator getComparator() {
		return comparator;
	}

	/**
	 * @return the comparison value. Can be {@literal null}.
	 */
	@Nullable
	public Object getValue() {
		return value;
	}

	/**
	 * Checks whether comparison should be done in case-insensitive way.
	 *
	 * @return {@literal true} if comparison should be done in case-insensitive way
	 */
	@Override
	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	@Override
	public String toString() {

		if (isEmpty()) {
			return "";
		}

		StringBuilder builder = new StringBuilder();
		unroll(this, builder);

		return builder.toString();
	}

	private void unroll(CriteriaDefinition criteria, StringBuilder stringBuilder) {

		CriteriaDefinition current = criteria;

		// reverse unroll criteria chain
		Map<CriteriaDefinition, CriteriaDefinition> forwardChain = new HashMap<>();

		while (current.hasPrevious()) {
			forwardChain.put(current.getPrevious(), current);
			current = current.getPrevious();
		}

		// perform the actual mapping
		render(current, stringBuilder);
		while (forwardChain.containsKey(current)) {
			
			CriteriaDefinition criterion = forwardChain.get(current);

			if (criterion.getCombinator() != Combinator.INITIAL) {
				if (criterion.getValue() != null) {
					stringBuilder.append(' ').append(criterion.getCombinator().name()).append(' ');
				}
			}
			render(criterion, stringBuilder);

			current = criterion;
		}
	}

	private void unrollGroup(List<? extends CriteriaDefinition> criteria, StringBuilder stringBuilder) {

		stringBuilder.append("(");

		boolean first = true;
		for (CriteriaDefinition criterion : criteria) {

			if (criterion.isEmpty()) {
				continue;
			}

			if (!first) {
				Combinator combinator = criterion.getCombinator() == Combinator.INITIAL ? Combinator.AND
						: criterion.getCombinator();
				stringBuilder.append(' ').append(combinator.name()).append(' ');
			}

			unroll(criterion, stringBuilder);
			first = false;
		}

		stringBuilder.append(")");
	}

	private void render(CriteriaDefinition criteria, StringBuilder stringBuilder) {
		if (criteria.getValue() == null) {
			return;
		}
		// dengxz
		String colName = criteria.getColumn().toSql(IdentifierProcessing.NONE);
		paramMap.put(colName, criteria.getValue());
		

		if (criteria.isEmpty()) {
			return;
		}

		if (criteria.isGroup()) {
			unrollGroup(criteria.getGroup(), stringBuilder);
			return;
		}
		
		
		stringBuilder.append(criteria.getColumn().toSql(IdentifierProcessing.NONE)).append(' ')
				.append(criteria.getComparator().getComparator());

		switch (criteria.getComparator()) {
			case BETWEEN:
			case NOT_BETWEEN:
				Pair<Object, Object> pair = (Pair<Object, Object>) criteria.getValue();
				stringBuilder.append(' ').append(pair.getFirst()).append(" AND ").append(pair.getSecond());
				break;

			case IS_NULL:
			case IS_NOT_NULL:
			case IS_TRUE:
			case IS_FALSE:
				break;

			case IN:
			case NOT_IN:
				// stringBuilder.append(" (").append(renderValue(criteria.getValue())).append(')');
				stringBuilder.append(" (").append(":" + colName).append(')');
				break;

			default:
				// stringBuilder.append(' ').append(renderValue(criteria.getValue()));
				stringBuilder.append(' ').append(":" + colName);
		}
	}

	private static String renderValue(@Nullable Object value) {
		return "?";

//		if (value instanceof Number) {
//			return value.toString();
//		}
//
//		if (value instanceof Collection) {
//
//			StringJoiner joiner = new StringJoiner(", ");
//			((Collection<?>) value).forEach(o -> joiner.add(renderValue(o)));
//			return joiner.toString();
//		}
//
//		if (value != null) {
//			return String.format("'%s'", value);
//		}
//
//		return "null";
	}

	/**
	 * Interface declaring terminal builder methods to build a {@link MyCriteria}.
	 */
	public interface CriteriaStep {

		/**
		 * Creates a {@link MyCriteria} using equality.
		 *
		 * @param value must not be {@literal null}.
		 */
		MyCriteria is(Object value);

		/**
		 * Creates a {@link MyCriteria} using equality (is not).
		 *
		 * @param value must not be {@literal null}.
		 */
		MyCriteria not(Object value);

		/**
		 * Creates a {@link MyCriteria} using {@code IN}.
		 *
		 * @param values must not be {@literal null}.
		 */
		MyCriteria in(Object... values);

		/**
		 * Creates a {@link MyCriteria} using {@code IN}.
		 *
		 * @param values must not be {@literal null}.
		 */
		MyCriteria in(Collection<?> values);

		/**
		 * Creates a {@link MyCriteria} using {@code NOT IN}.
		 *
		 * @param values must not be {@literal null}.
		 */
		MyCriteria notIn(Object... values);

		/**
		 * Creates a {@link MyCriteria} using {@code NOT IN}.
		 *
		 * @param values must not be {@literal null}.
		 */
		MyCriteria notIn(Collection<?> values);

		/**
		 * Creates a {@link MyCriteria} using between ({@literal BETWEEN begin AND end}).
		 *
		 * @param begin must not be {@literal null}.
		 * @param end must not be {@literal null}.
		 * @since 2.2
		 */
		MyCriteria between(Object begin, Object end);

		/**
		 * Creates a {@link MyCriteria} using not between ({@literal NOT BETWEEN begin AND end}).
		 *
		 * @param begin must not be {@literal null}.
		 * @param end must not be {@literal null}.
		 * @since 2.2
		 */
		MyCriteria notBetween(Object begin, Object end);

		/**
		 * Creates a {@link MyCriteria} using less-than ({@literal <}).
		 *
		 * @param value must not be {@literal null}.
		 */
		MyCriteria lessThan(Object value);

		/**
		 * Creates a {@link MyCriteria} using less-than or equal to ({@literal <=}).
		 *
		 * @param value must not be {@literal null}.
		 */
		MyCriteria lessThanOrEquals(Object value);

		/**
		 * Creates a {@link MyCriteria} using greater-than({@literal >}).
		 *
		 * @param value must not be {@literal null}.
		 */
		MyCriteria greaterThan(Object value);

		/**
		 * Creates a {@link MyCriteria} using greater-than or equal to ({@literal >=}).
		 *
		 * @param value must not be {@literal null}.
		 */
		MyCriteria greaterThanOrEquals(Object value);

		/**
		 * Creates a {@link MyCriteria} using {@code LIKE}.
		 *
		 * @param value must not be {@literal null}.
		 */
		MyCriteria like(Object value);

		/**
		 * Creates a {@link MyCriteria} using {@code NOT LIKE}.
		 *
		 * @param value must not be {@literal null}
		 * @return a new {@link MyCriteria} object
		 */
		MyCriteria notLike(Object value);

		/**
		 * Creates a {@link MyCriteria} using {@code IS NULL}.
		 */
		MyCriteria isNull();

		/**
		 * Creates a {@link MyCriteria} using {@code IS NOT NULL}.
		 */
		MyCriteria isNotNull();

		/**
		 * Creates a {@link MyCriteria} using {@code IS TRUE}.
		 *
		 * @return a new {@link MyCriteria} object
		 */
		MyCriteria isTrue();

		/**
		 * Creates a {@link MyCriteria} using {@code IS FALSE}.
		 *
		 * @return a new {@link MyCriteria} object
		 */
		MyCriteria isFalse();
	}

	/**
	 * Default {@link CriteriaStep} implementation.
	 */
	static class DefaultCriteriaStep implements CriteriaStep {

		private final SqlIdentifier property;

		DefaultCriteriaStep(SqlIdentifier property) {
			this.property = property;
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.relational.query.Criteria.CriteriaStep#is(java.lang.Object)
		 */
		@Override
		public MyCriteria is(Object value) {
			return createCriteria(Comparator.EQ, value);
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.relational.query.Criteria.CriteriaStep#not(java.lang.Object)
		 */
		@Override
		public MyCriteria not(Object value) {

			Assert.notNull(value, "Value must not be null!");

			return createCriteria(Comparator.NEQ, value);
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.relational.query.Criteria.CriteriaStep#in(java.lang.Object[])
		 */
		@Override
		public MyCriteria in(Object... values) {

			Assert.notNull(values, "Values must not be null!");
			Assert.noNullElements(values, "Values must not contain a null value!");

			if (values.length > 1 && values[1] instanceof Collection) {
				throw new InvalidDataAccessApiUsageException(
						"You can only pass in one argument of type " + values[1].getClass().getName());
			}

			return createCriteria(Comparator.IN, Arrays.asList(values));
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.relational.query.Criteria.CriteriaStep#in(java.util.Collection)
		 */
		@Override
		public MyCriteria in(Collection<?> values) {

			Assert.notNull(values, "Values must not be null!");
			Assert.noNullElements(values.toArray(), "Values must not contain a null value!");

			return createCriteria(Comparator.IN, values);
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.relational.query.Criteria.CriteriaStep#notIn(java.lang.Object[])
		 */
		@Override
		public MyCriteria notIn(Object... values) {

			Assert.notNull(values, "Values must not be null!");
			Assert.noNullElements(values, "Values must not contain a null value!");

			if (values.length > 1 && values[1] instanceof Collection) {
				throw new InvalidDataAccessApiUsageException(
						"You can only pass in one argument of type " + values[1].getClass().getName());
			}

			return createCriteria(Comparator.NOT_IN, Arrays.asList(values));
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.relational.query.Criteria.CriteriaStep#notIn(java.util.Collection)
		 */
		@Override
		public MyCriteria notIn(Collection<?> values) {

			Assert.notNull(values, "Values must not be null!");
			Assert.noNullElements(values.toArray(), "Values must not contain a null value!");

			return createCriteria(Comparator.NOT_IN, values);
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.relational.query.Criteria.CriteriaStep#between(java.lang.Object, java.lang.Object)
		 */
		@Override
		public MyCriteria between(Object begin, Object end) {

			Assert.notNull(begin, "Begin value must not be null!");
			Assert.notNull(end, "End value must not be null!");

			return createCriteria(Comparator.BETWEEN, Pair.of(begin, end));
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.relational.query.Criteria.CriteriaStep#notBetween(java.lang.Object, java.lang.Object)
		 */
		@Override
		public MyCriteria notBetween(Object begin, Object end) {

			Assert.notNull(begin, "Begin value must not be null!");
			Assert.notNull(end, "End value must not be null!");

			return createCriteria(Comparator.NOT_BETWEEN, Pair.of(begin, end));
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.relational.query.Criteria.CriteriaStep#lessThan(java.lang.Object)
		 */
		@Override
		public MyCriteria lessThan(Object value) {

			Assert.notNull(value, "Value must not be null!");

			return createCriteria(Comparator.LT, value);
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.relational.query.Criteria.CriteriaStep#lessThanOrEquals(java.lang.Object)
		 */
		@Override
		public MyCriteria lessThanOrEquals(Object value) {

			Assert.notNull(value, "Value must not be null!");

			return createCriteria(Comparator.LTE, value);
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.relational.query.Criteria.CriteriaStep#greaterThan(java.lang.Object)
		 */
		@Override
		public MyCriteria greaterThan(Object value) {

			Assert.notNull(value, "Value must not be null!");

			return createCriteria(Comparator.GT, value);
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.relational.query.Criteria.CriteriaStep#greaterThanOrEquals(java.lang.Object)
		 */
		@Override
		public MyCriteria greaterThanOrEquals(Object value) {

			Assert.notNull(value, "Value must not be null!");

			return createCriteria(Comparator.GTE, value);
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.relational.query.Criteria.CriteriaStep#like(java.lang.Object)
		 */
		@Override
		public MyCriteria like(Object value) {
			if (value == null) {
				return createCriteria(Comparator.LIKE, value);
			}

			Assert.notNull(value, "Value must not be null!");

			return createCriteria(Comparator.LIKE, value);
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.relational.query.Criteria.CriteriaStep#notLike(java.lang.Object)
		 */
		@Override
		public MyCriteria notLike(Object value) {
			Assert.notNull(value, "Value must not be null!");
			return createCriteria(Comparator.NOT_LIKE, value);
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.relational.query.Criteria.CriteriaStep#isNull()
		 */
		@Override
		public MyCriteria isNull() {
			return createCriteria(Comparator.IS_NULL, null);
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.relational.query.Criteria.CriteriaStep#isNotNull()
		 */
		@Override
		public MyCriteria isNotNull() {
			return createCriteria(Comparator.IS_NOT_NULL, null);
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.relational.query.Criteria.CriteriaStep#isTrue()
		 */
		@Override
		public MyCriteria isTrue() {
			return createCriteria(Comparator.IS_TRUE, null);
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.relational.query.Criteria.CriteriaStep#isFalse()
		 */
		@Override
		public MyCriteria isFalse() {
			return createCriteria(Comparator.IS_FALSE, null);
		}

		protected MyCriteria createCriteria(Comparator comparator, @Nullable Object value) {
			return new MyCriteria(this.property, comparator, value);
		}
	}
}
