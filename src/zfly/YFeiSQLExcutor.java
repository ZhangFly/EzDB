package zfly;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

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

	public void excuteSql(final String sql, final boolean isShowSQL) {
		excuteSql(sql, null, isShowSQL);
	}

	public void excuteSql(final String sql, final YFeiDBExcuteSqlHandler handler, final boolean isShowSQL) {

		final Connection conn = pool.request();
		if (conn == null) {
			log.error("Connection pool was empty!!");
		}

		final Statement stat = createStatement(conn);

		if (isShowSQL) {
			log.info(sql);
		}

		doExcute(sql, handler, stat);

		relaseStatement(conn, stat);

	}

	private void relaseStatement(final Connection conn, Statement stat) {
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

	private Statement createStatement(final Connection conn) {
		try {
			return conn.createStatement();
		} catch (SQLException e) {
			log.error(e.getMessage());
			pool.release(conn);
			return null;
		}
	}

	private void doExcute(final String sql, final YFeiDBExcuteSqlHandler handler, Statement stat) {
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
