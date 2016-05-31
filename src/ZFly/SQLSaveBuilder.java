package ZFly;

class SQLSaveBuilder implements SQLBuilder {

	@Override
	public StringBuilder getBaseBuilder(Object entity, Table table) {
		try {
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO ");
			sql.append(table.getName());
			sql.append(" (");
			for (Column column : table.getColumns()) {
				if (!table.isPrimaryKey(column)) {
					sql.append(table.getName());
					sql.append(".");
					sql.append(column.getName());
					sql.append(",");
				}
			}
			sql.delete(sql.length() - 1, sql.length());
			sql.append(") VALUES (");
			for (Column column : table.getColumns()) {
				if (!table.isPrimaryKey(column)) {
					sql.append("'");
					column.getField().setAccessible(true);
					sql.append(column.getField().get(entity));
					sql.append("',");
				}
			}
			sql.delete(sql.length() - 1, sql.length());
			sql.append(")");
			return sql;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

}
