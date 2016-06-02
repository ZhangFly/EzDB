package zfly;

class SQLFindBuilder implements SQLBuilder {

	@Override
	public StringBuilder getBaseBuilder(Object entity, Table table) {
		final StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ");
		sql.append(table.getName());
		return sql;
	}

}
