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
		
		//System.out.println("最长模式长度：" + maxlength);
		System.out.println("模式数目：" + patternNum);
		System.out.println("实例数目：" + instanceNum);
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
		ResizingIntArray point2Feature = new ResizingIntArray(); //结点与特征的对应关系
		ResizingIntArray featureInsSize = new ResizingIntArray(); //各个特征的实例数，计算流行度时需要
		//网格的横纵坐标有问题...
		Grids grids = new Grids(threshold, vertical / threshold + 1, horizontal / threshold + 1);//网格
		Coarse coarse = new Coarse(prevalence); //粗糙集
		Candidate candidate = new Candidate(prevalence);//候选集
		Distance distance = new Distance();//距离
		Rules rules = new Rules(confidence);//规则
		BDBManager database = new BDBManager(dbPath);
		database.createDatabase("instanceDatabase");//打开数据库
		
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
        System.out.println("占用内存:" + (totalMemory - freeMemory) + "MB");
		System.out.println("运行时间："+(System.currentTimeMillis()-start)+"ms");
		
		//Joinbase.analyse();
	}
}
