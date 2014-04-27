package joinbase.withgrid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;


public class Joinbase {
	
	public static void analyse(Candidate candidates){
		//int maxlength = 0; 
		int patternNum = 0;
		int instanceNum = 0;
		
		for(int i = 1; i < candidates.getMaxLength(); i++){
			HashSet<Pattern> candidate = candidates.getCandidate(i);
			if (candidate == null) 
				break;
			patternNum += candidate.size();
			/*for(Pattern cf : candidate.keySet())
				instanceNum += candidate.get(cf).size();
			maxlength ++;*/
		}
		
		//System.out.println("�ģʽ���ȣ�" + maxlength);
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
		
		//set parameters
		String dbPath = "g:/Research/Graduation Project/Database/real120000_50_0.4";
		String filename = "../Inno/realdata120000.txt";		// file path
		int horizontal = 1000;						// space frame width
		int vertical = 500;						// space frame length
		int threshold = 50;							// distance threshold
		double prevalence = 0.4;					// prevalence
		double confidence = 0.6;					// confidence
		
		//initialization
		ResizingIntArray point2Feature = new ResizingIntArray(); //����������Ķ�Ӧ��ϵ
		ResizingIntArray featureInsSize = new ResizingIntArray(); //����������ʵ�������������ж�ʱ��Ҫ
		//����ĺ�������������...
		Grids grids = new Grids(threshold, vertical / threshold + 1, horizontal / threshold + 1);//����
		Coarse coarse = new Coarse(prevalence); //�ֲڼ�
		Candidate candidate = new Candidate(prevalence);//��ѡ��
		Distance distance = new Distance();//����
		Rules rules = new Rules(confidence);//����
		BDBManager database = new BDBManager(dbPath);
		database.createDatabase("instanceDatabase");//�����ݿ�
		
		//read file and set various
		FileHandler.ReadFile(database, filename, grids, coarse, 
				candidate, point2Feature, featureInsSize);
		
		//caculate distance and fill specific fields
		distance.calculate(database, grids, candidate,
				coarse, featureInsSize, prevalence, threshold);
		
		// just for test
		/*
		int num = 0;
		for(int i = 0; i < grids.hsize ; i++)
			for(int j = 0; j < grids.vsize ; j++){
				num += grids.getGrid(i, j).getNum();
			}
		System.out.println(num);
		*/
		candidate.showCandidate(database, 1);
		candidate.showCandidate(database, 2);
		for (int k = 3; k <= candidate.getMaxLength(); k++){
			candidate.createCandidate(k);
			if (candidate.getCandidate(k-1).size() == 0)
				break;
			candidate.lookUpFiltration(k);
			coarse.createCoarse(candidate, k);
			coarse.prevalentFiltration(candidate, featureInsSize, k);
			candidate.createInstances(database, distance, grids, 
					coarse, featureInsSize, prevalence, k);
			candidate.showCandidate(database, k);
		}
		//rules.createRules(candidate);
		//rules.ShowRules();
		database.closeDatabase();
		
		int MegaBytes = 1024 * 1024;
		long freeMemory = Runtime.getRuntime().freeMemory() / MegaBytes;
		long totalMemory = Runtime.getRuntime().totalMemory() / MegaBytes;
        System.out.println("ռ���ڴ�:" + (totalMemory - freeMemory) + "MB");
		System.out.println("����ʱ�䣺"+(System.currentTimeMillis()-start)+"ms");
		
		//Joinbase.analyse();
	}
}
