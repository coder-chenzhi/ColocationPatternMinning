package joinbased.nogrid;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeSet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class App {
	static Join_base join;
	static double dis;
	static double pre;
	static double conf;
	static int num;
	static int avg;
	static String path;
	static DefaultTableModel dtm = new DefaultTableModel();
	public static void CreateInterface(){
		JFrame frame= new JFrame("实例测试");
		frame.setLocation(300, 150); 
		frame.setSize(500,500);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
//		Container c=frame.getContentPane();
//		c.setBackground(java.awt.Color.pink);
		MenuBar mb = new MenuBar (); //菜单栏
//		mb.setBackground(java.awt.Color.pink);
		frame.setLayout(null);

		final JButton button1=new JButton("参数设置");
		final JButton button2=new JButton("运行");
		Menu fileMenu1 = new Menu("数据 ");
		Menu fileMenu2 = new Menu("查询 ");
		Menu fileMenu3 = new Menu("帮助 ");
		MenuItem menuItem1 = new MenuItem("生成... ");
		
		
		JTable table = new JTable(dtm);
		JScrollPane jsp = new JScrollPane(table);
		dtm.setColumnIdentifiers(new String[]{"rules"});
		
		frame.add(button1);
		frame.add(button2);
		frame.add(jsp);
		mb.add(fileMenu1);
		mb.add(fileMenu2);
		mb.add(fileMenu3);

		//mb.setBounds(0,0,400,30);
		//mb.setSize(500,30);
		button1.setBounds(150, 50, 100, 25);
		button2.setBounds(150, 100, 100, 25);
		
		jsp.setBounds(0, 160, 500, 340);
		dtm.setColumnIdentifiers(new String[] {"<html><font size=4 >运行结果</font><html>"});

		fileMenu1.add(menuItem1);
		fileMenu1.add("从文件...");
		fileMenu1.add("数据分析...");
		fileMenu1.add("保存...");
		frame.setMenuBar(mb);
		//mb.setVisible(true);
		
//		事件监听
		button1.addActionListener(new ActionListener(){//参数设置的监听
			public void actionPerformed(ActionEvent e) {
				final JFrame frame= new JFrame("参数设置");
				frame.setSize(270, 240);
				frame.setLocation(380, 200); 
				frame.setResizable(true);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setVisible(true);		
				Container c=frame.getContentPane();
				c.setBackground(java.awt.Color.pink);
				JLabel label1=new JLabel("<html><i><font size=4 >距 离</font></i><html>");
				JLabel label2=new JLabel("<html><i><font size=4 >流行度</font></i><html>");
				JLabel label3=new JLabel("<html><i><font size=4 >置信度</font></i><html>");
				final JTextField textfield1=new JTextField();
				final JTextField textfield2=new JTextField();
				final JTextField textfield3=new JTextField();
				if(dis!=0.0)textfield1.setText(String.valueOf(dis));
				if(pre!=0.0)textfield2.setText(String.valueOf(pre));
				if(conf!=0.0)textfield3.setText(String.valueOf(conf));
				final JButton button=new JButton("<html><i><font size=3 >确定</font></i><html>");
				button.setBackground(java.awt.Color.orange);
				frame.setLayout(null);
				frame.add(label1);
				frame.add(label2);
				frame.add(label3);
				frame.add(textfield1);
				frame.add(textfield2);
				frame.add(textfield3);
				frame.add(button);
				label1.setBounds(0, 0, 200, 50);
				label2.setBounds(0, 50, 200, 50);
				label3.setBounds(0, 100, 200, 50);
				textfield1.setBounds(70, 10, 150, 30);
				textfield2.setBounds(70, 60, 150, 30);
				textfield3.setBounds(70, 110, 150, 30);
				button.setBounds(100, 160, 100, 25);
				
				button.addActionListener(new ActionListener(){//参数设置的确定监听
					public void actionPerformed(ActionEvent e) {
						dis =Double.parseDouble(textfield1.getText());
						pre =Double.parseDouble(textfield2.getText());
						conf =Double.parseDouble(textfield3.getText());
						frame.setVisible(false);
					}
				});
				
			}
		});
		menuItem1.addActionListener(new ActionListener(){//生成的监听
			public void actionPerformed(ActionEvent e) {
				final JFrame frame= new JFrame("生成属性");
				frame.setSize(300, 190);
				frame.setLocation(380, 200); 
				frame.setResizable(true);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setVisible(true);		
				Container c=frame.getContentPane();
				c.setBackground(java.awt.Color.pink);
				JLabel label1=new JLabel("<html><i><font size=4 >特征数目</font></i><html>");
				JLabel label2=new JLabel("<html><i><font size=4 >平均实例数目</font></i><html>");
				final JTextField textfield1=new JTextField();
				final JTextField textfield2=new JTextField();
				final JButton button1=new JButton("<html><i><font size=3 >确定</font></i><html>");
				button1.setBackground(java.awt.Color.orange);
				frame.setLayout(null);
				frame.add(label1);
				frame.add(label2);
				frame.add(textfield1);
				frame.add(textfield2);
				frame.add(button1);
				label1.setBounds(0, 0, 200, 50);
				label2.setBounds(0, 50, 200, 50);
				textfield1.setBounds(100, 10, 150, 30);
				textfield2.setBounds(100, 60, 150, 30);
				button1.setBounds(100, 110, 100, 25);
				if(num!=0.0)textfield1.setText(String.valueOf(num));
				if(avg!=0.0)textfield2.setText(String.valueOf(avg));
				button1.addActionListener(new ActionListener(){//生成的确定监听
					public void actionPerformed(ActionEvent e) {
						if(!textfield1.getText().equals("") && 
								!textfield2.getText().equals("")){
							num =Integer.parseInt(textfield1.getText());
							avg =Integer.parseInt(textfield2.getText());
								}
						path = "data"+num+"_"+avg+".txt";
						TreeSet<Point> points = Point.createPointSet(num,avg,2);
						try {
							FileOpr.WriteFile(points,path);
						} catch (IOException excep) {
							// TODO Auto-generated catch block
							excep.printStackTrace();
						}
						frame.setVisible(false);
					}
				});
				
			}
		});
		
		button2.addActionListener(new ActionListener(){//运行的监听
			public void actionPerformed(ActionEvent e) {
				if(dis==0||pre==0||conf==0){
					final JFrame frame= new JFrame("警告");
					frame.setSize(250, 120);
					frame.setLocation(380, 200); 
					frame.setResizable(true);
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					frame.setVisible(true);		
					JLabel label1=new JLabel("相关参数未进行设置，请先设置参数！");
					final JButton button1=new JButton("<html><i><font size=3 >确定</font></i><html>");
					frame.setLayout(null);
					frame.add(label1);
					frame.add(button1);
					label1.setBounds(0, 0, 250, 50);
					button1.setBounds(60, 50, 100, 25);
					button1.addActionListener(new ActionListener(){//参数设置的确定监听
						public void actionPerformed(ActionEvent e) {
							frame.setVisible(false);
						}
					});
				}
				if(num==0||avg==0){
					final JFrame frame= new JFrame("警告");
					frame.setSize(250, 120);
					frame.setLocation(380, 200); 
					frame.setResizable(true);
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					frame.setVisible(true);		
					JLabel label1=new JLabel("未设置实例个数，请先设置实例个数！");
					final JButton button1=new JButton("<html><i><font size=3 >确定</font></i><html>");
					frame.setLayout(null);
					frame.add(label1);
					frame.add(button1);
					label1.setBounds(0, 0, 250, 50);
					button1.setBounds(60, 50, 100, 25);
					button1.addActionListener(new ActionListener(){//参数设置的确定监听
						public void actionPerformed(ActionEvent e) {
							frame.setVisible(false);
						}
					});
				}
				else{
					Join_base.candidate = new Candidate();
					Join_base.point2Feature = new HashMap<Integer,Integer>();
					Join_base.rules = new Rules();
					try {						
						Join_base.setParameter(path, dis, pre, conf);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Join_base.function();
					while(dtm.getRowCount() != 0){						
						dtm.removeRow(0);
					}
					for(Rule ru : Join_base.rules.list){
						dtm.addRow(new String[]{ru.toString()});
					}
				}
			}
			
		});
//		
//		MenuItemListener listener = new MenuItemListener(this);
//		menuItem1.addActionListener(listener);

		
	}
	
	public static void main(String []args){
		CreateInterface();
	}
}