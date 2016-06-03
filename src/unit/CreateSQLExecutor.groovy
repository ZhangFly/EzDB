package unit

import zfly.YFeiConfig
import zfly.YFeiDB
import zfly.YFeiSQLExecutor

import java.sql.SQLException

/**
 * Created by YFei on 16/6/3.
 */
class CreateSQLExecutor extends GroovyTestCase{
    void testCreateSQLExecutor() {
        shouldFail(SQLException) {
            YFeiDB.createSQLExcutor(null);
        }
        shouldFail(SQLException) {
            YFeiDB.createSQLExcutor(new YFeiConfig());
        }
        shouldFail(SQLException) {
            YFeiDB.createSQLExcutor(new YFeiConfig().setDataBase("MySQL"));
        }
        shouldFail(SQLException) {
            YFeiDB.createSQLExcutor(new YFeiConfig().setDataBase("MySQL")).setUrl("jdbc:mysql://localhost:3306/YFeiDB_Test?characterEncoding=utf8");
        }
        final YFeiSQLExecutor executor = YFeiDB.createSQLExcutor(new YFeiConfig().setDataBase("MySQL")
                .setUrl("jdbc:mysql://localhost:3306/YFeiDB_Test?characterEncoding=utf8").setUserName("root")
                .setPassWord("123456").setPoolSize(1).setShowSql(true));
        assertNotNull(executor)
    }

}
