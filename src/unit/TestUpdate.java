package unit;

import org.junit.BeforeClass;

import model.Person;

public class TestUpdate {

	private static Person[] expect = new Person[2];

	@BeforeClass
	public static void initExpect() {
		final Person person1 = new Person();
		person1.setId(1);
		person1.setName("ZFly");
		person1.setAge(24);
		person1.setSex("Male");
		person1.setIntro("SB");
		final Person person2 = new Person();
		person2.setId(2);
		person2.setName("YFei");
		person2.setAge(24);
		person2.setSex("Female");
		person2.setIntro("SB*1");
		expect[0] = person1;
		expect[1] = person2;
	}

}
