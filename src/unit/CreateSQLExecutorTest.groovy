package unit

import zfly.ez.db.core.EzDBConfig
import zfly.ez.db.core.EzDB
import zfly.ez.db.core.EzSQLExecutor

import java.sql.SQLException

/**
 *
 * Created by YFei on 16/6/3.
 */
class CreateSQLExecutorTest extends GroovyTestCase {
    void testCreateSQLExecutor() {
        shouldFail(SQLException) {
            EzDB.createSQLExecutor(null);
        }
        shouldFail(SQLException) {
            EzDB.createSQLExecutor(new EzDBConfig());
        }
        shouldFail(SQLException) {
            EzDB.createSQLExecutor(new EzDBConfig().setDataBase("MySQL"));
        }
        shouldFail(SQLException) {
            EzDB.createSQLExecutor(new EzDBConfig().setDataBase("MySQL")).setUrl("jdbc:MySQL://localhost:3306/YFeiDB_Test?characterEncoding=utf8");
        }
        final EzSQLExecutor executor = EzDB.createSQLExecutor(new EzDBConfig().setDataBase("MySQL")
                .setUrl("jdbc:mysql://localhost:3306/YFeiDB_Test?characterEncoding=utf8").setUserName("root")
                .setPassWord("123456").setPoolSize(1).setShowSql(true));
        assertNotNull(executor)
    }

}
