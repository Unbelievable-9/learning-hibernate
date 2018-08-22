package info.unbelievable9.model.helloword;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created on : 2018/8/22
 * Author     : Unbelievable9
 **/
@Entity
public class Message {

    @Id
    @GeneratedValue()
    private Long id;

    private String text;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
