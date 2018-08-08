package info.unbelievable9.shared;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

/**
 * Created on : 2018/8/8
 * Author     : Unbelievable9
 **/
public class ImprovedH2Dialect extends H2Dialect {

    public ImprovedH2Dialect() {
        super();

        registerFunction("lpad", new StandardSQLFunction("lpad", StandardBasicTypes.STRING));
    }
}
