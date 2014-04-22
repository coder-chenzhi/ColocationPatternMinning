package joinless.grid;

import java.util.*;

/* ʵ��  */
class Ins{
	/**
	 * �����ĵ�
	 */
	ArrayList<Integer> value = new ArrayList<Integer>();
	
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
	
	public Ins(Ins other, Integer last){
		for(Integer inte : other.value){
			this.value.add(new Integer(inte));
		}
		this.value.add(new Integer(last));
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

public class Candidate {
	
	/**
	 * �û�ָ�������ж�
	 */
	private double prevalent;
	
	/**
	 * ��ѡ�������Ӧ������ʵ��
	 */
	public HashMap<CoFeatures,ArrayList<Ins>> starIns = new HashMap<CoFeatures,ArrayList<Ins>>();// store current instance
	
	/**
	 * ��ѡ�������Ӧ������ʵ��
	 */
	public HashMap<CoFeatures,HashSet<Ins>> cliqueIns[];
		
	public void setPrevalent(double prevalent){
		this.prevalent = prevalent;
	}
	
	/**
	 * ��ʾĳһ�ض����ȵĺ�ѡ������ʵ��
	 * @param num ��Ҫ��ʾ�ĺ�ѡ������
	 */
	public void showCandidate(int num) {
		 for (CoFeatures feature : cliqueIns[num-1].keySet()) {
			 HashSet<Ins> set = cliqueIns[num - 1].get(feature);
			 System.out.print(feature+": Instances");
			 if (set == null) {
				 System.out.println("null");
				 continue;
			 }
			 /*for (Ins tempins : set) {
				 System.out.print(tempins);
			 }*/
			 System.out.println(set.size());
		 }
	}
	
	public void SIprevalentFiltration() {
		//System.out.println("Star Instance prevalent filtration:");
		double currentPre = 1.0;
		
		//��������ʱ����ֱ��ɾ��Ԫ�أ���������һ��ArrayList��ʱ�洢Ҫɾ����Ԫ��
		ArrayList<CoFeatures> list = new ArrayList<CoFeatures>();
		
		for (CoFeatures cf : starIns.keySet()) {
			currentPre = calcPrevalent(starIns.get(cf));
			cf.prevalent = currentPre;
			//System.out.println(currentPre);
			if (currentPre < prevalent)
				list.add(cf);
		}
		for (CoFeatures features : list)
			starIns.remove(features);
	}
	
	
	/**
	 * ͨ������ʵ�������жȶԺ�ѡ�����й���
	 * @param num Ҫ���й��˵ĺ�ѡ���ĳ���
	 */
	public void CIprevalentFiltration(int num) {
		//System.out.println("Clique Instance prevalent filtration:");
		double currentPre = 1.0;
		
		//��������ʱ����ֱ��ɾ��Ԫ�أ���������һ��ArrayList��ʱ�洢Ҫɾ����Ԫ��
		ArrayList<CoFeatures> list = new ArrayList<CoFeatures>();
		
		for (CoFeatures cf : cliqueIns[num-1].keySet()) {
			currentPre = calcPrevalent(cliqueIns[num - 1].get(cf));
			cf.prevalent = currentPre;
			//System.out.println(currentPre);
			if (currentPre < prevalent)
				list.add(cf);
		}
		for (CoFeatures features : list)
			cliqueIns[num-1].remove(features);
	}
	
	/**
	 * ͨ������ÿ���Ӽ��ǲ���Ƶ�������й���
	 * @param num Ҫ���й��˵ĺ�ѡ���ĳ���
	 */
	public void lookUpFiltration(int num) {
		
		//��ʱ�洢Ҫɾ����Map
		ArrayList<CoFeatures> FeaList = new ArrayList<CoFeatures>();
		
		//���ѭ��ѡ��ĳһ����ѡ���
		for (CoFeatures features : cliqueIns[num-1].keySet()) {
			Integer tempIntArray[] =  features.value.toArray(new Integer[0]);
			
			CoFeatures tempFea = new CoFeatures(tempIntArray);
			//�ڴ�ѭ�����ض��ĺ�ѡ������μ���һ������
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
	 * ��������Ϊnum�ĺ�ѡ��
	 * @param num Ҫ�����ĺ�ѡ���ĳ���
	 */
	public void createCandidate(int num) {
		//System.out.println("Candidate" + num + ":");
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
					//System.out.println(tempFea);
					tempMap.put(tempFea, instance);					
				}
			}
		cliqueIns[num-1] = tempMap;
	}
	/**
	 * 
	 * @param num ʵ���ĳ���
	 */
	public void createInstances(int num){
		HashMap<CoFeatures,HashSet<Ins>> tempMap = new HashMap<CoFeatures,HashSet<Ins>>();
		
		for (CoFeatures cf: Joinless.coarse.candi2Coarse[num - 1].keySet()) {
			starIns.put(cf, Joinless.neighbors.getStarIns(cf, cliqueIns[num - 2]));
			
			Integer[] cofeatures = cf.value.toArray(new Integer[0]);
			CoFeatures prefix = new CoFeatures(cofeatures, cofeatures.length - 1);
			Integer lastFeature = cofeatures[cofeatures.length - 1];			
			
			CoFeatures lookUpcf = new CoFeatures(cf);
			lookUpcf.value.remove(lookUpcf.value.first());
			
			HashSet<Ins> pre = cliqueIns[num - 2].get(prefix);
			for (Ins ins: pre) {
				Integer centerpoint = ins.value.get(0);
				TreeSet<Integer> appendList = 
						Joinless.neighbors.matrix[centerpoint - 1].get(lastFeature);
				if(appendList == null)
					continue;
				for(Integer append : appendList) {
					Ins tmp = new Ins(ins, append);
					Ins otherins = new Ins(tmp);
					otherins.value.remove(0);//�����ĵ�ɾ��
					if(cliqueIns[num-2].get(lookUpcf)!=null){
						if(cliqueIns[num-2].get(lookUpcf).contains(otherins)){
							if(tempMap.containsKey(cf)){
								tempMap.get(cf).add(tmp);
							} else {
								HashSet<Ins> tempset = new HashSet<Ins>();
								tempset.add(tmp);
								tempMap.put(cf, tempset);
							}
						}
					}
				}
					
			}
		}
		
		//System.out.println("CoFeatures:" + cf + " StarInstance[" + set.size() +"]");
		
		cliqueIns[num-1] = tempMap;
		
		this.CIprevalentFiltration(num);
		//this.starIns.clear();
	}
	
	
	/**
	 * �ռ临�Ӷȸ��ߣ����ǿ���ʹ��Star Instance Prevalent Filter
	 * @param num ʵ���ĳ���
	 */
	public void createInstancesPre(int num){
		for (CoFeatures cf: Joinless.coarse.candi2Coarse[num - 1].keySet()) {
			starIns.put(cf, Joinless.neighbors.getStarIns(cf, cliqueIns[num - 2])); 
		}
		//long preSize = starIns.size();
		//System.out.println(preSize);
		
		this.SIprevalentFiltration();
		
		//long filterSize = starIns.size();
		//System.out.println(filterSize);
		//System.out.println("Length " + num + " Filter Effect:" + (double)filterSize / preSize);
		
		HashMap<CoFeatures,HashSet<Ins>> tempMap = new HashMap<CoFeatures,HashSet<Ins>>();
		for(CoFeatures cf : starIns.keySet()){
			ArrayList<Ins> set = starIns.get(cf);
			//System.out.println("CoFeatures:" + cf + " StarInstance[" + set.size() +"]");
			CoFeatures tempcf = new CoFeatures(cf);
			tempcf.value.remove(tempcf.value.first());
			for(Ins tempins : set){
				Ins otherins = new Ins(tempins);
				otherins.value.remove(0);//�����ĵ�ɾ��
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
			//System.out.println("CoFeatures:" + cf + " StarInstance[" + set.size() +"]");
		}
		cliqueIns[num-1] = tempMap;
		
		this.CIprevalentFiltration(num);
		int MegaBytes = 1024 * 1024;
		long freeMemory = Runtime.getRuntime().freeMemory() / MegaBytes;
		long totalMemory = Runtime.getRuntime().totalMemory() / MegaBytes;
		System.out.println("�����ڴ�:" + freeMemory + "MB");
		System.out.println("�ܹ��ڴ�:" + totalMemory + "MB");
        System.out.println("ռ���ڴ�:" + (totalMemory - freeMemory) + "MB");
		this.starIns.clear();
	}
	//�����Ż�
	public double calcPrevalent(Collection<Ins> set) {
		double tempPre = 1.0;
		HashMap<Integer,HashSet<Integer>> tempMap = 
				new HashMap<Integer,HashSet<Integer>>();
		
		//����������������е�ʵ�������ÿ�������Ĳ��ظ����
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
			
			//ֻҪĳһ���������ж�С���û����������жȣ���������Ͽ϶��������еģ��Ϳ���ֹͣ����
			if (tempPre < prevalent){
				return 0;
			}
		}
		return tempPre;
	}
	
}

	