package zfly;

interface SQLBuilder {

	abstract String getSql(final Object entity, final Table table, final Where condition);

}
