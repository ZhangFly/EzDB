package zfly.yfei.db;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

abstract class ReflectTableStrategy {

    private List<ReflectTableObserver> observers = new ArrayList<>();

    interface ReflectTableObserver {

        void gotPrimaryKey(final String primaryKey, final Field f, final Class<?> clazz);

        void gotColumnName(final String columnName, final Field f, final Class<?> clazz);

        void gotTableName(final String tableName, final Class<?> clazz);
    }

    ReflectTableStrategy addGotTableInfoObserver(ReflectTableObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
        return this;
    }

    void removeGotTableInfoObserver(ReflectTableObserver observer) {
        if (observers.contains(observer)) {
            observers.remove(observer);
        }
    }

    void clearGotTableInfoObserver() {
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

    abstract void doReflect(final Class<?> clazz);
}
