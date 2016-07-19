package zfly.ez.db.strategy.reflect;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import zfly.ez.db.core.annotation.YFeiColumn;
import zfly.ez.db.core.annotation.YFeiTable;

public class ReflectTableImpl1 extends ReflectTableStrategy {

	@Override
	public void doReflect(Class<?> clazz) {

		if (clazz == null) {
			return;
		}

		reflectTableName(clazz);

		Arrays.stream(clazz.getDeclaredFields())
				.filter(field -> !(field.isAnnotationPresent(YFeiColumn.class) && field.getAnnotation(YFeiColumn.class).ignore()))
				.forEach( field -> {
					YFeiColumn additionInfo = field.getAnnotation(YFeiColumn.class);
					reflectPrimaryKey(additionInfo, field, clazz);
					reflectColumnName(additionInfo, field, clazz);
		});
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
