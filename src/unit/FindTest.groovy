package unit

import model.Person
import zfly.yfei.db.condition.Where

import java.sql.SQLException

/**
 *
 * Created by YFei on 16/6/3.
 */
class FindTest extends GroovyTestCase {

    void testFindAll() {
        final List<Person> right = TestUtils.MySQL.find(Person.class);
        assertArrayEquals(TestUtils.EXPECT_ARRAY, right.toArray());

        shouldFail(SQLException) {
            TestUtils.MySQL.find(null);
        }
    }

    void testFindById() {
        // 数据库中有记录
        final Person right = TestUtils.MySQL.find(Person.class, 1);
        assertEquals(TestUtils.EXPECT_1, right);
        // 数据库中无记录
        final Person right1 = TestUtils.MySQL.find(Person.class, 3);
        assertNull(right1);

        shouldFail(SQLException) {
            TestUtils.MySQL.find(null, 1);
        }
    }

    void testFindByWhere() {
        final List<Person> right1 = TestUtils.MySQL.find(Person.class, new Where("id=1"));
        assertEquals(TestUtils.EXPECT_1, right1.get(0));
        final List<Person> right2 = TestUtils.MySQL.find(Person.class, new Where("t.id=1"));
        assertEquals(TestUtils.EXPECT_1, right2.get(0));
        final List<Person> right3 = TestUtils.MySQL.find(Person.class, new Where("t.\$1=1"));
        assertEquals(TestUtils.EXPECT_1, right3.get(0));
        final List<Person> right4 = TestUtils.MySQL.find(Person.class, new Where("\$1=1"));
        assertEquals(TestUtils.EXPECT_1, right4.get(0));
        final List<Person> right5 = TestUtils.MySQL.find(Person.class, new Where("id=\$c", 1));
        assertEquals(TestUtils.EXPECT_1, right5.get(0));
        final List<Person> right6 = TestUtils.MySQL.find(Person.class, new Where("t.id=\$c", 1));
        assertEquals(TestUtils.EXPECT_1, right6.get(0));
        final List<Person> right7 = TestUtils.MySQL.find(Person.class, new Where("t.\$1=\$c", 1));
        assertEquals(TestUtils.EXPECT_1, right7.get(0));
        final List<Person> right8 = TestUtils.MySQL.find(Person.class, new Where("\$1=\$c", 1));
        assertEquals(TestUtils.EXPECT_1, right8.get(0));
        final List<Person> right9 = TestUtils.MySQL.find(Person.class, new Where("\$1>\$c", 1));
        assertEquals(TestUtils.EXPECT_2, right9.get(0));
        final List<Person> right10 = TestUtils.MySQL.find(Person.class, new Where("\$1=\$c AND \$2=\$c", 1, "ZFly"));
        assertEquals(TestUtils.EXPECT_1, right10.get(0));
        final List<Person> right11 = TestUtils.MySQL.find(Person.class, null);
        assertArrayEquals(TestUtils.EXPECT_ARRAY, right11.toArray());
        // 数据库中无记录
        final List<Person> right12 = TestUtils.MySQL.find(Person.class, new Where("\$1>\$c", 2));
        assertTrue(right12.isEmpty());
        final List<Person> right13 = TestUtils.MySQL.find(Person.class, new Where("\$1>\$c AND \$2=\$c", 1, "ZFly"));
        assertTrue(right13.isEmpty());

        shouldFail(Exception) {
            TestUtils.MySQL.find(null, new Where("\$1=\$c", 1));
        }
        shouldFail(Exception) {
            TestUtils.MySQL.find(Person.class, new Where("\$100=\$c", 1));
        }
        shouldFail(Exception) {
            TestUtils.MySQL.find(null, new Where("\$1=\$c", 1));
        }
        shouldFail(Exception) {
            TestUtils.MySQL.find(null, null);
        }
    }
}
