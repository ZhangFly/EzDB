package ZFly;

class SQLUpdateBuilder implements SQLBuilder {

	@Override
	public StringBuilder getBaseBuilder(Object entity, Table table) {
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
			return sql;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

}
