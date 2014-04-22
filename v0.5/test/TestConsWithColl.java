package test;
import java.util.*;
public class TestConsWithColl {
	public static void main(String[] argc){
		HashSet<Integer> set;
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(new Integer(0));
		list.add(new Integer(1));
		list.add(new Integer(2));
		set = new HashSet<Integer>(list);
		System.out.println(set);
		list.remove(0);
		System.out.println(list);
		System.out.println(set);
		
	}
}
