package ZFly;

import java.lang.reflect.Field;

/**
 * 记录实体类属性与数据库表中列的对应关系
 * 
 * @author YFei
 *
 */
class Column {
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

	public String toString() {
		return String.format("{name=%s, filed=%s}", name, field == null ? "null" : field.toString());
	}

	public boolean equals(final Column columnInfo) {
		if (columnInfo == null) {
			return false;
		}
		return this.getName().equals(columnInfo.getName());
	}
}
