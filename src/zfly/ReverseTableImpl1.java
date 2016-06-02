package zfly;

import java.lang.reflect.Field;

class ReverseTableImpl1 extends ReverseTableStrategy {

	public ReverseTableImpl1(ReserseTableDelegate dataSource) {
		this.delegate = dataSource;
	}

	@Override
	public <T> T excute(Class<T> clazz) {
		try {
			final T entity = clazz.newInstance();
			for (int i = 0; i < delegate.getFieldCount(); i++) {
				final Field f = delegate.getField(i);
				f.setAccessible(true);
				if (f.getType().isPrimitive()) {
					if (f.getType() == byte.class) {
						f.setByte(entity, ((Byte) delegate.getFieldVale(i)).byteValue());
					} else if (f.getType() == short.class) {
						f.setShort(entity, ((Short) delegate.getFieldVale(i)).shortValue());
					} else if (f.getType() == int.class) {
						f.setInt(entity, ((Integer) delegate.getFieldVale(i)).intValue());
					} else if (f.getType() == long.class) {
						f.setLong(entity, ((Long) delegate.getFieldVale(i)).longValue());
					} else if (f.getType() == float.class) {
						f.setFloat(entity, ((Float) delegate.getFieldVale(i)).floatValue());
					} else if (f.getType() == double.class) {
						f.setDouble(entity, ((Double) delegate.getFieldVale(i)).doubleValue());
					} else if (f.getType() == boolean.class) {
						f.setBoolean(entity, ((Boolean) delegate.getFieldVale(i)).booleanValue());
					}
				} else {
					f.set(entity, delegate.getFieldVale(i));
				}
			}
			return entity;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

}
