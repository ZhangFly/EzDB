package zfly.yfei.db;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录实体类与数据库表的对应关系
 *
 * @author YFei
 */
class Table {

    private String name;
    private Column primaryKey;
    private List<Column> columns = new ArrayList<>();

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    Column getPrimaryKey() {
        return primaryKey;
    }

    void setPrimaryKey(Column primaryKey) {
        this.primaryKey = primaryKey;
    }

    void addColumn(Column column) {
        if (!columns.contains(column)) {
            columns.add(column);
        }
    }

    List<Column> getColumns() {
        return columns;
    }

    boolean isPrimaryKey(final Column column) {
        return primaryKey != null && primaryKey.equals(column);
    }

    @Override
    public String toString() {
        return String.format("{name=%s, primaryKey=%s, columns=%s}", name,
                primaryKey == null ? "null" : primaryKey.toString(), columns == null ? "null" : columns.toString());
    }

}
