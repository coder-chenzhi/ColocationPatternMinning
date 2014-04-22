package joinless.nogrid;

import java.io.IOException;
import java.util.HashMap;



public class Joinless {
	/**
	 * 结点与特征的对应关系
	 */
	static HashMap<Integer,Integer> point2Feature 
		= new HashMap<Integer,Integer>();
	/**
	 * 网格
	 */
	static Grids grids = new Grids(100,100,100);
	
	/**
	 * 粗糙集
	 */
	static Coarse coarse = new Coarse(); 
	
	/**
	 * 候选集
	 */
	static Candidate candidate = new Candidate();
		
	/**
	 * 邻居关系
	 */
	static Neighbors neighbors;
	
	/**
	 * 生成的规则
	 */
	static Rules rules = new Rules();
	
	
	public static void setParameter(String path, double dis, double pre, double conf) 
			throws IOException{ 
		FileOpr.ReadFile(path);
		neighbors = new Neighbors(dis);
		candidate.setPrevalent(pre);
		coarse.setPrevalent(pre);
		rules.setConfidence(conf);
	}
	
	public static void main(String[] argc) throws IOException{
		Long start = System.currentTimeMillis();		
		setParameter("20000.txt", 100, 0.4, 0.6);
		neighbors.calculate(grids);
		
		Joinless.candidate.showCandidate(1);
		Joinless.candidate.showCandidate(2);
		Joinless.candidate.CIprevalentFiltration(2);
		System.out.println("after filtration");
		Joinless.candidate.showCandidate(2);
		for (int k = 3; k <= Joinless.candidate.cliqueIns[0].keySet().size(); k++){
			
			Joinless.candidate.createCandidate(k);
			if (Joinless.candidate.cliqueIns[k-1].size() == 0)
				break;
			Joinless.candidate.lookUpFiltration(k);
			//Joinless.coarse.createCoarse(k);
			//Joinless.coarse.prevalentFiltration(k);
			Joinless.candidate.createInstances(k);
			Joinless.candidate.showCandidate(k);
		}
		Joinless.rules.createRules(Joinless.candidate);
		Joinless.rules.ShowRules();
		System.out.println("运行时间："+(System.currentTimeMillis()-start)+"ms");
	}
}
