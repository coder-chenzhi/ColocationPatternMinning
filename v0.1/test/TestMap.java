package test;
import java.util.*;


public class TestMap {
	public static Integer[] createGaussian(int num, int avg, int sigma2){
		Random temp = new Random();
		Integer[] array = new Integer[num];
		for(int i = 0; i < num; i ++){
			array[i] = new Integer(0);
			array[i] = (int)(temp.nextGaussian()
							* Math.sqrt(sigma2) + avg);
			if(array[i] <= 0) 
				array[i] = avg;
			
		}			
		return array;
	}
	
	
	public static void main(String[] argc){
		HashMap<Integer[], ArrayList<Integer[]>> candidate = 
				new HashMap<Integer[],ArrayList<Integer[]>>();
		for(int num = 1; num < 4; num++){
			Integer[] feature = new Integer[num];
			Integer[] instance = new Integer[num];
			ArrayList<Integer[]> instances = new ArrayList<Integer[]>();

			for(int i = num-1; i >= 0; i--){
				feature[i] = new Integer(0);
				instance[i] = new Integer(0);
			}

			feature = createGaussian(num,5,2);
			instance = createGaussian(num,5,2);
			instances.add(instance);
			candidate.put(feature, instances);
			candidate.get(instances);
		}
		Set<Integer[]> allFeature = candidate.keySet();
		Collection<ArrayList<Integer[]>> allInstances = candidate.values();
		System.out.println("all feature\n"+allFeature);
		System.out.println("all Instances\n"+allInstances);
	}
}
