package zfly.yfei.db.sql.builder;

import zfly.yfei.db.model.Table;
import zfly.yfei.db.core.condition.Condition;

import java.sql.SQLException;

public abstract class SQLBuilder {

    Condition[] conditions;
    Object entity;
    Table table;

    SQLBuilder(final Object entity, final Table table, final Condition... conditions) {
        this.entity = entity;
        this.table = table;
        this.conditions = conditions;
    }

    public abstract String getSql() throws SQLException;

}
