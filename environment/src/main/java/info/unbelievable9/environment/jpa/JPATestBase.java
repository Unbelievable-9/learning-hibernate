package info.unbelievable9.environment.jpa;

import info.unbelievable9.environment.transaction.TransactionManagerTestBase;
import org.testng.annotations.*;

/**
 * Created on : 2018/9/12
 * Author     : Unbelievable9
 **/
public class JPATestBase extends TransactionManagerTestBase {

    private String persistenceUnitName;
    private String[] hibernateResources;
    public JPASetup jpaSetup;

    public void configurePersistenceUnit() {
        configurePersistenceUnit(null);
    }

    public void configurePersistenceUnit(String persistenceUnitName,
                                         String... hibernateResources) {
        this.persistenceUnitName = persistenceUnitName;
        this.hibernateResources = hibernateResources;
    }

    /**
     * For override
     */
    public void afterJPABootstrap() {

    }

    /**
     * For override
     */
    public void beforeJPAClose() {

    }

    @BeforeClass
    public void beforeClass() {
        configurePersistenceUnit();
    }

    @BeforeMethod
    public void beforeMethod() {
        jpaSetup = new JPASetup(transactionManagerSetup.databaseProduct, persistenceUnitName, hibernateResources);

        // Drop and create with ease
        jpaSetup.dropSchema();
        jpaSetup.createSchema();

        afterJPABootstrap();
    }

    @Parameters("keepSchema")
    @AfterMethod(alwaysRun = true)
    public void afterMethod(@Optional String keepSchema) {
        if (jpaSetup != null) {
            beforeJPAClose();

            if (!keepSchema.equals("true")) {
                jpaSetup.dropSchema();
            }

            jpaSetup.getEntityManagerFactory().close();
        }
    }
}
