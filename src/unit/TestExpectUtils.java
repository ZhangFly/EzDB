package unit;

import model.Person;

import java.util.ArrayList;
import java.util.List;

public class TestExpectUtils {

	public static Person EXPECT_1 = null;
	public static Person EXPECT_2 = null;
	final public static Person EXPECT_ARRAY[] = new Person[2];

	static {
		init();
	}

	private TestExpectUtils() {
	}

	public static void reset() {
		init();
	}

	private static void init() {
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
	}

}
