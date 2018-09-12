package info.unbelievable9.examples.simple;

import info.unbelievable9.models.simple.Bid;
import info.unbelievable9.models.simple.Item;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.Set;

/**
 * Created on : 2018/9/3
 * Author     : Unbelievable9
 **/
public class ModelOperationTest {

    @Test(enabled = false)
    public void linkItemAndBids() {
        Item item = new Item();
        Bid firstBid = new Bid();

        // Bidirectional linking
        item.getBids().add(firstBid);
        firstBid.setItem(item);

        // Check
        Assert.assertEquals(item.getBids().size(), 1);
        Assert.assertTrue(item.getBids().contains(firstBid));
        Assert.assertEquals(firstBid.getItem(), item);

        // Second Bid linking
        Bid secondBid = new Bid();
        item.getBids().add(secondBid);

        // Check again
        Assert.assertEquals(item.getBids().size(), 2);
        Assert.assertTrue(item.getBids().contains(secondBid));
        Assert.assertNotEquals(secondBid.getItem(), item);
    }

    /**
     * Custom validator test
     */
    @Test(enabled = false)
    public void validateItem() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        // Create an item
        Item item = new Item();
        item.setName("Test item");
        item.setAuctionEnd(new Date());

        // Manually validate item
        Set<ConstraintViolation<Item>> violationSet = validator.validate(item);

        // Check
        Assert.assertEquals(violationSet.size(), 1);

        // Retrieve violation property
        ConstraintViolation<Item> constraintViolation = violationSet.iterator().next();
        String violatedProperty = constraintViolation.getPropertyPath().iterator().next().getName();

        // Check again
        Assert.assertEquals(violatedProperty, "auctionEnd");
        Assert.assertEquals(constraintViolation.getMessage(), "Auction end time must be in future.");
    }

    @Test
    public void queryItem() {

    }
}
