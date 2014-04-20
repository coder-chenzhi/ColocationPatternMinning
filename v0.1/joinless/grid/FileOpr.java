package joinless.grid;
import java.util.*;
import java.io.*;
import java.sql.*;

public class FileOpr {
	
	/**
	 * ����ڵ��Ѿ��б��ID��ID�����������ģ����Ҵ�1��ʼ��
	 * ������Ҫ���������������֣�IDҲ�����Ǵ�1��ʼ���������֣�������Ҫ��Ķ��������Ԥ����
	 * ���û��ID����ô�ڵ���밴�������ۼ�����
	 * Ԥ�����ÿ�����������ţ����Ұ���������ŵĴ�С˳���ÿ�������Ľڵ���
	 * @param filename ��Ҫ��ȡ���ļ���
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static void ReadFile(String filename) 
			throws IOException{
				
		CoFeatures feature;
		HashSet<Ins> set;
		HashMap<CoFeatures,HashSet<Ins>> tempMap = 
				new HashMap<CoFeatures,HashSet<Ins>>();
		
		int total = 0;
		String temp;
		String[] str; 
		
			BufferedReader in = new BufferedReader(
					new FileReader(filename));
			while ((temp = in.readLine()) != null) {
				str = temp.split("\\W+");
				Point point = new Point();
				int x;
				int y;
				int length = Joinless.grids.getLength();
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

					Joinless.point2Feature.put(point.id, point.feature);
					ArrayList<Integer> tmp = Joinless.feature2Point.get(point.feature);
					if (tmp == null) {
						tmp = new ArrayList<Integer>();
						tmp.add(point.id);
						Joinless.feature2Point.put(point.feature, tmp);
					} 
					else {
						tmp.add(point.id);
					}
					
					//ʹ���������Candi2Instan					
					feature = new CoFeatures(new Integer[]{point.feature});
					if (tempMap.get(feature) == null) {
						set = new HashSet<Ins>();
						set.add(new Ins(new Integer[]{point.id}));
						tempMap.put(feature, set);
					} else {
						set = tempMap.get(feature);
						set.add(new Ins(new Integer[]{point.id}));
					}
					
					//ʹ���������Grids
					x = point.x / length;
					y = point.y / length;
					Joinless.grids.getGrid(x, y).addPoint(point);
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

					Joinless.point2Feature.put(point.id, point.feature);
					ArrayList<Integer> tmp = Joinless.feature2Point.get(point.feature);
					if (tmp == null) {
						tmp = new ArrayList<Integer>();
						tmp.add(point.id);
						Joinless.feature2Point.put(point.feature, tmp);
					} 
					else {
						tmp.add(point.id);
					}
					total++;
					
					//ʹ���������Candi2Instan
					feature = new CoFeatures(new Integer[]{point.feature});
					if (tempMap.get(feature) == null) {
						set = new HashSet<Ins>();
						set.add(new Ins(new Integer[]{point.id}));
						tempMap.put(feature, set);
					} else {
						set = tempMap.get(feature);
						set.add(new Ins(new Integer[]{point.id}));
					}
					
					//ʹ���������Grids
					x = point.x / length;
					y = point.y / length;
					Joinless.grids.getGrid(x, y).addPoint(point);
				} else {
					System.out.println("the format of data should" +
							"be [id,feature,(x,y)] or [feature,(x,y)]");
					System.exit(0);
				}
			}
		Joinless.candidate.cliqueIns = new HashMap[tempMap.size()];
		Joinless.coarse.candi2Coarse = new HashMap[tempMap.size()];
		Joinless.candidate.cliqueIns[0] = tempMap;
		Joinless.grids.pointNum = total;
		in.close();
	}
	
	public static void WriteFile(TreeSet<Point> points,String filename) 
			throws IOException {
		PrintWriter out = new PrintWriter(filename);
		for (Point point:points){
			out.println(point);
			System.out.println(point);
		}
		out.close();
	}
	
	public static void WriteFile(HashMap<Integer, TreeSet<Point>> map, String filename)
			throws IOException {
		int total = 1;
		PrintWriter out = new PrintWriter(filename);
		for(Integer f : map.keySet()){
			for(Point p : map.get(f)){
				p.id = total;
				total++;
				out.println(p);
				System.out.println(p);
			}
		}
		out.close();
	}
	
	
	public static TreeSet<Point> getFromDatabase(int num){
		DataBaseIO db = new DataBaseIO();
		int total= 0;
		int count = 0;//����
		Object[] param = {};
		StringBuilder sql = new StringBuilder();
		TreeSet<Point> points = new TreeSet<Point>();
		HashMap<Integer,ArrayList<Point>> map = 
					new HashMap<Integer,ArrayList<Point>>();
		HashSet<Integer> random;
		ResultSet rs; 
		
		rs = db.executeSqlWithResult(
				"use CoVegetation select count(x) as Num from dbo.DistributionWithID", param);
		try {
			rs.next();
			total= rs.getInt("Num");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		random = Randoms.createRandom(1, total, num);
		sql.append("use CoVegetation select *  from dbo.DistributionWithID Where ID=");
		for(Integer i : random){
			sql.append(i);
			sql.append(" OR ID=");			
		}
		sql.append(0);//Ϊ�������һ��"ID="ƥ��
		rs = db.executeSqlWithResult(sql.toString(), param);
		try {
			while(rs.next()){
			Point p =new Point(rs.getInt("X"),rs.getInt("Y"),rs.getInt("Vegetation"));
			Integer key = new Integer(rs.getInt("Vegetation"));
				if(map.containsKey(key)){
					map.get(key).add(p);
				} else {
					ArrayList<Point> list = new ArrayList<Point>();
					list.add(p);
					map.put(key, list);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TreeSet<Integer> set = new TreeSet<Integer>(map.keySet());
		for(Integer key : set){
			ArrayList<Point> list = map.get(key);
			for(Point p : list){
				count++;
				p.id = count;
			}
		}
		for(ArrayList<Point> list : map.values()){
			for(Point p : list){
				points.add(p);
			}
		}
		return points;
	}
	
	public static TreeSet<Point> getFromDataBaseArg(int num){
		int type = 0;
		int arg = 0;
		int rest = 0;
		int count = 0;//����
		DataBaseIO db = new DataBaseIO();
		TreeSet<Point> points = new TreeSet<Point>();
		Object[] param = {};
		ResultSet rs; 
		
		rs = db.executeSqlWithResult(
				"use CoVegetation select count(distinct vegetation) as Num from dbo.Distribution", param);
		try {
			rs.next();
			type= rs.getInt("Num");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		arg = num / type;
		rest = num - arg * type;
		
		for(int i = 1; i <= rest; i++ ){
			System.out.println(i);
			Object[] parami = {i};
			int total = 0;
			Integer[] index; 
			rs = db.executeSqlWithResult(
					"use CoVegetation select count(x) as Num from dbo.Distribution where vegetation = ?", parami);
			try {
				rs.next();
				total = rs.getInt("Num");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			index = Randoms.createRandom(1, total, arg+1).toArray(new Integer[0]);
			for(Integer inte : index){
				Object[] paramint = {i, i};
				rs = db.executeSqlWithResult(
						"use CoVegetation select top "+ inte +" *  from dbo.Distribution where vegetation = ?" +
						" except ( select top "+(inte-1)+" *  from dbo.Distribution where vegetation = ? )", paramint);
			
				try {
					rs.next();
					Point p =new Point(rs.getInt("X"),rs.getInt("Y"),i);
					p.id = ++count;
					points.add(p);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println(i + " done");
		}
		for(int i = rest+1; i <= type; i++ ){
			System.out.println(i);
			int total = 0;
			Integer[] index; 
			Object[] parami = {i};
			rs = db.executeSqlWithResult(
					"use CoVegetation select count(x) as Num from dbo.Distribution where vegetation = ?", parami);
			try {
				rs.next();
				total = rs.getInt("Num");
				System.out.println(total);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			index = Randoms.createRandom(1, total, arg).toArray(new Integer[0]);
			for(Integer inte : index){
				Object[] paramint = {i, i};
				rs = db.executeSqlWithResult(
						"use CoVegetation select top "+ inte +" *  from dbo.Distribution where vegetation = ?" +
						" except ( select top "+(inte-1)+" *  from dbo.Distribution where vegetation = ? )", paramint);
			
				try {
					rs.next();
					Point p =new Point(rs.getInt("X"),rs.getInt("Y"),i);
					p.id = ++count;
					System.out.println(count);
					points.add(p);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}			
			System.out.println(i + " done");
		}
			
		return points;
	}
	
	public static void main(String[] argc) {
		/* test for write file */
		/*TreeSet<Point> points = Point.createPointSet(20,100,2);
		try {
			WriteFile(points,"data20_100.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Point point:points)
			System.out.println(point);*/
		
		/* test for read file*/ 		
		/*TreeSet<Point> points;		 
		try {
			points = ReadFile("data.txt");
			for(Point point:points)
				System.out.println(point);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/*test for read from database*/
		/*TreeSet<Point> set = getFromDataBaseArg(7000);*/
		/* test new create points method*/
		TreeSet<Point> set = Point.createPointSet(10,5000,10);
		try {
			WriteFile(set, "50000.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
	}
}
