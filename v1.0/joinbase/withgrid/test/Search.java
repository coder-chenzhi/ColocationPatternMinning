package joinbase.withgrid.test;

import java.util.*;

public class Search {
	public static void main(String[] args) {
		TreeSet<Item> set = new TreeSet<Item>();
		Item a = new Item(new int[]{1,2,3});
		Item b = new Item(new int[]{1,2,4});
		Item c = new Item(new int[]{1,2,5});
		Item d = new Item(new int[]{1,2,7});
		Item e = new Item(new int[]{1,5,6});
		Item f = new Item(new int[]{2,5,6});
		set.add(a);		set.add(b);		set.add(c);
		set.add(d);		set.add(e);		set.add(f);
		
		NavigableSet<Item> result = set.subSet(a, true, f, true);
		for(Item p : result) {
			System.out.println(p);
		}
		
	}
}
