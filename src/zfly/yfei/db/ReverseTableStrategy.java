package zfly.yfei.db;

import java.lang.reflect.Field;

abstract class ReverseTableStrategy {

    ReverseTableDelegate delegate;

    abstract <T> T doReverse(Class<T> clazz);

    interface ReverseTableDelegate {

        int getFieldCount();

        Field getField(final int position);

        Object getFieldVale(final int position);

    }

    ReverseTableStrategy setReserseDataDelegate(ReverseTableDelegate dataSource) {
        this.delegate = dataSource;
        return this;
    }

    ReverseTableDelegate getReserseDataDelegate() {
        return delegate;
    }
}
