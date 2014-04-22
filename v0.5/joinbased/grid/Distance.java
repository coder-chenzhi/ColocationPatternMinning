package joinbased.grid;
import java.util.*;

public class Distance {
	
	/**
	 * 所有结点的数量
	 */
	int size;
	
	/**
	 * 实际存储结果的一维数组
	 */
	boolean values[] ;
	
	/**
	 * 距离阈值
	 */
	double threshold;
	
	/**
	 * 根据用户提供的节点信息以及距离阈值初始化距离矩阵
	 * @param Points 含有所有结点的TreeSet
	 * @param threshold 用户提供的距离阈值
	 */
	public void DistancePrimary(TreeSet<Point> Points, double threshold) {
		size = Points.size();
		int location = 0;//当前结果在一维数组中实际存储的位置
		this.threshold = threshold;
		double thres = threshold * threshold;
		values = new boolean[size * (size + 1) / 2];
		Point[] points = Points.toArray(new Point[0]);
		for (int i = 0; i < size; i++)
			for (int j = i; j < size; j++){
				location = locate(points[i].id,points[j].id);
				if (points[i].feature != points[j].feature){					
					double dis = Math.pow(points[i].x - points[j].x, 2)
								 + Math.pow(points[i].y - points[j].y, 2);
					if (dis > thres){
						values[location] = false;
					} else {
						values[location] = true;
					}
				}
				else values[location] = false;
			}
		
	}
	
	/**
	 * 根据用户提供的节点信息以及距离阈值初始化距离矩阵，并初始化长度为2的候选集
	 * @param Points 含有所有结点的TreeSet
	 * @param threshold 用户提供的距离阈值
	 */
	Distance(TreeSet<Point> Points, double threshold) {
		
		//根据用户提供的节点信息以及距离阈值更新
		size = Points.size();
		this.threshold = threshold;
		
		//计算距离矩阵的临时变量
		int location = 0;//当前结果在一维数组中实际存储的位置		
		double thres = threshold * threshold;
		values = new boolean[size * ( size + 1 ) / 2];
		Point[] points = Points.toArray(new Point[0]);
		
		//填充候选集的临时变量
		CoFeatures feature;
		ArrayList<Integer[]> list1;
		HashMap<CoFeatures,ArrayList<Integer[]>> tempCandi2Ins = 
				new HashMap<CoFeatures,ArrayList<Integer[]>>();
		
		
		for (int i = 0; i < size; i++)
			for (int j = i; j < size; j++){
				location = locate(points[i].id, points[j].id);
				if (points[i].feature != points[j].feature){					
					double dis = Math.pow(points[i].x - points[j].x, 2)
								 + Math.pow(points[i].y - points[j].y, 2);
					if(dis > thres){
						values[location] = false;
					} else { 
						values[location] = true;
						feature = new CoFeatures(
								new Integer[]{points[i].feature,points[j].feature});
						//填充候选集
						if (tempCandi2Ins.get(feature) == null){
							list1 = new ArrayList<Integer[]>();
							if(points[i].id < points[j].id){
								list1.add(new Integer[]{points[i].id, points[j].id});
							} else {
								list1.add(new Integer[]{points[j].id, points[i].id});
							}
							tempCandi2Ins.put(feature, list1);
						}
						else{
							list1 = tempCandi2Ins.get(feature);
							if (points[i].id < points[j].id){
								list1.add(new Integer[]{points[i].id, points[j].id});
							} else {
								list1.add(new Integer[]{points[j].id, points[i].id});
							}
						}
						
					}
				}
				else values[location] = false;
			}
		Joinbase.candidate.candi2Ins[1] = tempCandi2Ins;
	}
	
	/**
	 * 根据用户提供的节点信息以及距离阈值初始化距离矩阵，并初始化长度为2的候选集
	 * @param Points 含有所有结点的TreeSet
	 * @param threshold 用户提供的距离阈值
	 */
	public Distance(Grids grids, double threshold) {
		
		//根据用户提供的节点信息以及距离阈值更新
		size = grids.pointNum;
		this.threshold = threshold;			
		double thres = threshold * threshold;
		values = new boolean[size * ( size + 1 ) / 2];
		Arrays.fill(values, false);
		
		//长度为2的候选集	
		HashMap<CoFeatures,ArrayList<Integer[]>> tempMap = 
				new HashMap<CoFeatures,ArrayList<Integer[]>>();
		
		//长度为2的粗糙集
		HashMap<CoFeatures,HashSet<CoarseIns>> tempCandi2Cos = 
				new HashMap<CoFeatures,HashSet<CoarseIns>>();
		
		int xsize = grids.getColNum();
		int ysize = grids.getRowNum();
				
		for(int x = 0; x < xsize - 1; x++){
			distanceWithGrid(grids.getGrid(x, 0), 
								grids.getGrid(x, 0), 
									thres, tempMap, tempCandi2Cos);
			distanceWithGrid(grids.getGrid(x, 0), 
								grids.getGrid(x + 1, 0), 
									thres, tempMap, tempCandi2Cos);
			distanceWithGrid(grids.getGrid(x, 0), 
								grids.getGrid(x, 1), 
									thres, tempMap, tempCandi2Cos);
			distanceWithGrid(grids.getGrid(x, 0), 
								grids.getGrid(x + 1, 1), 
									thres, tempMap, tempCandi2Cos);
		}
		
		for(int x = 0; x < xsize - 1; x++){
			distanceWithGrid(grids.getGrid(x, ysize - 1), 
								grids.getGrid(x, ysize - 1), 
									thres, tempMap, tempCandi2Cos);
			distanceWithGrid(grids.getGrid(x, ysize - 1), 
								grids.getGrid(x + 1, ysize - 1), 
									thres, tempMap, tempCandi2Cos);
			distanceWithGrid(grids.getGrid(x, ysize - 1), 
								grids.getGrid(x + 1, ysize - 2), 
									thres, tempMap, tempCandi2Cos);
		}
		
		for(int y = 0; y < ysize - 1; y++){
			distanceWithGrid(grids.getGrid(xsize - 1, y), 
								grids.getGrid(xsize - 1, y), 
									thres, tempMap, tempCandi2Cos);
			distanceWithGrid(grids.getGrid(xsize - 1, y), 
								grids.getGrid(xsize - 1, y + 1), 
									thres, tempMap, tempCandi2Cos);
		}	
		
		for(int x = 0; x < xsize - 1; x++)
			for(int y = 1; y < ysize - 1; y++){
				distanceWithGrid(grids.getGrid(x, y), 
									grids.getGrid(x, y), 
										thres, tempMap, tempCandi2Cos);
				distanceWithGrid(grids.getGrid(x, y), 
									grids.getGrid(x + 1, y), 
										thres, tempMap, tempCandi2Cos);
				distanceWithGrid(grids.getGrid(x, y), 
									grids.getGrid(x, y + 1), 
										thres, tempMap, tempCandi2Cos);
				distanceWithGrid(grids.getGrid(x, y), 
									grids.getGrid(x + 1, y + 1), 
										thres, tempMap, tempCandi2Cos);
				distanceWithGrid(grids.getGrid(x, y), 
									grids.getGrid(x + 1, y - 1), 
										thres, tempMap, tempCandi2Cos);
		}
		//不要忘了右上角的网格还要和自己求一次
		distanceWithGrid(grids.getGrid(xsize-1, ysize-1), 
							grids.getGrid(xsize-1, ysize-1),
								thres, tempMap, tempCandi2Cos);
		Joinbase.candidate.candi2Ins[1] = tempMap;
		Joinbase.coarse.candi2Coarse[1] = tempCandi2Cos;
	}
	
	void distanceWithGrid(Grid g1, Grid g2, double thres, 
							HashMap<CoFeatures,ArrayList<Integer[]>> tempMap,
							HashMap<CoFeatures,HashSet<CoarseIns>> tempCandi2Cos) {
		
		int location = 0;//当前结果在一维数组中实际存储的位置
		//填充候选集的临时变量
		CoFeatures feature;
		ArrayList<Integer[]> list;
		
		//填充粗糙集的临时变量
		CoarseIns cosIns;
		HashSet<CoarseIns> cosSet;
		
		
		if(g1.equals(g2)) {
			Point[] points = g1.getAllPoints().toArray(new Point[0]);
			for(int i = 0; i < points.length; i++) 
				for(int j = i; j < points.length; j++){
					if(points[i].feature != points[j].feature){
						double dis = Math.pow(points[i].x - points[j].x, 2)
								 + Math.pow(points[i].y - points[j].y, 2);
						if(dis <= thres){
							location = locate(points[i].id, points[j].id);
							values[location] = true;
							feature = new CoFeatures(
									new Integer[]{points[i].feature,points[j].feature});
							//填充候选集
							if (tempMap.get(feature) == null){
								list = new ArrayList<Integer[]>();
								if(points[i].id < points[j].id){
									list.add(new Integer[]{points[i].id, points[j].id});
								} else {
									list.add(new Integer[]{points[j].id, points[i].id});
								}
								tempMap.put(feature, list);
							}
							else{
								list = tempMap.get(feature);
								if (points[i].id < points[j].id){
									list.add(new Integer[]{points[i].id, points[j].id});
								} else {
									list.add(new Integer[]{points[j].id, points[i].id});
								}
							}
							//填充粗糙集
							if (tempCandi2Cos.containsKey(feature)){
								cosSet = tempCandi2Cos.get(feature);
								cosIns = new CoarseIns();
								cosIns.add(g1);
								cosIns.add(g1);
								cosSet.add(cosIns);
							} else {
								cosSet = new HashSet<CoarseIns>();
								cosIns = new CoarseIns();
								cosIns.add(g1);
								cosIns.add(g1);
								cosSet.add(cosIns);
								tempCandi2Cos.put(feature, cosSet);
							}
						}
					}
			}
		} else {
			Point[] p1 = g1.getAllPoints().toArray(new Point[0]);
			Point[] p2 = g2.getAllPoints().toArray(new Point[0]);
			
			for(int i = 0; i < p1.length; i++) 
				for(int j = 0; j < p2.length; j++){
					if(p1[i].feature != p2[j].feature){
						double dis = Math.pow(p1[i].x - p2[j].x, 2)
								 + Math.pow(p1[i].y - p2[j].y, 2);
						if(dis <= thres){
							location = locate(p1[i].id, p2[j].id);
							values[location] = true;
							feature = new CoFeatures(
									new Integer[]{p1[i].feature,p2[j].feature});
							//填充候选集
							if (tempMap.get(feature) == null){
								list = new ArrayList<Integer[]>();
								if(p1[i].id < p2[j].id){
									list.add(new Integer[]{p1[i].id, p2[j].id});
								} else {
									list.add(new Integer[]{p2[j].id, p1[i].id});
								}
								tempMap.put(feature, list);
							}
							else{
								list = tempMap.get(feature);
								if (p1[i].id < p2[j].id){
									list.add(new Integer[]{p1[i].id, p2[j].id});
								} else {
									list.add(new Integer[]{p2[j].id, p1[i].id});
								}
							}
							//填充粗糙集
							if (tempCandi2Cos.containsKey(feature)){
								cosSet = tempCandi2Cos.get(feature);
								cosIns = new CoarseIns();
								if(p1[i].feature < p2[j].feature){
									cosIns.add(g1);
									cosIns.add(g2);
								} else {
									cosIns.add(g2);
									cosIns.add(g1);
								}
								cosSet.add(cosIns);
							} else {
								cosSet = new HashSet<CoarseIns>();
								cosIns = new CoarseIns();
								if(p1[i].feature < p2[j].feature){
									cosIns.add(g1);
									cosIns.add(g2);
								} else {
									cosIns.add(g2);
									cosIns.add(g1);
								}
								cosSet.add(cosIns);
								tempCandi2Cos.put(feature, cosSet);
							}
						}//end of if(dis < thres) 
					}
			}//end of for  
		}
	}
	/**
	 * 
	 * @param id1 结点1的ID
	 * @param id2 结点2的ID
	 * @return 返回这两个结点的距离关系在一维数组中实际存储的位置
	 */
	protected int locate(int id1, int id2) {
		int value = 0;
		if (id1 > id2){
			value = id1 * (id1 - 1) / 2 + id2 - 1;
		} else {
			value = id2 * (id2 - 1) / 2 + id1 - 1;
		}
		return value;
	}
	
	/**
	 * 
	 * @param id1 结点1的ID
	 * @param id2 结点2的ID
	 * @return 结点1与结点2在当前距离阈值下是否临近
	 */
	boolean getDistance(int id1, int id2) {
		boolean result = false;
		result = values[locate(id1,id2)];
		return result;
	}
	
	/**
	 * 显示整个距离矩阵
	 */
	public void showResult() {
		for (int i = 1; i <= size; i++)
			for (int j = i; j <= size; j++) {
				System.out.println("Point "+ i + " Point "+ j +": " 
								   + values[locate(i,j)]);
			}
	}
			
}
