package zfly.ez.db.strategy.reflect;

import java.lang.reflect.Field;

/**
 * Created by YFei on 16/6/6.
 */
public interface ReflectTableObserver {
    void gotPrimaryKey(String primaryKey, Field f, Class<?> clazz);

    void gotColumnName(String columnName, Field f, Class<?> clazz);

    void gotTableName(String tableName, Class<?> clazz);
}
