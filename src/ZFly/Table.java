package ZFly;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录实体类与数据库表的对应关系
 * 
 * @author YFei
 *
 */
class Table {

	private String name;
	private Column primaryKey;
	private List<Column> columns = new ArrayList<Column>();

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
		columns.add(column);
	}

	public List<Column> getColumns() {
		return columns;
	}

	public boolean isPrimaryKey(final Column columnInfo) {
		if (primaryKey == null) {
			return false;
		}
		return primaryKey.equals(columnInfo);
	}

	@Override
	public String toString() {
		return String.format("{name=%s, primaryKey=%s, columns=%s}", name,
				primaryKey == null ? "null" : primaryKey.toString(), columns == null ? "null" : columns.toString());
	}

}
