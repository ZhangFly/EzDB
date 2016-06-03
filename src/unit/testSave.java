package unit;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import zfly.YFeiConfig;
import zfly.YFeiDB;

public class testSave {

	private static YFeiDB mysql = null;

	@Test
	public void testCreateDB() {
		mysql = YFeiDB.createDB(new YFeiConfig().setDataBase("MySQL")
				.setUrl("jdbc:mysql://localhost:3306/YFeiDB_Test?characterEncoding=utf8").setUserName("root")
				.setPassWord("123456").setPoolSize(1).setShowSql(true));
		assertNotNull(mysql);
	}

}
