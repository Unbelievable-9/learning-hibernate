package info.unbelievable9.models.advanced;

import info.unbelievable9.models.common.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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

    @Access(AccessType.PROPERTY)
    @Column(name = "item_name")
    protected String name;

    @NotNull
    @Basic(fetch = FetchType.LAZY)
    protected String description;

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
}
