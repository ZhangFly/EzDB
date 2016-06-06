package zfly.yfei.db.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import zfly.yfei.db.core.handler.YFeiDBSqlExecutorHandler;
import zfly.yfei.db.helper.SimpleConnectionPool;

/**
 * 简单SQL语句执行器，内部维持有简单连接池
 *
 * @author YFei
 */
public class YFeiSQLExecutor {

    private static Logger log = Logger.getLogger(YFeiSQLExecutor.class);
    private SimpleConnectionPool pool;

    /**
     * 构造函数,仅包内可访问
     *
     * @param size 内部连接池大小
     * @param url  数据库连接地址
     * @param usr  数据库用户名
     * @param pwd  数据库密码
     * @throws SQLException
     */
    YFeiSQLExecutor(final int size, final String url, final String usr, final String pwd) throws SQLException {
        pool = new SimpleConnectionPool(size, url, usr, pwd);
    }

    /**
     * 执行SQL语句
     *
     * @param sql SQL语句
     * @throws SQLException
     */
    public void doExecute(final String sql) throws SQLException {
        doExecute(sql, null);
    }

    /**
     * @param sql     SQL语句
     * @param handler 执行成功后的服务方法
     * @throws SQLException
     */
    public void doExecute(final String sql, final YFeiDBSqlExecutorHandler handler) throws SQLException {

        final Connection conn = pool.request();

        try {
            final Statement stat = conn.createStatement();
            if (StringUtils.containsIgnoreCase(sql, "select")) {
                final ResultSet res = stat.executeQuery(sql);
                if (handler != null) {
                    handler.onSuccess(res);
                }
            } else {
                stat.execute(sql);
            }
            stat.close();
            pool.release(conn);
        } catch (SQLException e) {
            pool.release(conn);
            throw e;
        }
    }
}
