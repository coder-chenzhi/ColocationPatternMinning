package joinbased.grid;

import java.util.*;

public class Candidate {
	
	/**
	 * 用户指定的流行度
	 */
	private double prevalent;
	
	
	/**
	 * 候选集及其对应的实例
	 */
	public HashMap<CoFeatures,ArrayList<Integer[]>> candi2Ins[];
	
	public void setPrevalent(double prevalent){
		this.prevalent = prevalent;
	}
	
	/**
	 * 显示某一特定长度的候选集及其实例
	 * @param num 所要显示的候选集长度
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
	 * 通过流行度对候选集进行过滤
	 * @param num 要进行过滤的候选集的长度
	 */
	public void prevalentFiltration(int num) {
		double currentPre = 1.0;
		
		//遍历容器时不能直接删除元素，所以申请一个ArrayList暂时存储要删除的元素
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
	 * 通过检验每个子集是不是频繁集进行过滤
	 * @param num 要进行过滤的候选集的长度
	 */
	public void lookUpFiltration(int num) {
		
		//临时存储要删除的Map
		ArrayList<CoFeatures> FeaList = new ArrayList<CoFeatures>();
		
		//外层循环选定某一个候选组合
		for (CoFeatures features : candi2Ins[num-1].keySet()) {
			Integer tempIntArray[] =  features.value.toArray(new Integer[0]);
			CoFeatures tempFea = new CoFeatures(tempIntArray);
			//内存循环对特定的候选组合依次减少一个特征
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
	 * 产生长度为num的候选集
	 * @param num 要产生的候选集的长度
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
			//分解num长度的CoFeatures为两个num-1长度的CoFeatures
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

						//Corase这里需要改，不能按照节点ID排序																
						TreeSet<Integer> tempSet = new TreeSet<Integer>();
						tempSet.addAll(Arrays.asList(pre1));
						tempSet.addAll(Arrays.asList(pre2));
						//只有最后一个节点不同，所以是每个数组的最后一个值
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
		
		//遍历该特征组合所有的实例，求出每个特征的不重复结点
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
			
			//只要某一特征的流行度小于用户给定的流行度，该特征组合肯定不是流行的，就可以停止计算
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
			System.out.println("大小不同，不能比较！");
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

	