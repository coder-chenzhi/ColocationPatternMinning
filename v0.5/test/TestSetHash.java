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
					//ע��ǿ��ת���������ת�����ȽϵĽ���Integer���͵�����
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
		 * ����Ϊʲô�޸�fs����Map���Ҳ���fs
		 * ��Ҫ����Ϊ��ʱfs��HashCode�Ѿ���������ĵ�ַ��һ��
		 * �޸��Ժ�fs��HashCode�Ѿ��ı䣬�������ĵ�ַ��Ȼ�Ǻ�֮ǰ��HashCode��Ӧ
		 * ��get�����ȸ���HashCode�����ַ��Ȼ�����equals�����Ƚ��Ƿ���ͬ
		 * ���Ե����õ�ǰ��ֵȥ����ʱ��HashCode��������ĵ�ַ����ȷ��
		 * ������֮ǰ��ֵȥ����ʱ��HashCode��ȷ�������ٵ���equals����ʱ�ֲ���ͬ�ˣ�
		 * ���Բ����������������Ϊ��ַ��ֵ�Ĳ�ƥ����Ҳ�Ҳ�����
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
		//����Ĭ��HashCode��������ʹ�õ�ַ����HashCode
		//System.out.println(System.identityHashCode(fs2));
	}
}
