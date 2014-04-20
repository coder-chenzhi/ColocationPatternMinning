package joinless.grid;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;



public class Joinless {
	/**
	 * ����������Ķ�Ӧ��ϵ
	 */
	static HashMap<Integer,Integer> point2Feature 
		= new HashMap<Integer,Integer>();
	/**
	 * ������ڵ�Ķ�Ӧ��ϵ
	 */
	static HashMap<Integer, ArrayList<Integer>> feature2Point
		= new HashMap<Integer, ArrayList<Integer>>();
	
	/**
	 * ����
	 */
	static Grids grids = new Grids(25, 40, 40);
	
	/**
	 * �ֲڼ�
	 */
	static Coarse coarse = new Coarse(); 
	
	/**
	 * ��ѡ��
	 */
	static Candidate candidate = new Candidate();
		
	/**
	 * �ھӹ�ϵ
	 */
	static Neighbors neighbors;
	
	/**
	 * ���ɵĹ���
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
		setParameter("data20_500.txt", 25, 0.2, 0.6);
		neighbors.calculate(grids);
		
		//redirect System.out to file
		/*File file = new File("out3.txt");
		FileOutputStream fos = new FileOutputStream(file);
		PrintStream ps = new PrintStream(fos);
		System.setOut(ps);*/
		
		Joinless.candidate.showCandidate(1);
		//Joinless.candidate.showCandidate(2);
		Joinless.candidate.CIprevalentFiltration(2);
		//System.out.println("after filtration");
		Joinless.candidate.showCandidate(2);
		for (int k = 3; k <= Joinless.candidate.cliqueIns[0].keySet().size(); k++){
			
			Joinless.candidate.createCandidate(k);
			if (Joinless.candidate.cliqueIns[k-1].size() == 0)
				break;
			Joinless.candidate.lookUpFiltration(k);
			Joinless.coarse.createCoarse(k);
			Joinless.coarse.prevalentFiltration(k);			
			Joinless.candidate.createInstances(k);
			Joinless.candidate.showCandidate(k);
		}
		Joinless.rules.createRules(Joinless.candidate);
		Joinless.rules.ShowRules();
		System.out.println("����ʱ�䣺"+(System.currentTimeMillis()-start)+"ms");
	}
}
