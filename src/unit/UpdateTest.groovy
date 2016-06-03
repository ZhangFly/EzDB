package unit

import model.Person
import org.junit.After
import org.junit.BeforeClass
import org.junit.Test
import zfly.Where
import zfly.YFeiConfig
import zfly.YFeiDB

import java.sql.SQLException

import static org.junit.Assert.assertEquals

/**
 *
 * Created by YFei on 16/6/3.
 */
class UpdateTest extends GroovyTestCase{

    private static def mysql = YFeiDB.createDB(new YFeiConfig()
            .setDataBase("MySQL")
            .setUrl("jdbc:mysql://localhost:3306/YFeiDB_Test?characterEncoding=utf8")
            .setUserName("root")
            .setPassWord("123456")
            .setPoolSize(1)
            .setShowSql(true));

    private static def alert = new Person();;


    @BeforeClass
    public static void initAlertPerson() {
        alert.setId(1);
        alert.setAge(0);
        alert.setName("maxpup");
        alert.setSex("unkonwn");
        alert.setIntro("Big SB!");
    }

    @After
    public void rollBackDB() {
        mysql.update(TestExpectUtils.EXPECT_1);
        mysql.update(TestExpectUtils.EXPECT_2);
    }

    @Test
    public void testUpdateFailure() {
        mysql.update(TestExpectUtils.EXPECT_1);

        shouldFail(SQLException) {
            mysql.update(null);
        }

        shouldFail(SQLException) {
            mysql.update(null, new Where("\$2=\$c", "ZFly"));
        }

        shouldFail(SQLException) {
            mysql.update(null, null);
        }
    }

    @Test
    public void testUpdateSuccess1() {
        mysql.update(alert);
        final Person right1 = mysql.find(Person.class, 1);
        assertEquals(alert, right1);
    }

    @Test
    public void testUpdateSuccess2() {
        mysql.update(alert, new Where("\$2=\$c", "ZFly"));
        assertEquals(alert, mysql.find(Person.class, 1));
    }

    @Test
    public void testUpdateSuccess3() {
        mysql.update(alert, null);
        assertEquals(alert, mysql.find(Person.class, 1));

    }
}
