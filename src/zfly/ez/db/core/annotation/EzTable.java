package zfly.ez.db.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EzTable {
	/**
	 * 为数据库表设置别名，如果不设置，自动将类别映射为数据库表名
	 * 
	 * @return
	 */
	String value() default "";

}
