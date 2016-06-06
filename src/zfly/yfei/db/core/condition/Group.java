package zfly.yfei.db.core.condition;

import zfly.yfei.db.model.Table;

import java.sql.SQLException;

/**
 *
 * Created by YFei on 16/6/6.
 */
public class Group extends Condition {
    public Group(String fmt, Object... args) {
        super(fmt, args);
    }

    @Override
    public String getCondition(Table table) throws SQLException {
        return null;
    }
}
