package zfly;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

class ReverseTableImpl1 extends ReverseTableStrategy {

	final private static Logger log = Logger.getLogger(ReverseTableImpl1.class);

	public ReverseTableImpl1(ReverseTableDelegate dataSource) {
		this.delegate = dataSource;
	}

	@Override
	public <T> T doReverse(Class<T> clazz) {
		try {

			if (clazz == null) {
				return null;
			}

			final T entity = clazz.newInstance();
			for (int i = 0; i < delegate.getFieldCount(); i++) {
				final Field f = delegate.getField(i);

				if (f == null) {
					log.error("Field can not be null!!");
					return null;
				}
				f.setAccessible(true);

				final Object value = delegate.getFieldVale(i);

				if (value == null) {
					log.error("Value can not be null!!");
					return null;
				}

				if (f.getType().isPrimitive()) {
					if (f.getType() == byte.class) {
						f.setByte(entity, ((Byte) value).byteValue());
					} else if (f.getType() == short.class) {
						f.setShort(entity, ((Short) value).shortValue());
					} else if (f.getType() == int.class) {
						f.setInt(entity, ((Integer) value).intValue());
					} else if (f.getType() == long.class) {
						f.setLong(entity, ((Long) value).longValue());
					} else if (f.getType() == float.class) {
						f.setFloat(entity, ((Float) value).floatValue());
					} else if (f.getType() == double.class) {
						f.setDouble(entity, ((Double) value).doubleValue());
					} else if (f.getType() == boolean.class) {
						f.setBoolean(entity, ((Boolean) value).booleanValue());
					}
				} else {
					f.set(entity, value);
				}
			}
			return entity;
		} catch (InstantiationException | IllegalAccessException e) {
			log.error(e.getMessage());
			return null;
		}
	}

}
