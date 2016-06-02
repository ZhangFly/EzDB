package zfly;

class SQLFindBuilder implements SQLBuilder {

	@Override
	public String getSql(Object entity, Table table, Where condition) {
		final StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ");
		sql.append(table.getName());
		sql.append(condition.getSql(table));
		return sql.toString();
	}

}
