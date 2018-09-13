package info.unbelievable9.models.other;

import info.unbelievable9.models.common.Constants;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created on : 2018/9/13
 * Author     : Unbelievable9
 **/
@Entity(name = "TestItem")
@Table(name = "TEST_ITEM")
public class Item {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;
}
