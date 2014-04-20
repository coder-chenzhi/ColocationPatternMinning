package joinbased.nogrid;

import java.util.*;

public class Candidate {
	
	/**
	 * 用户指定的流行度
	 */
	public double prevalent;
	
	/**
	 * 候选集及其对应的实例
	 */
	public HashMap<Features,ArrayList<Integer[]>> candi2Instan[];
	
	/**
	 * 显示某一特定长度的候选集及其实例
	 * @param num 所要显示的候选集长度
	 */
	
	public void showCandidate(int num) {
		 for (Features feature : candi2Instan[num-1].keySet()) {
			 ArrayList<Integer[]> list = candi2Instan[num - 1].get(feature);
			 System.out.print(feature+": Instances");
			 if (list == null) {
				 System.out.println("null");
				 continue;
			 }
			 for (Integer[] tempint : list) {
				 System.out.print("[ ");
				 for (int i = 0; i < tempint.length; i++ )
					 System.out.print(tempint[i]+" ");
				 System.out.print("] ");
			 }
			 System.out.println();
		 }
	}
	
	/**
	 * 通过流行度对候选集进行过滤
	 * @param num 要进行过滤的候选集的长度
	 */
	public void prevalentFiltration(int num) {
		double currentPre = 1.0;
		
		//遍历容器时不能直接删除元素，所以申请一个ArrayList暂时存储要删除的元素
		ArrayList<Features> list = new ArrayList<Features>();
		
		for (Features features : candi2Instan[num-1].keySet()) {
			currentPre = calcPrevalent(candi2Instan[num - 1].get(features));
			features.prevalent = currentPre;
			System.out.println(currentPre);
			if (currentPre < prevalent)
				list.add(features);
		}
		for (Features features : list)
			candi2Instan[num-1].remove(features);
	}
	
	/**
	 * 通过检验每个子集是不是频繁集进行过滤
	 * @param num 要进行过滤的候选集的长度
	 */
	public void lookUpFiltration(int num) {
		
		//临时存储要删除的Map
		ArrayList<Features> FeaList = new ArrayList<Features>();
		
		//外层循环选定某一个候选组合
		for (Features features : candi2Instan[num-1].keySet()) {
			Integer tempIntArray[] =  features.value.toArray(new Integer[0]);
			
			//内存循环对特定的候选组合依次减少一个特征
			for (int i = 0; i < tempIntArray.length; i++) {
				ArrayList<Integer> tempArray = 
						new ArrayList<Integer>(features.value);
				tempArray.remove(tempIntArray[i]);
				Features tempFea = new Features(tempArray.toArray(new Integer[0]));
				if (!candi2Instan[num-2].containsKey(tempFea)) {
					FeaList.add(features);
					break;
				} else {
					tempArray.add(tempIntArray[i]);
				}
			}
		}
		for (Features temp : FeaList)
			candi2Instan[num-1].remove(temp);
	}
	
	/**
	 * 产生长度为num的候选集
	 * @param num 要产生的候选集的长度
	 */
	public void createCandidate(int num) {
		Features tempFea;
		HashMap<Features,ArrayList<Integer[]>> tempMap = 
				new HashMap<Features,ArrayList<Integer[]>>();
		Features features[] = 
				candi2Instan[num-2].keySet().toArray(new Features[0]);
		for (int i = 0; i < features.length; i++)
			for (int j = i + 1; j < features.length; j++) {
				if (features[i].equalsExceptLast(features[j])) {
					tempFea = new Features();
					tempFea.value.addAll(features[i].value);
					tempFea.value.addAll(features[j].value);
					System.out.println(tempFea);
					
					ArrayList<Integer[]> instance = new ArrayList<Integer[]>();					
					ArrayList<Integer[]> preIns1 = 
							candi2Instan[num-2].get(features[i]);							
					ArrayList<Integer[]> preIns2 = 
							candi2Instan[num-2].get(features[j]);
							
					for (Integer[] pre1 : preIns1)
						for(Integer[] pre2 : preIns2) {
							if (equalsExceptLast(pre1, pre2)) {
								
								//找出两个不同的节点的ID																
								TreeSet<Integer> tempSet = new TreeSet<Integer>();
								tempSet.addAll(Arrays.asList(pre1));
								tempSet.addAll(Arrays.asList(pre2));
								
								if (Join_base.distance.getDistance(
										pre1[pre1.length-1], pre2[pre1.length-1])){
									instance.add(tempSet.toArray(new Integer[0]));
								}
							}
						}
					if (instance.size() != 0){
						tempMap.put(tempFea, instance);
					}
				}
			}
		candi2Instan[num-1] = tempMap;
	}
	
	public double calcPrevalent(ArrayList<Integer[]> list) {
		double tempPre = 1.0;
		HashMap<Integer,HashSet<Integer>> tempMap = 
				new HashMap<Integer,HashSet<Integer>>();
		
		//遍历该特征组合所有的实例，求出每个特征的不重复结点
		for (Integer[] intArray : list)
			for (Integer tempInt : intArray) {
				Integer feature = Join_base.point2Feature.get(tempInt);
				if (tempMap.containsKey(feature)) {
					tempMap.get(feature).add(tempInt);
				} else {
					HashSet<Integer> tempSet = new HashSet<Integer>();
					tempSet.add(tempInt);
					tempMap.put(feature, tempSet);
				}
			}
		for (Integer tempFea : tempMap.keySet()) {			
			int num = Join_base.candidate.candi2Instan[0].get(
							new Features(new Integer[]{tempFea})).size();
			tempPre = Math.min(tempPre, (double)tempMap.get(tempFea).size()/num);
			
			//只要某一特征的流行度小于用户给定的流行度，该特征组合肯定不是流行的，就可以停止计算
			if (tempPre < prevalent){
				return 0;
			}
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



	