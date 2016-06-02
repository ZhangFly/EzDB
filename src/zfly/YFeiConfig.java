package zfly;

public class YFeiConfig {

	private String dataBase;
	private String url;
	private String userName;
	private String passWord;
	private int poolSize;
	private boolean showSql;

	public YFeiConfig() {
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
	public YFeiConfig(final String dataBase, final String url, final String userName, final String passWord,
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
	 * @return
	 */
	public String getDataBase() {
		return dataBase;
	}

	/**
	 * 设置数据库名
	 * 
	 * @param dataBase
	 * @return
	 */
	public YFeiConfig setDataBase(String dataBase) {
		this.dataBase = dataBase;
		return this;
	}

	/**
	 * 获取数据库连接地址
	 * 
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置数据库连接地址
	 * 
	 * @param url
	 * @return
	 */
	public YFeiConfig setUrl(String url) {
		this.url = url;
		return this;
	}

	/**
	 * 获取数据库用户名
	 * 
	 * @return
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 设置数据库用户名
	 * 
	 * @param userName
	 * @return
	 */
	public YFeiConfig setUserName(String userName) {
		this.userName = userName;
		return this;
	}

	/**
	 * 获取数据库密码
	 * 
	 * @return
	 */
	public String getPassWord() {
		return passWord;
	}

	/**
	 * 设置数据库密码
	 * 
	 * @param passWord
	 * @return
	 */
	public YFeiConfig setPassWord(String passWord) {
		this.passWord = passWord;
		return this;
	}

	/**
	 * 获取连接池大小
	 * 
	 * @return
	 */
	public int getPoolSize() {
		return poolSize;
	}

	/**
	 * 设置连接池大小
	 * 
	 * @param poolSize
	 * @return
	 */
	public YFeiConfig setPoolSize(int poolSize) {
		this.poolSize = poolSize;
		return this;
	}

	/**
	 * 查询是否打印SQL语句
	 * 
	 * @return
	 */
	public boolean isShowSql() {
		return showSql;
	}

	/**
	 * 设置是否打印SQL语句
	 * 
	 * @param showSql
	 * @return
	 */
	public YFeiConfig setShowSql(boolean showSql) {
		this.showSql = showSql;
		return this;
	}

}
