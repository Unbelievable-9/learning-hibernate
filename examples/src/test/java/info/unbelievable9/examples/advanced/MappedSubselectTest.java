package info.unbelievable9.examples.advanced;

import info.unbelievable9.environment.jpa.JPATestBase;
import info.unbelievable9.models.advanced.Bid;
import info.unbelievable9.models.advanced.Item;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created on : 2018/9/13
 * Author     : Unbelievable9
 **/
public class MappedSubselectTest extends JPATestBase {

    private Long storeItemAndBids() throws Exception {
        UserTransaction userTransaction = transactionManagerSetup.getUserTransaction();
        userTransaction.begin();

        EntityManager entityManager = jpaSetup.createEntityManger();

        // Generate item
        Item item = new Item();
        item.setName("Test item");
        item.setDescription("This is a test item.");
        entityManager.persist(item);

        // Generate bids
        for (int i = 0; i < 10; i++) {
            double randomDouble = ThreadLocalRandom.current().nextDouble(0, 1000.0);
            randomDouble = Double.parseDouble(String.format("%.2f", randomDouble));

            Bid bid = new Bid();
            bid.setAmount(BigDecimal.valueOf(randomDouble));
            bid.setItem(item);

            entityManager.persist(bid);
        }

        userTransaction.commit();
        entityManager.close();

        return item.getId();
    }

    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("AdvancedPU");
    }
}
