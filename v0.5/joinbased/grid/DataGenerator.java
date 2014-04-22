package joinbased.grid;

import java.io.IOException;
import java.util.Comparator;
import java.util.Random;
import java.util.TreeSet;


public class DataGenerator {
	
	
	/**
	 * 生成一组服从正态分布的节点，正态分布的中心为数据空间的中心
	 * @param width 数据空间宽度
	 * @param fnum 特征数目
	 * @param favg 每个特征的结点数目
	 * @param sigma2 正态分布的方差
	 * @return 返回一个TreeSet，包含生成的Point
	 */
	public static TreeSet<Point> createGaussianPointSet(int width, int fnum, int favg, int sigma2){
		Random rt = new Random();
		TreeSet<Point> points = new TreeSet<Point>(
					new Comparator<Point>(){
						public int compare(Point a, Point b){
							return (a.id>b.id?1:(a.id==b.id?0:-1));
						}
					}
				);
		int total =  favg * fnum;
				
		for(int i = 0; i < total; i++){
			Point p = new Point();
			p.x = Math.abs((int)(rt.nextGaussian()
					* Math.sqrt(sigma2) + width / 2)%width);//横坐标范围(0, width)
			p.y = Math.abs((int)(rt.nextGaussian()
					* Math.sqrt(sigma2) + width / 2)%width);//纵坐标范围(0, width)
			p.id = i+1;
			p.feature = i / favg + 1;
			if(!points.add(p)) {
				i--;//如果当前坐标的点已经存在,则减少计数
				//System.out.println(false);
			}
		}
		
		return points;
	}
	
	
	/**
	 * 生成随机分布的节点
	 * @param width 数据空间大小
	 * @param fnum 特征数目
	 * @param avg 每个特征的平均结点数目
	 * @return 返回一个TreeSet，包含生成的Point
	 */
	public static TreeSet<Point> createRandomPointSet(int width, int fnum, int avg){
		Random rt = new Random();
		TreeSet<Point> points = new TreeSet<Point>(
					new Comparator<Point>(){
						public int compare(Point a, Point b){
							return (a.id>b.id?1:(a.id==b.id?0:-1));
						}
					}
				);
		int total = fnum * avg;
				
		for(int i = 0; i < total; i++){
			Point p = new Point();
			p.x = Math.abs(rt.nextInt()%width);//横坐标范围(0,width)
			p.y = Math.abs(rt.nextInt()%width);//纵坐标范围(0,width)
			p.id = i+1;
			p.feature = i / avg + 1;
			if(!points.add(p)) {
				i--;//如果当前坐标的点已经存在,则减少计数
				//System.out.println(false);
			}
		}
		
		return points;
	}
	
	public static void main(String[] args) {
		int width = 1000;
		int feature = 20;
		int instance = 500;
		int sigma2 = 32400;
		TreeSet<Point> points = createGaussianPointSet(width, feature, instance, sigma2);
		try {
			String filename = "data" + width + "_" +feature + "_" + instance + "Gaussian.txt";
			FileOpr.WriteFile(points,filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Point point:points)
			System.out.println(point);
	}
}
