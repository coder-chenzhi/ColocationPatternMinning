package joinbase.withgrid;
import java.util.*;

class Grid {
	public int x;
	public int y;
	public HashMap<Integer,ArrayList<Point>> fea2points = 
					new HashMap<Integer,ArrayList<Point>>();
	public Grid() { }
	public Grid(int x, int y) {
		this.x = x;
		this.y = y;
		}
	
	//添加节点
	public void addPoint(Point p){
		if(fea2points.containsKey(p.feature)){
			fea2points.get(p.feature).add(p);
		} else {
			ArrayList<Point> temp = new ArrayList<Point>();
			temp.add(p);
			fea2points.put(new Integer(p.feature), temp);
		}
	}
	
	//符合某一特征值的所有节点
	public ArrayList<Point> getPointsByFeature(int feature){
		return fea2points.get(feature);
	}
	
	//返回所有节点
	public ArrayList<Point> getAllPoints(){
		ArrayList<Point> all = new ArrayList<>();
		for(ArrayList<Point> temp : fea2points.values()) {
			all.addAll(temp);		
			}
		return all;
	}
	
	//返回该网格中的节点数目
	public int getNum(){
		int num = 0;
		for(ArrayList<Point> temp : fea2points.values()) {
			num += temp.size();		
			}
		return num;
	}
	
	public String toString(){
		return "G("+this.x+","+this.y+")";
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
		Grid other = (Grid) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	public static void main(String[] argc) {
		int length = 4;
		int width = 4;
		Grid[][] graph = new Grid[width][length];
		for(int i = 0; i < width; i++){
			graph[i] = new Grid[length];
			for(int j = 0; j < length; j++){
				graph[i][j] = new Grid(i,j);
				System.out.println(graph[i][j]);
			}
		}
	}
}



public class Grids{
	private int length;	//每个网格的长度
	private int row;	// 多少行
	private int column;	// 多少列
	private int pointNum;	//节点数目
	public Grid[][] graph;
	public Grids() { }
	public Grids(int length, int row, int column){
		this.length = length;
		this.row = row;
		this.column = column;
		graph = new Grid[row][column];
		for(int i = 0; i < row; i++){
			graph[i] = new Grid[column];
			for(int j = 0; j < column; j++){
				graph[i][j] = new Grid(j,i);
			}
		}
	}
	/**
	 * 
	 * @param x 网格左下角的横坐标
	 * @param y 网格左下角的纵坐标
	 * @return 返回对应的网格Grid
	 */
	public Grid getGrid(int x, int y){
		return graph[y][x];
	}
	
	public int getLength(){
		return this.length;
	}
	
	public int getRowNum(){
		return this.row;
	}
	
	public int getColNum(){
		return this.column;
	}
	
	public int getPointNum() {
		return this.pointNum;
	}
	
	public void setPointNum(int pointNum) {
		this.pointNum = pointNum;
	}
	
	public static void main(String[] argc){
		
		
	}
}
