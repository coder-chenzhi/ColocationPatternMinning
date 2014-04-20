package test;
import java.util.*;

public class TestArraySet {
	public static void main(String[] argc){
		HashMap<Features,ArrayList<Integer[]>> features = 
				new HashMap<Features,ArrayList<Integer[]>>();
		Features fs = new Features(new int[]{1,3,2,4});
		ArrayList<Integer[]> array = new ArrayList<Integer[]>();
		Integer[] intemp = {1,2,3,4}; 
		array.add(intemp);
		array.add(new Integer[]{1,2,3,5});
		features.put(fs, array);
		features.put(new Features(new int[]{1,2,2,4}), array);
		Collection<ArrayList<Integer[]>> coll = features.values();
		//问题主要在于不能定义泛型数组
		ArrayList<Integer[]>[] arrays = coll.toArray(new ArrayList[0]);
		
		for( int i = 0; i < arrays.length; i++){
			ArrayList<Integer[]> temp = arrays[i];
			for(int j = 0; j < temp.size(); j++){
				Integer[] int1 = temp.get(j);
				Features fea = new Features(int1);
				System.out.println(fea);
			}
		}
		
	}
}
