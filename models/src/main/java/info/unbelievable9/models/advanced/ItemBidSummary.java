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
        value = "select i.id as itemId, i.item_name as name, " +
                "count(b.id) as numberOfBids " +
                "from items i left join bids b on i.id = b.item_id " +
                "group by i.id, i.item_name"
)
@Synchronize({"items", "bids"})
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
