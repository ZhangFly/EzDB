package unit

import model.Person
import org.junit.Test
import zfly.Where
import zfly.YFeiConfig
import zfly.YFeiDB

import java.sql.SQLException

/**
 *
 * Created by YFei on 16/6/3.
 */
class FindTest extends GroovyTestCase {

    private static def mysql = YFeiDB.createDB(new YFeiConfig()
            .setDataBase("MySQL")
            .setUrl("jdbc:mysql://localhost:3306/YFeiDB_Test?characterEncoding=utf8")
            .setUserName("root")
            .setPassWord("123456")
            .setPoolSize(1)
            .setShowSql(true));

    @Test
    void testFindAll() {
        final List<Person> right = mysql.find(Person.class);
        assertArrayEquals(TestExpectUtils.EXPECT_ARRAY, right.toArray());

        shouldFail(SQLException) {
            mysql.find(null);
        }
    }

    @Test
    void testFindById() {
        // 数据库中有记录
        final Person right = mysql.find(Person.class, 1);
        assertEquals(TestExpectUtils.EXPECT_1, right);
        // 数据库中无记录
        final Person right1 = mysql.find(Person.class, 3);
        assertNull(right1);

        shouldFail(SQLException) {
            mysql.find(null, 1);
        }
    }

    @Test
    void testFindByWhere() {
        final List<Person> right1 = mysql.find(Person.class, new Where("id=1"));
        assertEquals(TestExpectUtils.EXPECT_1, right1.get(0));
        final List<Person> right2 = mysql.find(Person.class, new Where("t.id=1"));
        assertEquals(TestExpectUtils.EXPECT_1, right2.get(0));
        final List<Person> right3 = mysql.find(Person.class, new Where("t.\$1=1"));
        assertEquals(TestExpectUtils.EXPECT_1, right3.get(0));
        final List<Person> right4 = mysql.find(Person.class, new Where("\$1=1"));
        assertEquals(TestExpectUtils.EXPECT_1, right4.get(0));
        final List<Person> right5 = mysql.find(Person.class, new Where("id=\$c", 1));
        assertEquals(TestExpectUtils.EXPECT_1, right5.get(0));
        final List<Person> right6 = mysql.find(Person.class, new Where("t.id=\$c", 1));
        assertEquals(TestExpectUtils.EXPECT_1, right6.get(0));
        final List<Person> right7 = mysql.find(Person.class, new Where("t.\$1=\$c", 1));
        assertEquals(TestExpectUtils.EXPECT_1, right7.get(0));
        final List<Person> right8 = mysql.find(Person.class, new Where("\$1=\$c", 1));
        assertEquals(TestExpectUtils.EXPECT_1, right8.get(0));
        final List<Person> right9 = mysql.find(Person.class, new Where("\$1>\$c", 1));
        assertEquals(TestExpectUtils.EXPECT_2, right9.get(0));
        final List<Person> right10 = mysql.find(Person.class, new Where("\$1=\$c AND \$2=\$c", 1, "ZFly"));
        assertEquals(TestExpectUtils.EXPECT_1, right10.get(0));
        final List<Person> right11 = mysql.find(Person.class, null);
        assertArrayEquals(TestExpectUtils.EXPECT_ARRAY, right11.toArray());
        // 数据库中无记录
        final List<Person> right12 = mysql.find(Person.class, new Where("\$1>\$c", 2));
        assertTrue(right12.isEmpty());
        final List<Person> right13 = mysql.find(Person.class, new Where("\$1>\$c AND \$2=\$c", 1, "ZFly"));
        assertTrue(right13.isEmpty());

        shouldFail(Exception) {
            mysql.find(null, new Where("\$1=\$c", 1));
        }
        shouldFail(Exception) {
            mysql.find(Person.class, new Where("\$100=\$c", 1));
        }
        shouldFail(Exception) {
            mysql.find(null, new Where("\$1=\$c", 1));
        }
        shouldFail(Exception) {
            mysql.find(null, null);
        }
    }
}
