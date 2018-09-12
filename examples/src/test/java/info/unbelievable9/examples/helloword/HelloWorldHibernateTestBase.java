package info.unbelievable9.examples.helloword;

import info.unbelievable9.environment.transaction.TransactionManagerTestBase;
import info.unbelievable9.models.helloword.Message;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.resource.transaction.backend.jta.internal.JtaTransactionCoordinatorBuilderImpl;
import org.hibernate.service.ServiceRegistry;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.transaction.UserTransaction;
import java.util.List;

/**
 * Created on : 2018/8/23
 * Author     : Unbelievable9
 **/
public class HelloWorldHibernateTestBase extends TransactionManagerTestBase {

    /**
     * Create Hibernate Session Factory
     * @return Hibernate Session Factory Instance
     */
    private SessionFactory createSessionFactory() {
        StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();

        serviceRegistryBuilder
                .applySetting("hibernate.connection.datasource", "helloWorldDS")
                .applySetting("hibernate.format_sql", "true")
                .applySetting("hibernate.use_sql_comments", "true")
                .applySetting("hibernate.hbm2ddl.auto", "create-drop");

        serviceRegistryBuilder.applySetting(
                Environment.TRANSACTION_COORDINATOR_STRATEGY,
                JtaTransactionCoordinatorBuilderImpl.class
        );

        ServiceRegistry serviceRegistry = serviceRegistryBuilder.build();

        MetadataSources metadataSources = new MetadataSources(serviceRegistry);

        metadataSources.addAnnotatedClass(Message.class);

        MetadataBuilder metadataBuilder = metadataSources.getMetadataBuilder();

        Metadata metadata = metadataBuilder.build();

        Assert.assertEquals(metadata.getEntityBindings().size(), 1);

        SessionFactory sessionFactory = metadata.buildSessionFactory();

        return sessionFactory;
    }

    @Test()
    public void storeAndLoadMessage() throws Exception {
        SessionFactory sessionFactory = createSessionFactory();

        try {
            {
                UserTransaction userTransaction = transactionManagerSetup.getUserTransaction();
                userTransaction.begin();

                Message message = new Message();
                message.setText("Hello world from Hibernate!");

                sessionFactory.getCurrentSession().persist(message);

                userTransaction.commit();
            }

            {
                UserTransaction userTransaction = transactionManagerSetup.getUserTransaction();
                userTransaction.begin();

                List<Message> messageList = sessionFactory.getCurrentSession().createCriteria(Message.class).list();

                Assert.assertEquals(messageList.size(), 1);
                Assert.assertEquals(messageList.get(0).getText(), "Hello world from Hibernate!");

                messageList.get(0).setText("Now we are in Hibernate!");

                userTransaction.commit();
            }

            {
                // Validate new message
                UserTransaction userTransaction = transactionManagerSetup.getUserTransaction();
                userTransaction.begin();

                List<Message> messageList = sessionFactory.getCurrentSession().createCriteria(Message.class).list();

                Assert.assertEquals(messageList.size(), 1);
                Assert.assertEquals(messageList.get(0).getText(), "Now we are in Hibernate!");

                userTransaction.commit();
            }
        } finally {
            transactionManagerSetup.rollback();
        }
    }
}
