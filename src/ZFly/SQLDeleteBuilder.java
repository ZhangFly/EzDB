package ZFly;

class SQLDeleteBuilder implements SQLBuilder {

	@Override
	public StringBuilder getBaseBuilder(Object entity, Table table) {
		final StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(table.getName());
		return sql;
	}

}
