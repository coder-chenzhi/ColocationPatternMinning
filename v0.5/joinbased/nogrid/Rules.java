package joinbased.nogrid;
import java.util.*;

class Rule{
	
	/**
	 * ǰ��
	 */
	Features pre;
	
	/**
	 * ���
	 */
	Features post;
	
	/**
	 * ���Ŷ�
	 */
	double confindence = 0.0;
	
	Rule() {	}
	
	Rule(Features pre, Features post, double confidence) {
		this.pre = pre;
		this.post = post;
		this.confindence = confidence;
	}
	
	public String toString() {
		String str = "";
		str = pre + " -> " + post +" confidence: " + confindence;
		return str;
	}
}

public class Rules {
	
	/**
	 * �������й����ArrayList	
	 */
	public ArrayList<Rule> list = new ArrayList<Rule>();
	
	/**
	 * ���Ŷ�
	 */
	public double confidence;		
	
	/**
	 * �������Ŷ�
	 * @param confidence �û�ָ�������Ŷ�
	 */
	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	
	/**
	 * ����Ƶ�����������
	 * @param candi ����õ���Ƶ����
	 */
	public void createRules(Candidate candi) {
		for (HashMap<Features,ArrayList<Integer[]>> tempMap : candi.candi2Instan) {
			if (tempMap.size() == 0) {
				break;
			}
			for (Features tempFea : tempMap.keySet()) {
				Integer[] tempInte = tempFea.value.toArray(new Integer[0]);
				
				//���Ӽ��ǽ��Ӽ�ת��Ϊ�����ƣ�ÿ��Ԫ�ص������ö����Ʊ�ʾ��
				//Ҳ���Ǵ�0��2^n-1ѭ����Ӧ���е�2^n���Ӽ�
				//����Ϊÿ���Ӽ��Ĳ���Ҳ���Ӽ�������ֻ��Ҫһ���ѭ��
				for (int i = 1; i < (int)Math.pow(2, tempInte.length-1); i++) {
					int temp = i;
					int count = 0;//����count����¼��ǰ�Ӽ���Ӧ�Ķ�������Ϊ����λ��Ȼ����������һ����0λ֮ǰ�м���0
					ArrayList<Integer> sublocate = new ArrayList<Integer>();
					ArrayList<Integer> copylocate = new ArrayList<Integer>();
					Features copyFea = 
							new Features(tempFea.value.toArray(new Integer[0]));
					Features subFea = new Features();
					while (temp != 0) {
						if (temp % 2 == 1) {
							subFea.value.add(tempInte[count]);
							sublocate.add(new Integer(count));
						} else {
							copylocate.add(new Integer(count));
						}
						temp /= 2;
						count++;
					}
					
					for (int j = count; j < tempInte.length; j++){
						copylocate.add(new Integer(j));
					}
					copyFea.value.removeAll(subFea.value);
					
					//�������KeySet()��Key���±�ȥValue()�ж�Ӧλ��ȡ�������
					//֮���Կ�������������ΪKeySet()�е�����ֵ�ǰ���С�����
					//�����ı��Ҳ�ǰ�������ֵ����Դ�С�����
					//������������ʵ���Ĺ����У�ʵ���еĽڵ���Ҳ����С������
					//����Ƶ����������ֵ�����˳�򣬺�ʵ���н�������˳��һ��					
					HashSet<ArrayList<Integer>> tempSet = 
							new HashSet<ArrayList<Integer>>();
					ArrayList<Integer[]> instances =
							candi.candi2Instan[tempFea.value.size()-1].get(tempFea);
					for (Integer[] subIns : instances) {
						ArrayList<Integer> pointList = new ArrayList<Integer>();
						for (Integer subInt: sublocate)
							pointList.add(subIns[subInt]);												
						tempSet.add(pointList);
					}
					double tempConfidence = (double)tempSet.size() /
							candi.candi2Instan[subFea.value.size()-1].get(subFea).size();
					if (tempConfidence > this.confidence)
						list.add(new Rule(subFea,copyFea,tempConfidence));
					tempSet.clear();
					for (Integer[] subIns : instances) {
						ArrayList<Integer> pointList = new ArrayList<Integer>();
						for (Integer subInt: copylocate)
							pointList.add(subIns[subInt]);												
						tempSet.add(pointList);
					}
					tempConfidence = (double)tempSet.size() /
							candi.candi2Instan[copyFea.value.size()-1].get(copyFea).size();
					if (tempConfidence > this.confidence)
						list.add(new Rule(copyFea,subFea,tempConfidence));
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
