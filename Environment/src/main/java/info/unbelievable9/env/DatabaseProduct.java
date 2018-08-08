package info.unbelievable9.env;

import bitronix.tm.resource.jdbc.PoolingDataSource;
import info.unbelievable9.shared.ImprovedH2Dialect;
import org.hibernate.dialect.MySQL57InnoDBDialect;

/**
 * Created on : 2018/8/8
 * Author     : Unbelievable9
 **/
public enum DatabaseProduct {

    H2(
            (poolingDataSource, connectionURL) -> {
                poolingDataSource.setClassName("org.h2.jdbcx.JdbcDataSource");
                poolingDataSource.getDriverProperties().put(
                        "URL",
                        connectionURL != null ? connectionURL : "jdbc:h2:mem:test");

                poolingDataSource.getDriverProperties().put("user", "jack");
            },
            ImprovedH2Dialect.class.getName()
    ),

    MYSQL(
            (poolingDataSource, connectionURL) -> {
                poolingDataSource.setClassName("bitronix.tm.resource.jdbc.lrc.LrcXADataSource");
                poolingDataSource.getDriverProperties().put(
                        "url",
                        connectionURL != null ? connectionURL : "jdbc:mysql://localhost:3306/test?sessionVariables=sql_mode='PIPES_AS_CONCAT'"
                );

                poolingDataSource.getDriverProperties().put("driverClassName", "com.mysql.cj.jdbc.Driver");
            },
            MySQL57InnoDBDialect.class.getName()
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
