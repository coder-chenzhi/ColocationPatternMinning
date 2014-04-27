package joinbase.withgrid;
import java.util.*;

class Rule{
	
	/**
	 * 前键
	 */
	Pattern pre;
	
	/**
	 * 后键
	 */
	Pattern post;
	
	/**
	 * 自信度
	 */
	private double confindence = 0.0;
	
	Rule() {	}
	
	Rule(Pattern pre, Pattern post, double confidence) {
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
	
	
	Rules(double conThreshold) {
		this.conThreshold = conThreshold;
	}
	
	/**
	 * 包含所有规则的ArrayList	
	 */
	public ArrayList<Rule> list = new ArrayList<Rule>();
	
	/**
	 * 用户设置自信度阈值
	 */
	private double conThreshold;		
	
	/**
	 * 设置自信度
	 * @param confidence 用户指定的自信度
	 */
	public void setConfidence(double confidence) {
		this.conThreshold = confidence;
	}
	
	/**
	 * 根据频繁集计算规则
	 * @param candi 计算得到的频繁集
	 */
	public void createRules(BDBManager database, Candidate candi) {
		for (int i = 0; i < candi.getMaxLength(); i++) {
			HashSet<Pattern> tempMap = candi.getCandidate(i);
			if (tempMap.size() == 0) {
				break;
			}
			for (Pattern p : tempMap) {
				Integer[] tempInte = p.toArray();
				
				//求子集是将子集转换为二进制，每个元素的有无用二进制表示，
				//也就是从0到2^n-1循环对应所有的2^n个子集
				//又因为每个子集的补集也是子集，所以只需要一半的循环
				for (int j = 1; j < (int)Math.pow(2, tempInte.length-1); j++) {
					int temp = j;
					int count = 0;//设置count，记录当前子集对应的二进制数为多少位，然后就能算出第一个非0位之前有几个0
					ArrayList<Integer> sublocate = new ArrayList<Integer>();
					ArrayList<Integer> comlocate = new ArrayList<Integer>();

					ArrayList<Integer> sublist = new ArrayList<>();
					ArrayList<Integer> comlist = new ArrayList<>();
					while (temp != 0) {
						if (temp % 2 == 1) {
							sublist.add(tempInte[count]);
							sublocate.add(new Integer(count));
						} else {
							comlist.add(tempInte[count]);
							comlocate.add(new Integer(count));
						}
						temp /= 2;
						count++;
					}
					
					for (int k = count; k < tempInte.length; k++){
						comlist.add(tempInte[k]);
						comlocate.add(new Integer(k));
					}
					Pattern subPaten = new Pattern(sublist);
					Pattern comPaten = new Pattern(comlist);
					
					//这里根据KeySet()中Key的下标去Value()中对应位置取出结点编号
					//之所以可以这样，是因为KeySet()中的特征值是按大小排序的
					//而结点的编号也是按照特征值的相对大小排序的
					//并且在连接找实例的过程中，实例中的节点编号也按大小排了序
					//所以频繁集中特征值的相对顺序，和实例中结点编号相对顺序一致					
					HashSet<ArrayList<Integer>> tempSet = 
							new HashSet<ArrayList<Integer>>();
					//暂且不管生成规则
					//暂且不管生成规则
					//暂且不管生成规则
					//暂且不管生成规则
					//暂且不管生成规则
					Instance instances = database.get(p);
					for (Item subIns : instances) {
						ArrayList<Integer> pointList = new ArrayList<Integer>();
						int[] subArray = subIns.toArray();
						for (Integer subInt: sublocate)
							pointList.add(subArray[subInt]);												
						tempSet.add(pointList);
					}
					double tempConfidence = (double)tempSet.size() /
							database.get(subPaten).size();
					if (tempConfidence > this.conThreshold)
						list.add(new Rule(subPaten,comPaten,tempConfidence));
					tempSet.clear();
					for (Item subIns : instances) {
						int[] subArray = subIns.toArray();
						ArrayList<Integer> pointList = new ArrayList<Integer>();
						for (Integer subInt: comlocate)
							pointList.add(subArray[subInt]);												
						tempSet.add(pointList);
					}
					tempConfidence = (double)tempSet.size() /
							database.get(comPaten).size();
					if (tempConfidence > this.conThreshold)
						list.add(new Rule(comPaten,subPaten,tempConfidence));
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
