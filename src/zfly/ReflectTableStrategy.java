package zfly;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

abstract class ReflectTableStrategy {

	private List<ReflectTableObserver> observers = new ArrayList<ReflectTableObserver>();

	public interface ReflectTableObserver {

		void gotPrimaryKey(final String primaryKey, final Field f, final Class<?> clazz);

		void gotColumnName(final String columnName, final Field f, final Class<?> clazz);

		void gotTableName(final String tableName, final Class<?> clazz);
	}

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

	protected void notifyGotPrimaryKey(final String primaryKey, final Field f, final Class<?> clazz) {
		for (ReflectTableObserver observer : observers) {
			observer.gotPrimaryKey(primaryKey, f, clazz);
		}
	}

	protected void notifyGotColumnName(final String columnName, final Field f, final Class<?> clazz) {
		for (ReflectTableObserver observer : observers) {
			observer.gotColumnName(columnName, f, clazz);
		}
	}

	protected void notifyGotTableName(final String tableName, final Class<?> clazz) {
		for (ReflectTableObserver observer : observers) {
			observer.gotTableName(tableName, clazz);
		}
	}

	public abstract void excute(final Class<?> clazz);
}
