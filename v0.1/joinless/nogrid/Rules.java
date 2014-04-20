package joinless.nogrid;
import java.util.*;

class Rule{
	
	/**
	 * ǰ��
	 */
	CoFeatures pre;
	
	/**
	 * ���
	 */
	CoFeatures post;
	
	/**
	 * ���Ŷ�
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
	 * �������й����ArrayList	
	 */
	public ArrayList<Rule> list = new ArrayList<Rule>();
	
	/**
	 * �û��������Ŷ���ֵ
	 */
	private double confidence;		
	
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
		System.out.println("Rules:");
		for (HashMap<CoFeatures,HashSet<Ins>> tempMap : candi.cliqueIns) {
			if (tempMap.size() == 0) {
				break;
			}
			for (CoFeatures tempFea : tempMap.keySet()) {
				Integer[] tempInte = tempFea.value.toArray(new Integer[0]);
				
				//���Ӽ��ǽ��Ӽ�ת��Ϊ�����ƣ�ÿ��Ԫ�ص������ö����Ʊ�ʾ��
				//Ҳ���Ǵ�0��2^n-1ѭ����Ӧ���е�2^n���Ӽ�
				//����Ϊÿ���Ӽ��Ĳ���Ҳ���Ӽ�������ֻ��Ҫһ���ѭ��
				for (int i = 1; i < (int)Math.pow(2, tempInte.length-1); i++) {
					int temp = i;
					int count = 0;//����count����¼��ǰ�Ӽ���Ӧ�Ķ�������Ϊ����λ��Ȼ����������һ����0λ֮ǰ�м���0
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
					
					//�������KeySet()��Key���±�ȥValue()�ж�Ӧλ��ȡ�������
					//֮���Կ�������������ΪKeySet()�е�����ֵ�ǰ���С�����
					//�����ı��Ҳ�ǰ�������ֵ����Դ�С�����
					//������������ʵ���Ĺ����У�ʵ���еĽڵ���Ҳ����С������
					//����Ƶ����������ֵ�����˳�򣬺�ʵ���н�������˳��һ��					
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
	 * ��ʾ���еĹ���
	 */
	public void ShowRules() {
		for (Rule r : list)
			System.out.println(r);
	}
}
