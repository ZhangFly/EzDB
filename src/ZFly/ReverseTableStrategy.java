package ZFly;

import java.lang.reflect.Field;

abstract class ReverseTableStrategy {

	protected ReserseTableDelegate delegate;

	public abstract <T> T excute(Class<T> clazz);

	public interface ReserseTableDelegate {

		int getFieldCount();

		Field getField(final int position);

		Object getFieldVale(final int position);

	}

	public ReverseTableStrategy setReserseDataDelegate(ReserseTableDelegate dataSource) {
		this.delegate = dataSource;
		return this;
	}

	public ReserseTableDelegate getReserseDataDelegate() {
		return delegate;
	}
}
