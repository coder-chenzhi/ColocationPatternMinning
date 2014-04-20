package test;

public class TestStatic1 {
static TestStatic2 test;
public static void main(String[] argc){
	test.ini  = new Integer(2);
	System.out.println(test.ini);
}

}
//静态成员如果是引用，会被自动初始化为null