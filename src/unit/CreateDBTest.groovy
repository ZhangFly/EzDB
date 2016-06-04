package unit

import zfly.yfei.db.YFeiConfig
import zfly.yfei.db.YFeiDB

import java.sql.SQLException

/**
 *
 * Created by YFei on 16/6/3.
 */
class CreateDBTest extends GroovyTestCase{

    void testCreateDB() {
        shouldFail(SQLException) {
            YFeiDB.createDB(null);
        }
        shouldFail(SQLException) {
            YFeiDB.createDB(new YFeiConfig());
        }
        shouldFail(SQLException) {
            YFeiDB.createDB(new YFeiConfig().setDataBase("MySQL"));
        }
        shouldFail(SQLException) {
            YFeiDB.createDB(new YFeiConfig().setDataBase("MySQL")).setUrl("jdbc:MySQL://localhost:3306/YFeiDB_Test?characterEncoding=utf8");
        }
        assertNotNull(YFeiDB.createDB(new YFeiConfig()
                .setDataBase("MySQL")
                .setUrl("jdbc:mysql://localhost:3306/YFeiDB_Test?characterEncoding=utf8")
                .setUserName("root")
                .setPassWord("123456")
                .setPoolSize(1)
                .setShowSql(true)));
    }
}
