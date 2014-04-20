package joinbased.grid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;


public class Joinbase {
	
	/**
	 * ����������Ķ�Ӧ��ϵ
	 */
	static HashMap<Integer,Integer> point2Feature 
		= new HashMap<Integer,Integer>();
	/**
	 * ����
	 */
	static Grids grids = new Grids(25,40,40);
	
	/**
	 * �ֲڼ�
	 */
	static Coarse coarse = new Coarse(); 
	
	/**
	 * ��ѡ��
	 */
	static Candidate candidate = new Candidate();
	
	/**
	 * ����ľ���
	 */
	static Distance distance;
	
	/**
	 * ���ɵĹ���
	 */
	static Rules rules = new Rules();
	
	
	public static void setParameter(String path, double dis, double pre, double conf) 
			throws IOException{ 
		FileOpr.ReadFile(path);
		distance = new Distance(grids,dis);
		candidate.setPrevalent(pre);
		coarse.setPrevalent(pre);
		rules.setConfidence(conf);
	}
	
	public static void function(){
		Joinbase.candidate.showCandidate(1);
		Joinbase.candidate.prevalentFiltration(2);
		Joinbase.candidate.showCandidate(2);
		for (int k = 3; k <= Joinbase.candidate.candi2Ins[0].keySet().size(); k++){			
			Joinbase.candidate.createCandidate(k);
			if (Joinbase.candidate.candi2Ins[k-1].size() == 0)
				break;
			Joinbase.candidate.lookUpFiltration(k);
			Joinbase.coarse.createCoarse(k);
			Joinbase.coarse.prevalentFiltration(k);
			Joinbase.candidate.createInstances(k);
			Joinbase.candidate.prevalentFiltration(k);
			Joinbase.candidate.showCandidate(k);
		}
		Joinbase.rules.createRules(Joinbase.candidate);
		Joinbase.rules.ShowRules();
	}
	
	public static void analyse(){
		HashMap<CoFeatures,ArrayList<Integer[]>> result[] = Joinbase.candidate.candi2Ins;
		int maxlength = 0; 
		int patternNum = 0;
		int instanceNum = 0;
		
		for(int i = 1; i < result.length; i++){
			HashMap<CoFeatures,ArrayList<Integer[]>> candidate = result[i];
			if (candidate == null) 
				break;
			patternNum += candidate.keySet().size();
			for(CoFeatures cf : candidate.keySet())
				instanceNum += candidate.get(cf).size();
			maxlength ++;
		}
		
		System.out.println("�ģʽ���ȣ�" + maxlength);
		System.out.println("ģʽ��Ŀ��" + patternNum);
		System.out.println("ʵ����Ŀ��" + instanceNum);
	}
	
	public static void main(String[] argc) throws IOException{
		Long start = System.currentTimeMillis();
		//redirect System.out to file
		/*File file = new File("out.txt");
		FileOutputStream fos = new FileOutputStream(file);
		PrintStream ps = new PrintStream(fos);
		System.setOut(ps);*/
		
		setParameter("data20_500.txt", 25, 0.2, 0.6);
		// just for test
		/*
		int num = 0;
		for(int i = 0; i < Join_base.grids.hsize ; i++)
			for(int j = 0; j < Join_base.grids.vsize ; j++){
				num += Join_base.grids.getGrid(i, j).getNum();
			}
		System.out.println(num);
		*/
		Joinbase.candidate.showCandidate(1);
		Joinbase.candidate.showCandidate(2);
		Joinbase.candidate.prevalentFiltration(2);
		System.out.println("after filtration");
		Joinbase.candidate.showCandidate(2);
		for (int k = 3; k <= Joinbase.candidate.candi2Ins[0].keySet().size(); k++){
			
			Joinbase.candidate.createCandidate(k);
			if (Joinbase.candidate.candi2Ins[k-1].size() == 0)
				break;
			Joinbase.candidate.lookUpFiltration(k);
			Joinbase.coarse.createCoarse(k);
			Joinbase.coarse.prevalentFiltration(k);
			Joinbase.candidate.createInstances(k);
			Joinbase.candidate.prevalentFiltration(k);
			Joinbase.candidate.showCandidate(k);
		}
		Joinbase.rules.createRules(Joinbase.candidate);
		Joinbase.rules.ShowRules();
		System.out.println("����ʱ�䣺"+(System.currentTimeMillis()-start)+"ms");
		Joinbase.analyse();
	}
}
