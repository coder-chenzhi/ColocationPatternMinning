package joinbased.nogrid;

import java.util.*;


public class Features{
	
	/**
	 * ����õ������ж�
	 */
	double prevalent = 0.0;
	
	/**
	 * ����������
	 */
	TreeSet<Integer> value = new TreeSet<Integer>();
	
	public Features(){ }
	
	public Features(int[] num){		
		for (int i = 0; i < num.length; i++){
			this.value.add(new Integer(num[i]));
		}		
	}
	
	public Features(Integer[] num){		
		for (int i = 0; i < num.length; i++){
			this.value.add(new Integer(num[i]));
		}
		
	}
	
	public boolean equalsExceptLast(Features f){

		Integer[] fv1 = value.toArray(new Integer[0]);
		Integer[] fv2 = f.value.toArray(new Integer[0]);
		//System.out.println(this + " " + f);
		
		try{
			if (fv1.length != fv2.length)
				throw new Exception();
		} catch(Exception e) {
			System.out.println("��С��ͬ�����ܱȽϣ�");
			e.printStackTrace();
		}
		
		if (fv1[fv1.length-1].equals(fv2[fv2.length-1])){
			//System.out.println("false");
			return false;
		}
		for(int i = 0; i < fv1.length-1; i++){
			if(!fv1[i].equals(fv2[i])){
				//System.out.println("false");
				return false;
			}
		}
		//System.out.println("true");
		return true;
	}
	
	public String toString(){
		String str="Features[ ";
		Iterator<Integer> it = this.value.iterator();
		while (it.hasNext()){
			Integer in = it.next();
			str+=((int) in+" ");
		}
		str+="]";
		return str;
	}
	
	public boolean equals(Object temp){
		Integer[] tempArray =((Features) temp).value.toArray(new Integer[0]);	
		Integer[] array = this.value.toArray(new Integer[0]);
		
		if(array.length != tempArray.length)
			return false;
		else 
			for (int i = 0; i < array.length; i++){
				
				//ע��ǿ��ת���������ת�����ȽϵĽ���Integer���͵�����
				if ((int) array[i] != (int) tempArray[i])
					return false;
			}
		return true;
	}
	
	public int hashCode(){
		Integer[] array = this.value.toArray(new Integer[0]);
		int result = 17;
		for (int i = 0; i < array.length; i++)
			result = 37 * result + array[i];
		return result;
	}
	
}
