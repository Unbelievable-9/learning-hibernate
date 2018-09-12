package info.unbelievable9.examples.simple;

import info.unbelievable9.environment.jpa.JPATestBase;
import info.unbelievable9.models.simple.Item;
import info.unbelievable9.models.simple.Item_;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.*;
import javax.transaction.UserTransaction;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created on : 2018/9/12
 * Author     : Unbelievable9
 **/
public class AccessJPAMetamodelTest extends JPATestBase {

    @Override
    public void configurePersistenceUnit() {
        configurePersistenceUnit("SimpleXMLCompletePU");
    }

    @Test
    public void accessDynamicMetamodel() {
        EntityManagerFactory entityManagerFactory = jpaSetup.getEntityManagerFactory();

        Metamodel metamodel = entityManagerFactory.getMetamodel();

        Set<ManagedType<?>> managedTypeSet = metamodel.getManagedTypes();

        Assert.assertEquals(managedTypeSet.size(), 1);

        ManagedType itemType = managedTypeSet.iterator().next();
        Assert.assertEquals(itemType.getPersistenceType(), Type.PersistenceType.ENTITY);
        Assert.assertEquals(itemType.getDeclaredAttributes().size(), 3);

        SingularAttribute idAttribute = itemType.getSingularAttribute("id");
        Assert.assertEquals(idAttribute.getJavaType(), Long.class);
        Assert.assertTrue(idAttribute.isId());

        SingularAttribute nameAttribute = itemType.getSingularAttribute("name");
        Assert.assertEquals(nameAttribute.getJavaType(), String.class);
        Assert.assertEquals(nameAttribute.getPersistentAttributeType(), Attribute.PersistentAttributeType.BASIC);
        Assert.assertFalse(nameAttribute.isOptional());

        SingularAttribute auctionEndAttribute = itemType.getSingularAttribute("auctionEnd");
        Assert.assertEquals(auctionEndAttribute.getJavaType(), Date.class);
        Assert.assertEquals(auctionEndAttribute.getPersistentAttributeType(), Attribute.PersistentAttributeType.BASIC);
        Assert.assertFalse(auctionEndAttribute.isCollection());
        Assert.assertFalse(auctionEndAttribute.isAssociation());
    }

    @Test
    public void accessStaticMetamodel() {
        SingularAttribute nameAttribute = Item_.name;
        Assert.assertEquals(nameAttribute.getJavaType(), String.class);
        Assert.assertEquals(nameAttribute.getPersistentAttributeType(), Attribute.PersistentAttributeType.BASIC);
        Assert.assertFalse(nameAttribute.isOptional());
    }

    @Test
    public void queryModel() throws Exception {
        UserTransaction userTransaction = transactionManagerSetup.getUserTransaction();

        try {
            // Create 2 items;
            userTransaction.begin();

            EntityManager entityManager = jpaSetup.createEntityManger();

            Item firstItem = new Item();
            firstItem.setName("First Item");
            firstItem.setAuctionEnd(new Date(System.currentTimeMillis() + 100000));
            entityManager.persist(firstItem);

            Item secondItem = new Item();
            secondItem.setName("Second Item");
            secondItem.setAuctionEnd(new Date(System.currentTimeMillis() + 200000));
            entityManager.persist(secondItem);

            userTransaction.commit();
            entityManager.close();

            // Query
            entityManager = jpaSetup.createEntityManger();
            userTransaction.begin();

            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Item> itemCriteriaQuery = criteriaBuilder.createQuery(Item.class);
            Root<Item> itemRoot = itemCriteriaQuery.from(Item.class);
            itemCriteriaQuery.select(itemRoot);

            List<Item> itemList = entityManager.createQuery(itemCriteriaQuery).getResultList();

            Assert.assertEquals(itemList.size(), 2);

            // Where like
            Path<String> namePath = itemRoot.get("name");
            itemCriteriaQuery.where(criteriaBuilder.like(namePath, criteriaBuilder.parameter(String.class, "pattern")));

            itemList = entityManager
                    .createQuery(itemCriteriaQuery)
                    .setParameter("pattern", "%First%")
                    .getResultList();

            Assert.assertEquals(itemList.size(), 1);
            Assert.assertEquals(itemList.iterator().next().getName(), "First Item");

            // Where like using static metamodel, no need to use Path<T>
            itemCriteriaQuery.where(criteriaBuilder.like(itemRoot.get(Item_.name), criteriaBuilder.parameter(String.class, "pattern")));

            itemList = entityManager
                    .createQuery(itemCriteriaQuery)
                    .setParameter("pattern", "%Second%")
                    .getResultList();

            Assert.assertEquals(itemList.size(), 1);
            Assert.assertEquals(itemList.iterator().next().getName(), "Second Item");

            userTransaction.commit();
            entityManager.close();
        } finally {
            transactionManagerSetup.rollback();
        }
    }
}
