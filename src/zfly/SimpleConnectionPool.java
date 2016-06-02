package zfly;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.sun.istack.internal.NotNull;

/**
 * 超简单数据库连接池。不支持自动扩容；不支持懒加载，需要自行计算需要的链接数量；
 * 
 * @author YFei
 *
 */
class SimpleConnectionPool {

	private Map<Connection, Boolean> pool;
	private int poolSize;

	public SimpleConnectionPool(@NotNull final int size, @NotNull final String url, @NotNull final String usr,
			@NotNull final String pwd) throws SQLException {
		this.pool = new HashMap<>(size);
		this.poolSize = size;
		for (int i = 0; i < poolSize; i++) {
			final Connection connection = DriverManager.getConnection(url, usr, pwd);
			pool.put(connection, false);
		}
	}

	public void release(final Connection connection) {
		if (!pool.containsKey(connection)) {
			return;
		}
		if (!pool.get(connection)) {
			return;
		}
		pool.put(connection, false);
	}

	public Connection request() {
		for (Map.Entry<Connection, Boolean> entry : pool.entrySet()) {
			if (!entry.getValue()) {
				pool.put(entry.getKey(), true);
				return entry.getKey();
			}
		}
		return null;
	}
}
