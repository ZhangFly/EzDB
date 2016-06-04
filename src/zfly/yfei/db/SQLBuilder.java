package zfly.yfei.db;

import java.sql.SQLException;

abstract class SQLBuilder {

    Where condition;
    Object entity;
    Table table;

    SQLBuilder(final Object entity, final Table table, final Where condition) {
        this.entity = entity;
        this.table = table;
        this.condition = condition;
    }

    abstract String getSql() throws SQLException;

}
