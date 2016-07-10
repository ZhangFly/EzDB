package zfly.yfei.db.core;

import java.sql.ResultSet;


@FunctionalInterface
public interface YFeiDBHandler {

	/**
	 * 执行成功回调方法
	 * 
	 * @param result 查询结果集
	 */

	void onSuccess(final ResultSet result);


	/**
	 * 执行失败回调方法
	 *
	 * @param e 引起失败的异常
	 */
	default void onFailure(final Exception e) {

	}

}
