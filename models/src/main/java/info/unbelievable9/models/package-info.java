/**
 * Created on : 2018/9/3
 * Author     : Unbelievable9
 * Info       : Hibernate Package-Level Annotation
 **/
@org.hibernate.annotations.GenericGenerator(
        name = "ID_GENERATOR",
        strategy = "enhanced-sequence",
        parameters = {
                @org.hibernate.annotations.Parameter(
                        name = "sequence_name",
                        value = "LH_SEQUENCE"
                ),
                @org.hibernate.annotations.Parameter(
                        name = "initial_value",
                        value = "1000"
                )
        }
)
package info.unbelievable9.models;