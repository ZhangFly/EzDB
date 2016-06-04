package zfly.yfei.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface YFeiColumn {
	/**
	 * 设置数据库表字段别名，如果不设置将自动将属性名映射为表字段名
	 * 
	 * @return
	 */
	String alias() default "";

	/**
	 * 标记该属性是否为数据库表主键，数据库表主键应该有唯一注解，请勿对同一实体类重复标记
	 * 
	 * @return
	 */
	boolean primaryKey() default false;

	/**
	 * 不映射该属性到数据库表
	 * 
	 * @return
	 */
	boolean ignore() default false;
}
