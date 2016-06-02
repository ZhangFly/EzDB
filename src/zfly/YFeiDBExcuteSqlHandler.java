package zfly;

import java.sql.ResultSet;

import com.sun.istack.internal.Nullable;

public interface YFeiDBExcuteSqlHandler {

	/**
	 * 执行完成后回调该方法
	 * 
	 * @param result
	 */
	void onDone(@Nullable final ResultSet result);

}
