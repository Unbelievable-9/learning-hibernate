package info.unbelievable9.environment.transaction;

import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.jdbc.PoolingDataSource;
import info.unbelievable9.environment.db.DatabaseProduct;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.util.logging.Logger;

/**
 * Created on : 2018/8/8
 * Author     : Unbelievable9
 **/
public class TransactionManagerSetup {

    private static final Logger logger = Logger.getLogger(TransactionManagerSetup.class.getName());

    private final Context context = new InitialContext();
    private final PoolingDataSource poolingDataSource;

    private static final String DATASOURCE_NAME = "helloWorldDS";

    public final DatabaseProduct databaseProduct;

    public TransactionManagerSetup(DatabaseProduct databaseProduct) throws NamingException {
        this(databaseProduct, null);
    }

    TransactionManagerSetup(DatabaseProduct databaseProduct, String connectionURL) throws NamingException {
        logger.fine("Starting database connection pool");

        logger.fine("Setting stable unique identifier for transaction recovery");
        TransactionManagerServices.getConfiguration().setServerId("bitronixServer1234");

        logger.fine("Disabling JMX binding of manager in unit tests");
        TransactionManagerServices.getConfiguration().setDisableJmx(true);

        logger.fine("Disabling transaction logging for unit tests");
        TransactionManagerServices.getConfiguration().setJournal("null");

        logger.fine("Disabling warnings when the database isn't accessed in a transaction");
        TransactionManagerServices.getConfiguration().setWarnAboutZeroResourceTransaction(false);

        logger.fine("Creating connection pool");
        poolingDataSource = new PoolingDataSource();
        poolingDataSource.setUniqueName(DATASOURCE_NAME);
        poolingDataSource.setMinPoolSize(1);
        poolingDataSource.setMaxPoolSize(5);
        poolingDataSource.setPreparedStatementCacheSize(10);
        poolingDataSource.setIsolationLevel("READ_COMMITTED");
        poolingDataSource.setAllowLocalTransactions(true);

        logger.info("Setting up database connection: " + databaseProduct);
        this.databaseProduct = databaseProduct;
        databaseProduct.datasSourceConfiguration.configure(poolingDataSource, connectionURL);

        logger.fine("Initializing transaction and resource management");
        poolingDataSource.init();
    }

    private Context getNameContext() {
        return context;
    }

    public UserTransaction getUserTransaction() {
        try {
            return (UserTransaction) getNameContext().lookup("java:comp/UserTransaction");
        } catch (NamingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public DataSource getDataSource() {
        try {
            return (DataSource) getNameContext().lookup(DATASOURCE_NAME);
        } catch (NamingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void rollback() {
        UserTransaction userTransaction = getUserTransaction();

        try {
            if (userTransaction.getStatus() == Status.STATUS_ACTIVE ||
                    userTransaction.getStatus() == Status.STATUS_MARKED_ROLLBACK) {
                userTransaction.rollback();
            }
        } catch (SystemException e) {
            logger.info("Rollback of transaction failed, trace follows!");

            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    void stop() {
        logger.fine("Stopping database connection pool");
        poolingDataSource.close();
        TransactionManagerServices.getTransactionManager().shutdown();
    }
}
