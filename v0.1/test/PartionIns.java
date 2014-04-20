package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

class Point{
	int x;
	int y;
	public Point(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Point other = (Point) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}
	
}

public class PartionIns {
	public static void main(String[] argc){
		String[] str;
		String temp;
		BufferedReader in1,in2;
		HashSet<Point> set = new HashSet<Point>();
		try {
			in1 = new BufferedReader(
					new FileReader("ins2.txt"));
			while ((temp = in1.readLine()) != null) {
				str = temp.split("\\W+");
				System.out.println(str.length);
				for(int i = 1; i <= (str.length - 1) / 2; i++){
					Point tp = new Point(Integer.parseInt(str[i*2-1]),Integer.parseInt(str[i*2]));
					set.add(tp);
				}
			}
			in2 = new BufferedReader(
					new FileReader("ins1.txt"));
			while ((temp = in2.readLine()) != null) {
				str = temp.split("\\W+");
				System.out.println(str.length);
				for(int i = 1; i <= (str.length - 1) / 2; i++){
					Point tp = new Point(Integer.parseInt(str[i*2-1]),Integer.parseInt(str[i*2]));
					if(set.add(tp))
						System.out.println(tp);
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
