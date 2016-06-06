package zfly.yfei.db.model;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;

/**
 * 记录实体类属性与数据库表中列的对应关系
 *
 * @author YFei
 */
public class Column {
    private String name;
    private Field field;

    public Column() {
        this(null, null);
    }

    public Column(final String name, final Field field) {
        this.name = name;
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public boolean equals(final Column column) {
        return column != null && StringUtils.equals(name, column.getName());
    }

    @Override
    public String toString() {
        return String.format("{name=%s, filed=%s}", name, field == null ? "null" : field.toString());
    }
}
