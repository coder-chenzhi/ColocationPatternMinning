package joinbase.withgrid;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBaseIO {

	private Connection conn=null;
	private PreparedStatement ps=null;
	private ResultSet rs=null;
	
	public void close(){
		
		try {
			if (rs!=null) {
				
				rs.close();
				rs=null;
			}
			if (ps!=null) {
				
				ps.close();
				ps=null;
			}
			if (conn!=null) {
				
				conn.close();
				conn=null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Connection getConnetion() {
		// 连接数据库
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection("jdbc:sqlserver://127.0.0.1:1433;DatabaseName=CoVegetation","sa","zhichen1994");
					
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.print("连接数据库失败！");
			System.exit(0);
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		System.out.println("连接数据库成功");
		return conn;
	}
	
	public void executeSqlWithoutResult(String sql, Object[] params) {
		try {
			if (conn == null)
				getConnetion();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			// 组装SQL Server语句
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}
			// 执行SQL Server语句
			pstmt.execute();			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			
			this.close();
			//关闭数据库连接
		}
	}
	
	public ResultSet executeSqlWithResult(String sql, Object[] params) {
		//有返回值的SQL Server语句必须在调用该方法的程序获得返回结果以后再关闭数据库连接，
		//否则ResultSet中的内容就会被清空
		ResultSet rs = null;
		try {
			if (conn == null)
				getConnetion();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			// 组装SQL Server语句
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}
			// 执行SQL Server语句
			rs = pstmt.executeQuery();
			// conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
}

