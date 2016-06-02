package zfly;

import com.sun.istack.internal.NotNull;

interface SQLBuilder {

	abstract StringBuilder getBaseBuilder(final Object entity, @NotNull final Table table);

}
