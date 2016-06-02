package zfly;

import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

class SQLFindBuilder implements SQLBuilder {

	final private static Logger log = Logger.getLogger(SQLFindBuilder.class);

	@Override
	public String getSql(Object entity, Table table, Where condition) {
		try {
			final StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM ");
			sql.append(table.getName());
			sql.append(condition.getSql(table));
			return sql.toString();
		} catch (SQLException e) {
			log.error(e.getMessage());
			return StringUtils.EMPTY;
		}

	}

}
