package zfly.yfei.db;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;

/**
 * 记录实体类属性与数据库表中列的对应关系
 *
 * @author YFei
 */
class Column {
    private String name;
    private Field field;

    public Column() {
        this(null, null);
    }

    Column(final String name, final Field field) {
        this.name = name;
        this.field = field;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    Field getField() {
        return field;
    }

    void setField(Field field) {
        this.field = field;
    }

    boolean equals(final Column column) {
        return column != null && StringUtils.equals(name, column.getName());
    }

    @Override
    public String toString() {
        return String.format("{name=%s, filed=%s}", name, field == null ? "null" : field.toString());
    }
}
