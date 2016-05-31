package main;

import ZFly.Where;
import ZFly.YFeiDB;
import ZFly.YFeiDBConfig;

public class Test {

	public static void main(String args[]) {

		YFeiDB.conn(new YFeiDBConfig().setDataBase("MySQL")
				.setUrl("jdbc:mysql://localhost:3306/YFeiDB_Test?characterEncoding=utf8").setUserName("root")
				.setPassWord("123456").setPoolSize(1).setShowSql(true));

		System.out.println(YFeiDB.find(Person.class));
		System.out.println(YFeiDB.find(Person.class, 1));
		System.out.println(YFeiDB.find(Person.class, new Where("$1>$c", 1)));
	}

}
