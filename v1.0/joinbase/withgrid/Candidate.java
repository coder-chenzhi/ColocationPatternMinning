package joinbase.withgrid;

import java.util.*;


public class Candidate {

	/**
	 * 用户指定的流行度
	 */
	private double prevalence;


	/**
	 * 候选集及其对应的实例
	 */
	private HashSet<Pattern> value[];

	Candidate(double prevalence) {
		this.prevalence = prevalence;
	}

	@SuppressWarnings("unchecked")
	public void initMaxLength(int length) {
		this.value = new HashSet[length];
	}

	public int getMaxLength() {
		return this.value.length;
	}

	public void setPrevalent(double prevalence){
		this.prevalence = prevalence;
	}

	public void setCandidate(int index, HashSet<Pattern> set) {
		this.value[index] = set;
	}

	public HashSet<Pattern> getCandidate(int index) {
		return this.value[index];
	}

	/**
	 * 显示某一特定长度的候选集及其实例
	 * @param num 所要显示的候选集长度
	 */
	public void showCandidate(BDBManager database, int num) {
		for (Pattern pattern : value[num-1]) {
			Instance ins = database.get(pattern);
			System.out.print(pattern+": Instances ");
			if (ins == null) {
				System.out.println("null");
				continue;
			}
			/*for (Integer[] tempint : list) {
				 System.out.print("[ ");
				 for (int i = 0; i < tempint.length; i++ )
					 System.out.print(tempint[i]+" ");
				 System.out.print("] ");
			 }*/
			System.out.println(ins.size());
		}
	}

	/**
	 * 通过流行度对候选集进行过滤
	 * 单独写这个函数，是因为长度为2的候选集和实例是计算距离时一起生成的，不是一个一个候选集生成的，
	 * 所以只能统一进行过滤
	 * @param num 要进行过滤的候选集的长度
	 */
	public static void prevalentFiltration(BDBManager database, HashMap<Pattern, Instance> map, 
			ResizingIntArray featureInsSize, double prevalence) {
		double currentPre = 1.0;

		//遍历容器时不能直接删除元素，所以申请一个ArrayList暂时存储要删除的元素
		ArrayList<Pattern> list = new ArrayList<Pattern>();

		for (Map.Entry<Pattern, Instance> entry : map.entrySet()) {
			Pattern key = entry.getKey();
			Instance value = entry.getValue();
			currentPre = calcPrevalent(key, value, featureInsSize);
			//System.out.println(currentPre);
			if (currentPre < prevalence) {
				list.add(key);
			}
			else {
				key.setPrevalence(currentPre);
				database.put(key, value);
			}
		}

		for (Pattern p : list)
			map.remove(p);
	}

	/**
	 * 通过检验每个子集是不是频繁集进行过滤
	 * @param num 要进行过滤的候选集的长度
	 */
	public void lookUpFiltration(int num) {
		//System.out.println("Lookup Filteration " + num + ":");
		//临时存储要删除的Map
		ArrayList<Pattern> FeaList = new ArrayList<Pattern>();

		//外层循环选定某一个候选组合
		for (Pattern patterns : value[num-1]) {
			ArrayList<Integer> list = new ArrayList<>(Arrays.asList(patterns.toArray()));
			int length = list.size();
			//内存循环对特定的候选组合依次减少一个特征
			for (int i = 0; i < length; i++) {
				int removed = list.remove(0);
				Pattern sub = new Pattern(list);

				if (!this.value[num-2].contains(sub)) {
					FeaList.add(patterns);
					break;
				}
				list.add(removed);
			}
		}
		for (Pattern temp : FeaList)
			value[num-1].remove(temp);
	}

	/**
	 * 产生长度为num的候选集
	 * @param num 要产生的候选集的长度
	 */
	public void createCandidate(int num) {
		//System.out.println("Create Candidate " + num + ":");
		Pattern tempFea;
		HashSet<Pattern> tempSet = new HashSet<Pattern>();
		Pattern patterns[] = 
				value[num-2].toArray(new Pattern[0]);
		for (int i = 0; i < patterns.length; i++)
			for (int j = i + 1; j < patterns.length; j++) {
				if (patterns[i].equalsExceptLast(patterns[j])) {
					tempFea = new Pattern(patterns[i], patterns[j]);
					//System.out.println(tempFea);
					tempSet.add(tempFea);
				}
			}
		value[num-1] = tempSet;
	}
	/**
	 * 
	 * @param num
	 */
	public void createInstances(BDBManager database, Distance distance, 
			Grids grids, Coarse coarse, ResizingIntArray featureInsSize,
			double prevalence, int num){
		//System.out.println("Create Instance " + num + " :");
		//遍历容器时不能直接删除元素，所以申请一个ArrayList暂时存储要删除的元素
		for (Pattern pattern: coarse.candi2Coarse[num - 1].keySet()) {
			//分解num长度的CoFeatures为两个num-1长度的CoFeatures
			//System.out.println(pattern);
			Integer[] tempInte = pattern.toArray();
			Integer last = tempInte[tempInte.length - 1];
			Integer secondLast = tempInte[tempInte.length - 2];
			Integer[] prefix = Arrays.copyOf(tempInte, tempInte.length - 2);

			Instance instance = new Instance(pattern.length());

			Instance preIns1 = database.get(new Pattern(prefix, last));
			Instance preIns2 = database.get(new Pattern(prefix, secondLast));
			for (Item pre1 : preIns1) {
				int[] preArray = pre1.getPrefix();
				Item start = new Item(preArray, 0);
				Item end = new Item(preArray, grids.getPointNum());
				NavigableSet<Item> search = preIns2.subSet(start, true, end, true);
				for (Item pre2 : search) {
					//只有最后一个节点不同，所以是每个数组的最后一个值
					if (distance.isNeighbor(
							pre1.getLast(), pre2.getLast())) {
						int[] pre = pre1.getPrefix();
						int last1 = pre1.getLast();
						int last2 = pre2.getLast();
						int[] value = Arrays.copyOf(pre, pre.length + 2);
						if(last1 > last2) {
							value[value.length - 1] = last1;
							value[value.length - 2] = last2;
						} else {
							value[value.length - 1] = last2;
							value[value.length - 2] = last1;
						}
						instance.add(value);
					}
				}
			}
			//System.out.println(pattern +" Instance " + instance.size());
			if (instance.size() == 0) {
				value[num - 1].remove(pattern);
			}
			//prevalence filter
			double curPre = calcPrevalent(pattern, instance, featureInsSize);
			//System.out.println(curPre);
			//System.out.println("After filter " + pattern +" Instance " + instance.size());
			pattern.setPrevalence(curPre);
			if (curPre >= prevalence){
				//System.out.println("Put " + pattern + " into database.");
				database.put(pattern, instance);
			} else {
				value[num - 1].remove(pattern);
			}
		}
	}

	public double calcPrevalentBySet(ArrayList<Integer[]> list, 
			ResizingIntArray point2Feature, ResizingIntArray featureInsSize) {
		double tempPre = 1.0;
		HashMap<Integer,HashSet<Integer>> tempMap = 
				new HashMap<Integer,HashSet<Integer>>();

		//遍历该特征组合所有的实例，求出每个特征的不重复结点
		for (Integer[] intArray : list)
			for (Integer tempInt : intArray) {
				Integer feature = point2Feature.get(tempInt);
				if (tempMap.containsKey(feature)) {
					tempMap.get(feature).add(tempInt);
				} else {
					HashSet<Integer> tempSet = new HashSet<Integer>();
					tempSet.add(tempInt);
					tempMap.put(feature, tempSet);
				}
			}
		for (Integer tempFea : tempMap.keySet()) {			
			int num = featureInsSize.get(tempFea);
			tempPre = Math.min(tempPre, (double)tempMap.get(tempFea).size()/num);

			//只要某一特征的流行度小于用户给定的流行度，该特征组合肯定不是流行的，就可以停止计算
			if (tempPre < prevalence){
				return 0;
			}
		}
		return tempPre;
	}

	public static double calcPrevalent(Pattern p, Instance ins,
			ResizingIntArray featureInsSize) {
		double tempPre = 1.0;
		int length = p.length();
		Integer[] features = p.toArray();
		BitSet[] bitsets = new BitSet[length];

		for(int i = 0; i < length; i++){
			bitsets[i] = new BitSet(1024);
		}

		for (Item it : ins) {
			int[] arrInt = it.toArray();
			for (int i = 0; i < arrInt.length; i++){
				bitsets[i].set(arrInt[i], true);
			}
		}

		for (int i = 0; i < length; i++){
			int num = featureInsSize.get(features[i]);
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

	public boolean equalsExceptLast(Item it1, Item it2){
		int[] arr1 = it1.toArray();
		int[] arr2 = it2.toArray();

		try{
			if (arr1.length != arr2.length)
				throw new Exception();
		} catch(Exception e) {
			System.out.println("大小不同，不能比较！");
			e.printStackTrace();
		}

		if (arr1[arr1.length-1] == arr2[arr2.length-1])
			return false;
		for(int i = 0; i < arr1.length-1; i++){
			if(arr1[i] != arr2[i])
				return false;
		}

		return true;
	}

	public static void main(String[] args) {
		Pattern patterns = new Pattern(new Integer[]{1, 2, 3, 4, 5});
		ArrayList<Integer> value = new ArrayList<>(Arrays.asList(patterns.toArray()));
		int length = value.size();
		//内存循环对特定的候选组合依次减少一个特征
		for (int i = 0; i < length; i++) {
			int removed = value.remove(0);
			Pattern sub = new Pattern(value);
			System.out.println(sub);
			value.add(removed);
		}
	}
}

