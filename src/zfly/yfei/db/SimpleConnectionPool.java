package zfly.yfei.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 超简单数据库连接池。不支持自动扩容；不支持懒加载，需要自行计算需要的链接数量；
 *
 * @author YFei
 */
class SimpleConnectionPool {

    private static Logger log = Logger.getLogger(SimpleConnectionPool.class);
    private Map<Connection, Boolean> pool;

    SimpleConnectionPool(final int size, final String url, final String usr, final String pwd)
            throws SQLException {
        this.pool = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            final Connection connection = DriverManager.getConnection(url, usr, pwd);
            pool.put(connection, false);
        }
        log.debug(String.format("Created %dsh connection into pool!!", size));
    }

    void release(final Connection connection) {
        if (!pool.containsKey(connection)) {
            return;
        }
        if (!pool.get(connection)) {
            return;
        }
        log.debug("Given a connection back to pool!!");
        pool.put(connection, false);
    }

    Connection request() throws SQLException {
        for (Map.Entry<Connection, Boolean> entry : pool.entrySet()) {
            if (!entry.getValue()) {
                pool.put(entry.getKey(), true);
                log.debug("Taken a connection from pool!!");
                return entry.getKey();
            }
        }
        throw new SQLException("All connections in pool were used!!");
    }
}
