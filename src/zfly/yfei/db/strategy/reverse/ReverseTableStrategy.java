package zfly.yfei.db.strategy.reverse;

import java.lang.reflect.Field;

public abstract class ReverseTableStrategy {

    ReverseTableDelegate delegate;

    public abstract <T> T doReverse(Class<T> clazz);

    public ReverseTableStrategy setReserseDataDelegate(ReverseTableDelegate dataSource) {
        this.delegate = dataSource;
        return this;
    }

    public ReverseTableDelegate getReserseDataDelegate() {
        return delegate;
    }
}
