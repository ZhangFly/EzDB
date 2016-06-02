package main;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import zfly.Where;
import zfly.YFeiConfig;
import zfly.YFeiDB;

public class Test {

	private static Logger logger = Logger.getLogger(Test.class);

	public static void main(String args[]) {

		final Properties log4jProperties = new Properties();
		log4jProperties.setProperty("log4j.rootLogger", "debug, stdout");
		log4jProperties.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
		log4jProperties.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
		log4jProperties.setProperty("log4j.appender.stdout.layout.ConversionPattern",
				"%d{yyyy-MM-dd HH:mm:ss} %p [%c] %m%n");
		PropertyConfigurator.configure(log4jProperties);

		final YFeiDB mysql = YFeiDB.createDB(new YFeiConfig().setDataBase("MySQL")
				.setUrl("jdbc:mysql://localhost:3306/YFeiDB_Test?characterEncoding=utf8").setUserName("root")
				.setPassWord("123456").setPoolSize(1).setShowSql(true));

		logger.info(mysql.find(Person.class));
		logger.info(mysql.find(Person.class, 1));
		logger.info(mysql.find(Person.class, new Where("$1>$c", 1)));

		logger.info(mysql.find(null));

	}

}
