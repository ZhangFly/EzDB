package unit

import zfly.yfei.db.core.condition.Where

import java.sql.SQLException

/**
 *
 * Created by YFei on 16/6/4.
 */
class SaveAndDeleteTest extends GroovyTestCase {

    private static def alert = new Person();

    public void testSaveAndDelete() {
        TestUtils.MySQL.save(alert)
        assertLength(3, TestUtils.MySQL.find(Person.class).toArray())
        TestUtils.MySQL.save(alert)
        assertLength(4, TestUtils.MySQL.find(Person.class).toArray())

        shouldFail(SQLException) {
            TestUtils.MySQL.save(null)
        }

        TestUtils.MySQL.delete(Person.class, new Where("\$1>2"))
        assertLength(2, TestUtils.MySQL.find(Person.class).toArray())

        shouldFail(SQLException) {
            TestUtils.MySQL.delete(null)
        }

        shouldFail(SQLException) {
            TestUtils.MySQL.delete(null, null)
        }
    }

}
