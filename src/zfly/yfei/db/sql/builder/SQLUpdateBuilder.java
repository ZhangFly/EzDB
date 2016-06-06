package zfly.yfei.db.sql.builder;

import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import zfly.yfei.db.model.Column;
import zfly.yfei.db.model.Table;
import zfly.yfei.db.condition.Condition;

public class SQLUpdateBuilder extends SQLBuilder {

	private static Logger log = Logger.getLogger(SQLUpdateBuilder.class);

	public SQLUpdateBuilder(Object entity, Table table, Condition... conditions) {
		super(entity, table, conditions);
	}

	@Override
	public String getSql() throws SQLException {
		try {
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE ");
			sql.append(table.getName());
			sql.append(" SET ");
			for (Column column : table.getColumns()) {
				if (!table.isPrimaryKey(column)) {
					sql.append(table.getName());
					sql.append(".");
					sql.append(column.getName());
					sql.append("='");
					column.getField().setAccessible(true);
					sql.append(column.getField().get(entity));
					sql.append("',");
				}
			}
			sql.delete(sql.length() - 1, sql.length());
			for (Condition condition : conditions) {
				sql.append(condition.getCondition(table));
			}
			sql.append(";");
			return sql.toString();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.error(e.getMessage());
			return StringUtils.EMPTY;
		}
	}

}
