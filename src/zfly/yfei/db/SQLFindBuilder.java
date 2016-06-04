package zfly.yfei.db;

import java.sql.SQLException;

class SQLFindBuilder extends SQLBuilder {

	SQLFindBuilder(Object entity, Table table, Where condition) {
		super(entity, table, condition);
	}

	@Override
	String getSql() throws SQLException {

		final StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ");
		sql.append(table.getName());
		sql.append(condition.getCondition(table));
		return sql.toString();

	}

}
