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
	
	//��ӽڵ�
	public void addPoint(Point p){
		if(fea2points.containsKey(p.feature)){
			fea2points.get(p.feature).add(p);
		} else {
			ArrayList<Point> temp = new ArrayList<Point>();
			temp.add(p);
			fea2points.put(new Integer(p.feature), temp);
		}
	}
	
	//����ĳһ����ֵ�����нڵ�
	public ArrayList<Point> getPointsByFeature(int feature){
		return fea2points.get(feature);
	}
	
	//�������нڵ�
	public ArrayList<Point> getAllPoints(){
		ArrayList<Point> all = new ArrayList<>();
		for(ArrayList<Point> temp : fea2points.values()) {
			all.addAll(temp);		
			}
		return all;
	}
	
	//���ظ������еĽڵ���Ŀ
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
	private int length;	//ÿ������ĳ���
	private int row;	// ������
	private int column;	// ������
	private int pointNum;	//�ڵ���Ŀ
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
	 * @param x �������½ǵĺ�����
	 * @param y �������½ǵ�������
	 * @return ���ض�Ӧ������Grid
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
