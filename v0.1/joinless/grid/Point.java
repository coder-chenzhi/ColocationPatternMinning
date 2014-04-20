package joinless.grid;
import java.util.*;
/**
 * 
 * @author Daniel
 * a class of point we will be used in the program
 */

public class Point implements Comparable<Object>{
	
	
	/**
	 * the id of point
	 * start from one
	 */
	public int id=0; 
	
	/**
	 * x坐标
	 */
	public int x;
	/**
	 * y坐标
	 */
	public int y;
	/**
	 * 节点的特征值
	 */
	public int feature;
	
	/**
	 * the initialization of point, all the fields are initialized to -1 
	 * strat from 1
	 */
	public Point(){
		this.x = -1;
		this.y = -1;
		this.feature = -1;
		id = 0;
		
	}
	
	
			
	public Point(int x, int y, int feature) {
		super();
		this.x = x;
		this.y = y;
		this.feature = feature;
	}


	/**
	 * just for test and display the coordinate of the point
	 */
	public void print(){
		System.out.println("(" + this.x + "," + this.y + ")");
	}
	
	
	/**
	 * overrides the toString() inherited from Object
	 */
	public  String toString(){
		return "[" + this.id +"," + this.feature + "," + 
					"(" + this.x + "," + this.y + ")" + "]";
	}
	
	/**
	 * overrides the equals() inherited from Object
	 */
	public boolean equals(Object obj){
		if(this.x == ((Point)obj).x && this.y == ((Point)obj).y 
				&& this.feature == ((Point)obj).feature)
			return true;
		else return false;
	}
	
	/**
	 * sorted by coordinates when was inserted in a collection
	 * firstly compared with x-coordinate and then compared with y-coordinate
	 * sorted by ascending counts  
	 */
	public int compareTo(Object obj){
		Point temp=(Point)obj;
		if(this.feature > temp.feature) return 1;
		else 
			if(this.feature < temp.feature) return -1;
		else 
			if(this.x > temp.x) return 1;
		else 
			if(this.x < temp.x) return -1;
		else 
			if(this.y > temp.y) return 1;
		else 
			if(this.y < temp.y) return -1;
			return 0;		
	}

	/**
	 * 生成一组节点数目服从正态分布的数据
	 * @param num 特征数目
	 * @param avg 每个特征的平均结点数目，即正态分布的平均值
	 * @param sigma2 正态分布的 方差
	 * @return 返回一个TreeSet，包含生成的Point
	 */
	public static TreeSet<Point> createPointSet(int num, int avg, int sigma2){
		Random rt = new Random();
		TreeSet<Point> points = new TreeSet<Point>(
					new Comparator<Point>(){
						public int compare(Point a, Point b){
							return (a.id>b.id?1:(a.id==b.id?0:-1));
						}
					}
				);
		int feature[]=Randoms.createGaussian(num, avg, sigma2);
		int total = 0;
		for(int i = 0; i < feature.length; i++) 
			total += feature[i];
				
		for(int i = 0; i < total; i++){
			Point p = new Point();
			p.x = Math.abs(rt.nextInt()%10000);//横坐标范围(0,10000)
			p.y = Math.abs(rt.nextInt()%10000);//纵坐标范围(0,10000)
			p.id = i+1;
			if(!points.add(p)) {
				i--;//如果当前坐标的点已经存在,则减少计数
				//System.out.println(false);
			}
		}
		Iterator<Point> it = points.iterator();
		for(int i = 0; i < feature.length; i++)
			for(int j = 0; j < feature[i]; j++) {
				it.next().feature = i+1;				
			}
		return points;
	}
	
	/**
	 * 
	 * @param fnum 特征的数目
	 * @param pnum 总共的点数
	 * @param fd 数据空间的边长
	 * @param d 每个网格的边长
	 * @return
	 */
	public static HashMap<Integer, TreeSet<Point>> createPointSet(int fnum, int pnum, int fd, int d){
		Random rt = new Random();
		HashMap<Integer, TreeSet<Point>> map = new HashMap<>();
		int size = fd/d;
		int num = pnum/(size*size);//每个网格的节点数
		
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				for(int k = 0; k < num; k++){
					Integer tempf = Math.abs(rt.nextInt())%fnum+1;
					Point p = new Point();
					p.feature = tempf;
					p.x = Math.abs(rt.nextInt())%d+i*d;
					p.y = Math.abs(rt.nextInt())%d+j*d;
					if(map.containsKey(tempf)){
						if(!map.get(tempf).add(p)){
							k--;
						}
					} else {
						TreeSet<Point> set = new TreeSet<>();
						set.add(p);
						map.put(tempf, set);
					}
				}
			}
		}
		
		return map;
	}
}

