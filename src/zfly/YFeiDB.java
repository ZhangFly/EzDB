package zfly;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import zfly.ReflectTableStrategy.ReflectTableObserver;
import zfly.ReverseTableStrategy.ReverseTableDelegate;

/**
 * 简单JDBC封装，通过反射和注解的方式提供简易数据库操作
 * 
 * @author YFei
 *
 */
public class YFeiDB {

	private static Logger log = Logger.getLogger(YFeiDB.class);

	private YFeiConfig config;
	private YFeiSQLExcutor sqlExcutor;
	private Map<String, Table> tables = new HashMap<>();

	// 加载log4j配置
	static {
		final Properties log4jProperties = new Properties();
		log4jProperties.setProperty("log4j.rootLogger", "INFO, stdout");
		log4jProperties.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
		log4jProperties.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
		log4jProperties.setProperty("log4j.appender.stdout.layout.ConversionPattern", "[%l] %p: %m%n");
		PropertyConfigurator.configure(log4jProperties);
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
	 */
	public static YFeiDB createDB(final YFeiConfig config) {

		if (config == null) {
			log.error("Config must be not null !!");
			return null;
		}

		/* 彩蛋一枚，致我最爱的小黄君，哈哈！！ */
		if (StringUtils.equalsIgnoreCase("yfei", config.getDataBase())) {
			log.info("call me 小黄君！！最爱小黄君！！");
		}

		/* 加载数据库驱动 */
		if (StringUtils.equalsIgnoreCase("mysql", config.getDataBase())) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				log.error(e.getMessage());
			}
		}

		/* 初始化连接池 */
		final YFeiDB db = new YFeiDB();
		if ((db.sqlExcutor = createSQLExcutor(config)) != null) {
			db.config = config;
		}
		return db;
	}

	/**
	 * 创建一个简单SQL执行器
	 * 
	 * @param config
	 * @return
	 */
	public static YFeiSQLExcutor createSQLExcutor(final YFeiConfig config) {
		if (config == null) {
			return null;
		}
		return new YFeiSQLExcutor(config.getPoolSize(), config.getUrl(), config.getUserName(), config.getPassWord());
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
		return find(clazz, Where.emptyWhere());
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
		final Table table = getTableForClass(clazz);
		final List<T> res = find(clazz, Where.shrotcutForId(id, table));
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

		final Table table = getTableForClass(clazz);
		final List<Column> columns = table.getColumns();
		String sql = StringUtils.EMPTY;
		if (condition == null) {
			sql = new SQLFindBuilder().getSql(null, table, Where.emptyWhere());
		} else {
			sql = new SQLFindBuilder().getSql(null, table, condition);
		}
		final List<T> resList = new ArrayList<>();

		excuteSql(sql, (result) -> {
			final ReverseTableStrategy reverse = new ReverseTableImpl1(new ReverseTableDelegate() {

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
						return result.getObject(columns.get(position).getName());
					} catch (SQLException e) {
						log.error("Column was not mathed Class.Field<" + columns.get(position).getName() + ">");
						return null;
					}
				}

			});

			try {
				while (result.next()) {
					resList.add(reverse.doReverse(clazz));
				}
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		});

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
		final Table table = getTableForClass(entity);
		final String sql = new SQLSaveBuilder().getSql(entity, table, Where.shrotcutForId(entity, table));
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
		final Table table = getTableForClass(entity);
		update(entity, Where.shrotcutForId(entity, table));
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
		final Table table = getTableForClass(entity);
		final String sql = new SQLUpdateBuilder().getSql(entity, table,
				condition == null ? Where.emptyWhere() : condition);
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
		final Table table = getTableForClass(entity);
		final String sql = new SQLDeleteBuilder().getSql(entity, table, Where.shrotcutForId(entity, table));
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
	public void delete(final Class<?> clazz, final Where condition) {
		final Table table = getTableForClass(clazz);
		final String sql = new SQLDeleteBuilder().getSql(null, table, condition);
		excuteSql(sql);
	}

	/**
	 * 执行SQL语句操作
	 * 
	 * @param sql
	 */
	public void excuteSql(final String sql) {

		excuteSql(sql, null);
	}

	/**
	 * 执行SQL语句操作
	 * 
	 * @param sql
	 *            SQL语句
	 * @param handler
	 *            执行成功回调函数
	 * @return
	 * @throws SQLException
	 */
	public void excuteSql(final String sql, final YFeiDBExcuteSqlHandler handler) {

		if (sql == null) {
			log.error("SQL must be not null!!");
			return;
		}

		if (config == null) {
			log.error("Cannot find an valid configuration for YFeiDB, please initialize it at first!!");
			return;
		}

		sqlExcutor.excuteSql(sql, handler, config.isShowSql());

	}

	private Table getTableForClass(final Object entity) {
		if (entity == null) {
			return Table.emptyTable();
		}
		return getTableForClass(entity.getClass());
	}

	private Table getTableForClass(final Class<?> clazz) {
		if (clazz == null) {
			return Table.emptyTable();
		}

		if (!tables.containsKey(clazz.getName())) {
			loadTableFromClass(clazz);
		}

		return tables.get(clazz.getName());
	}

	private void loadTableFromClass(final Class<?> clazz) {
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
		}).doReflect(clazz);
		tables.put(clazz.getName(), table);
	}
}
