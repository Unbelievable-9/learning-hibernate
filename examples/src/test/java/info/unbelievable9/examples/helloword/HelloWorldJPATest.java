package info.unbelievable9.examples.helloword;

import info.unbelievable9.environment.TransactionManagerTest;
import info.unbelievable9.model.helloword.Message;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;
import java.util.List;

/**
 * Created on : 2018/8/22
 * Author     : Unbelievable9
 **/
@SuppressWarnings("unchecked")
public class HelloWorldJPATest extends TransactionManagerTest {

    @Test()
    public void storedAndLoadMessage() throws Exception {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("HelloWorldPU");

        try {
            {
                // Store Message
                UserTransaction userTransaction = transactionManagerSetup.getUserTransaction();
                userTransaction.begin();

                EntityManager entityManager = entityManagerFactory.createEntityManager();

                Message message = new Message();
                message.setText("Hello world from JPA!");

                entityManager.persist(message);

                userTransaction.commit();

                entityManager.close();
            }

            {
                // Load Message
                UserTransaction userTransaction = transactionManagerSetup.getUserTransaction();
                userTransaction.begin();

                EntityManager entityManager = entityManagerFactory.createEntityManager();

                List<Message> messageList = entityManager.createQuery("select m from Message m").getResultList();

                Assert.assertEquals(messageList.size(), 1);
                Assert.assertEquals(messageList.get(0).getText(), "Hello world from JPA!");

                messageList.get(0).setText("We will dive into Hibernate soon!");

                userTransaction.commit();

                entityManager.close();
            }

            {
                // Validate new message
                UserTransaction userTransaction = transactionManagerSetup.getUserTransaction();
                userTransaction.begin();

                EntityManager entityManager = entityManagerFactory.createEntityManager();

                List<Message> messageList = entityManager.createQuery("select m from Message m").getResultList();

                Assert.assertEquals(messageList.get(0).getText(), "We will dive into Hibernate soon!");

                userTransaction.commit();

                entityManager.close();
            }
        } finally {
            transactionManagerSetup.rollback();
            entityManagerFactory.close();
        }
    }
}
