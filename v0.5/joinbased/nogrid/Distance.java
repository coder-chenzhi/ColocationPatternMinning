package joinbased.nogrid;
import java.util.*;

public class Distance {
	
	/**
	 * ���н�������
	 */
	int size;
	
	/**
	 * ʵ�ʴ洢�����һά����
	 */
	boolean values[] ;
	
	/**
	 * ������ֵ
	 */
	double threshold;
	
	/**
	 * �����û��ṩ�Ľڵ���Ϣ�Լ�������ֵ��ʼ���������
	 * @param Points �������н���TreeSet
	 * @param threshold �û��ṩ�ľ�����ֵ
	 */
	public void DistancePrimary(TreeSet<Point> Points, double threshold) {
		size = Points.size();
		int location = 0;//��ǰ�����һά������ʵ�ʴ洢��λ��
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
	 * �����û��ṩ�Ľڵ���Ϣ�Լ�������ֵ��ʼ��������󣬲���ʼ������Ϊ2�ĺ�ѡ��
	 * @param Points �������н���TreeSet
	 * @param threshold �û��ṩ�ľ�����ֵ
	 */
	Distance(TreeSet<Point> Points, double threshold) {
		
		//�����û��ṩ�Ľڵ���Ϣ�Լ�������ֵ����
		size = Points.size();
		this.threshold = threshold;
		
		//�������������ʱ����
		int location = 0;//��ǰ�����һά������ʵ�ʴ洢��λ��		
		double thres = threshold * threshold;
		values = new boolean[size * ( size + 1 ) / 2];
		Point[] points = Points.toArray(new Point[0]);
		
		//����ѡ������ʱ����
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
	 * @param id1 ���1��ID
	 * @param id2 ���2��ID
	 * @return �������������ľ����ϵ��һά������ʵ�ʴ洢��λ��
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
	 * @param id1 ���1��ID
	 * @param id2 ���2��ID
	 * @return ���1����2�ڵ�ǰ������ֵ���Ƿ��ٽ�
	 */
	boolean getDistance(int id1, int id2) {
		boolean result = false;
		result = values[locate(id1,id2)];
		return result;
	}
	
	/**
	 * ��ʾ�����������
	 */
	public void showResult() {
		for (int i = 1; i <= size; i++)
			for (int j = i; j <= size; j++) {
				System.out.println("Point "+ i + " Point "+ j +": " 
								   + values[locate(i,j)]);
			}
	}
			
}
