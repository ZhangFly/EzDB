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

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import ZFly.ReflectTableStrategy.ReflectTableObserver;
import ZFly.ReverseTableStrategy.ReserseTableDelegate;

/**
 * 简单JDBC封装，通过反射和注解的方式提供简易数据库操作
 * 
 * @author YFei
 *
 */
public class YFeiDB {

	/**
	 * 模块配置
	 */
	private YFeiDBConfig config;
	/**
	 * 简单数据库连接池
	 */
	private SimpleConnectionPool pool;
	/**
	 * 已反射生成的表信息
	 */
	private Map<String, Table> tables = new HashMap<String, Table>();
	/**
	 * log4j
	 */
	private static Logger log = Logger.getLogger(YFeiDB.class);

	// 初始化log4j配置
	static {
		PropertyConfigurator.configure("log4j.properties");
	}

	// 屏蔽构造函数
	private YFeiDB() {

	}

	/**
	 *
	 * 初始化数据库连接，此方法必须在使用该模版前调用 ，且仅需调用一次 若连接失败则无法执行后续操作，如
	 * YFeiDB.conn(new YFeiDBConfig()
	 * .setDataBase("MySQL")
	 * .setUrl("jdbc:mysql://121.42.151.185:3306/sh?characterEncoding=utf8")
	 * .setUserName("root")
	 * .setPassWord("123456"));
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static YFeiDB createDB(final YFeiDBConfig config) throws ClassNotFoundException, SQLException {
		if (config == null) {
			throw new NullPointerException("YFeiDBConfig must be not null!!");
		}
		/* 彩蛋一枚，致我最爱的小黄君，哈哈！！ */
		if (config.getDataBase().equalsIgnoreCase("yfei")) {
			System.out.println("call me 小黄君！！最爱小黄君！！");
		}
		/* 加载数据库驱动 */
		if (config.getDataBase().equalsIgnoreCase("mysql")) {
			Class.forName("com.mysql.jdbc.Driver");
		}
		final YFeiDB db = new YFeiDB();
		/* 初始化连接池 */
		db.pool = new SimpleConnectionPool(config.getPoolSize(), config.getUrl(), config.getUserName(),
				config.getPassWord());
		db.config = config;
		return db;
	}

	/**
	 * 查询实体类对应的所有数据库表记录
	 * 
	 * @param clazz
	 *            实体类
	 * @return 所有数据库记录
	 * @throws SQLException
	 */
	public <T> List<T> find(Class<T> clazz) {
		loadTableInfoFromClass(clazz);
		return find(clazz, null);
	}

	/**
	 * 查询指定主键的实体类对应的数据库表记录
	 * 
	 * @param clazz
	 *            实体类
	 * @param id
	 *            主键
	 * @return 数据库记录
	 * @throws SQLException
	 */
	public <T> T find(Class<T> clazz, final int id) {
		loadTableInfoFromClass(clazz);
		final List<T> res = find(clazz, Where.shrotcutForId(id, getTableInfoForClass(clazz)));
		return res.isEmpty() ? null : res.get(0);
	}

	/**
	 * 查询指定条件的实体类对应的数据库表记录
	 * 
	 * @param clazz
	 *            实体类
	 * @param id
	 *            主键
	 * @return 数据库记录
	 * @throws SQLException
	 */
	public <T> List<T> find(Class<T> clazz, final Where condition) {
		loadTableInfoFromClass(clazz);
		final String sql = makeSQL(clazz, condition, new SQLFindBuilder());
		final ResultSet res = excuteSql(sql);
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
					log.error("Column was not mathed Class.Field<columns.get(position).getName()>");
					return null;
				}
			}

		});
		try {
			while (res.next()) {
				resList.add(reverse.excute(clazz));
				res.close();
			}
		} catch (SQLException e) {
			log.error(e.getStackTrace());
		}
		return resList;
	}

	/**
	 * 将实体类保存到数据库，如果实体类不对应数据库操作则忽略
	 * 
	 * @param entity
	 *            实体类
	 * @throws SQLException
	 */
	public void save(final Object entity) {
		loadTableInfoFromClass(entity.getClass());
		final String sql = makeSQL(entity, null, new SQLSaveBuilder());
		excuteSql(sql);
	}

	/**
	 * 更新实体类对应的数据库表，实体类的主键将用来作为指定条件
	 * 
	 * @param entity
	 *            实体类
	 * @throws SQLException
	 */
	public void update(final Object entity) {
		loadTableInfoFromClass(entity.getClass());
		update(entity, Where.shrotcutForId(entity, getTableInfoForClass(entity.getClass())));
	}

	/**
	 * 更新实体类对应的数据库表，更新内容为实体类属性值，更新条件通过Where指定
	 * 
	 * @param entity
	 *            实体类
	 * @param condition
	 *            指定条件
	 * @throws SQLException
	 */
	public void update(final Object entity, final Where condition) {
		loadTableInfoFromClass(entity.getClass());
		final String sql = makeSQL(entity, condition, new SQLUpdateBuilder());
		excuteSql(sql);
	}

	/**
	 * 删除实体类对应数据库数据，实体类的主键将用来作为指定条件
	 * 
	 * @param entity
	 *            实体类
	 * @throws SQLException
	 */
	public void delete(final Object entity) {
		loadTableInfoFromClass(entity.getClass());
		final String sql = makeSQL(entity, Where.shrotcutForId(entity, getTableInfoForClass(entity.getClass())),
				new SQLDeleteBuilder());
		excuteSql(sql);
	}

	/**
	 * 删除实体类对应的数据库表，删除内容为实体类属性值，更新条件通过Where指定
	 * 
	 * @param clazz
	 *            实体类
	 * @param condition
	 *            指定条件
	 * @throws SQLException
	 */
	public void delete(Class<?> clazz, final Where condition) {
		loadTableInfoFromClass(clazz);
		final String sql = makeSQL(clazz, condition, new SQLDeleteBuilder());
		excuteSql(sql);
	}

	/**
	 * 执行SQL语句操作
	 * 
	 * @param sql
	 *            SQL语句
	 * @return
	 * @throws SQLException
	 */
	public ResultSet excuteSql(final String sql) {

		try {
			if (config == null) {
				throw new NullPointerException(
						"Cannot find an valid configuration for YFeiDB, please initialize it at first!!");
			}
			final Connection conn = pool.request();
			Statement stat;
			stat = conn.createStatement();
			ResultSet res = null;
			if (sql.contains("SELECT")) {
				res = stat.executeQuery(sql);
			} else {
				stat.execute(sql);
			}
			pool.release(conn);
			if (config.isShowSql()) {
				log.info(sql);
			}
			return res;
		} catch (SQLException e) {
			log.error(e.getStackTrace());
			return null;
		}

	}

	private String makeSQL(final Object entity, final Where condition, final SQLBuilder builder) {
		final Table table;
		if (entity instanceof Class) {
			table = getTableInfoForClass((Class<?>) entity);
		} else {
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

	private Table getTableInfoForClass(final Class<?> clazz) {
		return tables.get(clazz.getSimpleName());
	}

	private void loadTableInfoFromClass(Class<?> clazz) {
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
