package joinless.grid;

import java.util.*;

public class Neighbors {

	/**
	 * 不等长的二维数组，行为一种特征，列为属于该种特征的点
	 */
	//HashMap<Integer,TreeSet<Integer>>[][] matrix;
	@SuppressWarnings("unchecked")
	HashMap<Integer,TreeSet<Integer>>[] matrix = 
			new HashMap[Joinless.grids.pointNum];
	/**
	 * 每一位存储的是下标所代表的特征的实例的最小ID
	 */
	//int range[];

	/**
	 * 距离阈值
	 */
	double threshold;


	Neighbors(){

	}
	
	Neighbors(double threshold){
		this.threshold = threshold;
		for(int i = 0; i < matrix.length; i++) {
			matrix[i] = new HashMap<Integer, TreeSet<Integer>>();
		}
	}


	HashMap<Integer,TreeSet<Integer>> getNeighbors(Point p){
		return this.matrix[p.id-1];
	}

	void calculate(Grids grids){

		//根据用户提供的节点信息以及距离阈值更新
		double thres = threshold * threshold;

		//长度为2的候选集	
		HashMap<CoFeatures,HashSet<Ins>> tempMap = 
				new HashMap<CoFeatures,HashSet<Ins>>();

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
		Joinless.candidate.cliqueIns[1] = tempMap;
		Joinless.coarse.candi2Coarse[1] = tempCandi2Cos;
	}

	void distanceWithGrid(Grid g1, Grid g2, double thres, 
			HashMap<CoFeatures,HashSet<Ins>> tempMap,
			HashMap<CoFeatures,HashSet<CoarseIns>> tempCandi2Cos) {

		//填充候选集的临时变量
		CoFeatures feature;
		HashSet<Ins> set;

		//填充粗糙集的临时变量
		CoarseIns cosIns;
		HashSet<CoarseIns> cosSet;

		//填充邻居关系的临时变量
		HashMap<Integer,TreeSet<Integer>> neighbors;
		TreeSet<Integer> pointset;

		if(g1.equals(g2)) {
			Point[] points = g1.getAllPoints().toArray(new Point[0]);
			for(int i = 0; i < points.length; i++) 
				for(int j = i; j < points.length; j++){
					if(points[i].feature != points[j].feature){
						double dis = Math.pow(points[i].x - points[j].x, 2)
								+ Math.pow(points[i].y - points[j].y, 2);
						if(dis <= thres){
							//填充邻居关系
							if(points[i].id < points[j].id){
								neighbors = this.getNeighbors(points[i]);
								if(neighbors.containsKey(new Integer(points[j].feature))){
									pointset = neighbors.get(new Integer(points[j].feature));
									pointset.add(new Integer(points[j].id));
								} else {
									pointset = new TreeSet<Integer>();
									pointset.add(new Integer(points[j].id));
									neighbors.put(new Integer(points[j].feature), pointset);
								}
								feature = new CoFeatures(
										new Integer[]{points[i].feature,points[j].feature});
								//填充候选集
								if (tempMap.get(feature) == null){
									set = new HashSet<Ins>();
									set.add(new Ins(new Integer[]{points[i].id, points[j].id}));							
									tempMap.put(feature, set);
								}
								else{
									set = tempMap.get(feature);
									set.add(new Ins(new Integer[]{points[i].id, points[j].id}));
								}
							} else {
								neighbors = this.getNeighbors(points[j]);
								if(neighbors.containsKey(new Integer(points[i].feature))){
									pointset = neighbors.get(new Integer(points[i].feature));
									pointset.add(new Integer(points[i].id));
								} else {
									pointset = new TreeSet<Integer>();
									pointset.add(new Integer(points[i].id));
									neighbors.put(new Integer(points[i].feature), pointset);
								}
								
								feature = new CoFeatures(
										new Integer[]{points[j].feature, points[i].feature});
								//填充候选集
								if (tempMap.get(feature) == null){
									set = new HashSet<Ins>();
									set.add(new Ins(new Integer[]{points[j].id, points[i].id}));							
									tempMap.put(feature, set);
								}
								else{
									set = tempMap.get(feature);
									set.add(new Ins(new Integer[]{points[j].id, points[i].id}));
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
							
							
							if(p1[i].id < p2[j].id){
								//填充邻居关系
								neighbors = this.getNeighbors(p1[i]);
								if(neighbors.containsKey(new Integer(p2[j].feature))){
									pointset = neighbors.get(new Integer(p2[j].feature));
									pointset.add(new Integer(p2[j].id));
								} else {
									pointset = new TreeSet<Integer>();
									pointset.add(new Integer(p2[j].id));
									neighbors.put(new Integer(p2[j].feature), pointset);
								}
								
								feature = new CoFeatures(
										new Integer[]{p1[i].feature,p2[j].feature});
								//填充候选集
								if (tempMap.get(feature) == null){
									set = new HashSet<Ins>();
									set.add(new Ins(new Integer[]{p1[i].id, p2[j].id}));
									tempMap.put(feature, set);
								}
								else{
									set = tempMap.get(feature);
									set.add(new Ins(new Integer[]{p1[i].id, p2[j].id}));
								}
							} else {
								neighbors = this.getNeighbors(p2[j]);
								if(neighbors.containsKey(new Integer(p1[i].feature))){
									pointset = neighbors.get(new Integer(p1[i].feature));
									pointset.add(new Integer(p1[i].id));
								} else {
									pointset = new TreeSet<Integer>();
									pointset.add(new Integer(p1[i].id));
									neighbors.put(new Integer(p1[i].feature), pointset);
								}
								feature = new CoFeatures(
										new Integer[]{p2[j].feature, p1[i].feature});
								//填充候选集
								if (tempMap.get(feature) == null){
									set = new HashSet<Ins>();
									set.add(new Ins(new Integer[]{p2[j].id, p1[i].id}));
									tempMap.put(feature, set);
								}
								else{
									set = tempMap.get(feature);
									set.add(new Ins(new Integer[]{p2[j].id, p1[i].id}));
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
	 * @param cf 候选特征组合
	 * @return 指定特征组合的星型实例
	 */
	HashSet<Ins> getStarIns(CoFeatures cf){
		HashSet<Ins> starIns = new HashSet<Ins>();		
		Integer[] cofeatures = cf.value.toArray(new Integer[0]);
		Integer centerfeature = cofeatures[0];
		ArrayList<Integer> centerpoints = Joinless.feature2Point.get(centerfeature);
		/* 按照节点分别求笛卡尔积，每求完一个就转化为Ins加入到startIns中 */
		for(Integer centerpoint: centerpoints){
			int flag = 1;
			HashMap<Integer,TreeSet<Integer>> map = matrix[centerpoint - 1];

			ArrayList<TreeSet<Integer>> pre = new ArrayList<TreeSet<Integer>>();//之前的结果
			ArrayList<TreeSet<Integer>> result = new ArrayList<TreeSet<Integer>>();//一次乘法运算后的结果
			TreeSet<Integer> term;//下一个要乘的集合
			for(int j = 1; j < cofeatures.length; j++){
				result = new ArrayList<TreeSet<Integer>>();
				term = map.get(cofeatures[j]);
				if(term == null) {
					flag = 0;
					break;
				}
				if(j == 1){
					for(Integer inte : term){
						TreeSet<Integer> tempset = new TreeSet<Integer>();
						tempset.add(new Integer(centerpoint));
						tempset.add(new Integer(inte));
						result.add(tempset);
					}
				} else {
					for(TreeSet<Integer> set : pre){
						for(Integer inte : term){
							TreeSet<Integer> tempset = new TreeSet<Integer>(set);
							tempset.add(new Integer(inte));
							result.add(tempset);
						}
					}
				}
				pre = result;
			}
			if(flag == 0) {
				continue;
			}
			for(TreeSet<Integer> set : result){
				Ins ins = new Ins(set);
				starIns.add(ins);
			}
		}
		return starIns;
	}

	/**
	 * 
	 * @param cf 候选特征组合
	 * @param preCliqueIns 之前一次的所有团型实例
	 * @return 指定特征组合的星型实例
	 */
	ArrayList<Ins> getStarIns(CoFeatures cf, HashMap<CoFeatures,HashSet<Ins>> preCliqueIns){
		ArrayList<Ins> starIns = new ArrayList<Ins>();
		Integer[] cofeatures = cf.value.toArray(new Integer[0]);
		CoFeatures prefix = new CoFeatures(cofeatures, cofeatures.length - 1);
		Integer centerfeature = cofeatures[0];
		Integer lastFeature = cofeatures[cofeatures.length - 1];
		/* 按照节点分别求笛卡尔积，每求完一个就转化为Ins加入到startIns中 */
		if (cofeatures.length >= 3) {
			HashSet<Ins> pre = preCliqueIns.get(prefix);
			for (Ins ins: pre) {
				Integer centerpoint = ins.value.get(0);
				TreeSet<Integer> appendList = 
						this.matrix[centerpoint - 1].get(lastFeature);
				if(appendList == null)
					continue;
				for(Integer append : appendList) {
					Ins tmp = new Ins(ins, append);
					starIns.add(tmp);
				}
					
			}
		}
		else {
			ArrayList<Integer> centerpoints = Joinless.feature2Point.get(centerfeature);
			for(Integer centerpoint : centerpoints){
				int flag = 1;

				HashMap<Integer,TreeSet<Integer>> map = matrix[centerpoint-1];

				ArrayList<TreeSet<Integer>> pre = new ArrayList<TreeSet<Integer>>();//之前的结果
				ArrayList<TreeSet<Integer>> result = new ArrayList<TreeSet<Integer>>();//一次乘法运算后的结果
				TreeSet<Integer> term;//下一个要乘的集合
				for(int j = 1; j < cofeatures.length; j++){
					result = new ArrayList<TreeSet<Integer>>();
					term = map.get(cofeatures[j]);
					if(term == null) {
						flag = 0;
						break;
					}
					if(j == 1){
						for(Integer inte : term){
							TreeSet<Integer> tempset = new TreeSet<Integer>();
							tempset.add(new Integer(centerpoint));
							tempset.add(new Integer(inte));
							result.add(tempset);
						}
					} else {
						for(TreeSet<Integer> set : pre){
							for(Integer inte : term){
								TreeSet<Integer> tempset = new TreeSet<Integer>(set);
								tempset.add(new Integer(inte));
								result.add(tempset);
							}
						}
					}
					pre = result;
				}
				if(flag == 0) {
					continue;
				}
				for(TreeSet<Integer> set : result){
					Ins ins = new Ins(set);
					starIns.add(ins);
				}
			}
		}
		return starIns;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < matrix.length; i++){

			str.append("pointID"+(i + 1)+":\n");
			for(Integer f : matrix[i].keySet()){
				str.append("feature "+f+" ");
				str.append(matrix[i].get(f));
				str.append(" ");
			}
			str.append("\n");

		}
		return str.toString();
	}


}
