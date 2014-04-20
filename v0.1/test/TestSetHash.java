package test;

import java.util.*;

class Features{
	TreeSet<Integer> feature = new TreeSet<Integer>();
	public Features(int[] num){		
		for(int i = 0; i < num.length; i++){
			this.feature.add(new Integer(num[i]));
		}
		
	}
	
	public Features(Integer[] num){		
		for(int i = 0; i < num.length; i++){
			this.feature.add(new Integer(num[i]));
		}
		
	}
	
	public String toString(){
		String str="[";
		Iterator<Integer> it = this.feature.iterator();
		while(it.hasNext()){
			Integer in = it.next();
			str+=((int)in+" ");
		}
		str+="]";
		return str;
	}
	public boolean equals(Object temp){
		Integer[] tempArray =((Features)temp).feature.toArray(new Integer[0]);	
		Integer[] array = this.feature.toArray(new Integer[0]);
		
		if(array.length != tempArray.length)
			return false;
		else 
			for(int i = 0; i < array.length; i++){
			
				if((int)array[i] != (int)tempArray[i])
					//注意强制转换，如果不转换，比较的将是Integer类型的引用
					return false;
			}
		return true;
	}
	public int hashCode(){
		Integer[] array = this.feature.toArray(new Integer[0]);
		int result = 17;
		for(int i = 0; i < array.length; i++)
			result = 37 * result + array[i];
		return result;
	}
	
}

public class TestSetHash {
	public static void main(String[] argc){
		HashMap<Features,String> features = new HashMap<Features,String>();
		
		Features fs = new Features(new int[]{1,3,2,4});
		Features fs2 = new Features(new int[]{4,2,3});
		Features fs3 = new Features(new int[]{1,3,2,4});
		features.put(fs, "String");
		System.out.println(fs.hashCode());
		fs.feature.remove(new Integer(1));
		features.get(fs2);
		/**
		 * 关于为什么修改fs后，在Map中找不到fs
		 * 主要是因为此时fs的HashCode已经和它本身的地址不一致
		 * 修改以后fs的HashCode已经改变，但是它的地址依然是和之前的HashCode对应
		 * 而get方法先根据HashCode计算地址，然后调用equals方法比较是否相同
		 * 所以当你用当前的值去查找时，HashCode计算出来的地址不正确；
		 * 当你用之前的值去查找时，HashCode正确，但是再调用equals方法时又不相同了；
		 * 所以不管怎样这个对象都因为地址与值的不匹配再也找不到了
		 */
		System.out.println(fs.equals(fs2));
		System.out.println(features.containsKey(fs2));
		System.out.println(features.containsKey(fs));
		System.out.println(features.containsKey(fs3));
		for(Features f : features.keySet()){
			System.out.println(f+" "+f.hashCode());
		}
		System.out.println(fs.hashCode());
		System.out.println(fs2.hashCode());
		//调用默认HashCode方法，即使用地址计算HashCode
		//System.out.println(System.identityHashCode(fs2));
	}
}
