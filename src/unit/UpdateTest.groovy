package unit

import model.Person
import zfly.yfei.db.Where

import java.sql.SQLException

import static org.junit.Assert.assertEquals

/**
 *
 * Created by YFei on 16/6/3.
 */
class UpdateTest extends GroovyTestCase{

    private static def alert = new Person();

    public void tearDown() {
        TestUtils.MySQL.update(TestUtils.EXPECT_1);
        TestUtils.MySQL.update(TestUtils.EXPECT_2);
        println "after test"
    }

    public void testUpdateFailure() {
        TestUtils.MySQL.update(TestUtils.EXPECT_1);

        shouldFail(SQLException) {
            TestUtils.MySQL.update(null);
        }

        shouldFail(SQLException) {
            TestUtils.MySQL.update(null, new Where("\$2=\$c", "ZFly"));
        }

        shouldFail(SQLException) {
            TestUtils.MySQL.update(null, null);
        }
    }

    public void testUpdateSuccess1() {
        TestUtils.MySQL.update(alert);
        final Person right1 = TestUtils.MySQL.find(Person.class, 1);
        assertEquals(alert, right1);
    }

    public void testUpdateSuccess2() {
        TestUtils.MySQL.update(alert, new Where("\$2=\$c", "ZFly"));
        assertEquals(alert, TestUtils.MySQL.find(Person.class, 1));
    }

    public void testUpdateSuccess3() {
        TestUtils.MySQL.update(alert, null);
        assertEquals(alert, TestUtils.MySQL.find(Person.class, 1));

    }
}
