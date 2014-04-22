package basic;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class MainInterface  {
	public static void CreateInterface(){
		
		JFrame frame= new JFrame("hello");
		frame.setLocation(400, 300); 
		frame.setSize(300, 210);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setVisible(true);
		Container c=frame.getContentPane();
		c.setBackground(java.awt.Color.pink);
		JLabel label1=new JLabel("<html><i><font size=4 >特征数目</font></i></html>");
		JLabel label2=new JLabel("<html><i><font size=4 >平均实例数目</font></i></html>");
		JLabel label3=new JLabel("<html><i><font size=4 >距离阈值</font></i></html>");
		final JTextField textfield1=new JTextField();
		final JTextField textfield2=new JTextField();
		final JTextField textfield3=new JTextField();
		final JButton button1=new JButton("<html><i><font size=3 >确定</font></i></html>");
		button1.setBackground(java.awt.Color.orange);
		frame.setLayout(null);
		frame.add(label1);
		frame.add(label2);
		frame.add(label3);
		frame.add(textfield1);
		frame.add(textfield2);
		frame.add(textfield3);
		frame.add(button1);
		label1.setBounds(0, 0, 200, 50);
		label2.setBounds(0, 50, 200, 50);
		label3.setBounds(0, 100, 200, 50);
		textfield1.setBounds(100, 10, 150, 30);
		textfield2.setBounds(100, 60, 150, 30);
		textfield3.setBounds(100, 110, 150, 30);
		button1.setBounds(100, 150, 100, 25);
		button1.addActionListener(new ActionListener(){
			private int num;
			private int avg;
			private double dis;
			public void actionPerformed(ActionEvent e) {
				if(e.getSource()==button1)
					if(!textfield1.getText().equals("")&&!textfield2.getText().equals("")&&!textfield3.getText().equals("")){
				num =Integer.parseInt(textfield1.getText());
				avg =Integer.parseInt(textfield2.getText());
				dis=Double.parseDouble(textfield3.getText());
				Main.calc(num, avg, dis);
					}
				
			}
		});

		
		

		
	}
	
	public static void main(String []args){
		CreateInterface();
	}
}
