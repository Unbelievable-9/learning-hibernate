package info.unbelievable9.examples.advanced;

import info.unbelievable9.environment.jpa.JPATestBase;
import info.unbelievable9.models.advanced.Bid;
import info.unbelievable9.models.advanced.Item;
import info.unbelievable9.models.advanced.ItemBidSummary;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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

    @Test
    public void loadSubselectEntity() throws Exception {
        Long itemId = storeItemAndBids();

        UserTransaction userTransaction = transactionManagerSetup.getUserTransaction();

        try {
            userTransaction.begin();
            EntityManager entityManager = jpaSetup.createEntityManger();

            {
                ItemBidSummary itemBidSummary = entityManager.find(ItemBidSummary.class, itemId);

                Assert.assertEquals(itemBidSummary.getName(), "AUCTION: Test item");
                Assert.assertEquals(itemBidSummary.getNumberOfBids(), 10);
            }

            entityManager.clear();

            {
                Item item = entityManager.find(Item.class, itemId);
                item.setName("New test item");


                // Hibernate doesn't flush automatically before find() operation,
                // but will flush before a query is executed
                // ItemBidSummary itemBidSummary = entityManager.find(ItemBidSummary.class, itemId);

                Query query = entityManager.createQuery(
                        "select ibs from ItemBidSummary ibs where ibs.itemId = :id"
                );

                ItemBidSummary itemBidSummary = (ItemBidSummary) query.setParameter("id", itemId).getSingleResult();

                Assert.assertEquals(itemBidSummary.getName(), "AUCTION: New test item");
                Assert.assertEquals(itemBidSummary.getNumberOfBids(), 10);
            }

            userTransaction.commit();
            entityManager.close();
        } finally {
            transactionManagerSetup.rollback();
        }
    }
}
