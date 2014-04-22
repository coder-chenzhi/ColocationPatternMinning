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
		JFrame frame= new JFrame("ʵ������");
		frame.setLocation(300, 150); 
		frame.setSize(500,500);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
//		Container c=frame.getContentPane();
//		c.setBackground(java.awt.Color.pink);
		MenuBar mb = new MenuBar (); //�˵���
//		mb.setBackground(java.awt.Color.pink);
		frame.setLayout(null);

		final JButton button1=new JButton("��������");
		final JButton button2=new JButton("����");
		Menu fileMenu1 = new Menu("���� ");
		Menu fileMenu2 = new Menu("��ѯ ");
		Menu fileMenu3 = new Menu("���� ");
		MenuItem menuItem1 = new MenuItem("����... ");
		
		
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
		dtm.setColumnIdentifiers(new String[] {"<html><font size=4 >���н��</font><html>"});

		fileMenu1.add(menuItem1);
		fileMenu1.add("���ļ�...");
		fileMenu1.add("���ݷ���...");
		fileMenu1.add("����...");
		frame.setMenuBar(mb);
		//mb.setVisible(true);
		
//		�¼�����
		button1.addActionListener(new ActionListener(){//�������õļ���
			public void actionPerformed(ActionEvent e) {
				final JFrame frame= new JFrame("��������");
				frame.setSize(270, 240);
				frame.setLocation(380, 200); 
				frame.setResizable(true);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setVisible(true);		
				Container c=frame.getContentPane();
				c.setBackground(java.awt.Color.pink);
				JLabel label1=new JLabel("<html><i><font size=4 >�� ��</font></i><html>");
				JLabel label2=new JLabel("<html><i><font size=4 >���ж�</font></i><html>");
				JLabel label3=new JLabel("<html><i><font size=4 >���Ŷ�</font></i><html>");
				final JTextField textfield1=new JTextField();
				final JTextField textfield2=new JTextField();
				final JTextField textfield3=new JTextField();
				if(dis!=0.0)textfield1.setText(String.valueOf(dis));
				if(pre!=0.0)textfield2.setText(String.valueOf(pre));
				if(conf!=0.0)textfield3.setText(String.valueOf(conf));
				final JButton button=new JButton("<html><i><font size=3 >ȷ��</font></i><html>");
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
				
				button.addActionListener(new ActionListener(){//�������õ�ȷ������
					public void actionPerformed(ActionEvent e) {
						dis =Double.parseDouble(textfield1.getText());
						pre =Double.parseDouble(textfield2.getText());
						conf =Double.parseDouble(textfield3.getText());
						frame.setVisible(false);
					}
				});
				
			}
		});
		menuItem1.addActionListener(new ActionListener(){//���ɵļ���
			public void actionPerformed(ActionEvent e) {
				final JFrame frame= new JFrame("��������");
				frame.setSize(300, 190);
				frame.setLocation(380, 200); 
				frame.setResizable(true);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setVisible(true);		
				Container c=frame.getContentPane();
				c.setBackground(java.awt.Color.pink);
				JLabel label1=new JLabel("<html><i><font size=4 >������Ŀ</font></i><html>");
				JLabel label2=new JLabel("<html><i><font size=4 >ƽ��ʵ����Ŀ</font></i><html>");
				final JTextField textfield1=new JTextField();
				final JTextField textfield2=new JTextField();
				final JButton button1=new JButton("<html><i><font size=3 >ȷ��</font></i><html>");
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
				button1.addActionListener(new ActionListener(){//���ɵ�ȷ������
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
		
		button2.addActionListener(new ActionListener(){//���еļ���
			public void actionPerformed(ActionEvent e) {
				if(dis==0||pre==0||conf==0){
					final JFrame frame= new JFrame("����");
					frame.setSize(250, 120);
					frame.setLocation(380, 200); 
					frame.setResizable(true);
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					frame.setVisible(true);		
					JLabel label1=new JLabel("��ز���δ�������ã��������ò�����");
					final JButton button1=new JButton("<html><i><font size=3 >ȷ��</font></i><html>");
					frame.setLayout(null);
					frame.add(label1);
					frame.add(button1);
					label1.setBounds(0, 0, 250, 50);
					button1.setBounds(60, 50, 100, 25);
					button1.addActionListener(new ActionListener(){//�������õ�ȷ������
						public void actionPerformed(ActionEvent e) {
							frame.setVisible(false);
						}
					});
				}
				if(num==0||avg==0){
					final JFrame frame= new JFrame("����");
					frame.setSize(250, 120);
					frame.setLocation(380, 200); 
					frame.setResizable(true);
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					frame.setVisible(true);		
					JLabel label1=new JLabel("δ����ʵ����������������ʵ��������");
					final JButton button1=new JButton("<html><i><font size=3 >ȷ��</font></i><html>");
					frame.setLayout(null);
					frame.add(label1);
					frame.add(button1);
					label1.setBounds(0, 0, 250, 50);
					button1.setBounds(60, 50, 100, 25);
					button1.addActionListener(new ActionListener(){//�������õ�ȷ������
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