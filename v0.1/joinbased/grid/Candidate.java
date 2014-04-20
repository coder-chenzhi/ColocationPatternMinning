package joinbased.grid;

import java.util.*;

public class Candidate {
	
	/**
	 * �û�ָ�������ж�
	 */
	private double prevalent;
	
	
	/**
	 * ��ѡ�������Ӧ��ʵ��
	 */
	public HashMap<CoFeatures,ArrayList<Integer[]>> candi2Ins[];
	
	public void setPrevalent(double prevalent){
		this.prevalent = prevalent;
	}
	
	/**
	 * ��ʾĳһ�ض����ȵĺ�ѡ������ʵ��
	 * @param num ��Ҫ��ʾ�ĺ�ѡ������
	 */
	public void showCandidate(int num) {
		 for (CoFeatures feature : candi2Ins[num-1].keySet()) {
			 ArrayList<Integer[]> list = candi2Ins[num - 1].get(feature);
			 System.out.print(feature+": Instances ");
			 if (list == null) {
				 System.out.println("null");
				 continue;
			 }
			 /*for (Integer[] tempint : list) {
				 System.out.print("[ ");
				 for (int i = 0; i < tempint.length; i++ )
					 System.out.print(tempint[i]+" ");
				 System.out.print("] ");
			 }*/
			 System.out.println(list.size());
		 }
	}
	
	/**
	 * ͨ�����жȶԺ�ѡ�����й���
	 * @param num Ҫ���й��˵ĺ�ѡ���ĳ���
	 */
	public void prevalentFiltration(int num) {
		double currentPre = 1.0;
		
		//��������ʱ����ֱ��ɾ��Ԫ�أ���������һ��ArrayList��ʱ�洢Ҫɾ����Ԫ��
		ArrayList<CoFeatures> list = new ArrayList<CoFeatures>();
		
		for (CoFeatures features : candi2Ins[num-1].keySet()) {
			currentPre = calcPrevalent(candi2Ins[num - 1].get(features));
			features.prevalent = currentPre;
			//System.out.println(currentPre);
			if (currentPre < prevalent)
				list.add(features);
		}
		for (CoFeatures features : list)
			candi2Ins[num-1].remove(features);
	}
	
	/**
	 * ͨ������ÿ���Ӽ��ǲ���Ƶ�������й���
	 * @param num Ҫ���й��˵ĺ�ѡ���ĳ���
	 */
	public void lookUpFiltration(int num) {
		
		//��ʱ�洢Ҫɾ����Map
		ArrayList<CoFeatures> FeaList = new ArrayList<CoFeatures>();
		
		//���ѭ��ѡ��ĳһ����ѡ���
		for (CoFeatures features : candi2Ins[num-1].keySet()) {
			Integer tempIntArray[] =  features.value.toArray(new Integer[0]);
			CoFeatures tempFea = new CoFeatures(tempIntArray);
			//�ڴ�ѭ�����ض��ĺ�ѡ������μ���һ������
			for (int i = 0; i < tempIntArray.length; i++) {
				
				tempFea.value.remove(tempIntArray[i]);
				
				if (!candi2Ins[num-2].containsKey(tempFea)) {
					FeaList.add(features);
					break;
				} else {
					tempFea.value.add(tempIntArray[i]);
				}
			}
		}
		for (CoFeatures temp : FeaList)
			candi2Ins[num-1].remove(temp);
	}
	
	/**
	 * ��������Ϊnum�ĺ�ѡ��
	 * @param num Ҫ�����ĺ�ѡ���ĳ���
	 */
	public void createCandidate(int num) {
		CoFeatures tempFea;
		HashMap<CoFeatures,ArrayList<Integer[]>> tempMap = 
				new HashMap<CoFeatures,ArrayList<Integer[]>>();
		CoFeatures features[] = 
				candi2Ins[num-2].keySet().toArray(new CoFeatures[0]);
		for (int i = 0; i < features.length; i++)
			for (int j = i + 1; j < features.length; j++) {
				if (features[i].equalsExceptLast(features[j])) {
					ArrayList<Integer[]> instance = new ArrayList<Integer[]>();
					tempFea = new CoFeatures();
					tempFea.value.addAll(features[i].value);
					tempFea.value.addAll(features[j].value);
					//System.out.println(tempFea);
					tempMap.put(tempFea, instance);	
				}
			}
		candi2Ins[num-1] = tempMap;
	}
	/**
	 * 
	 * @param num
	 */
	public void createInstances(int num){
		for (CoFeatures f: Joinbase.coarse.candi2Coarse[num - 1].keySet()) {
			//�ֽ�num���ȵ�CoFeaturesΪ����num-1���ȵ�CoFeatures
			ArrayList<Integer> tempList1 = new ArrayList<Integer>(f.value);
			tempList1.remove(tempList1.size()-1);
			ArrayList<Integer> tempList2 = new ArrayList<Integer>(f.value);
			tempList2.remove(tempList2.size()-2);
			
			ArrayList<Integer[]> instance = candi2Ins[num - 1].get(f);
			
			ArrayList<Integer[]> preIns1 = candi2Ins[num - 2]
					.get(new CoFeatures(tempList1.toArray(new Integer[0])));
			ArrayList<Integer[]> preIns2 = candi2Ins[num - 2]
					.get(new CoFeatures(tempList2.toArray(new Integer[0])));
			for (Integer[] pre1 : preIns1)
				for (Integer[] pre2 : preIns2) {
					if (equalsExceptLast(pre1, pre2)) {

						//Corase������Ҫ�ģ����ܰ��սڵ�ID����																
						TreeSet<Integer> tempSet = new TreeSet<Integer>();
						tempSet.addAll(Arrays.asList(pre1));
						tempSet.addAll(Arrays.asList(pre2));
						//ֻ�����һ���ڵ㲻ͬ��������ÿ����������һ��ֵ
						if (Joinbase.distance.getDistance(
								pre1[pre1.length - 1], pre2[pre1.length - 1])) {
							instance.add(tempSet.toArray(new Integer[0]));
						}
					}
				}
			if (instance.size() == 0) {
				candi2Ins[num - 1].remove(f);
			} 
		}
	}
	
	public double calcPrevalentBySet(ArrayList<Integer[]> list) {
		double tempPre = 1.0;
		HashMap<Integer,HashSet<Integer>> tempMap = 
				new HashMap<Integer,HashSet<Integer>>();
		
		//����������������е�ʵ�������ÿ�������Ĳ��ظ����
		for (Integer[] intArray : list)
			for (Integer tempInt : intArray) {
				Integer feature = Joinbase.point2Feature.get(tempInt);
				if (tempMap.containsKey(feature)) {
					tempMap.get(feature).add(tempInt);
				} else {
					HashSet<Integer> tempSet = new HashSet<Integer>();
					tempSet.add(tempInt);
					tempMap.put(feature, tempSet);
				}
			}
		for (Integer tempFea : tempMap.keySet()) {			
			int num = Joinbase.candidate.candi2Ins[0].get(
							new CoFeatures(new Integer[]{tempFea})).size();
			tempPre = Math.min(tempPre, (double)tempMap.get(tempFea).size()/num);
			
			//ֻҪĳһ���������ж�С���û����������жȣ���������Ͽ϶��������еģ��Ϳ���ֹͣ����
			if (tempPre < prevalent){
				return 0;
			}
		}
		return tempPre;
	}

	public double calcPrevalent(ArrayList<Integer[]> list) {
		double tempPre = 1.0;
		int size = list.get(0).length;
		BitSet[] bitsets = new BitSet[size];
		
		for(int i = 0; i < size; i++){
			bitsets[i] = new BitSet(1024);
		}
		
		for (Integer[] arrInt : list) {
			for (int i = 0; i < arrInt.length; i++){
				bitsets[i].set(arrInt[i], true);
			}
		}
		
		for (int i = 0; i < size; i++){
			int num = Joinbase.candidate.candi2Ins[0].get(
					new CoFeatures(new Integer[]{Joinbase.point2Feature.get(list.get(0)[i])})).size();
			tempPre = Math.min(tempPre, (double)bitsets[i].cardinality() / num);
		}
		
		return tempPre;
	}
	
	public boolean equalsExceptLast(Integer[] i1, Integer[] i2){
		
		try{
			if (i1.length != i2.length)
				throw new Exception();
		} catch(Exception e) {
			System.out.println("��С��ͬ�����ܱȽϣ�");
			e.printStackTrace();
		}
		
		if (i1[i1.length-1].equals(i2[i2.length-1]))
			return false;
		for(int i = 0; i < i1.length-1; i++){
			if(!i1[i].equals(i2[i]))
				return false;
		}
		
		return true;
	}
}

	