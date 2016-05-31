package ZFly;

public class YFeiDBConfig {

	private String dataBase;
	private String url;
	private String userName;
	private String passWord;
	private int poolSize;
	private boolean showSql;

	public YFeiDBConfig() {
		this(null, null, null, null, 1, false);
	}

	public YFeiDBConfig(final String dataBase, final String url, final String userName, final String passWord,
			final int poolSize, final boolean showSql) {
		this.dataBase = dataBase;
		this.url = url;
		this.userName = userName;
		this.passWord = passWord;
		this.poolSize = poolSize;
		this.showSql = showSql;
	}

	public String getDataBase() {
		return dataBase;
	}

	public YFeiDBConfig setDataBase(String dataBase) {
		this.dataBase = dataBase;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public YFeiDBConfig setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getUserName() {
		return userName;
	}

	public YFeiDBConfig setUserName(String userName) {
		this.userName = userName;
		return this;
	}

	public String getPassWord() {
		return passWord;
	}

	public YFeiDBConfig setPassWord(String passWord) {
		this.passWord = passWord;
		return this;
	}

	public int getPoolSize() {
		return poolSize;
	}

	public YFeiDBConfig setPoolSize(int poolSize) {
		this.poolSize = poolSize;
		return this;
	}

	public boolean isShowSql() {
		return showSql;
	}

	public YFeiDBConfig setShowSql(boolean showSql) {
		this.showSql = showSql;
		return this;
	}

}
