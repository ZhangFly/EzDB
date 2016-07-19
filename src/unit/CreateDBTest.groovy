package unit

import zfly.ez.db.core.EzDBConfig
import zfly.ez.db.core.EzDB

import java.sql.SQLException

/**
 *
 * Created by YFei on 16/6/3.
 */
class CreateDBTest extends GroovyTestCase{

    void testCreateDB() {
        shouldFail(SQLException) {
            EzDB.createDB(null);
        }
        shouldFail(SQLException) {
            EzDB.createDB(new EzDBConfig());
        }
        shouldFail(SQLException) {
            EzDB.createDB(new EzDBConfig().setDataBase("MySQL"));
        }
        shouldFail(SQLException) {
            EzDB.createDB(new EzDBConfig().setDataBase("MySQL")).setUrl("jdbc:MySQL://localhost:3306/EzDB_Test?characterEncoding=utf8");
        }
        assertNotNull(EzDB.createDB(new EzDBConfig()
                .setDataBase("MySQL")
                .setUrl("jdbc:mysql://localhost:3306/EzDB_Test?characterEncoding=utf8")
                .setUserName("root")
                .setPassWord("123456")
                .setPoolSize(1)
                .setShowSql(true)));
    }
}
