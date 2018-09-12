package info.unbelievable9.environment.jpa;

import info.unbelievable9.environment.db.DatabaseProduct;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on : 2018/9/12
 * Author     : Unbelievable9
 **/
public class JPASetup {

    protected final String persistenceUnitName;
    protected final Map<String, String> properties = new HashMap<>();
    protected final EntityManagerFactory entityManagerFactory;

    public String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public EntityManager createEntityManger() {
        return getEntityManagerFactory().createEntityManager();
    }

    public JPASetup(DatabaseProduct databaseProduct,
                    String persistenceUnitName,
                    String... hibernateResources) {
        this.persistenceUnitName = persistenceUnitName;

        properties.put(
                "hibernate.archive.autodetection",
                "none"
        );

        if (hibernateResources != null && hibernateResources.length > 0) {
            properties.put(
                    "hibernate.hbmxml.files",
                    String.join(",", hibernateResources)
            );
        }

        properties.put(
                "hibernate.format_sql",
                "true"
        );

        properties.put(
                "hibernate.use_sql_comments",
                "true"
        );

        properties.put(
                "hibernate.dialect",
                databaseProduct.hibernateDialect
        );

        entityManagerFactory = Persistence.createEntityManagerFactory(getPersistenceUnitName(), properties);
    }

    public void genereteSchema(String action) {
        Map<String, String> schemaProperties = new HashMap<>(properties);

        schemaProperties.put(
                "javax.persistence.schema-generation.database.action",
                action
        );

        Persistence.generateSchema(persistenceUnitName, schemaProperties);
    }

    public void createSchema() {
        genereteSchema("create");
    }

    public void dropSchema() {
        genereteSchema("drop");
    }
}
