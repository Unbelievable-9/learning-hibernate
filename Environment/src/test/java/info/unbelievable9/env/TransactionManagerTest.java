package info.unbelievable9.env;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import javax.naming.NamingException;
import java.util.Locale;

/**
 * Created on : 2018/8/8
 * Author     : Unbelievable9
 **/
public class TransactionManagerTest {

    private static TransactionManagerSetup transactionManagerSetup;

    @Parameters({"database", "connectionURL"})
    @BeforeSuite
    public void beforeSuite(@Optional String database,
                            @Optional String connectionURL) throws NamingException {
        transactionManagerSetup = new TransactionManagerSetup(
                database != null ? DatabaseProduct.valueOf(database.toUpperCase(Locale.US)) : DatabaseProduct.H2,
                connectionURL
        );
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        if (transactionManagerSetup != null) {
            transactionManagerSetup.stop();
        }
    }
}
