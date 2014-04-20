package basic;
import java.awt.*;
import javax.swing.*;

public class Main {
	public static void calc(int num,int avg,double dis){
		int feature[]=Randoms.createGaussian(num, avg, 2);
		int total=0;
		for(int i=0;i<feature.length;i++) total+=feature[i];
		Point[] points = new Point[total];
		int[][] temp = Randoms.createRand(total);
		
		for(int i = 0; i < total; i++){
			points[i] = new Point();
			points[i].x = temp[0][i];
			points[i].y = temp[1][i];
		}
		for(int i=0,k=0;i<feature.length;i++)
			for(int j=0;j<feature[i];j++,k++) 
				points[k].feature=i;
		double distance[][]=new double[total][total];
		for(int i=0;i<total;i++)
			for(int j=0;j<total;j++){
				if(points[i].feature!=points[j].feature){
					double dis2=Math.pow(points[i].x-points[j].x, 2)+Math.pow(points[i].y-points[j].y, 2);
					distance[i][j]=Math.sqrt(dis2);
				}
			}
		Myframe frame=new Myframe(points,distance,dis);
		frame.setSize(1000, 1000);
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.repaint();
		

				
	}
}

class Myframe extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public double distance[][];
	public double dis;
	private Point point[];
	Myframe(Point point[],double distance[][],double dis){
		this.distance=distance;
		this.dis=dis;
		this.point=point;
	}
	public void paint(Graphics g) {
		
		for(int i=0;i<point.length;i++){
			g.setColor(new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255)));
			g.fillOval(point[i].x-5, point[i].y-5, 10, 10);
		}
		g.setColor(Color.yellow);
		for(int i=0;i<distance.length;i++)
			for(int j=0;j<distance.length;j++)
				if(distance[i][j]<dis&&point[i].feature!=point[j].feature)
					g.drawLine(point[i].x, point[i].y, point[j].x, point[j].y);
	}
}