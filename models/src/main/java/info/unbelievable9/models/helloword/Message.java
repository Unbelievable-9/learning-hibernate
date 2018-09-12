package info.unbelievable9.models.helloword;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created on : 2018/8/22
 * Author     : Unbelievable9
 **/
@Entity
@Table(name = "Messages")
public class Message {

    @Id
    @GeneratedValue()
    protected Long id;

    protected String text;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
