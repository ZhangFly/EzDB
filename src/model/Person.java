package model;

import org.apache.commons.lang3.StringUtils;

import zfly.yfei.db.core.annotation.YFeiColumn;
import zfly.yfei.db.core.annotation.YFeiTable;

@YFeiTable("person")
public class Person {

	@YFeiColumn(primaryKey = true)
	private int id = 1;

	private String name = "SB";

	private int age = 100;

	private String sex = "Female";

	@YFeiColumn(alias = "say")
	private String intro = "SB*2";

	@YFeiColumn(ignore = true)
	private String unuse;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getUnuse() {
		return unuse;
	}

	public void setUnuse(String unuse) {
		this.unuse = unuse;
	}

	@Override
	public String toString() {
		return String.format("{id=%d, name=\"%s\", age=%d, sex=\"%s\", intro=\"%s\", ununse=%s}", getId(), getName(),
				getAge(), getSex(), getIntro(), getUnuse());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Person)) {
			return false;
		}

		Person person = (Person) obj;

		return id == person.id && StringUtils.equals(name, person.name) && StringUtils.equals(intro, person.intro)
				&& age == person.age && StringUtils.equals(sex, person.sex);
	}
}
