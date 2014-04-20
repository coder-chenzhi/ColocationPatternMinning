package joinbased.grid;
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
	 * x����
	 */
	public int x;
	/**
	 * y����
	 */
	public int y;
	/**
	 * �ڵ������ֵ
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
	 * ����һ��ڵ���Ŀ������̬�ֲ�������
	 * @param num ������Ŀ
	 * @param avg ÿ��������ƽ�������Ŀ������̬�ֲ���ƽ��ֵ
	 * @param sigma2 ��̬�ֲ��� ����
	 * @return ����һ��TreeSet���������ɵ�Point
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
			p.x = Math.abs(rt.nextInt()%1000);//�����귶Χ(0,1000)
			p.y = Math.abs(rt.nextInt()%1000);//�����귶Χ(0,1000)
			p.id = i+1;
			if(!points.add(p)) {
				i--;//�����ǰ����ĵ��Ѿ�����,����ټ���
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
}
