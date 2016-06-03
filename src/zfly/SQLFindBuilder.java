package zfly;

import java.sql.SQLException;

class SQLFindBuilder implements SQLBuilder {

	@Override
	public String getSql(Object entity, Table table, Where condition) throws SQLException {

		final StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ");
		sql.append(table.getName());
		sql.append(condition.getCondition(table));
		return sql.toString();

	}

}
