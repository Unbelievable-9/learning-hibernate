package info.unbelievable9.environment.db;

import bitronix.tm.resource.jdbc.PoolingDataSource;
import info.unbelievable9.shared.util.ImprovedH2Dialect;
import org.hibernate.dialect.MySQL57Dialect;

/**
 * Created on : 2018/8/8
 * Author     : Unbelievable9
 **/
public enum DatabaseProduct {

    H2(
            (poolingDataSource, connectionURL) -> {
                poolingDataSource.setClassName("org.h2.jdbcx.JdbcDataSource");

                poolingDataSource.getDriverProperties().put("user", "sa");
                poolingDataSource.getDriverProperties().put(
                        "URL",
                        connectionURL != null ? connectionURL : "jdbc:h2:mem:test");
            },
            ImprovedH2Dialect.class.getName()
    ),

    MYSQL(
            (poolingDataSource, connectionURL) -> {
                // Use latest MySQL XA support, no need to set driver class name.
                poolingDataSource.setClassName("com.mysql.cj.jdbc.MysqlXADataSource");

                poolingDataSource.getDriverProperties().put("user", "demo");
                poolingDataSource.getDriverProperties().put("password", "demo");
                poolingDataSource.getDriverProperties().put(
                        "url",
                        connectionURL != null ? connectionURL : "jdbc:mysql://localhost:3306/hibernate"
                );
            },
            MySQL57Dialect.class.getName()
    );

    public DatasSourceConfiguration datasSourceConfiguration;
    public String hibernateDialect;

    DatabaseProduct(DatasSourceConfiguration datasSourceConfiguration, String hibernateDialect) {
        this.datasSourceConfiguration = datasSourceConfiguration;
        this.hibernateDialect = hibernateDialect;
    }

    public interface DatasSourceConfiguration {
        void configure(PoolingDataSource poolingDataSource, String connectionURL);
    }
}
