package info.unbelievable9.models.simple;

import info.unbelievable9.models.Constants;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created on : 2018/8/23
 * Author     : Unbelievable9
 **/
@Entity
@Table(name = "BIDS")
public class Bid implements Serializable {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    protected Long id;

    @Transient
    protected Item item;

    public Long getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        if (item == null) {
            throw new NullPointerException("Can't assign null item.");
        }

        this.item = item;
    }
}
