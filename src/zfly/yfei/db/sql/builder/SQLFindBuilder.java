package zfly.yfei.db.sql.builder;

import org.apache.commons.lang3.ArrayUtils;
import zfly.yfei.db.model.Table;
import zfly.yfei.db.core.condition.Condition;

import java.sql.SQLException;

public class SQLFindBuilder extends SQLBuilder {

    public SQLFindBuilder(Object entity, Table table, Condition... conditions) {
        super(entity, table, conditions);
    }

    @Override
    public String getSql() throws SQLException {

        final StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append(table.getName());
        if (!ArrayUtils.isEmpty(conditions))
            for (Condition condition : conditions) {
                sql.append(condition.getCondition(table));
            }
        sql.append(";");
        return sql.toString();

    }

}
