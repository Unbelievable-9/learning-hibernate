package info.unbelievable9.models.advanced;

import info.unbelievable9.models.common.Constants;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created on : 2018/9/13
 * Author     : Unbelievable9
 **/
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    protected Long id;

    @NotNull
    @Access(AccessType.PROPERTY)
    @Column(name = "item_name", nullable = false)
    protected String name;

    @NotNull
    @Basic(optional = false, fetch = FetchType.LAZY)
    protected String description;

    // Derived property
    @Formula("concat(substr(description, 1, 12), '...')")
    protected String shortDescription;

    // Derived property
    @Formula("(select avg(b.amount) from bids b where b.item_id = id)")
    protected BigDecimal averageBidAmount;

    // Transforming column values
    @Column(name = "imperial_weight")
    @ColumnTransformer(read = "imperial_weight / 2.20462", write = "? * 2.20462")
    protected BigDecimal metricWeight;

    // Generated default property
    @Column(insertable = false)
    @ColumnDefault("1.00")
    @Generated(GenerationTime.INSERT)
    protected BigDecimal initialPrice;

    // Generated temporal property
    @Column(insertable = false, updatable = false)
    @Generated(GenerationTime.ALWAYS)
    protected Date lastModified;

    // Generated temporal property
    @Column(updatable = false)
    @CreationTimestamp
    protected Date createOn;

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = !name.startsWith("AUCTION: ") ? "AUCTION: " + name : name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setAverageBidAmount(BigDecimal averageBidAmount) {
        this.averageBidAmount = averageBidAmount;
    }

    public BigDecimal getAverageBidAmount() {
        return averageBidAmount;
    }

    public void setMetricWeight(BigDecimal metricWeight) {
        this.metricWeight = metricWeight;
    }

    public BigDecimal getMetricWeight() {
        return metricWeight;
    }

    public BigDecimal getInitialPrice() {
        return initialPrice;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public Date getCreateOn() {
        return createOn;
    }
}
