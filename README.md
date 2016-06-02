# YFeiDB

YFeiDB是一个简单封装JDBC操作的模块。可以通过使用注解和对实体类的反射，动态生成SQL语句；以及将数据库查询结果反射成对应JAVA实体类。目前仅支持MySql数据库。

## Catalog
[toc]

## Installs

* 最简单的安装方法是下载 [./](http://git.oschina.net/SmallHuang/YFeiDB/tree/master) 目录下的 YFeiDB.x.x.x.jar，导入工程目录下。

* 你也可以下载 [zip](http://git.oschina.net/SmallHuang/YFeiDB/repository/archive/master) 导入工程。 

## Examples

使用YFeiDB模块需要经过一下几个步骤：

###创建一个数据库对象：

```java
	final YFeiDB mysql = YFeiDB.createDB(new YFeiDBConfig()
				.setDataBase("MySQL")
				.setUrl("jdbc:mysql://xxxx")
				.setUserName("xxxx")
				.setPassWord("xxxx")
				.setPoolSize(1)
				.setShowSql(true));
```

###使用注解：
想要对数据库进行操作，你还需要建立数据库对应的实体类：

```java
YFeiTable("Student") // 设置实体类对应表名，如果不设置自动生成的表名为类名的小写(student)
public class Student {

	@YFeiColumn(primaryKey = true) // 声明该属性为主键
	private int id;

	@YFeiColumn(alias = "nickName") // 为该属性设置数据库表别名，如果不设置自动生成为(name)
	private String name;

	@YFeiColumn(ignore = true) // 该属性不对应数据库
	private String unuse;

	// ...
}
```

###查询操作：

```java
// 从数据库中获取所有Student对象
// sql: SELECT * FROM Student;
final List<Student> students = mysql.find(Student.class);

// 从数据库中获取id为1的Student对象
// sql: SELECT * FROM Student WHERE Student.id='1';
final Student student = mysql.find(Student.class, 1);

// 从数据库中获取所有id>1的Student对象
// sql: SELECT * FROM Student WHERE Student.id>'1';
final List<Student> students1 = mysql.find(Student.class, new Where("$1>$c", 1));
```

###指定查询条件：
类Where，该类通过占位符来指定实体类的查询条件：

```java
// t 将会替换为表名 Student，$C 将会替换为可变参数的值 1
// sql: SELECT * FROM Student WHERE Student.id>'1';
YFeiDB.find(Student.class, new Where("t.id>$c", 1));

// t 将会替换为表名 Student, $1 将会替换为Student的第一个非忽略属性对应的列名 id，$C 将会替换为可变参数的值 1
// sql: SELECT * FROM Student WHERE Student.id>'1';
YFeiDB.find(Student.class, new Where("t.$1>$c", 1));

// $1 将会替换为表名 Student + "." + 第一个非忽略属性对应的列名 id，$C 将会替换为可变参数的值 1
// sql: SELECT * FROM Student WHERE Student.id>'1';
YFeiDB.find(Student.class, new Where("$1>$c", 1));
```

###保存操作：

```java
final Student student = new Student();
student.setName("xx");
// 保存Student对象到数据库
// sql: INSERT INTO Student (Student.name) VALUES ('xx');
mysql.save(student);
```
###删除操作：

```java
final Student student = new Student();
student.setId(1);
// 删除数据库Student对象
// sql: DELETE FROM Student WHERE Student.id='1';
mysql.delete(student);

// 删除数据库Student对象
// sql: DELETE FROM Student WHERE Student.name='xx';
mysql.delete(Student.class, new Where("$2='xx'"));
```

###更新操作：

```java
final Student student = new Student();
student.setId(1);
student.setName("xx");
// 更新数据库Student对象
// sql: UPDATE Student SET Student.name='xx' WHERE Student.id='1';
mysql.update(student);

student.setName("oo");
// 更新数据库Student对象
// sql: UPDATE Student SET Student.name='oo' WHERE Student.name='xx';
mysql.delete(Student.class, new Where("$2='xx'"));
```

## PS

该模块目前仅能工作在JRE1.8环境上；仅支持MySQL数据库；不支持外键链接等高级功能；内部链接池不支持自动扩容，不支持懒加载；异常处理尚不规范；本人学生，一边学习一边玩耍。。。