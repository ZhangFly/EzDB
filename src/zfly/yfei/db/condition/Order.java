package zfly.yfei.db.condition;

import org.apache.commons.lang3.StringUtils;
import zfly.yfei.db.model.Table;

import java.sql.SQLException;

/**
 *
 * Created by YFei on 16/6/6.
 */
public class Order extends Condition {

    public Order(String fmt, Object... args) {
        super(fmt, args);
    }

    @Override
    public String getCondition(Table table) throws SQLException {
        final String comm = parsePlaceholder(table);
        return (StringUtils.isEmpty(comm) ? StringUtils.EMPTY : " ORDER BY ") + comm;
    }
}
