//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package org.springframework.data.jdbc.core.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mapping.PersistentPropertyPath;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.dialect.RenderContextFactory;
import org.springframework.data.relational.core.mapping.PersistentPropertyPathExtension;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.data.relational.core.sql.AssignValue;
import org.springframework.data.relational.core.sql.Assignments;
import org.springframework.data.relational.core.sql.BindMarker;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Delete;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Expressions;
import org.springframework.data.relational.core.sql.Functions;
import org.springframework.data.relational.core.sql.Insert;
import org.springframework.data.relational.core.sql.LockMode;
import org.springframework.data.relational.core.sql.OrderByField;
import org.springframework.data.relational.core.sql.SQL;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.data.relational.core.sql.StatementBuilder;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.core.sql.Update;
import org.springframework.data.relational.core.sql.DeleteBuilder.DeleteWhere;
import org.springframework.data.relational.core.sql.DeleteBuilder.DeleteWhereAndOr;
import org.springframework.data.relational.core.sql.InsertBuilder.InsertIntoColumnsAndValuesWithBuild;
import org.springframework.data.relational.core.sql.InsertBuilder.InsertValues;
import org.springframework.data.relational.core.sql.InsertBuilder.InsertValuesWithBuild;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectAndFrom;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectJoin;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectLimitOffset;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectOrdered;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectWhere;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectWhereAndOr;
import org.springframework.data.relational.core.sql.UpdateBuilder.UpdateWhereAndOr;
import org.springframework.data.relational.core.sql.render.RenderContext;
import org.springframework.data.relational.core.sql.render.SqlRenderer;
import org.springframework.data.util.Lazy;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

class SqlGenerator {
    static final SqlIdentifier VERSION_SQL_PARAMETER = SqlIdentifier.unquoted("___oldOptimisticLockingVersion");
    static final SqlIdentifier ID_SQL_PARAMETER = SqlIdentifier.unquoted("id");
    static final SqlIdentifier IDS_SQL_PARAMETER = SqlIdentifier.unquoted("ids");
    static final SqlIdentifier ROOT_ID_PARAMETER = SqlIdentifier.unquoted("rootId");
    private static final Pattern parameterPattern = Pattern.compile("\\W");
    private final RelationalPersistentEntity<?> entity;
    private final MappingContext<RelationalPersistentEntity<?>, RelationalPersistentProperty> mappingContext;
    private final RenderContext renderContext;
    private final SqlContext sqlContext;
    private final SqlRenderer sqlRenderer;
    private final SqlGenerator.Columns columns;
    private final Lazy<String> findOneSql = Lazy.of(this::createFindOneSql);
    private final Lazy<String> findAllSql = Lazy.of(this::createFindAllSql);
    private final Lazy<String> findAllInListSql = Lazy.of(this::createFindAllInListSql);
    private final Lazy<String> existsSql = Lazy.of(this::createExistsSql);
    private final Lazy<String> countSql = Lazy.of(this::createCountSql);
    private final Lazy<String> updateSql = Lazy.of(this::createUpdateSql);
    private final Lazy<String> updateWithVersionSql = Lazy.of(this::createUpdateWithVersionSql);
    private final Lazy<String> deleteByIdSql = Lazy.of(this::createDeleteSql);
    private final Lazy<String> deleteByIdAndVersionSql = Lazy.of(this::createDeleteByIdAndVersionSql);
    private final Lazy<String> deleteByListSql = Lazy.of(this::createDeleteByListSql);

    SqlGenerator(RelationalMappingContext mappingContext, JdbcConverter converter, RelationalPersistentEntity<?> entity, Dialect dialect) {
        this.mappingContext = mappingContext;
        this.entity = entity;
        this.sqlContext = new SqlContext(entity);
        this.sqlRenderer = SqlRenderer.create((new RenderContextFactory(dialect)).createRenderContext());
        this.columns = new SqlGenerator.Columns(entity, mappingContext, converter);
        this.renderContext = (new RenderContextFactory(dialect)).createRenderContext();
    }

    private Condition getSubselectCondition(PersistentPropertyPathExtension path, Function<Column, Condition> rootCondition, Column filterColumn) {
        PersistentPropertyPathExtension parentPath = path.getParentPath();
        if (!parentPath.hasIdProperty()) {
            return parentPath.getLength() > 1 ? this.getSubselectCondition(parentPath, rootCondition, filterColumn) : (Condition)rootCondition.apply(filterColumn);
        } else {
            Table subSelectTable = Table.create(parentPath.getTableName());
            Column idColumn = subSelectTable.column(parentPath.getIdColumnName());
            Column selectFilterColumn = subSelectTable.column(parentPath.getEffectiveIdColumnName());
            Condition innerCondition;
            if (parentPath.getLength() == 1) {
                innerCondition = (Condition)rootCondition.apply(selectFilterColumn);
            } else {
                innerCondition = this.getSubselectCondition(parentPath, rootCondition, selectFilterColumn);
            }

            Select select = Select.builder().select(idColumn).from(subSelectTable).where(innerCondition).build();
            return filterColumn.in(select);
        }
    }

    private BindMarker getBindMarker(SqlIdentifier columnName) {
        return SQL.bindMarker(":" + parameterPattern.matcher(this.renderReference(columnName)).replaceAll(""));
    }

    String getFindAllInList() {
        return (String)this.findAllInListSql.get();
    }

    String getFindAll() {
        return (String)this.findAllSql.get();
    }

    String getFindAll(Sort sort) {
        return this.render(this.selectBuilder(Collections.emptyList(), sort, Pageable.unpaged()).build());
    }

    String getFindAll(Pageable pageable) {
        return this.render(this.selectBuilder(Collections.emptyList(), pageable.getSort(), pageable).build());
    }

    String getFindAllByProperty(Identifier parentIdentifier, @Nullable SqlIdentifier keyColumn, boolean ordered) {
        Assert.isTrue(keyColumn != null || !ordered, "If the SQL statement should be ordered a keyColumn to order by must be provided.");
        Table table = this.getTable();
        SelectWhere builder = this.selectBuilder((Collection)(keyColumn == null ? Collections.emptyList() : Collections.singleton(keyColumn)));
        Condition condition = this.buildConditionForBackReference(parentIdentifier, table);
        SelectWhereAndOr withWhereClause = builder.where(condition);
        Select select = ordered ? withWhereClause.orderBy(new Column[]{table.column(keyColumn).as(keyColumn)}).build() : withWhereClause.build();
        return this.render(select);
    }

    private Condition buildConditionForBackReference(Identifier parentIdentifier, Table table) {
        Condition condition = null;

        Comparison newCondition;
        for(Iterator var4 = parentIdentifier.toMap().keySet().iterator(); var4.hasNext(); condition = condition == null ? newCondition : ((Condition)condition).and(newCondition)) {
            SqlIdentifier backReferenceColumn = (SqlIdentifier)var4.next();
            newCondition = table.column(backReferenceColumn).isEqualTo(this.getBindMarker(backReferenceColumn));
        }

        Assert.state(condition != null, "We need at least one condition");
        return (Condition)condition;
    }

    String getExists() {
        return (String)this.existsSql.get();
    }

    String getFindOne() {
        return (String)this.findOneSql.get();
    }

    String getAcquireLockById(LockMode lockMode) {
        return this.createAcquireLockById(lockMode);
    }

    String getAcquireLockAll(LockMode lockMode) {
        return this.createAcquireLockAll(lockMode);
    }

    String getInsert(Set<SqlIdentifier> additionalColumns, SqlIdentifierParameterSource parameterSource) {
        return this.createInsertSql(additionalColumns, parameterSource);
    }

    String getUpdate() {
        return (String)this.updateSql.get();
    }

    String getUpdateWithVersion() {
        return (String)this.updateWithVersionSql.get();
    }

    String getCount() {
        return (String)this.countSql.get();
    }

    String getDeleteById() {
        return (String)this.deleteByIdSql.get();
    }

    String getDeleteByIdAndVersion() {
        return (String)this.deleteByIdAndVersionSql.get();
    }

    String getDeleteByList() {
        return (String)this.deleteByListSql.get();
    }

    String createDeleteAllSql(@Nullable PersistentPropertyPath<RelationalPersistentProperty> path) {
        Table table = this.getTable();
        DeleteWhere deleteAll = Delete.builder().from(table);
        return path == null ? this.render(deleteAll.build()) : this.createDeleteByPathAndCriteria(new PersistentPropertyPathExtension(this.mappingContext, path), Column::isNotNull);
    }

    String createDeleteByPath(PersistentPropertyPath<RelationalPersistentProperty> path) {
        return this.createDeleteByPathAndCriteria(new PersistentPropertyPathExtension(this.mappingContext, path), (filterColumn) -> {
            return filterColumn.isEqualTo(this.getBindMarker(ROOT_ID_PARAMETER));
        });
    }

    private String createFindOneSql() {
        Select select = this.selectBuilder().where(this.getIdColumn().isEqualTo(this.getBindMarker(ID_SQL_PARAMETER))).build();
        return this.render(select);
    }

    private String createAcquireLockById(LockMode lockMode) {
        Table table = this.getTable();
        Select select = StatementBuilder.select(this.getIdColumn()).from(table).where(this.getIdColumn().isEqualTo(this.getBindMarker(ID_SQL_PARAMETER))).lock(lockMode).build();
        return this.render(select);
    }

    private String createAcquireLockAll(LockMode lockMode) {
        Table table = this.getTable();
        Select select = StatementBuilder.select(this.getIdColumn()).from(table).lock(lockMode).build();
        return this.render(select);
    }

    private String createFindAllSql() {
        return this.render(this.selectBuilder().build());
    }

    private SelectWhere selectBuilder() {
        return this.selectBuilder(Collections.emptyList());
    }

    private SelectWhere selectBuilder(Collection<SqlIdentifier> keyColumns) {
        Table table = this.getTable();
        List<Expression> columnExpressions = new ArrayList();
        List<SqlGenerator.Join> joinTables = new ArrayList();
        Iterator var5 = this.mappingContext.findPersistentPropertyPaths(this.entity.getType(), (p) -> {
            return true;
        }).iterator();

        SqlGenerator.Join join;
        while(var5.hasNext()) {
            PersistentPropertyPath<RelationalPersistentProperty> path = (PersistentPropertyPath)var5.next();
            PersistentPropertyPathExtension extPath = new PersistentPropertyPathExtension(this.mappingContext, path);
            join = this.getJoin(extPath);
            if (join != null) {
                joinTables.add(join);
            }

            Column column = this.getColumn(extPath);
            if (column != null) {
                columnExpressions.add(column);
            }
        }

        var5 = keyColumns.iterator();

        while(var5.hasNext()) {
            SqlIdentifier keyColumn = (SqlIdentifier)var5.next();
            columnExpressions.add(table.column(keyColumn).as(keyColumn));
        }

        SelectAndFrom selectBuilder = StatementBuilder.select(columnExpressions);
        SelectJoin baseSelect = selectBuilder.from(table);

        for(Iterator var13 = joinTables.iterator(); var13.hasNext(); baseSelect = ((SelectJoin)baseSelect).leftOuterJoin(join.joinTable).on(join.joinColumn).equals(join.parentId)) {
            join = (SqlGenerator.Join)var13.next();
        }

        return (SelectWhere)baseSelect;
    }

    private SelectOrdered selectBuilder(Collection<SqlIdentifier> keyColumns, Sort sort, Pageable pageable) {
        SelectOrdered sortable = this.selectBuilder(keyColumns);
        sortable = this.applyPagination(pageable, sortable);
        return sortable.orderBy(this.extractOrderByFields(sort));
    }

    private SelectOrdered applyPagination(Pageable pageable, SelectOrdered select) {
        if (!pageable.isPaged()) {
            return select;
        } else {
            Assert.isTrue(select instanceof SelectLimitOffset, () -> {
                return String.format("Can't apply limit clause to statement of type %s", select.getClass());
            });
            SelectLimitOffset limitable = (SelectLimitOffset)select;
            SelectLimitOffset limitResult = limitable.limitOffset((long)pageable.getPageSize(), pageable.getOffset());
            Assert.state(limitResult instanceof SelectOrdered, String.format("The result of applying the limit-clause must be of type SelectOrdered in order to apply the order-by-clause but is of type %s.", select.getClass()));
            return (SelectOrdered)limitResult;
        }
    }

    @Nullable
    Column getColumn(PersistentPropertyPathExtension path) {
        if (!path.isEmbedded() && !path.getParentPath().isMultiValued()) {
            if (path.isEntity()) {
                return !path.isQualified() && !path.isCollectionLike() && !path.hasIdProperty() ? this.sqlContext.getReverseColumn(path) : null;
            } else {
                return this.sqlContext.getColumn(path);
            }
        } else {
            return null;
        }
    }

    @Nullable
    SqlGenerator.Join getJoin(PersistentPropertyPathExtension path) {
        if (path.isEntity() && !path.isEmbedded() && !path.isMultiValued()) {
            Table currentTable = this.sqlContext.getTable(path);
            PersistentPropertyPathExtension idDefiningParentPath = path.getIdDefiningParentPath();
            Table parentTable = this.sqlContext.getTable(idDefiningParentPath);
            return new SqlGenerator.Join(currentTable, currentTable.column(path.getReverseColumnName()), parentTable.column(idDefiningParentPath.getIdColumnName()));
        } else {
            return null;
        }
    }

    private String createFindAllInListSql() {
        Select select = this.selectBuilder().where(this.getIdColumn().in(new Expression[]{this.getBindMarker(IDS_SQL_PARAMETER)})).build();
        return this.render(select);
    }

    private String createExistsSql() {
        Table table = this.getTable();
        Select select = StatementBuilder.select(Functions.count(new Expression[]{this.getIdColumn()})).from(table).where(this.getIdColumn().isEqualTo(this.getBindMarker(ID_SQL_PARAMETER))).build();
        return this.render(select);
    }

    private String createCountSql() {
        Table table = this.getTable();
        Select select = StatementBuilder.select(Functions.count(new Expression[]{Expressions.asterisk()})).from(table).build();
        return this.render(select);
    }

    private String createInsertSql(Set<SqlIdentifier> additionalColumns, SqlIdentifierParameterSource parameterSource) {
        Table table = this.getTable();
        Set<SqlIdentifier> columnNamesForInsert = new HashSet();
//        columnNamesForInsert.addAll(this.columns.getInsertableColumns());
        for (SqlIdentifier sqlIdentifier : this.columns.getInsertableColumns()) {
            if (parameterSource.getValue(sqlIdentifier.getReference()) != null) {
                columnNamesForInsert.add(sqlIdentifier);
            }
        }
        columnNamesForInsert.addAll(additionalColumns);
        InsertIntoColumnsAndValuesWithBuild insert = Insert.builder().into(table);

        SqlIdentifier cn;
        for(Iterator var5 = columnNamesForInsert.iterator(); var5.hasNext(); insert = insert.column(table.column(cn))) {
            cn = (SqlIdentifier)var5.next();
        }

        InsertValuesWithBuild insertWithValues = null;

        for(Iterator var9 = columnNamesForInsert.iterator(); var9.hasNext(); insertWithValues = ((InsertValues)(insertWithValues == null ? insert : insertWithValues)).values(new Expression[]{this.getBindMarker(cn)})) {
            cn = (SqlIdentifier)var9.next();
        }

        return this.render(insertWithValues == null ? insert.build() : insertWithValues.build());
    }

    private String createUpdateSql() {
        return this.render(this.createBaseUpdate().build());
    }

    private String createUpdateWithVersionSql() {
        Update update = this.createBaseUpdate().and(this.getVersionColumn().isEqualTo(SQL.bindMarker(":" + this.renderReference(VERSION_SQL_PARAMETER)))).build();
        return this.render(update);
    }

    private UpdateWhereAndOr createBaseUpdate() {
        Table table = this.getTable();
        List<AssignValue> assignments = (List)this.columns.getUpdateableColumns().stream().map((columnName) -> {
            return Assignments.value(table.column(columnName), this.getBindMarker(columnName));
        }).collect(Collectors.toList());
        return Update.builder().table(table).set(assignments).where(this.getIdColumn().isEqualTo(this.getBindMarker(this.entity.getIdColumn())));
    }

    private String createDeleteSql() {
        return this.render(this.createBaseDeleteById(this.getTable()).build());
    }

    private String createDeleteByIdAndVersionSql() {
        Delete delete = this.createBaseDeleteById(this.getTable()).and(this.getVersionColumn().isEqualTo(SQL.bindMarker(":" + this.renderReference(VERSION_SQL_PARAMETER)))).build();
        return this.render(delete);
    }

    private DeleteWhereAndOr createBaseDeleteById(Table table) {
        return Delete.builder().from(table).where(this.getIdColumn().isEqualTo(SQL.bindMarker(":" + this.renderReference(ID_SQL_PARAMETER))));
    }

    private String createDeleteByPathAndCriteria(PersistentPropertyPathExtension path, Function<Column, Condition> rootCondition) {
        Table table = Table.create(path.getTableName());
        DeleteWhere builder = Delete.builder().from(table);
        Column filterColumn = table.column(path.getReverseColumnName());
        Delete delete;
        if (path.getLength() == 1) {
            delete = builder.where((Condition)rootCondition.apply(filterColumn)).build();
        } else {
            Condition condition = this.getSubselectCondition(path, rootCondition, filterColumn);
            delete = builder.where(condition).build();
        }

        return this.render(delete);
    }

    private String createDeleteByListSql() {
        Table table = this.getTable();
        Delete delete = Delete.builder().from(table).where(this.getIdColumn().in(new Expression[]{this.getBindMarker(IDS_SQL_PARAMETER)})).build();
        return this.render(delete);
    }

    private String render(Select select) {
        return this.sqlRenderer.render(select);
    }

    private String render(Insert insert) {
        return this.sqlRenderer.render(insert);
    }

    private String render(Update update) {
        return this.sqlRenderer.render(update);
    }

    private String render(Delete delete) {
        return this.sqlRenderer.render(delete);
    }

    private Table getTable() {
        return this.sqlContext.getTable();
    }

    private Column getIdColumn() {
        return this.sqlContext.getIdColumn();
    }

    private Column getVersionColumn() {
        return this.sqlContext.getVersionColumn();
    }

    private String renderReference(SqlIdentifier identifier) {
        return identifier.getReference(this.renderContext.getIdentifierProcessing());
    }

    private List<OrderByField> extractOrderByFields(Sort sort) {
        return (List)sort.stream().map(this::orderToOrderByField).collect(Collectors.toList());
    }

    private OrderByField orderToOrderByField(Order order) {
        SqlIdentifier columnName = ((RelationalPersistentProperty)this.entity.getRequiredPersistentProperty(order.getProperty())).getColumnName();
        Column column = Column.create(columnName, this.getTable());
        return OrderByField.from(column, order.getDirection());
    }

    static class Columns {
        private final MappingContext<RelationalPersistentEntity<?>, RelationalPersistentProperty> mappingContext;
        private final JdbcConverter converter;
        private final List<SqlIdentifier> columnNames = new ArrayList();
        private final List<SqlIdentifier> idColumnNames = new ArrayList();
        private final List<SqlIdentifier> nonIdColumnNames = new ArrayList();
        private final Set<SqlIdentifier> readOnlyColumnNames = new HashSet();
        private final Set<SqlIdentifier> insertableColumns;
        private final Set<SqlIdentifier> updateableColumns;

        Columns(RelationalPersistentEntity<?> entity, MappingContext<RelationalPersistentEntity<?>, RelationalPersistentProperty> mappingContext, JdbcConverter converter) {
            this.mappingContext = mappingContext;
            this.converter = converter;
            this.populateColumnNameCache(entity, "");
            Set<SqlIdentifier> insertable = new LinkedHashSet(this.nonIdColumnNames);
            insertable.removeAll(this.readOnlyColumnNames);
            this.insertableColumns = Collections.unmodifiableSet(insertable);
            Set<SqlIdentifier> updateable = new LinkedHashSet(this.columnNames);
            updateable.removeAll(this.idColumnNames);
            updateable.removeAll(this.readOnlyColumnNames);
            this.updateableColumns = Collections.unmodifiableSet(updateable);
        }

        private void populateColumnNameCache(RelationalPersistentEntity<?> entity, String prefix) {
            entity.doWithAll((property) -> {
                if (!property.isEntity()) {
                    this.initSimpleColumnName(property, prefix);
                } else if (property.isEmbedded()) {
                    this.initEmbeddedColumnNames(property, prefix);
                }

            });
        }

        private void initSimpleColumnName(RelationalPersistentProperty property, String prefix) {
            SqlIdentifier var10000 = property.getColumnName();
            prefix.getClass();
            SqlIdentifier columnName = var10000.transform(prefix::concat);
            this.columnNames.add(columnName);
            if (!property.getOwner().isIdProperty(property)) {
                this.nonIdColumnNames.add(columnName);
            } else {
                this.idColumnNames.add(columnName);
            }

            if (!property.isWritable()) {
                this.readOnlyColumnNames.add(columnName);
            }

        }

        private void initEmbeddedColumnNames(RelationalPersistentProperty property, String prefix) {
            String embeddedPrefix = property.getEmbeddedPrefix();
            RelationalPersistentEntity<?> embeddedEntity = (RelationalPersistentEntity)this.mappingContext.getRequiredPersistentEntity(this.converter.getColumnType(property));
            this.populateColumnNameCache(embeddedEntity, prefix + embeddedPrefix);
        }

        Set<SqlIdentifier> getInsertableColumns() {
            return this.insertableColumns;
        }

        Set<SqlIdentifier> getUpdateableColumns() {
            return this.updateableColumns;
        }
    }

    static final class Join {
        private final Table joinTable;
        private final Column joinColumn;
        private final Column parentId;

        Join(Table joinTable, Column joinColumn, Column parentId) {
            Assert.notNull(joinTable, "JoinTable must not be null.");
            Assert.notNull(joinColumn, "JoinColumn must not be null.");
            Assert.notNull(parentId, "ParentId must not be null.");
            this.joinTable = joinTable;
            this.joinColumn = joinColumn;
            this.parentId = parentId;
        }

        Table getJoinTable() {
            return this.joinTable;
        }

        Column getJoinColumn() {
            return this.joinColumn;
        }

        Column getParentId() {
            return this.parentId;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o != null && this.getClass() == o.getClass()) {
                SqlGenerator.Join join = (SqlGenerator.Join)o;
                return this.joinTable.equals(join.joinTable) && this.joinColumn.equals(join.joinColumn) && this.parentId.equals(join.parentId);
            } else {
                return false;
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.joinTable, this.joinColumn, this.parentId});
        }

        public String toString() {
            return "Join{joinTable=" + this.joinTable + ", joinColumn=" + this.joinColumn + ", parentId=" + this.parentId + '}';
        }
    }
}
