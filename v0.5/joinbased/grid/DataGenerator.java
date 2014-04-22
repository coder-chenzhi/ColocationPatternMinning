package joinbased.grid;

import java.io.IOException;
import java.util.Comparator;
import java.util.Random;
import java.util.TreeSet;


public class DataGenerator {
	
	
	/**
	 * ����һ�������̬�ֲ��Ľڵ㣬��̬�ֲ�������Ϊ���ݿռ������
	 * @param width ���ݿռ���
	 * @param fnum ������Ŀ
	 * @param favg ÿ�������Ľ����Ŀ
	 * @param sigma2 ��̬�ֲ��ķ���
	 * @return ����һ��TreeSet���������ɵ�Point
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
					* Math.sqrt(sigma2) + width / 2)%width);//�����귶Χ(0, width)
			p.y = Math.abs((int)(rt.nextGaussian()
					* Math.sqrt(sigma2) + width / 2)%width);//�����귶Χ(0, width)
			p.id = i+1;
			p.feature = i / favg + 1;
			if(!points.add(p)) {
				i--;//�����ǰ����ĵ��Ѿ�����,����ټ���
				//System.out.println(false);
			}
		}
		
		return points;
	}
	
	
	/**
	 * ��������ֲ��Ľڵ�
	 * @param width ���ݿռ��С
	 * @param fnum ������Ŀ
	 * @param avg ÿ��������ƽ�������Ŀ
	 * @return ����һ��TreeSet���������ɵ�Point
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
			p.x = Math.abs(rt.nextInt()%width);//�����귶Χ(0,width)
			p.y = Math.abs(rt.nextInt()%width);//�����귶Χ(0,width)
			p.id = i+1;
			p.feature = i / avg + 1;
			if(!points.add(p)) {
				i--;//�����ǰ����ĵ��Ѿ�����,����ټ���
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
