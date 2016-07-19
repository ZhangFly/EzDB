package zfly.ez.db.strategy.reflect;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import zfly.ez.db.core.annotation.EzColumn;
import zfly.ez.db.core.annotation.EzTable;

public class ReflectTableImpl1 extends ReflectTableStrategy {

	@Override
	public void doReflect(Class<?> clazz) {

		if (clazz == null) {
			return;
		}

		reflectTableName(clazz);

		Arrays.stream(clazz.getDeclaredFields())
				.filter(field -> !(field.isAnnotationPresent(EzColumn.class) && field.getAnnotation(EzColumn.class).ignore()))
				.forEach( field -> {
					EzColumn additionInfo = field.getAnnotation(EzColumn.class);
					reflectPrimaryKey(additionInfo, field, clazz);
					reflectColumnName(additionInfo, field, clazz);
		});
	}

	private void reflectTableName(final Class<?> clazz) {
		String tableName;
		if (clazz.isAnnotationPresent(EzTable.class)) {
			tableName = clazz.getAnnotation(EzTable.class).value();
		} else {
			tableName = clazz.getSimpleName();
		}
		notifyGotTableName(tableName, clazz);
	}

	private void reflectColumnName(final EzColumn additionInfo, final Field f, final Class<?> clazz) {
		String columnName;
		if (additionInfo == null) {
			columnName = f.getName();
		} else {
			columnName = StringUtils.equals(additionInfo.alias(), "") ? f.getName() : additionInfo.alias();
		}
		notifyGotColumnName(columnName, f, clazz);
	}

	private void reflectPrimaryKey(final EzColumn additionInfo, final Field f, final Class<?> clazz) {
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
