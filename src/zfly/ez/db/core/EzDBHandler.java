package zfly.ez.db.core;

import java.sql.ResultSet;
import java.sql.SQLException;


@FunctionalInterface
public interface EzDBHandler {

	/**
	 * 执行成功回调方法
	 * 
	 * @param result 查询结果集
	 */

	void onSuccess(final ResultSet result) throws SQLException;


	/**
	 * 执行失败回调方法
	 *
	 * @param e 引起失败的异常
	 */
	default void onFailure(final Exception e) {

	}

}
