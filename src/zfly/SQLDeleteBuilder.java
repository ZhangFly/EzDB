package zfly;

import com.sun.istack.internal.NotNull;

class SQLDeleteBuilder implements SQLBuilder {

	@Override
	public StringBuilder getBaseBuilder(Object entity, @NotNull Table table) {
		final StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(table.getName());
		return sql;
	}

}
