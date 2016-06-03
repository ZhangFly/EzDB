package unit;

import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;

import org.junit.Test;

import model.Person;
import zfly.YFeiConfig;
import zfly.YFeiDB;

public class testSave {

	private static YFeiDB mysql = null;

	@Test
	public void testCreateDB() throws ClassNotFoundException, SQLException {
		mysql = YFeiDB.createDB(new YFeiConfig().setDataBase("MySQL")
				.setUrl("jdbc:mysql://localhost:3306/YFeiDB_Test?characterEncoding=utf8").setUserName("root")
				.setPassWord("123456").setPoolSize(1).setShowSql(true));
		assertNotNull(mysql);
	}

	@Test
	public void testSave() {
		final Person alert = new Person();
		alert.setId(1);
		alert.setAge(0);
		alert.setName("maxpup");
		alert.setSex("unkonwn");
		alert.setIntro("Big SB!");
	}
}
