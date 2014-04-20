package joinless.nogrid;

import java.util.*;

class CoarseIns{
	private ArrayList<Grid> ins = new ArrayList<Grid>();
	
	public CoarseIns(){
		
	}
	
	public void add(Grid g){
		ins.add(g);
	}
	/**
	 * 返回该粗糙实例中所有的网格
	 * @return 该粗糙实例中所有的网格
	 */
	public ArrayList<Grid> getAll(){
		return this.ins;
	}
	/**
	 * 返回一个粗糙实例中指定位置的网格
	 * @param index 要访问的位置
	 * @return 粗糙实例中指定位置的网格
	 */
	public Grid get(int index){
		try {
			if(index >= this.size())
				throw new Exception();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("要访问的" + index + "大于实例大小" +this.size());
			e.printStackTrace();
		}
		return this.ins.get(index);
	}
	/**
	 * 返回粗糙实例的大小
	 * @return 粗糙实例的大小 
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
	 * 用户指定的流行度
	 */
	private double prevalent;
	
	/**
	 * 粗糙集及其对应的实例
	 */
	public HashMap<CoFeatures,HashSet<CoarseIns>> candi2Coarse[];
	
	public void setPrevalent(double prevalent){
		this.prevalent = prevalent;
	}
	
	public Coarse() {
		super();
	}
	
	/**
	 * 显示长度为k的粗糙集
	 */
	public void showCoarse(int num) {
		 for (CoFeatures feature : candi2Coarse[num-1].keySet()) {
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
	 * 产生长度为num的粗糙集
	 * @param num 要产生的粗糙集的长度
	 */
	public void createCoarse(int num) {
		HashMap<CoFeatures,HashSet<Ins>> candi2Instan =
										Joinless.candidate.cliqueIns[num-1];
		HashMap<CoFeatures,HashSet<CoarseIns>> candi2Cos =
										Joinless.coarse.candi2Coarse[num-2];
		HashMap<CoFeatures,HashSet<CoarseIns>> tempCandi2Cos = 
										new HashMap<CoFeatures,HashSet<CoarseIns>>();		
		for(CoFeatures cf : candi2Instan.keySet()){
			
			CoFeatures tempcf = new CoFeatures();
			Integer[] tempInte = cf.value.toArray(new Integer[0]);
			tempcf.value.add(tempInte[tempInte.length-1]);
			tempcf.value.add(tempInte[tempInte.length-2]);
			//分解num长度的CoFeatures为两个num-1长度的CoFeatures
			ArrayList<Integer> tempList1 = new ArrayList<Integer>(cf.value);
			tempList1.remove(tempList1.size()-1);
			ArrayList<Integer> tempList2 = new ArrayList<Integer>(cf.value);
			tempList2.remove(tempList2.size()-2);
			
			
			CoarseIns[] cosIns1 = candi2Cos.get(
					new CoFeatures(tempList1.toArray(new Integer[0]))).toArray(new CoarseIns[0]);
			CoarseIns[] cosIns2 = candi2Cos.get(
					new CoFeatures(tempList2.toArray(new Integer[0]))).toArray(new CoarseIns[0]);
			
			for(int i = 0; i < cosIns1.length; i++)
				for(int j = 0; j < cosIns2.length; j++){
					if(equalsExceptLast(cosIns1[i], cosIns2[j])){
						Grid g1 = cosIns1[i].get(cosIns1[i].size()-1);
						Grid g2 = cosIns2[j].get(cosIns2[j].size()-1);
						CoarseIns tempIns = new CoarseIns();
						tempIns.add(g1);
						tempIns.add(g2);
						if(Joinless.coarse.candi2Coarse[1].get(tempcf).contains(tempIns)){
							if(tempCandi2Cos.containsKey(cf)){
								HashSet<CoarseIns> set = tempCandi2Cos.get(cf);
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
								tempCandi2Cos.put(cf, set);
							}
						}
					}
				}
		}
		Joinless.coarse.candi2Coarse[num-1] = tempCandi2Cos;
	}
		
	public void prevalentFiltration(int num){
		HashMap<CoFeatures,HashSet<CoarseIns>> candi2Coarse =
								Joinless.coarse.candi2Coarse[num-1];
		ArrayList<CoFeatures> delete = new ArrayList<CoFeatures>();
		for(CoFeatures cf : candi2Coarse.keySet()){
			double pre = 1.0;
			HashSet<CoarseIns> cosIns = candi2Coarse.get(cf);
			Integer[] inte = cf.value.toArray(new Integer[0]);
			for(int i = 0; i < num; i++){
				CoFeatures tempcf = new CoFeatures(new Integer[]{inte[i]});
				int total = Joinless.candidate.cliqueIns[0].get(tempcf).size();
				int sum = 0;
				HashSet<Grid> grids = new HashSet<Grid>();
				for(CoarseIns tempins : cosIns){
					if(grids.add(tempins.get(i)))
						sum += tempins.get(i).getPointsByFeature(inte[i]).size();
				}
				pre = Math.min(pre, (double)sum/total);
			}
			if(pre < this.prevalent){
				delete.add(cf);
				Joinless.candidate.cliqueIns[num-1].remove(cf);
			}
		}
		for(CoFeatures tempcf : delete)
			Joinless.coarse.candi2Coarse[num-1].remove(tempcf);
	}
	public boolean equalsExceptLast(CoarseIns ins1, CoarseIns ins2){
		try {
			if(ins1.size() != ins2.size())
				throw new Exception();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(ins1);
			System.out.println(ins2);
			System.out.println("大小不同，不能比较！");
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
		Coarse c = new Coarse();
		CoarseIns ins1 = new CoarseIns();
		CoarseIns ins2 = new CoarseIns();
		Grid g1 = new Grid(0, 1);
		Grid g2 = new Grid(0, 2);
		Grid g3 = new Grid(0, 3);
		ins1.add(g1);
		ins1.add(g2);
		ins2.add(g2);
		ins2.add(g3);
		System.out.println(c.equalsExceptLast(ins1, ins2));								
	}
	
}
