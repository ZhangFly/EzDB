package zfly.ez.db.core.condition;

import zfly.ez.db.model.Table;

import java.sql.SQLException;

/**
 *
 * Created by YFei on 16/6/6.
 */
public class Having extends Condition{
    public Having(String fmt, Object... args) {
        super(fmt, args);
    }

    @Override
    public String getCondition(Table table) throws SQLException {
        return null;
    }
}
