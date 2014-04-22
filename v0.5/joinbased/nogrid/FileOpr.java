package joinbased.nogrid;
import java.util.*;
import java.io.*;

public class FileOpr {
	
	/**
	 * ����ڵ��Ѿ��б��ID��ID�����������ģ����Ҵ�1��ʼ��
	 * ������Ҫ���������������֣�IDҲ�����Ǵ�1��ʼ���������֣�������Ҫ��Ķ��������Ԥ����
	 * Ԥ�����ÿ�����������ţ����Ұ���������ŵĴ�С˳���ÿ�������Ľڵ���
	 * @param filename the name of file you want to read
	 * @return a TreeSet of points 
	 * @throws IOException
	 */
	public static TreeSet<Point> ReadFilePrimary(String filename) 
			throws IOException {
		TreeSet<Point> points = new TreeSet<Point>();
		
		int total = 0;
		String temp;
		String[] str; 
		
			BufferedReader in = new BufferedReader(
					new FileReader(filename));
			while ((temp = in.readLine()) != null) {
				str = temp.split("\\W+");
				Point point = new Point();
								
				if(str.length == 4){
					
					try{	
						point.feature = Integer.parseInt(str[1]);
						point.x = Integer.parseInt(str[2]);
						point.y = Integer.parseInt(str[3]);	
					}catch(NumberFormatException e){
						e.printStackTrace();
						System.out.println("id and coodinates of the point" +
								" should be integer");
					}
					points.add(point);
					total++;
				} else if (str.length == 5) {
					
					try{
						point.id = Integer.parseInt(str[1]);
						point.feature = Integer.parseInt(str[2]);
						point.x = Integer.parseInt(str[3]);
						point.y = Integer.parseInt(str[4]);
					}catch(NumberFormatException e){
						e.printStackTrace();
						System.out.println("id and coodinates of the point" +
								" should be integer");
					}
					points.add(point);
					total++;
				} else {
					System.out.println("the format of data should" +
							"be [id,feature,(x,y)] or [feature,(x,y)]");
					System.exit(0);
				}
			}
		
		in.close();
		Point.count = total;
		return points;
	}
	@SuppressWarnings("unchecked")
	public static TreeSet<Point> ReadFile(String filename) 
			throws IOException{
		TreeSet<Point> points = new TreeSet<Point>();
		
		Features feature;
		ArrayList<Integer[]> list;
		HashMap<Features,ArrayList<Integer[]>> tempMap = 
				new HashMap<Features,ArrayList<Integer[]>>();
		
		int total = 0;
		String temp;
		String[] str; 
		
			BufferedReader in = new BufferedReader(
					new FileReader(filename));
			while ((temp = in.readLine()) != null) {
				str = temp.split("\\W+");
				Point point = new Point();
								
				if (str.length == 4) {
					//ʹ���������point
					try{	
						point.feature = Integer.parseInt(str[1]);
						point.x = Integer.parseInt(str[2]);
						point.y = Integer.parseInt(str[3]);	
					}catch(NumberFormatException e){
						e.printStackTrace();
						System.out.println("id and coodinates of the point" +
								" should be integer");
					}					
					total++;
					point.id = total;
					points.add(point);
					Join_base.point2Feature.put(point.id, point.feature);
					//ʹ���������Candi2Instan
					
					feature = new Features(new Integer[]{point.feature});
					if (tempMap.get(feature) == null) {
						list = new ArrayList<Integer[]>();
						list.add(new Integer[]{point.id});
						tempMap.put(feature, list);
					} else {
						list = tempMap.get(feature);
						list.add(new Integer[]{point.id});
					}
				} else if (str.length == 5) {
					
					try{
						point.id = Integer.parseInt(str[1]);
						point.feature = Integer.parseInt(str[2]);
						point.x = Integer.parseInt(str[3]);
						point.y = Integer.parseInt(str[4]);
					}catch(NumberFormatException e){
						e.printStackTrace();
						System.out.println("id and coodinates of the point" +
								" should be integer");
					}
					points.add(point);
					Join_base.point2Feature.put(point.id, point.feature);
					total++;
					
					//ʹ���������Candi2Instan
					feature = new Features(new Integer[]{point.feature});
					if (tempMap.get(feature) == null) {
						list = new ArrayList<Integer[]>();
						list.add(new Integer[]{point.id});
						tempMap.put(feature, list);
					} else {
						list = tempMap.get(feature);
						list.add(new Integer[]{point.id});
					}
				} else {
					System.out.println("the format of data should" +
							"be [id,feature,(x,y)] or [feature,(x,y)]");
					System.exit(0);
				}
			}
		Join_base.candidate.candi2Instan = new HashMap[tempMap.size()];
		Join_base.candidate.candi2Instan[0] = tempMap; 
		in.close();
		Point.count = total;
		return points;
	}
	
	public static void WriteFile(Point[] points,String filename) 
			throws IOException {
		PrintWriter out = new PrintWriter(filename);
		for (Point point:points)
			out.println(point);
		out.close();
	}
	public static void WriteFile(TreeSet<Point> points,String filename) 
			throws IOException {
		PrintWriter out = new PrintWriter(filename);
		for (Point point:points)
			out.println(point);
		out.close();
	}
	
	public static void main(String[] argc) {
		/* test for write file */
		TreeSet<Point> points = Point.createPointSet(10,100,2);
		try {
			WriteFile(points,"data10_100.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Point point:points)
			System.out.println(point);
		
		/* test for read file 		
		TreeSet<Point> points;		 
		try {
			points = ReadFile("data.txt");
			for(Point point:points)
				System.out.println(point);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
}
