package joinbased.grid;
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
						//����ѡ��
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
	 * �����û��ṩ�Ľڵ���Ϣ�Լ�������ֵ��ʼ��������󣬲���ʼ������Ϊ2�ĺ�ѡ��
	 * @param Points �������н���TreeSet
	 * @param threshold �û��ṩ�ľ�����ֵ
	 */
	public Distance(Grids grids, double threshold) {
		
		//�����û��ṩ�Ľڵ���Ϣ�Լ�������ֵ����
		size = grids.pointNum;
		this.threshold = threshold;			
		double thres = threshold * threshold;
		values = new boolean[size * ( size + 1 ) / 2];
		Arrays.fill(values, false);
		
		//����Ϊ2�ĺ�ѡ��	
		HashMap<CoFeatures,ArrayList<Integer[]>> tempMap = 
				new HashMap<CoFeatures,ArrayList<Integer[]>>();
		
		//����Ϊ2�Ĵֲڼ�
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
		//��Ҫ�������Ͻǵ�����Ҫ���Լ���һ��
		distanceWithGrid(grids.getGrid(xsize-1, ysize-1), 
							grids.getGrid(xsize-1, ysize-1),
								thres, tempMap, tempCandi2Cos);
		Joinbase.candidate.candi2Ins[1] = tempMap;
		Joinbase.coarse.candi2Coarse[1] = tempCandi2Cos;
	}
	
	void distanceWithGrid(Grid g1, Grid g2, double thres, 
							HashMap<CoFeatures,ArrayList<Integer[]>> tempMap,
							HashMap<CoFeatures,HashSet<CoarseIns>> tempCandi2Cos) {
		
		int location = 0;//��ǰ�����һά������ʵ�ʴ洢��λ��
		//����ѡ������ʱ����
		CoFeatures feature;
		ArrayList<Integer[]> list;
		
		//���ֲڼ�����ʱ����
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
							//����ѡ��
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
							//���ֲڼ�
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
							//����ѡ��
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
							//���ֲڼ�
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
