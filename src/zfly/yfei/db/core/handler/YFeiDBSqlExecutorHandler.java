package zfly.yfei.db.core.handler;

import java.sql.ResultSet;

import com.sun.istack.internal.Nullable;

public abstract class YFeiDBSqlExecutorHandler {

	/**
	 * 执行完成后回调该方法
	 * 
	 * @param result
	 */
	public void onSuccess(final ResultSet result) {

	}

	/**
	 * 执行完成后回调该方法
	 *
	 */
	public void onFailure() {

	}

}
