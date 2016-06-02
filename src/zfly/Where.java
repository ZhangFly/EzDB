package zfly;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 查询条件生成类。支持 t 表名通配符；支持 $1,$2... 属性通配符；支持值通配符 $c。
 * 
 * Class Person {
 * int id;
 * String name;
 * }
 * 
 * new Where("$1>$c AND $2=$c", 1, "YFei")
 * -> "WHERE person.id>'1' AND person.name='YFei'"
 * 
 * @author YFei
 *
 */
public class Where {

	private String sql;

	public Where(final String fmt, final Object... args) {
		this.sql = "WHERE " + fmt;
		for (Object arg : args) {
			sql = sql.replaceFirst("\\$c", "'" + arg + "'");
		}
	}

	public String getSql(final Table table) {

		final String regex = "(t\\.)?\\$([0-9]+)|(t\\.)";

		final String tableName = table.getName();

		final Matcher macther = Pattern.compile(regex).matcher(sql);
		while (macther.find()) {
			if (macther.group(2) == null) {
				sql = macther.replaceFirst(tableName + ".");
			} else {
				final int position = Integer.valueOf(macther.group(2));
				final Column columnInfo = table.getColumns().get(position - 1);
				sql = macther.replaceFirst(tableName + "." + columnInfo.getName());
			}
			macther.reset(sql);
		}
		return sql;
	}

	public static Where shrotcutForId(final Object entity, final Table table) {
		try {
			final Column primaryKey = table.getPrimaryKey();
			if (primaryKey == null) {
				return null;
			}
			primaryKey.getField().setAccessible(true);
			if (primaryKey.getField().getType().isPrimitive()) {
				return shrotcutForId(primaryKey.getField().getInt(entity), table);
			} else {
				return shrotcutForId(((Integer) primaryKey.getField().get(entity)).intValue(), table);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Where shrotcutForId(final int id, final Table table) {
		final Column primaryKey = table.getPrimaryKey();
		if (primaryKey == null) {
			return null;
		}
		return new Where(table.getName() + "." + primaryKey.getName() + "=$c", id);
	}
}