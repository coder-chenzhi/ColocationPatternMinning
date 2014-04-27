package joinbase.withgrid;
import java.util.*;

class Rule{
	
	/**
	 * ǰ��
	 */
	Pattern pre;
	
	/**
	 * ���
	 */
	Pattern post;
	
	/**
	 * ���Ŷ�
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
	 * �������й����ArrayList	
	 */
	public ArrayList<Rule> list = new ArrayList<Rule>();
	
	/**
	 * �û��������Ŷ���ֵ
	 */
	private double conThreshold;		
	
	/**
	 * �������Ŷ�
	 * @param confidence �û�ָ�������Ŷ�
	 */
	public void setConfidence(double confidence) {
		this.conThreshold = confidence;
	}
	
	/**
	 * ����Ƶ�����������
	 * @param candi ����õ���Ƶ����
	 */
	public void createRules(BDBManager database, Candidate candi) {
		for (int i = 0; i < candi.getMaxLength(); i++) {
			HashSet<Pattern> tempMap = candi.getCandidate(i);
			if (tempMap.size() == 0) {
				break;
			}
			for (Pattern p : tempMap) {
				Integer[] tempInte = p.toArray();
				
				//���Ӽ��ǽ��Ӽ�ת��Ϊ�����ƣ�ÿ��Ԫ�ص������ö����Ʊ�ʾ��
				//Ҳ���Ǵ�0��2^n-1ѭ����Ӧ���е�2^n���Ӽ�
				//����Ϊÿ���Ӽ��Ĳ���Ҳ���Ӽ�������ֻ��Ҫһ���ѭ��
				for (int j = 1; j < (int)Math.pow(2, tempInte.length-1); j++) {
					int temp = j;
					int count = 0;//����count����¼��ǰ�Ӽ���Ӧ�Ķ�������Ϊ����λ��Ȼ����������һ����0λ֮ǰ�м���0
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
					
					//�������KeySet()��Key���±�ȥValue()�ж�Ӧλ��ȡ�������
					//֮���Կ�������������ΪKeySet()�е�����ֵ�ǰ���С�����
					//�����ı��Ҳ�ǰ�������ֵ����Դ�С�����
					//������������ʵ���Ĺ����У�ʵ���еĽڵ���Ҳ����С������
					//����Ƶ����������ֵ�����˳�򣬺�ʵ���н�������˳��һ��					
					HashSet<ArrayList<Integer>> tempSet = 
							new HashSet<ArrayList<Integer>>();
					//���Ҳ������ɹ���
					//���Ҳ������ɹ���
					//���Ҳ������ɹ���
					//���Ҳ������ɹ���
					//���Ҳ������ɹ���
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
	 * ��ʾ���еĹ���
	 */
	public void ShowRules() {
		for (Rule r : list)
			System.out.println(r);
	}
}
