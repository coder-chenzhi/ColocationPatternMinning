package test;

import java.util.ArrayList;
import java.util.HashMap;

import join_based.grid.Point;

public class TestGrid {
	public int x;
	public int y;
	public HashMap<Integer,ArrayList<Integer>> fea2points = 
					new HashMap<Integer,ArrayList<Integer>>();
	public TestGrid() { }
	public TestGrid(int x, int y) {
		this.x = x;
		this.y = y;
		}
	
	//添加节点
	public void addPoint(Point p){
		if(fea2points.containsKey(p.feature)){
			fea2points.get(p.feature).add(p.id);
		} else {
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.add(p.id);
			fea2points.put(new Integer(p.feature), temp);
		}
	}
	
	//符合某一特征值的所有节点
	public ArrayList<Integer> getPointsByFeature(int feature){
		return fea2points.get(feature);
	}
	
	//返回所有节点
	public ArrayList<Integer> getAllPoints(){
		ArrayList<Integer> all = new ArrayList<>();
		for(ArrayList<Integer> temp : fea2points.values()) {
			all.addAll(temp);		
			}
		return all;
	}
	
	//返回该网格中的节点数目
	public int getNum(){
		int num = 0;
		for(ArrayList<Integer> temp : fea2points.values()) {
			num += temp.size();		
			}
		return num;
	}
	
	public String toString(){
		return "Grid("+this.x+","+this.y+")";
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 37;
		int result = 17;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestGrid other = (TestGrid) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	public static void main(String[] argc) {
			TestGrid g1 = new TestGrid(1,1);
			TestGrid g2 = new TestGrid(1,1);
			System.out.println(g1.equals(g2));
		
	}
}