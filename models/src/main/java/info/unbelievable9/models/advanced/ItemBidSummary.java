package info.unbelievable9.models.advanced;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created on : 2018/9/13
 * Author     : Unbelievable9
 **/
@Entity
@Immutable
@Subselect(
        value = "select i.ID as ITEMID, i.ITEM_NAME as NAME, " +
                "count(b.ID) as NUMBEROFBIDS " +
                "from ITEM i left join BID b on i.ID = b.ITEM_ID " +
                "group by i.ID, i.ITEM_NAME"
)
@Synchronize({"Item", "Bid"})
public class ItemBidSummary {

    @Id
    protected Long itemId;

    protected String name;

    protected long numberOfBids;

    public Long getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public long getNumberOfBids() {
        return numberOfBids;
    }
}
