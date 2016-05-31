package ZFly;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ZFly.ReflectTableStrategy.ReflectTableObserver;
import ZFly.ReverseTableStrategy.ReserseTableDelegate;

/**
 * 简单JDBC封装，通过反射和注解的方式提供简易数据库操作
 * 
 * @author YFei
 *
 */
public class YFeiDB {

	private static YFeiDBConfig config;
	private static SimpleConnectionPool pool;
	private static Map<String, Table> tables = new HashMap<String, Table>();

	/**
	 * @Description 初始化数据库连接，此方法必须在使用该模版前调用 ，且仅需调用一次 若连接失败则无法执行后续操作，如
	 *              YFeiDB.conn(new YFeiDBConfig()
	 *              .setDataBase("MySQL")
	 *              .setUrl(
	 *              "jdbc:mysql://121.42.151.185:3306/sh?characterEncoding=utf8")
	 *              .setUserName("root")
	 *              .setPassWord("123456"));
	 */
	public static void conn(final YFeiDBConfig config) {
		try {
			/* 彩蛋一枚，致我最爱的小黄君，哈哈！！ */
			if (config.getDataBase().toLowerCase().equals("yfei")) {
				System.out.println("call me 小黄君！！最爱小黄君！！");
			}
			/* 加载数据库驱动 */
			if (config.getDataBase().toLowerCase().equals("mysql")) {
				Class.forName("com.mysql.jdbc.Driver");
			}
			/* 初始化连接池 */
			YFeiDB.pool = new SimpleConnectionPool(config.getPoolSize(), config.getUrl(), config.getUserName(),
					config.getPassWord());
			YFeiDB.config = config;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}

	public static <T> List<T> find(Class<T> clazz) {
		return find(clazz, null);
	}

	public static <T> T find(Class<T> clazz, final int id) {
		loadTableInfoFromClass(clazz);
		return find(clazz, Where.shrotcutForId(id, getTableInfoForClass(clazz))).get(0);
	}

	public static <T> List<T> find(Class<T> clazz, final Where condition) {
		try {
			final String sql = makeGeneralSql(clazz, condition, new SQLFindBuilder());
			final ResultSet res = excuteSql(sql, true);
			final List<Column> columns = getTableInfoForClass(clazz).getColumns();
			final List<T> resList = new ArrayList<T>();
			final ReverseTableStrategy reverse = new ReverseTableImpl1(new ReserseTableDelegate() {

				@Override
				public int getFieldCount() {
					return columns.size();
				}

				@Override
				public Field getField(int position) {
					return columns.get(position).getField();
				}

				@Override
				public Object getFieldVale(int position) {
					try {
						return res.getObject(columns.get(position).getName());
					} catch (SQLException e) {
						e.printStackTrace();
						return null;
					}
				}

			});
			while (res.next()) {
				resList.add(reverse.excute(clazz));
			}
			return resList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T> void save(final T entity) {
		final String sql = makeGeneralSql(entity, null, new SQLSaveBuilder());
		excuteSql(sql, false);
	}

	public static <T> void update(final T entity) {
		loadTableInfoFromClass(entity.getClass());
		update(entity, Where.shrotcutForId(entity, getTableInfoForClass(entity.getClass())));
	}

	public static <T> void update(final T entity, final Where condition) {
		final String sql = makeGeneralSql(entity, condition, new SQLUpdateBuilder());
		excuteSql(sql, false);
	}

	public static <T> void delete(final T entity) {
		loadTableInfoFromClass(entity.getClass());
		final String sql = makeGeneralSql(entity, Where.shrotcutForId(entity, getTableInfoForClass(entity.getClass())),
				new SQLDeleteBuilder());
		excuteSql(sql, false);
	}

	public static <T> void delete(Class<T> clazz, final Where condition) {
		final String sql = makeGeneralSql(clazz, condition, new SQLDeleteBuilder());
		excuteSql(sql, false);
	}

	public static ResultSet excuteSql(final String sql, final boolean result) {
		try {
			if (config.isShowSql()) {
				System.out.println(sql);
			}
			final Connection conn = pool.request();
			final Statement stat = conn.createStatement();
			ResultSet res = null;
			if (result) {
				res = stat.executeQuery(sql);
			} else {
				stat.execute(sql);
			}
			pool.release(conn);
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String makeGeneralSql(final Object entity, final Where condition, final SQLBuilder builder) {
		final Table table;
		if (entity instanceof Class) {
			loadTableInfoFromClass((Class<?>) entity);
			table = getTableInfoForClass((Class<?>) entity);
		} else {
			loadTableInfoFromClass(entity.getClass());
			table = getTableInfoForClass(entity.getClass());
		}
		final StringBuilder sql = builder.getBaseBuilder(entity, table);
		if (condition != null) {
			sql.append(" ");
			sql.append(condition.getSql(table));
		}
		sql.append(";");
		return sql.toString();
	}

	private static Table getTableInfoForClass(final Class<?> clazz) {
		return tables.get(clazz.getSimpleName());
	}

	private static <T> void loadTableInfoFromClass(Class<T> clazz) {
		// 已经存在表信息
		if (tables.containsKey(clazz.getSimpleName())) {
			return;
		}
		// 生成表信息
		final Table table = new Table();
		new ReflectTableImpl1().addGotTableInfoObserver(new ReflectTableObserver() {

			@Override
			public void gotTableName(String tableName, Class<?> clazz) {
				table.setName(tableName);
			}

			@Override
			public void gotPrimaryKey(String primaryKey, Field f, Class<?> clazz) {
				table.setPrimaryKey(new Column(primaryKey, f));
			}

			@Override
			public void gotColumnName(String columnName, Field f, Class<?> clazz) {
				table.addColumn(new Column(columnName, f));
			}
		}).excute(clazz);
		tables.put(clazz.getSimpleName(), table);
	}
}
