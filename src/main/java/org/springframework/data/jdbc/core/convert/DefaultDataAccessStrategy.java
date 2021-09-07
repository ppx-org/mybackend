//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.springframework.data.jdbc.core.convert;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.core.convert.*;
import org.springframework.data.jdbc.support.JdbcUtil;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.PersistentPropertyAccessor;
import org.springframework.data.mapping.PersistentPropertyPath;
import org.springframework.data.relational.core.dialect.IdGeneration;
import org.springframework.data.relational.core.mapping.PersistentPropertyPathExtension;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.data.relational.core.sql.IdentifierProcessing;
import org.springframework.data.relational.core.sql.LockMode;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public class DefaultDataAccessStrategy implements DataAccessStrategy {
    private final SqlGeneratorSource sqlGeneratorSource;
    private final RelationalMappingContext context;
    private final JdbcConverter converter;
    private final NamedParameterJdbcOperations operations;

    public DefaultDataAccessStrategy(SqlGeneratorSource sqlGeneratorSource, RelationalMappingContext context, JdbcConverter converter, NamedParameterJdbcOperations operations) {
        Assert.notNull(sqlGeneratorSource, "SqlGeneratorSource must not be null");
        Assert.notNull(context, "RelationalMappingContext must not be null");
        Assert.notNull(converter, "JdbcConverter must not be null");
        Assert.notNull(operations, "NamedParameterJdbcOperations must not be null");
        this.sqlGeneratorSource = sqlGeneratorSource;
        this.context = context;
        this.converter = converter;
        this.operations = operations;
    }

    public <T> Object insert(T instance, Class<T> domainType, Identifier identifier) {
        SqlGenerator sqlGenerator = this.sql(domainType);
        RelationalPersistentEntity<T> persistentEntity = this.getRequiredPersistentEntity(domainType);
        SqlIdentifierParameterSource parameterSource = this.getParameterSource(instance, persistentEntity, "", PersistentProperty::isIdProperty, this.getIdentifierProcessing());
        System.out.println("------------>>>009:" + parameterSource.toString());
        identifier.forEach((name, value, type) -> {
            this.addConvertedPropertyValue(parameterSource, name, value, type);
        });
        Object idValue = this.getIdValueOrNull(instance, persistentEntity);
        if (idValue != null) {
            RelationalPersistentProperty idProperty = (RelationalPersistentProperty)persistentEntity.getRequiredIdProperty();
            this.addConvertedPropertyValue(parameterSource, idProperty, idValue, idProperty.getColumnName());
        }


        String insertSql = sqlGenerator.getInsert(new HashSet<>(), parameterSource);

        System.out.println("----------sqlxxx1:" + insertSql);
        if (idValue == null) {
            return this.executeInsertAndReturnGeneratedId(domainType, persistentEntity, parameterSource, insertSql);
        } else {
            this.operations.update(insertSql, parameterSource);
            return null;
        }
    }

    @Nullable
    private <T> Object executeInsertAndReturnGeneratedId(Class<T> domainType, RelationalPersistentEntity<T> persistentEntity, SqlIdentifierParameterSource parameterSource, String insertSql) {
        KeyHolder holder = new GeneratedKeyHolder();
        IdGeneration idGeneration = this.sqlGeneratorSource.getDialect().getIdGeneration();
        if (idGeneration.driverRequiresKeyColumnNames()) {
            String[] keyColumnNames = this.getKeyColumnNames(domainType);
            if (keyColumnNames.length == 0) {
                this.operations.update(insertSql, parameterSource, holder);
            } else {
                this.operations.update(insertSql, parameterSource, holder, keyColumnNames);
            }
        } else {
            this.operations.update(insertSql, parameterSource, holder);
        }

        return this.getIdFromHolder(holder, persistentEntity);
    }

    public <S> boolean update(S instance, Class<S> domainType) {
        RelationalPersistentEntity<S> persistentEntity = this.getRequiredPersistentEntity(domainType);
        return this.operations.update(this.sql(domainType).getUpdate(), this.getParameterSource(instance, persistentEntity, "", DefaultDataAccessStrategy.Predicates.includeAll(), this.getIdentifierProcessing())) != 0;
    }

    public <S> boolean updateWithVersion(S instance, Class<S> domainType, Number previousVersion) {
        RelationalPersistentEntity<S> persistentEntity = this.getRequiredPersistentEntity(domainType);
        SqlIdentifierParameterSource parameterSource = this.getParameterSource(instance, persistentEntity, "", DefaultDataAccessStrategy.Predicates.includeAll(), this.getIdentifierProcessing());
        parameterSource.addValue(SqlGenerator.VERSION_SQL_PARAMETER, previousVersion);
        int affectedRows = this.operations.update(this.sql(domainType).getUpdateWithVersion(), parameterSource);
        if (affectedRows == 0) {
            throw new OptimisticLockingFailureException(String.format("Optimistic lock exception on saving entity of type %s.", persistentEntity.getName()));
        } else {
            return true;
        }
    }

    public void delete(Object id, Class<?> domainType) {
        String deleteByIdSql = this.sql(domainType).getDeleteById();
        SqlParameterSource parameter = this.createIdParameterSource(id, domainType);
        this.operations.update(deleteByIdSql, parameter);
    }

    public <T> void deleteWithVersion(Object id, Class<T> domainType, Number previousVersion) {
        Assert.notNull(id, "Id must not be null.");
        RelationalPersistentEntity<T> persistentEntity = this.getRequiredPersistentEntity(domainType);
        SqlIdentifierParameterSource parameterSource = this.createIdParameterSource(id, domainType);
        parameterSource.addValue(SqlGenerator.VERSION_SQL_PARAMETER, previousVersion);
        int affectedRows = this.operations.update(this.sql(domainType).getDeleteByIdAndVersion(), parameterSource);
        if (affectedRows == 0) {
            throw new OptimisticLockingFailureException(String.format("Optimistic lock exception deleting entity of type %s.", persistentEntity.getName()));
        }
    }

    public void delete(Object rootId, PersistentPropertyPath<RelationalPersistentProperty> propertyPath) {
        RelationalPersistentEntity<?> rootEntity = (RelationalPersistentEntity)this.context.getRequiredPersistentEntity(((RelationalPersistentProperty)propertyPath.getBaseProperty()).getOwner().getType());
        RelationalPersistentProperty referencingProperty = (RelationalPersistentProperty)propertyPath.getLeafProperty();
        Assert.notNull(referencingProperty, "No property found matching the PropertyPath " + propertyPath);
        String delete = this.sql(rootEntity.getType()).createDeleteByPath(propertyPath);
        SqlIdentifierParameterSource parameters = new SqlIdentifierParameterSource(this.getIdentifierProcessing());
        this.addConvertedPropertyValue(parameters, (RelationalPersistentProperty)rootEntity.getRequiredIdProperty(), rootId, SqlGenerator.ROOT_ID_PARAMETER);
        this.operations.update(delete, parameters);
    }

    public <T> void deleteAll(Class<T> domainType) {
        this.operations.getJdbcOperations().update(this.sql(domainType).createDeleteAllSql((PersistentPropertyPath)null));
    }

    public void deleteAll(PersistentPropertyPath<RelationalPersistentProperty> propertyPath) {
        this.operations.getJdbcOperations().update(this.sql(((RelationalPersistentProperty)propertyPath.getBaseProperty()).getOwner().getType()).createDeleteAllSql(propertyPath));
    }

    public <T> void acquireLockById(Object id, LockMode lockMode, Class<T> domainType) {
        String acquireLockByIdSql = this.sql(domainType).getAcquireLockById(lockMode);
        SqlIdentifierParameterSource parameter = this.createIdParameterSource(id, domainType);
        this.operations.query(acquireLockByIdSql, parameter, ResultSet::next);
    }

    public <T> void acquireLockAll(LockMode lockMode, Class<T> domainType) {
        String acquireLockAllSql = this.sql(domainType).getAcquireLockAll(lockMode);
        this.operations.getJdbcOperations().query(acquireLockAllSql, ResultSet::next);
    }

    public long count(Class<?> domainType) {
        Long result = (Long)this.operations.getJdbcOperations().queryForObject(this.sql(domainType).getCount(), Long.class);
        Assert.notNull(result, "The result of a count query must not be null.");
        return result;
    }

    public <T> T findById(Object id, Class<T> domainType) {
        String findOneSql = this.sql(domainType).getFindOne();
        SqlIdentifierParameterSource parameter = this.createIdParameterSource(id, domainType);

        try {
            return (T) this.operations.queryForObject(findOneSql, parameter, this.getEntityRowMapper(domainType));
        } catch (EmptyResultDataAccessException var6) {
            return null;
        }
    }

    public <T> Iterable<T> findAll(Class<T> domainType) {
        return (Iterable<T>) this.operations.query(this.sql(domainType).getFindAll(), this.getEntityRowMapper(domainType));
    }

    public <T> Iterable<T> findAllById(Iterable<?> ids, Class<T> domainType) {
        if (!ids.iterator().hasNext()) {
            return Collections.emptyList();
        } else {
            RelationalPersistentProperty idProperty = (RelationalPersistentProperty)this.getRequiredPersistentEntity(domainType).getRequiredIdProperty();
            SqlIdentifierParameterSource parameterSource = new SqlIdentifierParameterSource(this.getIdentifierProcessing());
            this.addConvertedPropertyValuesAsList(parameterSource, idProperty, ids, SqlGenerator.IDS_SQL_PARAMETER);
            String findAllInListSql = this.sql(domainType).getFindAllInList();
            return (Iterable<T>) this.operations.query(findAllInListSql, parameterSource, this.getEntityRowMapper(domainType));
        }
    }

    public Iterable<Object> findAllByPath(Identifier identifier, PersistentPropertyPath<? extends RelationalPersistentProperty> propertyPath) {
        Assert.notNull(identifier, "identifier must not be null.");
        Assert.notNull(propertyPath, "propertyPath must not be null.");
        PersistentPropertyPathExtension path = new PersistentPropertyPathExtension(this.context, propertyPath);
        Class<?> actualType = path.getActualType();
        String findAllByProperty = this.sql(actualType).getFindAllByProperty(identifier, path.getQualifierColumn(), path.isOrdered());
        RowMapper<?> rowMapper = path.isMap() ? this.getMapEntityRowMapper(path, identifier) : this.getEntityRowMapper(path, identifier);
        return this.operations.query(findAllByProperty, this.createParameterSource(identifier, this.getIdentifierProcessing()), (RowMapper)rowMapper);
    }

    private SqlParameterSource createParameterSource(Identifier identifier, IdentifierProcessing identifierProcessing) {
        SqlIdentifierParameterSource parameterSource = new SqlIdentifierParameterSource(identifierProcessing);
        identifier.toMap().forEach((name, value) -> {
            this.addConvertedPropertyValue(parameterSource, name, value, value.getClass());
        });
        return parameterSource;
    }

    public <T> boolean existsById(Object id, Class<T> domainType) {
        String existsSql = this.sql(domainType).getExists();
        SqlParameterSource parameter = this.createIdParameterSource(id, domainType);
        Boolean result = (Boolean)this.operations.queryForObject(existsSql, parameter, Boolean.class);
        Assert.state(result != null, "The result of an exists query must not be null");
        return result;
    }

    public <T> Iterable<T> findAll(Class<T> domainType, Sort sort) {
        return (Iterable<T>) this.operations.query(this.sql(domainType).getFindAll(sort), this.getEntityRowMapper(domainType));
    }

    public <T> Iterable<T> findAll(Class<T> domainType, Pageable pageable) {
        return (Iterable<T>) this.operations.query(this.sql(domainType).getFindAll(pageable), this.getEntityRowMapper(domainType));
    }

    private <S, T> SqlIdentifierParameterSource getParameterSource(@Nullable S instance, RelationalPersistentEntity<S> persistentEntity, String prefix, Predicate<RelationalPersistentProperty> skipProperty, IdentifierProcessing identifierProcessing) {
        SqlIdentifierParameterSource parameters = new SqlIdentifierParameterSource(identifierProcessing);
        PersistentPropertyAccessor<S> propertyAccessor = instance != null ? persistentEntity.getPropertyAccessor(instance) : DefaultDataAccessStrategy.NoValuePropertyAccessor.instance();
        persistentEntity.doWithAll((property) -> {
            if (!skipProperty.test(property) && property.isWritable()) {
                if (!property.isEntity() || property.isEmbedded()) {
                    Object value;
                    if (property.isEmbedded()) {
                        value = propertyAccessor.getProperty(property);
                        RelationalPersistentEntity<?> embeddedEntity = (RelationalPersistentEntity)this.context.getPersistentEntity(property.getType());
                        SqlIdentifierParameterSource additionalParameters = this.getParameterSource((S)value, (RelationalPersistentEntity<S>) embeddedEntity, prefix + property.getEmbeddedPrefix(), skipProperty, identifierProcessing);
                        parameters.addAll(additionalParameters);
                    } else {
                        value = propertyAccessor.getProperty(property);
                        SqlIdentifier var10000 = property.getColumnName();
                        prefix.getClass();
                        SqlIdentifier paramName = var10000.transform(prefix::concat);
                        //if (value != null) {
                            System.out.println("00000>>>3:" + value);
                            this.addConvertedPropertyValue(parameters, property, value, paramName);
                        //}
                    }

                }
            }
        });
        return parameters;
    }

    @Nullable
    private <S, ID> ID getIdValueOrNull(S instance, RelationalPersistentEntity<S> persistentEntity) {
        ID idValue = (ID) persistentEntity.getIdentifierAccessor(instance).getIdentifier();
        return isIdPropertyNullOrScalarZero(idValue, persistentEntity) ? null : idValue;
    }

    private static <S, ID> boolean isIdPropertyNullOrScalarZero(@Nullable ID idValue, RelationalPersistentEntity<S> persistentEntity) {
        RelationalPersistentProperty idProperty = (RelationalPersistentProperty)persistentEntity.getIdProperty();
        return idValue == null || idProperty == null || idProperty.getType() == Integer.TYPE && idValue.equals(0) || idProperty.getType() == Long.TYPE && idValue.equals(0L);
    }

    @Nullable
    private <S> Object getIdFromHolder(KeyHolder holder, RelationalPersistentEntity<S> persistentEntity) {
        try {
            return holder.getKey();
        } catch (InvalidDataAccessApiUsageException | DataRetrievalFailureException var5) {
            Map<String, Object> keys = holder.getKeys();
            return keys != null && persistentEntity.getIdProperty() != null ? keys.get(persistentEntity.getIdColumn().getReference(this.getIdentifierProcessing())) : null;
        }
    }

    private EntityRowMapper<?> getEntityRowMapper(Class<?> domainType) {
        return new EntityRowMapper(this.getRequiredPersistentEntity(domainType), this.converter);
    }

    private EntityRowMapper<?> getEntityRowMapper(PersistentPropertyPathExtension path, Identifier identifier) {
        return new EntityRowMapper(path, this.converter, identifier);
    }

    private RowMapper<?> getMapEntityRowMapper(PersistentPropertyPathExtension path, Identifier identifier) {
        SqlIdentifier keyColumn = path.getQualifierColumn();
        Assert.notNull(keyColumn, () -> {
            return "KeyColumn must not be null for " + path;
        });
        return new MapEntityRowMapper(path, this.converter, identifier, keyColumn, this.getIdentifierProcessing());
    }

    private <T> SqlIdentifierParameterSource createIdParameterSource(Object id, Class<T> domainType) {
        SqlIdentifierParameterSource parameterSource = new SqlIdentifierParameterSource(this.getIdentifierProcessing());
        this.addConvertedPropertyValue(parameterSource, (RelationalPersistentProperty)this.getRequiredPersistentEntity(domainType).getRequiredIdProperty(), id, SqlGenerator.ID_SQL_PARAMETER);
        return parameterSource;
    }

    private IdentifierProcessing getIdentifierProcessing() {
        return this.sqlGeneratorSource.getDialect().getIdentifierProcessing();
    }

    private void addConvertedPropertyValue(SqlIdentifierParameterSource parameterSource, RelationalPersistentProperty property, @Nullable Object value, SqlIdentifier name) {
        this.addConvertedValue(parameterSource, value, name, this.converter.getColumnType(property), this.converter.getSqlType(property));
    }

    private void addConvertedPropertyValue(SqlIdentifierParameterSource parameterSource, SqlIdentifier name, Object value, Class<?> javaType) {
        this.addConvertedValue(parameterSource, value, name, javaType, JdbcUtil.sqlTypeFor(javaType));
    }

    private void addConvertedValue(SqlIdentifierParameterSource parameterSource, @Nullable Object value, SqlIdentifier paramName, Class<?> javaType, int sqlType) {
        JdbcValue jdbcValue = this.converter.writeJdbcValue(value, javaType, sqlType);
        parameterSource.addValue(paramName, jdbcValue.getValue(), JdbcUtil.sqlTypeFor(jdbcValue.getJdbcType()));
    }

    private void addConvertedPropertyValuesAsList(SqlIdentifierParameterSource parameterSource, RelationalPersistentProperty property, Iterable<?> values, SqlIdentifier paramName) {
        List<Object> convertedIds = new ArrayList();
        JdbcValue jdbcValue = null;
        Iterator var7 = values.iterator();

        while(var7.hasNext()) {
            Object id = var7.next();
            Class<?> columnType = this.converter.getColumnType(property);
            int sqlType = this.converter.getSqlType(property);
            jdbcValue = this.converter.writeJdbcValue(id, columnType, sqlType);
            convertedIds.add(jdbcValue.getValue());
        }

        Assert.state(jdbcValue != null, "JdbcValue must be not null at this point. Please report this as a bug.");
        JDBCType jdbcType = jdbcValue.getJdbcType();
        int typeNumber = jdbcType == null ? -2147483648 : jdbcType.getVendorTypeNumber();
        parameterSource.addValue(paramName, convertedIds, typeNumber);
    }

    private <S> RelationalPersistentEntity<S> getRequiredPersistentEntity(Class<S> domainType) {
        return (RelationalPersistentEntity)this.context.getRequiredPersistentEntity(domainType);
    }

    private SqlGenerator sql(Class<?> domainType) {
        return this.sqlGeneratorSource.getSqlGenerator(domainType);
    }

    private <T> String[] getKeyColumnNames(Class<T> domainType) {
        RelationalPersistentEntity<?> requiredPersistentEntity = (RelationalPersistentEntity)this.context.getRequiredPersistentEntity(domainType);
        if (!requiredPersistentEntity.hasIdProperty()) {
            return new String[0];
        } else {
            SqlIdentifier idColumn = requiredPersistentEntity.getIdColumn();
            return new String[]{idColumn.getReference(this.getIdentifierProcessing())};
        }
    }

    static class NoValuePropertyAccessor<T> implements PersistentPropertyAccessor<T> {
        private static final DefaultDataAccessStrategy.NoValuePropertyAccessor INSTANCE = new DefaultDataAccessStrategy.NoValuePropertyAccessor();

        NoValuePropertyAccessor() {
        }

        static <T> DefaultDataAccessStrategy.NoValuePropertyAccessor<T> instance() {
            return INSTANCE;
        }

        public void setProperty(PersistentProperty<?> property, @Nullable Object value) {
            throw new UnsupportedOperationException("Cannot set value on 'null' target object.");
        }

        public Object getProperty(PersistentProperty<?> property) {
            return null;
        }

        public T getBean() {
            return null;
        }
    }

    static class Predicates {
        Predicates() {
        }

        static Predicate<RelationalPersistentProperty> includeAll() {
            return (it) -> {
                return false;
            };
        }
    }
}
