package zfly;

import java.sql.SQLException;

interface SQLBuilder {

	abstract String getSql(final Object entity, final Table table, final Where condition) throws SQLException;

}
