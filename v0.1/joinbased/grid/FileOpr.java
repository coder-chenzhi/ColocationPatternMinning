package joinbased.grid;
import java.util.*;
import java.io.*;
import java.sql.*;

public class FileOpr {
	
	/**
	 * 如果节点已经有编号ID，ID必须是连续的，而且从1开始。
	 * 主程序要求特征必须是数字，ID也必须是从1开始的连续数字，不符合要求的都必须进行预处理
	 * 预处理给每个特征分配编号，并且按照特征编号的大小顺序给每个特征的节点编号
	 * @param filename the name of file you want to read
	 * @return a TreeSet of points 
	 * @throws IOException
	 */
	public static TreeSet<Point> ReadFilePrimary(String filename) 
			throws IOException {
		TreeSet<Point> points = new TreeSet<Point>();
		@SuppressWarnings("unused")
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
		return points;
	}
	@SuppressWarnings("unchecked")
	public static TreeSet<Point> ReadFileOld(String filename) 
			throws IOException{
		TreeSet<Point> points = new TreeSet<Point>();
		
		CoFeatures feature;
		ArrayList<Integer[]> list;
		HashMap<CoFeatures,ArrayList<Integer[]>> tempMap = 
				new HashMap<CoFeatures,ArrayList<Integer[]>>();
		
		int total = 0;
		String temp;
		String[] str; 
		
			BufferedReader in = new BufferedReader(
					new FileReader(filename));
			while ((temp = in.readLine()) != null) {
				str = temp.split("\\W+");
				Point point = new Point();
				int column;
				int row;
				int length = Joinbase.grids.getLength();
				if (str.length == 4) {
					//使用数据填充point
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
					Joinbase.point2Feature.put(point.id, point.feature);
					
					//使用数据填充Candi2Instan					
					feature = new CoFeatures(new Integer[]{point.feature});
					if (tempMap.get(feature) == null) {
						list = new ArrayList<Integer[]>();
						list.add(new Integer[]{point.id});
						tempMap.put(feature, list);
					} else {
						list = tempMap.get(feature);
						list.add(new Integer[]{point.id});
					}
					
					//使用数据填充Grids
					row = point.x / length;//横坐标
					column = point.y / length;//纵坐标
					Joinbase.grids.getGrid(row, column).addPoint(point);
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
					Joinbase.point2Feature.put(point.id, point.feature);
					total++;
					
					//使用数据填充Candi2Instan
					feature = new CoFeatures(new Integer[]{point.feature});
					if (tempMap.get(feature) == null) {
						list = new ArrayList<Integer[]>();
						list.add(new Integer[]{point.id});
						tempMap.put(feature, list);
					} else {
						list = tempMap.get(feature);
						list.add(new Integer[]{point.id});
					}
					
					//使用数据填充Grids
					row = point.x / length;//横坐标
					column = point.y / length;//纵坐标
					Joinbase.grids.getGrid(row, column).addPoint(point);
				} else {
					System.out.println("the format of data should" +
							"be [id,feature,(x,y)] or [feature,(x,y)]");
					System.exit(0);
				}
			}
		Joinbase.candidate.candi2Ins = new HashMap[tempMap.size()];
		Joinbase.candidate.candi2Ins[0] = tempMap; 
		in.close();
		return points;
	}
	
	@SuppressWarnings("unchecked")
	public static void ReadFile(String filename) 
			throws IOException{
				
		CoFeatures feature;
		ArrayList<Integer[]> list;
		HashMap<CoFeatures,ArrayList<Integer[]>> tempMap = 
				new HashMap<CoFeatures,ArrayList<Integer[]>>();
		
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
				int length = Joinbase.grids.getLength();
				if (str.length == 4) {
					//使用数据填充point
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

					Joinbase.point2Feature.put(point.id, point.feature);
					
					//使用数据填充Candi2Instan					
					feature = new CoFeatures(new Integer[]{point.feature});
					if (tempMap.get(feature) == null) {
						list = new ArrayList<Integer[]>();
						list.add(new Integer[]{point.id});
						tempMap.put(feature, list);
					} else {
						list = tempMap.get(feature);
						list.add(new Integer[]{point.id});
					}
					
					//使用数据填充Grids
					x = point.x / length;
					y = point.y / length;
					Joinbase.grids.getGrid(x, y).addPoint(point);
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

					Joinbase.point2Feature.put(point.id, point.feature);
					total++;
					
					//使用数据填充Candi2Instan
					feature = new CoFeatures(new Integer[]{point.feature});
					if (tempMap.get(feature) == null) {
						list = new ArrayList<Integer[]>();
						list.add(new Integer[]{point.id});
						tempMap.put(feature, list);
					} else {
						list = tempMap.get(feature);
						list.add(new Integer[]{point.id});
					}
					
					//使用数据填充Grids
					x = point.x / length;
					y = point.y / length;
					Joinbase.grids.getGrid(x, y).addPoint(point);
				} else {
					System.out.println("the format of data should" +
							"be [id,feature,(x,y)] or [feature,(x,y)]");
					System.exit(0);
				}
			}
		Joinbase.candidate.candi2Ins = new HashMap[tempMap.size()];
		Joinbase.coarse.candi2Coarse = new HashMap[tempMap.size()];
		Joinbase.candidate.candi2Ins[0] = tempMap;
		Joinbase.grids.pointNum = total;
		in.close();
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
	
	public static TreeSet<Point> getFromDatabase(int num){
		DataBaseIO db = new DataBaseIO();
		int total= 0;
		int count = 0;//计数
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
		sql.append(0);//为了与最后一个"ID="匹配
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
		int count = 0;//计数
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
		TreeSet<Point> points = Point.createPointSet(20,500,2);
		try {
			WriteFile(points,"data20_500.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Point point:points)
			System.out.println(point);
		
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
		/*TreeSet<Point> set = getFromDataBaseArg(3500);
		for(Point p : set){
			System.out.println(p);
		}
		try {
			WriteFile(set, "virtual3500_3.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
	}
}
