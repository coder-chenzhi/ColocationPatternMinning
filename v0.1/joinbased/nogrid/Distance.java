package joinbased.nogrid;
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
		Features feature;
		ArrayList<Integer[]> list;
		HashMap<Features,ArrayList<Integer[]>> tempMap = 
				new HashMap<Features,ArrayList<Integer[]>>();
		
		for (int i = 0; i < size; i++)
			for (int j = i; j < size; j++){
				location = locate(points[i].id, points[j].id);
				if (points[i].feature != points[j].feature){					
					double dis = Math.pow(points[i].x - points[j].x, 2)
								 + Math.pow(points[i].y - points[j].y, 2);
					if(dis > thres){
						try {
							values[location] = false;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println(points[i]);
							System.out.println(points[j]);
							System.out.println("size=" + size);
							System.out.println("location=" + location);
						}
					} else { 
						values[location] = true;
						feature = new Features(
								new Integer[]{points[i].feature,points[j].feature});
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
					}
				}
				else values[location] = false;
			}
		Join_base.candidate.candi2Instan[1] = tempMap;
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
