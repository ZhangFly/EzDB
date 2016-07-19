package zfly.ez.db.sql.builder;

import zfly.ez.db.core.condition.Condition;
import zfly.ez.db.model.Table;

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
