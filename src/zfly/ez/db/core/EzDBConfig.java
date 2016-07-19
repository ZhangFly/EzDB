package zfly.ez.db.core;

public class EzDBConfig {

	private String dataBase;
	private String url;
	private String userName;
	private String passWord;
	private int poolSize;
	private boolean showSql;

	public EzDBConfig() {
		this(null, null, null, null, 1, false);
	}

	/**
	 * YFeiDB模块配置信息
	 * 
	 * @param dataBase
	 *            数据库名称
	 * @param url
	 *            数据库连接地址
	 * @param userName
	 *            数据库用户名
	 * @param passWord
	 *            数据库密码
	 * @param poolSize
	 *            连接池大小
	 * @param showSql
	 *            是否显示SQL语句
	 */
	public EzDBConfig(final String dataBase, final String url, final String userName, final String passWord,
					  final int poolSize, final boolean showSql) {
		this.dataBase = dataBase;
		this.url = url;
		this.userName = userName;
		this.passWord = passWord;
		this.poolSize = poolSize;
		this.showSql = showSql;
	}

	/**
	 * 获取数据库名
	 *
	 * @return 数据库名
	 */
	public String getDataBase() {
		return dataBase;
	}

	/**
	 * 设置数据库名
	 *
	 * @param dataBase 数据库名
	 * @return 配置信息
	 */
	public EzDBConfig setDataBase(String dataBase) {
		this.dataBase = dataBase;
		return this;
	}

	/**
	 * 获取数据库连接地址
	 *
	 * @return 数据库连接地址
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置数据库连接地址
	 *
	 * @param url 数据库连接地址
	 * @return 配置信息
	 */
	public EzDBConfig setUrl(String url) {
		this.url = url;
		return this;
	}

	/**
	 * 获取数据库用户名
	 *
	 * @return 配置信息
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 设置数据库用户名
	 *
	 * @param userName 数据库用户名
	 * @return 配置信息
	 */
	public EzDBConfig setUserName(String userName) {
		this.userName = userName;
		return this;
	}

	/**
	 * 获取数据库密码
	 *
	 * @return 数据库密码
	 */
	public String getPassWord() {
		return passWord;
	}

	/**
	 * 设置数据库密码
	 *
	 * @param passWord 数据库密码
	 * @return 配置信息
	 */
	public EzDBConfig setPassWord(String passWord) {
		this.passWord = passWord;
		return this;
	}

	/**
	 * 获取连接池大小
	 *
	 * @return 连接池大小
	 */
	public int getPoolSize() {
		return poolSize;
	}

	/**
	 * 设置连接池大小
	 *
	 * @param poolSize 连接池大小
	 * @return 配置信息
	 */
	public EzDBConfig setPoolSize(int poolSize) {
		this.poolSize = poolSize;
		return this;
	}

	/**
	 * 查询是否打印SQL语句
	 *
	 * @return 是否打印SQL语句
	 */
	public boolean isShowSql() {
		return showSql;
	}

	/**
	 * 设置是否打印SQL语句
	 *
	 * @param showSql 是否打印SQL语句
	 * @return 配置信息
	 */
	public EzDBConfig setShowSql(boolean showSql) {
		this.showSql = showSql;
		return this;
	}

}
