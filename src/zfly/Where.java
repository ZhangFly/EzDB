package zfly;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

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
	final private static Logger log = Logger.getLogger(Where.class);

	private String sql;

	/**
	 * 构建一个空的限定条件
	 * 
	 * @return
	 */
	public static Where emptyWhere() {
		return new Where(null);
	}

	/**
	 * 构造限定条件
	 * 
	 * @param fmt
	 *            限定条件格式
	 *            t：表名占位符；$1,$2...：属性占位符；$c：值占位符
	 * @param args
	 *            限定条件参数
	 */
	public Where(final String fmt, final Object... args) {
		sql = fmt == null ? "" : " WHERE " + fmt;
		for (Object arg : args) {
			sql = sql.replaceFirst("\\$c", "'" + arg + "'");
		}
	}

	String getCondition(final Table table) throws SQLException {

		final String regex = "(t\\.)?\\$([0-9]+)|(t\\.)";
		final String tableName = table.getName();
		final Matcher macther = Pattern.compile(regex).matcher(sql);
		while (macther.find()) {
			if (macther.group(2) == null) {
				sql = macther.replaceFirst(tableName + ".");
			} else {
				final int position = Integer.valueOf(macther.group(2));
				if (position > table.getColumns().size() || position < 1) {
					throw new SQLException("Placeholder was overflow!!");
				}
				final Column columnInfo = table.getColumns().get(position - 1);
				sql = macther.replaceFirst(String.format("%s.%s", tableName, columnInfo.getName()));
			}
			macther.reset(sql);
		}
		return sql + ";";
	}

	static Where shrotcutForId(final Object entity, final Table table) {
		try {
			final Column primaryKey = table.getPrimaryKey();
			if (primaryKey == null) {
				return Where.emptyWhere();
			}

			primaryKey.getField().setAccessible(true);
			if (primaryKey.getField().getType().isPrimitive()) {
				return shrotcutForId(primaryKey.getField().getInt(entity), table);
			} else {
				return shrotcutForId(((Integer) primaryKey.getField().get(entity)).intValue(), table);
			}

		} catch (IllegalAccessException e) {
			log.error(e.getMessage());
			return Where.emptyWhere();
		}
	}

	static Where shrotcutForId(final int id, final Table table) {

		if (table == null) {
			return Where.emptyWhere();
		}

		final Column primaryKey = table.getPrimaryKey();
		if (primaryKey == null) {
			return Where.emptyWhere();
		}

		return new Where(table.getName() + "." + primaryKey.getName() + "=$c", id);
	}
}