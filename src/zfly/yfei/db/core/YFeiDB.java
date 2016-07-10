package zfly.yfei.db.core;

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

import zfly.yfei.db.core.condition.Condition;
import zfly.yfei.db.core.condition.Where;
import zfly.yfei.db.model.Column;
import zfly.yfei.db.sql.builder.*;
import zfly.yfei.db.strategy.reflect.ReflectTableObserver;
import zfly.yfei.db.strategy.reverse.ReverseTableDelegate;
import zfly.yfei.db.strategy.reverse.ReverseTableStrategy;
import zfly.yfei.db.model.Table;
import zfly.yfei.db.strategy.reflect.ReflectTableImpl1;
import zfly.yfei.db.strategy.reverse.ReverseTableImpl1;

/**
 * 简单JDBC封装，通过反射和注解的方式提供简易数据库操作
 * 
 * @author YFei
 *
 */
public class YFeiDB {

	private static Logger log = Logger.getLogger(YFeiDB.class);

	private YFeiConfig config;
	private YFeiSQLExecutor sqlExecutor;
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
	 * 初始化数据库连接，此方法必须在使用该模版前调用 ，且仅需调用一次 若连接失败则无法执行后续操作，如
	 * YFeiDB.conn(new YFeiDBConfig()
	 * 					.setDataBase("MySQL")
	 * 					.setUrl("jdbc:MySQL://xxx.xx.xxx.xxx:xxxx/xxx?characterEncoding=utf8")
	 * 					.setUserName("root")
	 * 					.setPassWord("123456"));
	 *
	 * @param config 配置信息
	 * @return 数据库对象
	 * @throws SQLException
	 */
	public static YFeiDB createDB(final YFeiConfig config) throws SQLException {

		if (config == null) {
			throw new SQLException("Config must be not null!!");
		}

		/* 初始化SQL连接器 */
		final YFeiDB db = new YFeiDB();
		db.sqlExecutor = createSQLExecutor(config);
		db.config = config;
		return db;
	}

	/**
	 * 创建一个简单SQL执行器
	 *
	 * @param config 配置信息
	 * @return SQL执行器
	 * @throws SQLException
	 */
	public static YFeiSQLExecutor createSQLExecutor(final YFeiConfig config) throws SQLException {

		if (config == null) {
			throw new SQLException("Config must be not null!!");
		}

		/* 加载数据库驱动 */
		if (StringUtils.equalsIgnoreCase("MySQL", config.getDataBase())) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				throw new SQLException(String.format("Can not find driver for database<%s>!!", config.getDataBase()));
			}
		}

		return new YFeiSQLExecutor(config.getPoolSize(), config.getUrl(), config.getUserName(), config.getPassWord());
	}

	/**
	 * 查询指定条件的实体类对应的数据库表记录
	 *
	 * @param clazz 实体类
	 * @param conditions 查询条件
	 * @return 所有满足添加的数据库记录对应的实体类的链表
	 * @throws SQLException
	 */
	public <T> List<T> find(Class<T> clazz, final Condition... conditions) throws SQLException {

		final Table table = getTableForClass(clazz);
		final List<Column> columns = table.getColumns();
		final List<T> resList = new ArrayList<>();

		executeSQL(new SQLFindBuilder(null, table, conditions), result -> {
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
	 * 查询指定主键的实体类对应的数据库表记录
	 *
	 * @param clazz 实体类类型
	 * @param id 主键
	 * @return 数据库记录对应实体类, 未查询到则返还null
	 * @throws SQLException
	 */
	public <T> T find(Class<T> clazz, final int id) throws SQLException {
		final Table table = getTableForClass(clazz);
		final List<T> res = find(clazz, Where.shortcutForId(id, table));
		return res.isEmpty() ? null : res.get(0);
	}

	/**
	 * 将实体类保存到数据库，如果实体类不对应数据库操作则忽略
	 * 
	 * @param entity 实体类
	 * @throws SQLException
	 */
	public <T> void save(final T entity) throws SQLException {
		final Table table = getTableForClass(entity);
		executeSQL(new SQLSaveBuilder(entity, table));
	}

	/**
	 * 更新实体类对应的数据库表，实体类的主键将用来作为指定条件
	 * 
	 * @param entity 实体类
	 * @throws SQLException
	 */
	public void update(final Object entity) throws SQLException {
		final Table table = getTableForClass(entity);
		update(entity, Where.shortcutForId(entity, table));
	}

	/**
	 * 更新实体类对应的数据库表，更新内容为实体类属性值，更新条件通过Where指定
	 * 
	 * @param entity
	 *            实体类
	 * @param conditions
	 *            指定条件
	 * @throws SQLException
	 */
	public void update(final Object entity, final Condition... conditions) throws SQLException {
		final Table table = getTableForClass(entity);
		executeSQL(new SQLUpdateBuilder(entity, table,
				conditions));
	}

	/**
	 * 删除实体类对应数据库数据，实体类的主键将用来作为指定条件
	 * 
	 * @param entity
	 *            实体类
	 * @throws SQLException
	 */
	public void delete(final Object entity) throws SQLException {
		final Table table = getTableForClass(entity);
		executeSQL(new SQLDeleteBuilder(entity, table, Where.shortcutForId(entity, table)));
	}

	/**
	 * 删除实体类对应的数据库表，删除内容为实体类属性值，更新条件通过Where指定
	 * 
	 * @param clazz
	 *            实体类
	 * @param conditions
	 *            指定条件
	 * @throws SQLException
	 */
	public void delete(final Class<?> clazz, final Condition... conditions) throws SQLException {
		final Table table = getTableForClass(clazz);
		executeSQL(new SQLDeleteBuilder(null, table, conditions));
	}

	private void executeSQL(final SQLBuilder sqlBuilder) throws SQLException {
		executeSQL(sqlBuilder, null);
	}

	private void executeSQL(final SQLBuilder sqlBuilder, final YFeiDBHandler handler) throws SQLException {
		final String sql = sqlBuilder.getSql();
		if (config.isShowSql()) {
			log.info(sql);
		}
		sqlExecutor.doExecute(sql, handler);
	}

	private Table getTableForClass(final Object entity) throws SQLException {
		return getTableForClass(entity == null ? null : entity.getClass());
	}

	private Table getTableForClass(final Class<?> clazz) throws SQLException {
		if (clazz == null) {
			throw new SQLException(
					"Can not find table information for Class!!");
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
