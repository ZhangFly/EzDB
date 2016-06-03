package unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import model.Person;
import zfly.Where;
import zfly.YFeiConfig;
import zfly.YFeiDB;

public class TestUpdate {

	private static YFeiDB mysql = null;

	@Test
	public void testCreateDB() {
		mysql = YFeiDB.createDB(new YFeiConfig().setDataBase("MySQL")
				.setUrl("jdbc:mysql://localhost:3306/YFeiDB_Test?characterEncoding=utf8").setUserName("root")
				.setPassWord("123456").setPoolSize(1).setShowSql(true));
		assertNotNull(mysql);
	}

	@Test
	public void testUpdate() {
		final Person alert = new Person();
		alert.setId(1);
		alert.setAge(0);
		alert.setName("maxpup");
		alert.setSex("unkonwn");
		alert.setIntro("Big SB!");

		mysql.update(TestExpectUtils.EXPECT_1);

		mysql.update(null);
		final Person wrong1 = mysql.find(Person.class, 1);
		assertEquals(TestExpectUtils.EXPECT_1, wrong1);

		mysql.update(null, new Where("$2=$c", "ZFly"));
		final Person wrong2 = mysql.find(Person.class, 1);
		assertEquals(TestExpectUtils.EXPECT_1, wrong2);

		mysql.update(null, null);
		final Person wrong3 = mysql.find(Person.class, 1);
		assertEquals(TestExpectUtils.EXPECT_1, wrong3);

		mysql.update(alert);
		final Person right1 = mysql.find(Person.class, 1);
		assertEquals(alert, right1);

		mysql.update(TestExpectUtils.EXPECT_1);
		final Person right2 = mysql.find(Person.class, 1);
		assertEquals(TestExpectUtils.EXPECT_1, right2);

		mysql.update(alert, new Where("$2=$c", "ZFly"));
		final Person right3 = mysql.find(Person.class, 1);
		assertEquals(alert, right3);

		mysql.update(TestExpectUtils.EXPECT_1);
		final Person right4 = mysql.find(Person.class, 1);
		assertEquals(TestExpectUtils.EXPECT_1, right4);

		mysql.update(alert, null);
		final Person right5 = mysql.find(Person.class, 1);
		assertEquals(alert, right5);

		mysql.update(TestExpectUtils.EXPECT_1);
		final Person right6 = mysql.find(Person.class, 1);
		assertEquals(TestExpectUtils.EXPECT_1, right6);
	}
}
