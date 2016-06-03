package unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import model.Person;
import zfly.Where;
import zfly.YFeiConfig;
import zfly.YFeiDB;

public class TestFind {

	private static YFeiDB mysql = null;

	@Test
	public void testCreateDB() {
		mysql = YFeiDB.createDB(new YFeiConfig().setDataBase("MySQL")
				.setUrl("jdbc:mysql://localhost:3306/YFeiDB_Test?characterEncoding=utf8").setUserName("root")
				.setPassWord("123456").setPoolSize(1).setShowSql(true));
		assertNotNull(mysql);
	}

	@Test
	public void testFindAll() {
		final List<Person> right = mysql.find(Person.class);
		Assert.assertArrayEquals(TestExpectUtils.EXPECT_ARRAY, right.toArray());

		final List<Person> wrong = mysql.find(null);
		assertTrue(wrong.isEmpty());
	}

	@Test
	public void testFindById() {
		final Person right1 = mysql.find(Person.class, 1);
		assertEquals(TestExpectUtils.EXPECT_1, right1);
		final Person right2 = mysql.find(Person.class, 2);
		assertEquals(TestExpectUtils.EXPECT_2, right2);
		final Person wrong1 = mysql.find(Person.class, 0);
		assertNull(wrong1);
		final Person wrong2 = mysql.find(null, 1);
		assertNull(wrong2);
		final Person wrong3 = mysql.find(null, 0);
		assertNull(wrong3);
	}

	@Test
	public void testFindByCondition() {
		final List<Person> right1 = mysql.find(Person.class, new Where("id=1"));
		assertEquals(TestExpectUtils.EXPECT_1, right1.get(0));
		final List<Person> right2 = mysql.find(Person.class, new Where("t.id=1"));
		assertEquals(TestExpectUtils.EXPECT_1, right2.get(0));
		final List<Person> right3 = mysql.find(Person.class, new Where("t.$1=1"));
		assertEquals(TestExpectUtils.EXPECT_1, right3.get(0));
		final List<Person> right4 = mysql.find(Person.class, new Where("$1=1"));
		assertEquals(TestExpectUtils.EXPECT_1, right4.get(0));
		final List<Person> right5 = mysql.find(Person.class, new Where("id=$c", 1));
		assertEquals(TestExpectUtils.EXPECT_1, right5.get(0));
		final List<Person> right6 = mysql.find(Person.class, new Where("t.id=$c", 1));
		assertEquals(TestExpectUtils.EXPECT_1, right6.get(0));
		final List<Person> right7 = mysql.find(Person.class, new Where("t.$1=$c", 1));
		assertEquals(TestExpectUtils.EXPECT_1, right7.get(0));
		final List<Person> right8 = mysql.find(Person.class, new Where("$1=$c", 1));
		assertEquals(TestExpectUtils.EXPECT_1, right8.get(0));
		final List<Person> right9 = mysql.find(Person.class, new Where("$1>$c", 1));
		assertEquals(TestExpectUtils.EXPECT_2, right9.get(0));
		final List<Person> right10 = mysql.find(Person.class, new Where("$1>$c", 2));
		assertTrue(right10.isEmpty());
		final List<Person> right11 = mysql.find(Person.class, new Where("$1=$c AND $2=$c", 1, "ZFly"));
		assertEquals(TestExpectUtils.EXPECT_1, right11.get(0));
		final List<Person> right12 = mysql.find(Person.class, new Where("$1>$c AND $2=$c", 1, "ZFly"));
		assertTrue(right12.isEmpty());

		final List<Person> wrong1 = mysql.find(null, new Where("$1=$c", 1));
		assertTrue(wrong1.isEmpty());
		final List<Person> wrong2 = mysql.find(Person.class, new Where("$100=$c", 1));
		assertTrue(wrong2.isEmpty());
		final List<Person> wrong3 = mysql.find(null, new Where("$100=$c", 1));
		assertTrue(wrong3.isEmpty());
		final List<Person> wrong4 = mysql.find(null, null);
		assertTrue(wrong4.isEmpty());
	}

	@AfterClass
	public static void reset() {
		TestExpectUtils.reset();
	}
}
