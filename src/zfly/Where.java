package zfly;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.sun.istack.internal.NotNull;

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

	final public static Where NULL = new Where("");
	final private static Logger log = Logger.getLogger(Where.class);

	private String sql;

	/**
	 * 构造限定条件
	 * 
	 * @param fmt
	 *            限定条件格式
	 * @param args
	 *            限定条件参数
	 */
	public Where(@NotNull final String fmt, final Object... args) {
		this.sql = "WHERE " + fmt;
		for (Object arg : args) {
			sql = sql.replaceFirst("\\$c", "'" + arg + "'");
		}
		// 消除SQL语句中的注释符号，防止SQL注入攻击
		sql.replaceAll("--", "");
	}

	String getSql(@NotNull final Table table) {

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

	public static Where shrotcutForId(@NotNull final Object entity, @NotNull final Table table) {
		try {
			final Column primaryKey = table.getPrimaryKey();
			if (primaryKey == null) {
				return Where.NULL;
			}

			primaryKey.getField().setAccessible(true);
			if (primaryKey.getField().getType().isPrimitive()) {
				return shrotcutForId(primaryKey.getField().getInt(entity), table);
			} else {
				return shrotcutForId(((Integer) primaryKey.getField().get(entity)).intValue(), table);
			}

		} catch (IllegalAccessException e) {
			log.error(e.getMessage());
			return Where.NULL;
		}
	}

	public static Where shrotcutForId(final int id, @NotNull final Table table) {

		final Column primaryKey = table.getPrimaryKey();
		if (primaryKey == null) {
			return Where.NULL;
		}

		return new Where(table.getName() + "." + primaryKey.getName() + "=$c", id);
	}
}