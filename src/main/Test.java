package main;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import ZFly.Where;
import ZFly.YFeiDB;
import ZFly.YFeiDBConfig;

public class Test {

	private static Logger logger = Logger.getLogger(Test.class);

	public static void main(String args[]) {

		try {
			PropertyConfigurator.configure("log4j.properties");
			YFeiDB.conn(new YFeiDBConfig().setDataBase("MySQL")
					.setUrl("jdbc:mysql://localhost:3306/YFeiDB_Test?characterEncoding=utf8").setUserName("root")
					.setPassWord("123456").setPoolSize(1).setShowSql(true));

			logger.info(YFeiDB.find(Person.class));
			logger.info(YFeiDB.find(Person.class, 1));
			logger.info(YFeiDB.find(Person.class, new Where("$1>$c", 1)));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
