package ZFly;

import java.lang.reflect.Field;

class ReflectTableImpl1 extends ReflectTableStrategy {

	@Override
	public void excute(Class<?> clazz) {
		notifyGotTableName(reflectTableName(clazz), clazz);
		for (Field f : clazz.getDeclaredFields()) {
			final YFeiColumn additionInfo = f.getAnnotation(YFeiColumn.class);
			if (additionInfo != null && additionInfo.ignore()) {
				continue;
			}
			final String primaryKey = reflectPrimaryKey(additionInfo, f);
			if (primaryKey != null) {
				notifyGotPrimaryKey(primaryKey, f, clazz);
			}
			notifyGotColumnName(reflectColumnName(additionInfo, f), f, clazz);
		}
	}

	private String reflectTableName(final Class<?> clazz) {
		final String tableName;
		if (clazz.isAnnotationPresent(YFeiTable.class)) {
			tableName = clazz.getAnnotation(YFeiTable.class).value();
		} else {
			tableName = clazz.getSimpleName();
		}
		return tableName;
	}

	private String reflectColumnName(final YFeiColumn additionInfo, final Field f) {
		if (additionInfo == null) {
			return f.getName();
		}
		if (!additionInfo.alias().equals("")) {
			return additionInfo.alias();
		}
		return f.getName();
	}

	private String reflectPrimaryKey(final YFeiColumn additionInfo, final Field f) {
		if (additionInfo == null) {
			return null;
		}
		if (!additionInfo.primaryKey()) {
			return null;
		}
		if (!additionInfo.alias().equals("")) {
			return additionInfo.alias();
		}
		return f.getName();
	}
}
