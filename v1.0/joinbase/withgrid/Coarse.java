package joinbase.withgrid;

import java.util.*;

class CoarseIns{
	private ArrayList<Grid> ins = new ArrayList<Grid>();
	
	public CoarseIns(){
		
	}
	
	public void add(Grid g){
		ins.add(g);
	}
	/**
	 * ���ظôֲ�ʵ�������е�����
	 * @return �ôֲ�ʵ�������е�����
	 */
	public ArrayList<Grid> getAll(){
		return this.ins;
	}
	/**
	 * ����һ���ֲ�ʵ����ָ��λ�õ�����
	 * @param index Ҫ���ʵ�λ��
	 * @return �ֲ�ʵ����ָ��λ�õ�����
	 */
	public Grid get(int index){
		try {
			if(index >= this.size())
				throw new Exception();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Ҫ���ʵ�" + index + "����ʵ����С" +this.size());
			e.printStackTrace();
		}
		return this.ins.get(index);
	}
	/**
	 * ���شֲ�ʵ���Ĵ�С
	 * @return �ֲ�ʵ���Ĵ�С 
	 */
	public int size(){
		return this.ins.size();
	}
	
	@Override
	public int hashCode() {
		final int prime = 37;
		int result = 17;
		for(Grid g : ins)
			result += g.hashCode() * prime;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CoarseIns other = (CoarseIns) obj;
		if (ins == null) {
			if (other.ins != null)
				return false;
		} else if(other.ins.size() != this.ins.size()) {
				return false;
			} else for(int i = 0; i < this.ins.size(); i++){
				if(!this.ins.get(i).equals(other.ins.get(i)))
					return false;
			}
		return true;
	} 
	
	
	
	@Override
	public String toString() {
		String str = "";
		str += "[";
		for(Grid g : this.ins)
			str += g.toString();
		str +="]";
		return str;
	}
	@SuppressWarnings("unused")
	public static void main(String[] argc){
		
		
		HashSet<CoarseIns> inset = new HashSet<CoarseIns>();
		CoarseIns ins1 = new CoarseIns();
		CoarseIns ins2 = new CoarseIns();
		Grid g1 = new Grid(0, 1);
		Grid g2 = new Grid(0, 2);
		Grid g3 = new Grid(0, 3);
		Grid g4 = new Grid(0 ,4);
		ins1.add(g1);
		ins1.add(g2);
		ins2.add(g1);
		ins2.add(g2);
		inset.add(ins1);
		System.out.println(inset);
		System.out.println(inset.add(ins2));
		System.out.println(inset);
				
	}
	
}

public class Coarse {
	/**
	 * �û�ָ�������ж�
	 */
	private double prevalence;
	
	/**
	 * �ֲڼ������Ӧ��ʵ��
	 */
	public HashMap<Pattern,HashSet<CoarseIns>> candi2Coarse[];
	
	public void setPrevalent(double prevalence){
		this.prevalence = prevalence;
	}
	
	public Coarse(double prevalence) {
		super();
		this.prevalence = prevalence;
	}
	
	/**
	 * ��ʾ����Ϊk�Ĵֲڼ�
	 */
	public void showCoarse(int num) {
		 for (Pattern feature : candi2Coarse[num-1].keySet()) {
			 HashSet<CoarseIns> set = candi2Coarse[num-1].get(feature);
			 System.out.print(feature+": Coarse");
			 if (set == null) {
				 System.out.println("null");
				 continue;
			 }
			 System.out.print("GridIns");
			 for (CoarseIns temp : set) {				 
				 System.out.println(temp);
			 }
			 System.out.println();
		 }
	}
	
	/**
	 * ��������Ϊnum�Ĵֲڼ�
	 * @param num Ҫ�����Ĵֲڼ��ĳ���
	 */
	public void createCoarse(Candidate candidate, int num) {
		//System.out.println("Create Coarse " + num +" :");
		HashSet<Pattern> patterns = candidate.getCandidate(num-1);
		HashMap<Pattern,HashSet<CoarseIns>> candi2Cos =
										this.candi2Coarse[num-2];
		HashMap<Pattern,HashSet<CoarseIns>> tempCandi2Cos = 
										new HashMap<Pattern,HashSet<CoarseIns>>();
		for(Pattern p : patterns){
			
			//�ֽ�num���ȵ�CoFeaturesΪ����num-1���ȵ�CoFeatures
			Integer[] tempInte = p.toArray();
			Integer last = tempInte[tempInte.length - 1];
			Integer secondLast =  tempInte[tempInte.length - 2];
			Integer[] prefix = Arrays.copyOf(tempInte, tempInte.length - 2);
			Pattern lookUp = new Pattern(last, secondLast);
			
			CoarseIns[] cosIns1 = candi2Cos.get(
					new Pattern(prefix, secondLast)).toArray(new CoarseIns[0]);//relate to tempIns.add(g1);
			CoarseIns[] cosIns2 = candi2Cos.get(
					new Pattern(prefix, last)).toArray(new CoarseIns[0]);//relate to tempIns.add(g1);
			
			for(int i = 0; i < cosIns1.length; i++)
				for(int j = 0; j < cosIns2.length; j++){
					if(equalsExceptLast(cosIns1[i], cosIns2[j])){
						Grid g1 = cosIns1[i].get(cosIns1[i].size()-1);
						Grid g2 = cosIns2[j].get(cosIns2[j].size()-1);
						CoarseIns tempIns = new CoarseIns();
						tempIns.add(g1);
						tempIns.add(g2);
						if(this.candi2Coarse[1].get(lookUp).contains(tempIns)){
							if(tempCandi2Cos.containsKey(p)){
								HashSet<CoarseIns> set = tempCandi2Cos.get(p);
								CoarseIns ins = new CoarseIns();
								ins.getAll().addAll(cosIns1[i].getAll());
								ins.getAll().add(cosIns2[j].get(cosIns2[j].size()-1));
								set.add(ins);
							} else {
								HashSet<CoarseIns> set = new HashSet<CoarseIns>();
								CoarseIns ins = new CoarseIns();
								ins.getAll().addAll(cosIns1[i].getAll());
								ins.getAll().add(cosIns2[j].get(cosIns2[j].size()-1));
								set.add(ins);
								tempCandi2Cos.put(p, set);
							}
						}
					}
				}
		}
		this.candi2Coarse[num-1] = tempCandi2Cos;
	}
		
	public void prevalentFiltration(Candidate candidate, 
			ResizingIntArray featureInsSize, int num){
		//System.out.println("Coarse Prevalent Filtration " + num +" :");
		HashMap<Pattern,HashSet<CoarseIns>> candi2Coarse =
								this.candi2Coarse[num-1];
		ArrayList<Pattern> delete = new ArrayList<Pattern>();
		for(Pattern pattern : candi2Coarse.keySet()){
			double pre = 1.0;
			HashSet<CoarseIns> cosIns = candi2Coarse.get(pattern);
			Integer[] inte = pattern.toArray();
			for(int i = 0; i < num; i++){
				int total = featureInsSize.get(inte[i]);
				int sum = 0;
				HashSet<Grid> grids = new HashSet<Grid>();
				for(CoarseIns tempins : cosIns){
					if(grids.add(tempins.get(i)))
						sum += tempins.get(i).getPointsByFeature(inte[i]).size();
				}
				pre = Math.min(pre, (double)sum/total);
			}
			if(pre < this.prevalence){
				delete.add(pattern);
				candidate.getCandidate(num-1).remove(pattern);
			}
		}
		for(Pattern tempcf : delete)
			this.candi2Coarse[num-1].remove(tempcf);
	}
	public boolean equalsExceptLast(CoarseIns ins1, CoarseIns ins2){
		try {
			if(ins1.size() != ins2.size())
				throw new Exception();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(ins1);
			System.out.println(ins2);
			System.out.println("��С��ͬ�����ܱȽϣ�");
			e.printStackTrace();
		}
		for(int i = 0; i < ins1.size()-1; i++){
			if(!ins1.get(i).equals(ins2.get(i))){
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] argc){
		Pattern pattern = new Pattern(new Integer[]{1,2,3,4});
		Integer[] tempInte = pattern.toArray();
		Integer last = tempInte[tempInte.length - 1];
		Integer secondLast =  tempInte[tempInte.length - 2];
		Integer[] prefix = Arrays.copyOf(tempInte, tempInte.length - 2);
		System.out.println(new Pattern(prefix, last));
		System.out.println(new Pattern(prefix, secondLast));
	}
	
}
