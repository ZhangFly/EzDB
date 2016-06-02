package zfly;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface YFeiColumn {

	public String alias() default "";

	public boolean primaryKey() default false;

	public boolean ignore() default false;
}
