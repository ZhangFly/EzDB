package unit;

import java.sql.SQLException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import model.Person;
import zfly.Where;
import zfly.YFeiConfig;
import zfly.YFeiDB;

public class TestFind {

	private static YFeiDB mysql = null;

	@Rule
	public ExpectedException exp = ExpectedException.none();

	@Test
	public void testCreateDB() throws SQLException {
		mysql = YFeiDB.createDB(new YFeiConfig().setDataBase("MySQL")
				.setUrl("jdbc:mysql://localhost:3306/YFeiDB_Test?characterEncoding=utf8").setUserName("root")
				.setPassWord("123456").setPoolSize(1).setShowSql(true));
		Assert.assertNotNull(mysql);
	}

	@Test
	public void testFindAllRightUse() throws SQLException {
		final List<Person> right = mysql.find(Person.class);
		Assert.assertArrayEquals(TestExpectUtils.EXPECT_ARRAY, right.toArray());
	}

	@Test
	public void testFindAllWrongBecauseNullClass() throws SQLException {
		exp.expectMessage("Can not find table information for Class");
		final List<Person> wrong = mysql.find(null);
		Assert.assertTrue(wrong.isEmpty());
	}

	@Test
	public void testFindByIdRightUse() throws SQLException {
		// 数据库中有记录
		final Person right = mysql.find(Person.class, 1);
		Assert.assertEquals(TestExpectUtils.EXPECT_1, right);
		// 数据库中无记录
		final Person right1 = mysql.find(Person.class, 3);
		Assert.assertNull(right1);
	}

	@Test
	public void testFindByIdWrongBecauseNullClass() throws SQLException {
		exp.expectMessage("Can not find table information for Class");
		final Person wrong = mysql.find(null, 1);
		Assert.assertNull(wrong);
	}

	@Test
	public void testFindByWhereRightUse() throws SQLException {
		// 数据库中有记录
		final List<Person> right1 = mysql.find(Person.class, new Where("id=1"));
		Assert.assertEquals(TestExpectUtils.EXPECT_1, right1.get(0));
		final List<Person> right2 = mysql.find(Person.class, new Where("t.id=1"));
		Assert.assertEquals(TestExpectUtils.EXPECT_1, right2.get(0));
		final List<Person> right3 = mysql.find(Person.class, new Where("t.$1=1"));
		Assert.assertEquals(TestExpectUtils.EXPECT_1, right3.get(0));
		final List<Person> right4 = mysql.find(Person.class, new Where("$1=1"));
		Assert.assertEquals(TestExpectUtils.EXPECT_1, right4.get(0));
		final List<Person> right5 = mysql.find(Person.class, new Where("id=$c", 1));
		Assert.assertEquals(TestExpectUtils.EXPECT_1, right5.get(0));
		final List<Person> right6 = mysql.find(Person.class, new Where("t.id=$c", 1));
		Assert.assertEquals(TestExpectUtils.EXPECT_1, right6.get(0));
		final List<Person> right7 = mysql.find(Person.class, new Where("t.$1=$c", 1));
		Assert.assertEquals(TestExpectUtils.EXPECT_1, right7.get(0));
		final List<Person> right8 = mysql.find(Person.class, new Where("$1=$c", 1));
		Assert.assertEquals(TestExpectUtils.EXPECT_1, right8.get(0));
		final List<Person> right9 = mysql.find(Person.class, new Where("$1>$c", 1));
		Assert.assertEquals(TestExpectUtils.EXPECT_2, right9.get(0));
		final List<Person> right10 = mysql.find(Person.class, new Where("$1=$c AND $2=$c", 1, "ZFly"));
		Assert.assertEquals(TestExpectUtils.EXPECT_1, right10.get(0));
		final List<Person> right11 = mysql.find(Person.class, null);
		Assert.assertArrayEquals(TestExpectUtils.EXPECT_ARRAY, right11.toArray());
		// 数据库中无记录
		final List<Person> right12 = mysql.find(Person.class, new Where("$1>$c", 2));
		Assert.assertTrue(right12.isEmpty());
		final List<Person> right13 = mysql.find(Person.class, new Where("$1>$c AND $2=$c", 1, "ZFly"));
		Assert.assertTrue(right13.isEmpty());
	}

	@Test
	public void testFindByWhereWrongBecauseNullClas() throws SQLException {
		exp.expectMessage("Can not find table information for Class");
		final List<Person> wrong = mysql.find(null, new Where("$1=$c", 1));
		Assert.assertTrue(wrong.isEmpty());
	}

	@Test
	public void testFindByWhereWrongBecauseOverfloww() throws SQLException {
		exp.expectMessage("Placeholder was overflow!!");
		final List<Person> wrong = mysql.find(Person.class, new Where("$100=$c", 1));
		Assert.assertTrue(wrong.isEmpty());
	}

	@Test
	public void testFindByWhereWrongBecauseNullClasAndOverfloww() throws SQLException {
		exp.expectMessage("Can not find table information for Class");
		final List<Person> wrong = mysql.find(null, new Where("$100=$c", 1));
		Assert.assertTrue(wrong.isEmpty());
	}

	@AfterClass
	public static void reset() {
		TestExpectUtils.reset();
	}
}
