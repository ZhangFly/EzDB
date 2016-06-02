package zfly;

import org.apache.log4j.Logger;

class SQLSaveBuilder implements SQLBuilder {

	private static Logger log = Logger.getLogger(SQLUpdateBuilder.class);

	@Override
	public String getSql(Object entity, Table table, Where condition) {
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
			sql.append(condition.getSql(table));
			return sql.toString();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.error("Should not go to here!!");
			return null;
		}
	}

}
