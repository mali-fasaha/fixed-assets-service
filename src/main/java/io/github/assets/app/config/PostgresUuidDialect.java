package io.github.assets.app.config;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.TypeContributions;
import org.hibernate.dialect.PostgreSQL95Dialect;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.PostgresUUIDType;
import org.hibernate.type.descriptor.sql.BinaryTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

import java.sql.Types;

@Slf4j
public class PostgresUuidDialect extends PostgreSQL95Dialect {

    public PostgresUuidDialect() {
        super();
        registerColumnType(Types.BLOB, "bytea");
    }

    /** {@inheritDoc} */
    @Override
    public SqlTypeDescriptor remapSqlTypeDescriptor(SqlTypeDescriptor sqlTypeDescriptor) {
        if (sqlTypeDescriptor.getSqlType() == java.sql.Types.BLOB) {
            log.debug("Remapping BLOB sql type to binary...");
            return BinaryTypeDescriptor.INSTANCE;
        }
        return super.remapSqlTypeDescriptor(sqlTypeDescriptor);
    }

    @Override
    public void contributeTypes(final TypeContributions typeContributions, final ServiceRegistry serviceRegistry) {
        super.contributeTypes(typeContributions, serviceRegistry);
        typeContributions.contributeType(new JHipsterPostgresUUIDType());
    }

    protected static class JHipsterPostgresUUIDType extends PostgresUUIDType {

        @Override
        public String getName() {
            return "uuid-jhipster";
        }

        @Override
        protected boolean registerUnderJavaType() {
            return true;
        }
    }

}
