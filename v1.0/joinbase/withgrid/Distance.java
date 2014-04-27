package joinbase.withgrid;
import java.util.*;

public class Distance {

	/**
	 * ���н�������
	 */
	int size;

	/**
	 * �洢ÿ������ھ�
	 */
	HashSet<Integer> neighbors[];

	/**
	 * ������ֵ
	 */
	double threshold;

	/**
	 * �����û��ṩ�Ľڵ���Ϣ�Լ�������ֵ��ʼ���������
	 * @param Points �������н���TreeSet
	 * @param threshold �û��ṩ�ľ�����ֵ
	 */
	@SuppressWarnings("unchecked")
	public void DistancePrimary(TreeSet<Point> Points, double threshold) {
		size = Points.size();
		this.threshold = threshold;
		double thres = threshold * threshold;
		neighbors = new HashSet[size];
		for (int i = 0; i < size; i ++)
			neighbors[i] = new HashSet<Integer>();

		Point[] points = Points.toArray(new Point[0]);
		for (int i = 0; i < size; i++)
			for (int j = i; j < size; j++){
				if (points[i].feature != points[j].feature){					
					double dis = Math.pow(points[i].x - points[j].x, 2)
							+ Math.pow(points[i].y - points[j].y, 2);
					if (dis <= thres){
						if(points[i].id < points[j].id) {
							neighbors[points[i].id].add(points[j].id);
						} else {
							neighbors[points[j].id].add(points[i].id);
						}
					}
				}
			}
	}

	/**
	 * �����û��ṩ�Ľڵ���Ϣ�Լ�������ֵ��ʼ��������󣬲���ʼ������Ϊ2�ĺ�ѡ��
	 * @param Points �������н���TreeSet
	 * @param threshold �û��ṩ�ľ�����ֵ
	 */
	@SuppressWarnings("unchecked")
	public void calculate(TreeSet<Point> Points, Candidate candidate, double threshold) {

		//�����û��ṩ�Ľڵ���Ϣ�Լ�������ֵ����
		size = Points.size();
		this.threshold = threshold;

		//�������������ʱ����
		double thres = threshold * threshold;
		neighbors = new HashSet[size];
		Point[] points = Points.toArray(new Point[0]);

		//����ѡ������ʱ����
		Pattern feature;
		ArrayList<Integer[]> list1;
		HashMap<Pattern,ArrayList<Integer[]>> tempCandi2Ins = 
				new HashMap<Pattern,ArrayList<Integer[]>>();


		for (int i = 0; i < size; i++)
			for (int j = i; j < size; j++){
				if (points[i].feature != points[j].feature){					
					double dis = Math.pow(points[i].x - points[j].x, 2)
							+ Math.pow(points[i].y - points[j].y, 2);
					if(dis <= thres){
						if(points[i].id < points[j].id) {
							neighbors[points[i].id].add(points[j].id);
						} else {
							neighbors[points[j].id].add(points[i].id);
						}
						feature = new Pattern(
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
			}
		candidate.setCandidate(1, new HashSet<Pattern>(tempCandi2Ins.keySet()));
	}

	/**
	 * �����û��ṩ�Ľڵ���Ϣ�Լ�������ֵ��ʼ��������󣬲���ʼ������Ϊ2�ĺ�ѡ��
	 * @param Points �������н���TreeSet
	 * @param threshold �û��ṩ�ľ�����ֵ
	 */
	@SuppressWarnings("unchecked")
	public void calculate(BDBManager database, Grids grids, Candidate candidate, 
			Coarse coarse, ResizingIntArray featureInsSize, 
			double prevalence, int threshold) {

		//�����û��ṩ�Ľڵ���Ϣ�Լ�������ֵ����
		size = grids.getPointNum();
		this.threshold = threshold;			
		double thres = threshold * threshold;
		neighbors = new HashSet[size];
		for (int i = 0; i < size; i++)
			neighbors[i] = new HashSet<Integer>();
		
		//����Ϊ2�ĺ�ѡ��	
		HashMap<Pattern, Instance> tempMap = 
				new HashMap<Pattern, Instance>();

		//����Ϊ2�Ĵֲڼ�
		HashMap<Pattern,HashSet<CoarseIns>> tempCandi2Cos = 
				new HashMap<Pattern,HashSet<CoarseIns>>();

		int xsize = grids.getColNum();
		int ysize = grids.getRowNum();

		for(int x = 0; x < xsize; x++) {
			for(int y = 0; y < ysize; y++) {
				distanceWithGrid(grids.getGrid(x, y), 
						grids.getGrid(x, y), 
						thres, tempMap, tempCandi2Cos);
				if (x != xsize - 1) {
					distanceWithGrid(grids.getGrid(x, y), 
							grids.getGrid(x + 1, y), 
							thres, tempMap, tempCandi2Cos);
					if (y != 0) {
						distanceWithGrid(grids.getGrid(x, y), 
								grids.getGrid(x + 1, y - 1), 
								thres, tempMap, tempCandi2Cos);
					}
					if(y != ysize - 1) {
						distanceWithGrid(grids.getGrid(x, y), 
								grids.getGrid(x + 1, y + 1), 
								thres, tempMap, tempCandi2Cos);
					}
				}
				if(y != ysize - 1) {
					distanceWithGrid(grids.getGrid(x, y), 
							grids.getGrid(x, y + 1), 
							thres, tempMap, tempCandi2Cos);
				}
			}
		}
		//prevalence filter and store prevalent value into database
		Candidate.prevalentFiltration(database, tempMap, featureInsSize, prevalence);

		candidate.setCandidate(1, new HashSet<Pattern>(tempMap.keySet()));
		coarse.candi2Coarse[1] = tempCandi2Cos;
	}

	void distanceWithGrid(Grid g1, Grid g2, double thres, 
			HashMap<Pattern,Instance> tempMap,
			HashMap<Pattern,HashSet<CoarseIns>> tempCandi2Cos) {

		//����ѡ������ʱ����
		Pattern p;
		Instance ins;

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
							if(points[i].id < points[j].id) {
								neighbors[points[i].id].add(points[j].id);
							} else {
								neighbors[points[j].id].add(points[i].id);
							}
							p = new Pattern(
									new Integer[]{points[i].feature,points[j].feature});
							//����ѡ��
							if (tempMap.get(p) == null){
								ins = new Instance(p.length());
								if(points[i].id < points[j].id){
									ins.add(new int[]{points[i].id, points[j].id});
								} else {
									ins.add(new int[]{points[j].id, points[i].id});
								}
								tempMap.put(p, ins);
							}
							else{
								ins = tempMap.get(p);
								if (points[i].id < points[j].id){
									ins.add(new int[]{points[i].id, points[j].id});
								} else {
									ins.add(new int[]{points[j].id, points[i].id});
								}
							}
							//���ֲڼ�
							if (tempCandi2Cos.containsKey(p)){
								cosSet = tempCandi2Cos.get(p);
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
								tempCandi2Cos.put(p, cosSet);
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
							if(p1[i].id < p2[j].id) {
								neighbors[p1[i].id].add(p2[j].id);
							} else {
								neighbors[p2[j].id].add(p1[i].id);
							}
							p = new Pattern(
									new Integer[]{p1[i].feature,p2[j].feature});
							//����ѡ��
							if (tempMap.get(p) == null){
								ins = new Instance(p.length());
								if(p1[i].id < p2[j].id){
									ins.add(new int[]{p1[i].id, p2[j].id});
								} else {
									ins.add(new int[]{p2[j].id, p1[i].id});
								}
								tempMap.put(p, ins);
							}
							else{
								ins = tempMap.get(p);
								if (p1[i].id < p2[j].id){
									ins.add(new int[]{p1[i].id, p2[j].id});
								} else {
									ins.add(new int[]{p2[j].id, p1[i].id});
								}
							}
							//���ֲڼ�
							if (tempCandi2Cos.containsKey(p)){
								cosSet = tempCandi2Cos.get(p);
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
								tempCandi2Cos.put(p, cosSet);
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
	 * @return ���1����2�ڵ�ǰ������ֵ���Ƿ��ٽ�
	 */
	boolean isNeighbor(int id1, int id2) {
		boolean result = false;
		if (id1 < id2) {
			result = neighbors[id1].contains(id2);
		} else {
			result = neighbors[id2].contains(id1);
		}
		return result;
	}

	/**
	 * ��ʾ�����������
	 */
	public void showResult() {
		for (int i = 0; i <= size; i++) {
			HashSet<Integer> neighbor = neighbors[i];
			System.out.println("Point " + (i + 1) + "'s neighbor:");
			Iterator<Integer> it = neighbor.iterator();
			while (it.hasNext()) {
				System.out.print(it.next());
				if(it.hasNext()) {
					System.out.print(", ");
				}
			}
		}
	}

}
