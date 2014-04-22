package reference.apriori;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MyApp extends Frame
{
	public MyApp()
	{
		this.addWindowListener	(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				dispose();
				System.exit(0);
			}
		});
	}
	
	public void test()
	{
	 
		try{			
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		}catch(java.lang.ClassNotFoundException e){
			System.err.println(e.getMessage());
		}
		
		try{
			Connection con = DriverManager.getConnection("jdbc:sqlserver://127.0.0.1:1433;DatabaseName=BookMall","sa","zhichen1994");
			Statement stmt = con.createStatement();
		
			ResultSet rs = stmt.executeQuery("use BookMall select * from books");
			while(rs.next())
			{
				System.out.println(rs.getString(1)+rs.getString(1));
			}
		
		}catch(SQLException e){
			System.err.println(e.getMessage());
		}

	}
	
	public static void main(String args[])
	{
//		System.out.println("Starting App");
//		MyApp f = new MyApp();
//		f.setSize(100,100);
//		f.show();

		MyApp app = new MyApp() ;
		app.test() ;

/*
		String l = "jdbc:odbc:tutorial";
		try{			
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		}catch(java.lang.ClassNotFoundException e){
			System.err.println(e.getMessage());
		}
		
		try{
			Connection con = DriverManager.getConnection(l,"","");
			Statement stmt = con.createStatement();
		
			ResultSet rs = stmt.executeQuery("select time_id from sales_fact_1998");
			while(rs.next())
			{
				System.out.println(rs.getString(1));
			}
		
		}catch(SQLException e){
			System.err.println(e.getMessage());
		}
*/

		
	}
}