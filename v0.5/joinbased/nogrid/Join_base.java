package joinbased.nogrid;
import java.io.IOException;
import java.util.*;


public class Join_base {
	
	/**
	 * 结点与特征的对应关系
	 */
	static HashMap<Integer,Integer> point2Feature 
		= new HashMap<Integer,Integer>();
	
	/**
	 * 候选集
	 */
	static Candidate candidate = new Candidate();
	
	/**
	 * 结点间的距离
	 */
	static Distance distance;
	
	/**
	 * 生成的规则
	 */
	static Rules rules = new Rules();
	
	
	public static void setParameter(String path, double dis, double pre, double conf) 
			throws IOException{
		TreeSet<Point> points;		 
		points = FileOpr.ReadFile(path);
		distance = new Distance(points,dis);
		candidate.prevalent = pre;
		rules.confidence = conf;
	}
	
	public static void function(){
		Join_base.candidate.showCandidate(1);
		Join_base.candidate.prevalentFiltration(2);
		Join_base.candidate.showCandidate(2);
		for (int k = 3; k <= Join_base.candidate.candi2Instan[0].keySet().size(); k++){			
			Join_base.candidate.createCandidate(k);
			if (Join_base.candidate.candi2Instan[k-1].size() == 0)
				break;
			Join_base.candidate.lookUpFiltration(k);
			Join_base.candidate.prevalentFiltration(k);
			Join_base.candidate.showCandidate(k);
		}
		Join_base.rules.createRules(Join_base.candidate);
		Join_base.rules.ShowRules();
	}
	
	public static void main(String[] argc) throws IOException{
		Long start = System.currentTimeMillis();
		TreeSet<Point> points;		 
		points = FileOpr.ReadFile("virtual2500.txt");	
		distance = new Distance(points,100);
		Join_base.candidate.showCandidate(1);
		Join_base.candidate.showCandidate(2);
		Join_base.candidate.prevalent = 0.6;
		Join_base.candidate.prevalentFiltration(2);
		System.out.println("after filtration");
		Join_base.candidate.showCandidate(2);
		for (int k = 3; k <= Join_base.candidate.candi2Instan[0].keySet().size(); k++){
			
			Join_base.candidate.createCandidate(k);
			if (Join_base.candidate.candi2Instan[k-1].size() == 0)
				break;
			Join_base.candidate.lookUpFiltration(k);
			Join_base.candidate.prevalentFiltration(k);
			Join_base.candidate.showCandidate(k);
		}
		Join_base.rules.setConfidence(0.8);
		Join_base.rules.createRules(Join_base.candidate);
		Join_base.rules.ShowRules();
		System.out.println("运行时间："+(System.currentTimeMillis()-start)+"ms");
	}
}
