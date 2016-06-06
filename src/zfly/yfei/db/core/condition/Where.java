package zfly.yfei.db.core.condition;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import zfly.yfei.db.model.Column;
import zfly.yfei.db.model.Table;

import java.sql.SQLException;

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
public class Where extends Condition {

	final private static Logger log = Logger.getLogger(Where.class);

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
		super(fmt,args);
	}

	@Override
	public String getCondition(final Table table) throws SQLException {
		final String comm = parsePlaceholder(table);
		return (StringUtils.isEmpty(comm) ? StringUtils.EMPTY : " WHERE ") + comm;

	}

	public static Where shortcutForId(final Object entity, final Table table) {
		try {
			final Column primaryKey = table.getPrimaryKey();
			if (primaryKey == null) {
				return null;
			}

			primaryKey.getField().setAccessible(true);
			if (primaryKey.getField().getType().isPrimitive()) {
				return shortcutForId(primaryKey.getField().getInt(entity), table);
			} else {
				return shortcutForId(((Integer) primaryKey.getField().get(entity)).intValue(), table);
			}

		} catch (IllegalAccessException e) {
			log.error(e.getMessage());
			return null;
		}
	}

	public static Where shortcutForId(final int id, final Table table) {

		if (table == null) {
			return null;
		}

		final Column primaryKey = table.getPrimaryKey();
		if (primaryKey == null) {
			return null;
		}

		return new Where(table.getName() + "." + primaryKey.getName() + "=$c", id);
	}
}