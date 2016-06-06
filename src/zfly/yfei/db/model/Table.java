package zfly.yfei.db.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录实体类与数据库表的对应关系
 *
 * @author YFei
 */
public class Table {

    private String name;
    private Column primaryKey;
    private List<Column> columns = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Column getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Column primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void addColumn(Column column) {
        if (!columns.contains(column)) {
            columns.add(column);
        }
    }

    public List<Column> getColumns() {
        return columns;
    }

    public boolean isPrimaryKey(final Column column) {
        return primaryKey != null && primaryKey.equals(column);
    }

    @Override
    public String toString() {
        return String.format("{name=%s, primaryKey=%s, columns=%s}", name,
                primaryKey == null ? "null" : primaryKey.toString(), columns == null ? "null" : columns.toString());
    }

}
