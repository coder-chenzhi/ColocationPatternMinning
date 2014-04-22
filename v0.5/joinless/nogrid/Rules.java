package joinless.nogrid;
import java.util.*;

class Rule{
	
	/**
	 * 前键
	 */
	CoFeatures pre;
	
	/**
	 * 后键
	 */
	CoFeatures post;
	
	/**
	 * 自信度
	 */
	private double confindence = 0.0;
	
	Rule() {	}
	
	Rule(CoFeatures pre, CoFeatures post, double confidence) {
		this.pre = pre;
		this.post = post;
		this.confindence = confidence;
	}
	
	public String toString() {
		String str = "";
		str = pre + " -> " + post +" confidence: " + confindence;
		return str;
	}
	
	public void setConfidence(double confidence){
		this.confindence = confidence;
	}
}

public class Rules {
	
	/**
	 * 包含所有规则的ArrayList	
	 */
	public ArrayList<Rule> list = new ArrayList<Rule>();
	
	/**
	 * 用户设置自信度阈值
	 */
	private double confidence;		
	
	/**
	 * 设置自信度
	 * @param confidence 用户指定的自信度
	 */
	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	
	/**
	 * 根据频繁集计算规则
	 * @param candi 计算得到的频繁集
	 */
	public void createRules(Candidate candi) {
		System.out.println("Rules:");
		for (HashMap<CoFeatures,HashSet<Ins>> tempMap : candi.cliqueIns) {
			if (tempMap.size() == 0) {
				break;
			}
			for (CoFeatures tempFea : tempMap.keySet()) {
				Integer[] tempInte = tempFea.value.toArray(new Integer[0]);
				
				//求子集是将子集转换为二进制，每个元素的有无用二进制表示，
				//也就是从0到2^n-1循环对应所有的2^n个子集
				//又因为每个子集的补集也是子集，所以只需要一半的循环
				for (int i = 1; i < (int)Math.pow(2, tempInte.length-1); i++) {
					int temp = i;
					int count = 0;//设置count，记录当前子集对应的二进制数为多少位，然后就能算出第一个非0位之前有几个0
					ArrayList<Integer> sublocate = new ArrayList<Integer>();
					ArrayList<Integer> complocate = new ArrayList<Integer>();
					CoFeatures compFea = 
							new CoFeatures(tempFea.value.toArray(new Integer[0]));
					CoFeatures subFea = new CoFeatures();
					while (temp != 0) {
						if (temp % 2 == 1) {
							subFea.value.add(tempInte[count]);
							sublocate.add(new Integer(count));
						} else {
							complocate.add(new Integer(count));
						}
						temp /= 2;
						count++;
					}
					
					for (int j = count; j < tempInte.length; j++){
						complocate.add(new Integer(j));
					}
					compFea.value.removeAll(subFea.value);
					
					//这里根据KeySet()中Key的下标去Value()中对应位置取出结点编号
					//之所以可以这样，是因为KeySet()中的特征值是按大小排序的
					//而结点的编号也是按照特征值的相对大小排序的
					//并且在连接找实例的过程中，实例中的节点编号也按大小排了序
					//所以频繁集中特征值的相对顺序，和实例中结点编号相对顺序一致					
					HashSet<ArrayList<Integer>> tempSet = 
							new HashSet<ArrayList<Integer>>();
					HashSet<Ins> instances =
							candi.cliqueIns[tempFea.value.size()-1].get(tempFea);
					for (Ins subIns : instances) {
						ArrayList<Integer> pointList = new ArrayList<Integer>();
						for (Integer subInt: sublocate)
							pointList.add(subIns.value.toArray(new Integer[0])[subInt]);												
						tempSet.add(pointList);
					}
					double tempConfidence = (double)tempSet.size() /
							candi.cliqueIns[subFea.value.size()-1].get(subFea).size();
					if (tempConfidence > this.confidence)
						list.add(new Rule(subFea,compFea,tempConfidence));
					tempSet.clear();
					for (Ins subIns : instances) {
						ArrayList<Integer> pointList = new ArrayList<Integer>();
						for (Integer subInt: complocate)
							pointList.add(subIns.value.toArray(new Integer[0])[subInt]);												
						tempSet.add(pointList);
					}
					tempConfidence = (double)tempSet.size() /
							candi.cliqueIns[compFea.value.size()-1].get(compFea).size();
					if (tempConfidence > this.confidence)
						list.add(new Rule(compFea,subFea,tempConfidence));
				}
			}
		}
	}
	

	
	/**
	 * 显示所有的规则
	 */
	public void ShowRules() {
		for (Rule r : list)
			System.out.println(r);
	}
}
