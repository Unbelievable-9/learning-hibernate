package info.unbelievable9.models.simple;

import info.unbelievable9.models.common.Constants;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created on : 2018/8/23
 * Author     : Unbelievable9
 **/
@Entity
@Table(name = "ITEMS")
public class Item implements Serializable {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    protected Long id;

    @NotNull
    @Size(min = 2, max = 255, message = "Item name is required, maximum 255 characters.")
    protected String name;

    @Future(message = "Auction end time must be in future.")
    protected Date auctionEnd;

    @Transient
    protected Set<Bid> bids = new HashSet<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getAuctionEnd() {
        return auctionEnd;
    }

    public void setAuctionEnd(Date auctionEnd) {
        this.auctionEnd = auctionEnd;
    }

    public Set<Bid> getBids() {
        return bids;
    }

    public void setBids(Set<Bid> bids) {
        this.bids = bids;
    }

    public void addBids(Bid bid) {
        if (bid == null) {
            throw new NullPointerException("Can't add null bid.");
        } else if (bid.getItem() != null) {
            throw new IllegalStateException("Bid is already assign to an item.");
        }

        getBids().add(bid);
        bid.setItem(this);
    }
}
