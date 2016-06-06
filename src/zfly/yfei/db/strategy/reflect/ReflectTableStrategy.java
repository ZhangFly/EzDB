package zfly.yfei.db.strategy.reflect;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class ReflectTableStrategy {

    private List<ReflectTableObserver> observers = new ArrayList<>();

    public ReflectTableStrategy addGotTableInfoObserver(ReflectTableObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
        return this;
    }

    public void removeGotTableInfoObserver(ReflectTableObserver observer) {
        if (observers.contains(observer)) {
            observers.remove(observer);
        }
    }

    public void clearGotTableInfoObserver() {
        observers.clear();
    }

    void notifyGotPrimaryKey(final String primaryKey, final Field f, final Class<?> clazz) {
        for (ReflectTableObserver observer : observers) {
            observer.gotPrimaryKey(primaryKey, f, clazz);
        }
    }

    void notifyGotColumnName(final String columnName, final Field f, final Class<?> clazz) {
        for (ReflectTableObserver observer : observers) {
            observer.gotColumnName(columnName, f, clazz);
        }
    }

    void notifyGotTableName(final String tableName, final Class<?> clazz) {
        for (ReflectTableObserver observer : observers) {
            observer.gotTableName(tableName, clazz);
        }
    }

    public abstract void doReflect(final Class<?> clazz);
}
