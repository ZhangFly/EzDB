package ZFly;

interface SQLBuilder {

	abstract StringBuilder getBaseBuilder(final Object entity, final Table table);

}
