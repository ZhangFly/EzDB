package zfly;

import java.lang.reflect.Field;

abstract class ReverseTableStrategy {

	protected ReverseTableDelegate delegate;

	public abstract <T> T doReverse(Class<T> clazz);

	public interface ReverseTableDelegate {

		int getFieldCount();

		Field getField(final int position);

		Object getFieldVale(final int position);

	}

	public ReverseTableStrategy setReserseDataDelegate(ReverseTableDelegate dataSource) {
		this.delegate = dataSource;
		return this;
	}

	public ReverseTableDelegate getReserseDataDelegate() {
		return delegate;
	}
}
