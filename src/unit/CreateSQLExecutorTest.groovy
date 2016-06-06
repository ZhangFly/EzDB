package unit

import zfly.yfei.db.core.YFeiConfig
import zfly.yfei.db.core.YFeiDB
import zfly.yfei.db.core.YFeiSqlExecutor

import java.sql.SQLException

/**
 *
 * Created by YFei on 16/6/3.
 */
class CreateSQLExecutorTest extends GroovyTestCase {
    void testCreateSQLExecutor() {
        shouldFail(SQLException) {
            YFeiDB.createSQLExecutor(null);
        }
        shouldFail(SQLException) {
            YFeiDB.createSQLExecutor(new YFeiConfig());
        }
        shouldFail(SQLException) {
            YFeiDB.createSQLExecutor(new YFeiConfig().setDataBase("MySQL"));
        }
        shouldFail(SQLException) {
            YFeiDB.createSQLExecutor(new YFeiConfig().setDataBase("MySQL")).setUrl("jdbc:MySQL://localhost:3306/YFeiDB_Test?characterEncoding=utf8");
        }
        final YFeiSqlExecutor executor = YFeiDB.createSQLExecutor(new YFeiConfig().setDataBase("MySQL")
                .setUrl("jdbc:mysql://localhost:3306/YFeiDB_Test?characterEncoding=utf8").setUserName("root")
                .setPassWord("123456").setPoolSize(1).setShowSql(true));
        assertNotNull(executor)
    }

}
