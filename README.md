# Learning-Hibernate  
Hibernate Learning Notes

## 学习用书
[Java Persistentce with Hibernate Second Edition](https://www.manning.com/books/java-persistence-with-hibernate-second-edition)

## 笔记

### Chapter 3

在使用 JPA XML 方式对 Model 做 Mapping 的时候，遇到了一个问题怎么都解决不了。
现象是 Item 类在生成 Metamodel 元数据时, @NotNull @Size @Furutre 等非持久化相关注解无法正常转换。


```java
# 正常的 Metamodel
package info.unbelievable9.models.simple;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Item.class)
public abstract class Item_ {

	public static volatile SingularAttribute<Item, Date> auctionEnd;
	public static volatile SingularAttribute<Item, String> name;
	public static volatile SingularAttribute<Item, Long> id;

	public static final String AUCTION_END = "auctionEnd";
	public static final String NAME = "name";
	public static final String ID = "id";

}
```

```java
# 错误的 Metamodel
package info.unbelievable9.models.simple;

import (@javax.validation.constraints.NotNull,@javax.validation.constraints.Size(min=2, max=255, message="Item name is required, maximum 255 characters.") :: java.lang.String);
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Item.class)
public abstract class Item_ {

	public static volatile SingularAttribute<Item, Date> auctionEnd;
	public static volatile SingularAttribute<Item, String)> name;
	public static volatile SingularAttribute<Item, Long> id;

	public static final String AUCTION_END = "auctionEnd";
	public static final String NAME = "name";
	public static final String ID = "id";

}
```

最后查了很久原因，发现是跟验证相关的两个包的版本问题

- javax-validation.version: ~~2.0.x.Final~~ **1.x.x.Final**
- hibernate.validator.version: ~~6.0.x.Final~~ **5.x.x.Final**

回退到低版本的包，生成 Metamodel 元数据就没有问题，猜测是 apt 相关的部分还没有对新版验证包进行适配。

另外说到 Java 的 apt (Annotation Processing Tool), 我在本机上尝试体验下，
结果发现 apt 已经不在 JDK 8 的可执行文件中，经过一番搜索发现 apt 在 JDK 7 之后已经被废弃

[apt API deprecated in JDK 7](http://openjdk.java.net/jeps/117)


