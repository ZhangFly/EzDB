package zfly;

import java.sql.SQLException;

class SQLDeleteBuilder implements SQLBuilder {

	@Override
	public String getSql(Object entity, Table table, Where condition) throws SQLException {

		final StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(table.getName());
		sql.append(condition.getCondition(table));
		return sql.toString();

	}

}
