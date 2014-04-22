package joinless.nogrid;

import java.util.*;

/* 实例  */
class Ins{
	/**
	 * 包含的点
	 */
	TreeSet<Integer> value = new TreeSet<Integer>();
	
	public Ins(){ }
	
	public Ins(int[] num){		
		for (int i = 0; i < num.length; i++){
			this.value.add(new Integer(num[i]));
		}		
	}
	
	public Ins(Integer[] num){		
		for (int i = 0; i < num.length; i++){
			this.value.add(new Integer(num[i]));
		}		
	}
	
	public Ins(Collection<Integer> num){
		for(Integer inte : num){
			this.value.add(new Integer(inte));
		}
	}
	
	public Ins(Ins other){
		for(Integer inte : other.value){
			this.value.add(new Integer(inte));
		}
	}
	
	public String toString(){
		String str="[ ";
		Iterator<Integer> it = this.value.iterator();
		while (it.hasNext()){
			Integer in = it.next();
			str+=((int) in+" ");
		}
		str+="]";
		return str;
	}
	
	public boolean equals(Object temp){
		Integer[] tempArray =((Ins) temp).value.toArray(new Integer[0]);	
		Integer[] array = this.value.toArray(new Integer[0]);
		
		if(array.length != tempArray.length)
			return false;
		else 
			for (int i = 0; i < array.length; i++){
				
				//注意强制转换，如果不转换，比较的将是Integer类型的引用
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

public class Candidate {
	
	/**
	 * 用户指定的流行度
	 */
	double prevalent;
	
	/**
	 * 候选集及其对应的星型实例
	 */
	public HashMap<CoFeatures,HashSet<Ins>> starIns = new HashMap<CoFeatures,HashSet<Ins>>();
	
	/**
	 * 候选集及其对应的团型实例
	 */
	public HashMap<CoFeatures,HashSet<Ins>> cliqueIns[];
		
	public void setPrevalent(double prevalent){
		this.prevalent = prevalent;
	}
	
	/**
	 * 显示某一特定长度的候选集及其实例
	 * @param num 所要显示的候选集长度
	 */
	public void showCandidate(int num) {
		 for (CoFeatures feature : cliqueIns[num-1].keySet()) {
			 HashSet<Ins> set = cliqueIns[num - 1].get(feature);
			 System.out.print(feature+": Instances");
			 if (set == null) {
				 System.out.println("null");
				 continue;
			 }
			 for (Ins tempins : set) {
				 System.out.print(tempins);
			 }
			 System.out.println();
		 }
	}
	
	public void SIprevalentFiltration() {
		System.out.println("Star Instance prevalent filtration:");
		double currentPre = 1.0;
		
		//遍历容器时不能直接删除元素，所以申请一个ArrayList暂时存储要删除的元素
		ArrayList<CoFeatures> list = new ArrayList<CoFeatures>();
		
		for (CoFeatures cf : starIns.keySet()) {
			currentPre = calcPrevalent(starIns.get(cf));
			cf.prevalent = currentPre;
			System.out.println(currentPre);
			if (currentPre < prevalent)
				list.add(cf);
		}
		for (CoFeatures features : list)
			starIns.remove(features);
	}
	
	
	/**
	 * 通过团型实例的流行度对候选集进行过滤
	 * @param num 要进行过滤的候选集的长度
	 */
	public void CIprevalentFiltration(int num) {
		System.out.println("Clique Instance prevalent filtration:");
		double currentPre = 1.0;
		
		//遍历容器时不能直接删除元素，所以申请一个ArrayList暂时存储要删除的元素
		ArrayList<CoFeatures> list = new ArrayList<CoFeatures>();
		
		for (CoFeatures cf : cliqueIns[num-1].keySet()) {
			currentPre = calcPrevalent(cliqueIns[num - 1].get(cf));
			cf.prevalent = currentPre;
			System.out.println(currentPre);
			if (currentPre < prevalent)
				list.add(cf);
		}
		for (CoFeatures features : list)
			cliqueIns[num-1].remove(features);
	}
	
	/**
	 * 通过检验每个子集是不是频繁集进行过滤
	 * @param num 要进行过滤的候选集的长度
	 */
public void lookUpFiltration(int num) {
		
		//临时存储要删除的Map
		ArrayList<CoFeatures> FeaList = new ArrayList<CoFeatures>();
		
		//外层循环选定某一个候选组合
		for (CoFeatures features : cliqueIns[num-1].keySet()) {
			Integer tempIntArray[] =  features.value.toArray(new Integer[0]);
			
			CoFeatures tempFea = new CoFeatures(tempIntArray);
			//内存循环对特定的候选组合依次减少一个特征
			for (int i = 0; i < tempIntArray.length; i++) {
				
				tempFea.value.remove(tempIntArray[i]);
				
				if (!cliqueIns[num-2].containsKey(tempFea)) {
					FeaList.add(features);
					break;
				} else {
					tempFea.value.add(tempIntArray[i]);
				}
			}
		}
		for (CoFeatures temp : FeaList)
			cliqueIns[num-1].remove(temp);
	}
	
	/**
	 * 产生长度为num的候选集
	 * @param num 要产生的候选集的长度
	 */
	public void createCandidate(int num) {
		System.out.println("Candidate" + num + ":");
		CoFeatures tempFea;
		HashMap<CoFeatures,HashSet<Ins>> tempMap = 
				new HashMap<CoFeatures,HashSet<Ins>>();
		CoFeatures features[] = 
				cliqueIns[num-2].keySet().toArray(new CoFeatures[0]);
		for (int i = 0; i < features.length; i++)
			for (int j = i + 1; j < features.length; j++) {
				if (features[i].equalsExceptLast(features[j])) {
					HashSet<Ins> instance = new HashSet<Ins>();
					tempFea = new CoFeatures();
					tempFea.value.addAll(features[i].value);
					tempFea.value.addAll(features[j].value);
					System.out.println(tempFea);
					tempMap.put(tempFea, instance);					
				}
			}
		cliqueIns[num-1] = tempMap;
	}
	/**
	 * 
	 * @param num
	 */
	public void createInstances(int num){
		for (CoFeatures cf: Joinless.candidate.cliqueIns[num - 1].keySet()) {
			starIns.put(cf, Joinless.neighbors.getStarIns(cf)); 
		}
		this.SIprevalentFiltration();
		
		HashMap<CoFeatures,HashSet<Ins>> tempMap = new HashMap<CoFeatures,HashSet<Ins>>();
		for(CoFeatures cf : starIns.keySet()){
			HashSet<Ins> set = starIns.get(cf);
			CoFeatures tempcf = new CoFeatures(cf);
			tempcf.value.remove(tempcf.value.first());
			for(Ins tempins : set){
				Ins otherins = new Ins(tempins);
				otherins.value.remove(otherins.value.first());//把中心点删除
				if(cliqueIns[num-2].get(tempcf)!=null){
					if(cliqueIns[num-2].get(tempcf).contains(otherins)){
						if(tempMap.containsKey(cf)){
							tempMap.get(cf).add(tempins);
						} else {
							HashSet<Ins> tempset = new HashSet<Ins>();
							tempset.add(tempins);
							tempMap.put(cf, tempset);
						}
					}
				}
			}
		}
		cliqueIns[num-1] = tempMap;
		
		this.CIprevalentFiltration(num);
	}
	
	public double calcPrevalent(HashSet<Ins> set) {
		double tempPre = 1.0;
		HashMap<Integer,HashSet<Integer>> tempMap = 
				new HashMap<Integer,HashSet<Integer>>();
		
		//遍历该特征组合所有的实例，求出每个特征的不重复结点
		for (Ins ins: set)
			for (Integer tempInt : ins.value) {
				Integer feature = Joinless.point2Feature.get(tempInt);
				if (tempMap.containsKey(feature)) {
					tempMap.get(feature).add(tempInt);
				} else {
					HashSet<Integer> tempSet = new HashSet<Integer>();
					tempSet.add(tempInt);
					tempMap.put(feature, tempSet);
				}
			}
		for (Integer tempFea : tempMap.keySet()) {			
			int num = Joinless.candidate.cliqueIns[0].get(
							new CoFeatures(new Integer[]{tempFea})).size();
			tempPre = Math.min(tempPre, (double)tempMap.get(tempFea).size()/num);
			
			//只要某一特征的流行度小于用户给定的流行度，该特征组合肯定不是流行的，就可以停止计算
			if (tempPre < prevalent){
				return 0;
			}
		}
		return tempPre;
	}
	
}

	