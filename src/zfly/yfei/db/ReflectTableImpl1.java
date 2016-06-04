package zfly.yfei.db;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;

class ReflectTableImpl1 extends ReflectTableStrategy {

	@Override
	public void doReflect(Class<?> clazz) {

		if (clazz == null) {
			return;
		}

		reflectTableName(clazz);

		for (Field f : clazz.getDeclaredFields()) {
			YFeiColumn additionInfo = null;
			if (f.isAnnotationPresent(YFeiColumn.class)) {
				additionInfo = f.getAnnotation(YFeiColumn.class);
				if (additionInfo.ignore()) {
					continue;
				}
			}
			reflectPrimaryKey(additionInfo, f, clazz);
			reflectColumnName(additionInfo, f, clazz);
		}
	}

	private void reflectTableName(final Class<?> clazz) {
		String tableName;
		if (clazz.isAnnotationPresent(YFeiTable.class)) {
			tableName = clazz.getAnnotation(YFeiTable.class).value();
		} else {
			tableName = clazz.getSimpleName();
		}
		notifyGotTableName(tableName, clazz);
	}

	private void reflectColumnName(final YFeiColumn additionInfo, final Field f, final Class<?> clazz) {
		String columnName;
		if (additionInfo == null) {
			columnName = f.getName();
		} else {
			columnName = StringUtils.equals(additionInfo.alias(), "") ? f.getName() : additionInfo.alias();
		}
		notifyGotColumnName(columnName, f, clazz);
	}

	private void reflectPrimaryKey(final YFeiColumn additionInfo, final Field f, final Class<?> clazz) {
		if (additionInfo == null) {
			return;
		}
		if (!additionInfo.primaryKey()) {
			return;
		}
		String primaryKey;
		if (!StringUtils.equals(additionInfo.alias(), "")) {
			primaryKey = additionInfo.alias();
		} else {
			primaryKey = f.getName();
		}
		notifyGotPrimaryKey(primaryKey, f, clazz);
	}
}
