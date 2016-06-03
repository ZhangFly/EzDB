package zfly;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 简单SQL语句执行器，内部维持有简单连接池
 * 
 * @author YFei
 *
 */
public class YFeiSQLExecutor {

	private static Logger log = Logger.getLogger(YFeiSQLExecutor.class);
	private SimpleConnectionPool pool;

	YFeiSQLExecutor(final int size, final String url, final String usr, final String pwd) throws SQLException {

		pool = new SimpleConnectionPool(size, url, usr, pwd);
	}

	/**
	 * 执行SQL语句
	 * 
	 * @param sql
	 *            SQL语句
	 * @throws SQLException
	 */
	public void doExcute(final String sql) throws SQLException {
		doExcute(sql, null);
	}

	/**
	 * 
	 * @param sql
	 * @param handler
	 * @throws SQLException
	 */
	public void doExcute(final String sql, final YFeiDBExcuteSqlHandler handler) throws SQLException {

		final Connection conn = pool.request();
		if (conn == null) {
			throw new SQLException("Connection in pool was all used!!");
		}
		try {
			final Statement stat = conn.createStatement();
			if (StringUtils.containsIgnoreCase(sql, "select")) {
				final ResultSet res = stat.executeQuery(sql);
				if (handler != null) {
					handler.onDone(res);
				}
			} else {
				stat.execute(sql);
			}
			stat.close();
		} catch (SQLException e) {
			throw e;
		} finally {
			pool.release(conn);
		}
	}
}
