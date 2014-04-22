package joinbased.nogrid;
import java.util.*;
/**
 * 
 * @author Daniel
 * a class of point we will be used in the program
 */

public class Point implements Comparable<Object>{
	
	/**
	 * the number of the point's object
	 */
	public static int count;
	
	/**
	 * the id of point
	 * start from one
	 */
	public int id=0; 
	
	/**
	 * the x-coordinate of point
	 */
	public int x;
	/**
	 * the y-coordinate of point
	 */
	public int y;
	/**
	 * the feature of point and use the numbers as the feature temporarily
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
		if(this.x > temp.x) return 1;
		else 
			if(this.x < temp.x) return -1;
		else 
			if(this.y > temp.y) return 1;
		else 
			if(this.y < temp.y) return -1;
		else 
			return 0;		
	}
	/**
	 * ����һ��ڵ���Ŀ������̬�ֲ�������
	 * @param num ������Ŀ
	 * @param avg ÿ��������ƽ�������Ŀ������̬�ֲ���ƽ��ֵ
	 * @param sigma2 ��̬�ֲ��� ����
	 * @return ����ָ����Point����
	 */
	public static Point[] createPointArray(int num, int avg, int sigma2){
		
		int feature[]=Randoms.createGaussian(num, avg, sigma2);
		int total = 0;
		for(int i = 0; i < feature.length; i++) 
			total += feature[i];
		Point[] points = new Point[total];
		int[][] temp = Randoms.createRandomPoint(total);
		
		for(int i = 0; i < total; i++){
			points[i] = new Point();
			points[i].x = temp[0][i];
			points[i].y = temp[1][i];
			points[i].id = i+1;
		}
		for(int i = 0, k = 0; i < feature.length; i++)
			for(int j = 0; j < feature[i]; j++, k++) 
				points[k].feature = i+1;
		return points;
	}
	/**
	 * ����һ��ڵ���Ŀ������̬�ֲ�������
	 * @param num ������Ŀ
	 * @param avg ÿ��������ƽ�������Ŀ������̬�ֲ���ƽ��ֵ
	 * @param sigma2 ��̬�ֲ��� ����
	 * @return ����һ��TreeSet���������ɵ�Point
	 */
	public static TreeSet<Point> createPointSet(int num, int avg, int sigma2){
		Point[] point = createPointArray(num, avg, sigma2);
		TreeSet<Point> points = new TreeSet<Point>();
		for(Point temp:point)
			points.add(temp);
		return points;
	}
}
