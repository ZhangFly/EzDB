package zfly.yfei.db.core;

import java.sql.ResultSet;

import com.sun.istack.internal.Nullable;

public abstract class YFeiDBHandler {

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
