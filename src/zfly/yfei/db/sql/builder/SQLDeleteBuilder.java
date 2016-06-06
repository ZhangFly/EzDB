package zfly.yfei.db.sql.builder;

import zfly.yfei.db.model.Table;
import zfly.yfei.db.condition.Condition;

import java.sql.SQLException;

public class SQLDeleteBuilder extends SQLBuilder {

	public SQLDeleteBuilder(Object entity, Table table, Condition... conditions) {
		super(entity, table, conditions);
	}

	@Override
	public String getSql() throws SQLException {

		final StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(table.getName());
		for (Condition condition : conditions) {
			sql.append(condition.getCondition(table));
		}
		sql.append(";");
		return sql.toString();

	}

}
