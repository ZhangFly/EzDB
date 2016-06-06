package zfly.yfei.db.strategy.reverse;

import java.lang.reflect.Field;

/**
 * Created by YFei on 16/6/6.
 */
public interface ReverseTableDelegate {
    int getFieldCount();

    Field getField(int position);

    Object getFieldVale(int position);
}
