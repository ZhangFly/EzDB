package zfly.yfei.db;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

class ReverseTableImpl1 extends ReverseTableStrategy {

    final private static Logger log = Logger.getLogger(ReverseTableImpl1.class);

    ReverseTableImpl1(ReverseTableDelegate dataSource) {
        this.delegate = dataSource;
    }

    @Override
    <T> T doReverse(Class<T> clazz) {
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
                        f.setByte(entity, (Byte) value);
                    } else if (f.getType() == short.class) {
                        f.setShort(entity, (Short) value);
                    } else if (f.getType() == int.class) {
                        f.setInt(entity, (Integer) value);
                    } else if (f.getType() == long.class) {
                        f.setLong(entity, (Long) value);
                    } else if (f.getType() == float.class) {
                        f.setFloat(entity, (Float) value);
                    } else if (f.getType() == double.class) {
                        f.setDouble(entity, (Double) value);
                    } else if (f.getType() == boolean.class) {
                        f.setBoolean(entity, (Boolean) value);
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
