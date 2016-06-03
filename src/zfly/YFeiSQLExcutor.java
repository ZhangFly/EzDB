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
public class YFeiSQLExcutor {

	private static Logger log = Logger.getLogger(YFeiSQLExcutor.class);
	private SimpleConnectionPool pool;

	YFeiSQLExcutor(final int size, final String url, final String usr, final String pwd) {
		try {
			pool = new SimpleConnectionPool(size, url, usr, pwd);
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * 执行SQL语句
	 * 
	 * @param sql
	 *            SQL语句
	 */
	public void doExcute(final String sql) {
		doExcute(sql, null);
	}

	/**
	 * 
	 * @param sql
	 * @param handler
	 */
	public void doExcute(final String sql, final YFeiDBExcuteSqlHandler handler) {

		final Connection conn = pool.request();
		if (conn == null) {
			log.error("Connection pool was empty!!");
		}

		final Statement stat = openSource(conn);

		excuteSQL(sql, handler, stat);

		closeSource(conn, stat);

	}

	private void closeSource(final Connection conn, final Statement stat) {
		try {
			if (stat == null) {
				return;
			}
			stat.close();
		} catch (SQLException e) {
			log.error(e.getMessage());
		} finally {
			pool.release(conn);
		}
	}

	private Statement openSource(final Connection conn) {
		try {
			return conn.createStatement();
		} catch (SQLException e) {
			log.error(e.getMessage());
			pool.release(conn);
			return null;
		}
	}

	private void excuteSQL(final String sql, final YFeiDBExcuteSqlHandler handler, Statement stat) {
		try {
			if (StringUtils.containsIgnoreCase(sql, "select")) {
				final ResultSet res = stat.executeQuery(sql);
				if (handler != null) {
					handler.onDone(res);
				}
			} else {
				stat.execute(sql);
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
	}

}
