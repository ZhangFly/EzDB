package unit;

import model.Person;
import zfly.yfei.db.core.YFeiConfig;
import zfly.yfei.db.core.YFeiDB;

import java.sql.SQLException;

public class TestUtils {

	public static Person EXPECT_1 = null;
	public static Person EXPECT_2 = null;
	final public static Person EXPECT_ARRAY[] = new Person[2];
	public static YFeiDB MySQL = null;


	static {
		EXPECT_1 = new Person();
		EXPECT_1.setId(1);
		EXPECT_1.setName("ZFly");
		EXPECT_1.setAge(24);
		EXPECT_1.setSex("Male");
		EXPECT_1.setIntro("SB");
		EXPECT_2 = new Person();
		EXPECT_2.setId(2);
		EXPECT_2.setName("YFei");
		EXPECT_2.setAge(24);
		EXPECT_2.setSex("Female");
		EXPECT_2.setIntro("SB*1");
		EXPECT_ARRAY[0] = EXPECT_1;
		EXPECT_ARRAY[1] = EXPECT_2;

		try {
			MySQL = YFeiDB.createDB(new YFeiConfig()
					.setDataBase("MySQL")
					.setUrl("jdbc:MySQL://localhost:3306/YFeiDB_Test?characterEncoding=utf8")
					.setUserName("root")
					.setPassWord("123456")
					.setPoolSize(1)
					.setShowSql(true));
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
