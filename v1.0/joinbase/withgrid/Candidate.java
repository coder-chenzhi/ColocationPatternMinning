package joinbase.withgrid;

import java.util.*;


public class Candidate {

	/**
	 * �û�ָ�������ж�
	 */
	private double prevalence;


	/**
	 * ��ѡ�������Ӧ��ʵ��
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
	 * ��ʾĳһ�ض����ȵĺ�ѡ������ʵ��
	 * @param num ��Ҫ��ʾ�ĺ�ѡ������
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
	 * ͨ�����жȶԺ�ѡ�����й���
	 * ����д�������������Ϊ����Ϊ2�ĺ�ѡ����ʵ���Ǽ������ʱһ�����ɵģ�����һ��һ����ѡ�����ɵģ�
	 * ����ֻ��ͳһ���й���
	 * @param num Ҫ���й��˵ĺ�ѡ���ĳ���
	 */
	public static void prevalentFiltration(BDBManager database, HashMap<Pattern, Instance> map, 
			ResizingIntArray featureInsSize, double prevalence) {
		double currentPre = 1.0;

		//��������ʱ����ֱ��ɾ��Ԫ�أ���������һ��ArrayList��ʱ�洢Ҫɾ����Ԫ��
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
	 * ͨ������ÿ���Ӽ��ǲ���Ƶ�������й���
	 * @param num Ҫ���й��˵ĺ�ѡ���ĳ���
	 */
	public void lookUpFiltration(int num) {
		//System.out.println("Lookup Filteration " + num + ":");
		//��ʱ�洢Ҫɾ����Map
		ArrayList<Pattern> FeaList = new ArrayList<Pattern>();

		//���ѭ��ѡ��ĳһ����ѡ���
		for (Pattern patterns : value[num-1]) {
			ArrayList<Integer> list = new ArrayList<>(Arrays.asList(patterns.toArray()));
			int length = list.size();
			//�ڴ�ѭ�����ض��ĺ�ѡ������μ���һ������
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
	 * ��������Ϊnum�ĺ�ѡ��
	 * @param num Ҫ�����ĺ�ѡ���ĳ���
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
		//��������ʱ����ֱ��ɾ��Ԫ�أ���������һ��ArrayList��ʱ�洢Ҫɾ����Ԫ��
		for (Pattern pattern: coarse.candi2Coarse[num - 1].keySet()) {
			//�ֽ�num���ȵ�CoFeaturesΪ����num-1���ȵ�CoFeatures
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
					//ֻ�����һ���ڵ㲻ͬ��������ÿ����������һ��ֵ
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

		//����������������е�ʵ�������ÿ�������Ĳ��ظ����
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

			//ֻҪĳһ���������ж�С���û����������жȣ���������Ͽ϶��������еģ��Ϳ���ֹͣ����
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

	public boolean equalsExceptLast(Item it1, Item it2){
		int[] arr1 = it1.toArray();
		int[] arr2 = it2.toArray();

		try{
			if (arr1.length != arr2.length)
				throw new Exception();
		} catch(Exception e) {
			System.out.println("��С��ͬ�����ܱȽϣ�");
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
		//�ڴ�ѭ�����ض��ĺ�ѡ������μ���һ������
		for (int i = 0; i < length; i++) {
			int removed = value.remove(0);
			Pattern sub = new Pattern(value);
			System.out.println(sub);
			value.add(removed);
		}
	}
}

