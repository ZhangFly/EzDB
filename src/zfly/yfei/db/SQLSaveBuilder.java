package zfly.yfei.db;

import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

class SQLSaveBuilder extends SQLBuilder {

	private static Logger log = Logger.getLogger(SQLUpdateBuilder.class);

	SQLSaveBuilder(Object entity, Table table, Where condition) {
		super(entity, table, condition);
	}

	@Override
	String getSql() throws SQLException {
		try {
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO ");
			sql.append(table.getName());
			sql.append(" (");

			table.getColumns().stream().filter(column -> !table.isPrimaryKey(column)).forEach(column -> {
				sql.append(table.getName());
				sql.append(".");
				sql.append(column.getName());
				sql.append(",");
			});

			sql.delete(sql.length() - 1, sql.length());
			sql.append(") VALUES (");

			table.getColumns().stream().filter(column -> !table.isPrimaryKey(column)).forEach(column -> {
				sql.append("'");
				column.getField().setAccessible(true);
				try {
					sql.append(column.getField().get(entity));
				} catch (IllegalAccessException e) {
					sql.append("");
				}
				sql.append("',");
			});


			sql.delete(sql.length() - 1, sql.length());
			sql.append(")");
			sql.append(condition.getCondition(table));
			return sql.toString();
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage());
			return StringUtils.EMPTY;
		}
	}

}
