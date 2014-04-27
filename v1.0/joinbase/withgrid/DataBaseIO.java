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
		// �������ݿ�
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection("jdbc:sqlserver://127.0.0.1:1433;DatabaseName=CoVegetation","sa","zhichen1994");
					
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.print("�������ݿ�ʧ�ܣ�");
			System.exit(0);
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		System.out.println("�������ݿ�ɹ�");
		return conn;
	}
	
	public void executeSqlWithoutResult(String sql, Object[] params) {
		try {
			if (conn == null)
				getConnetion();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			// ��װSQL Server���
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}
			// ִ��SQL Server���
			pstmt.execute();			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			
			this.close();
			//�ر����ݿ�����
		}
	}
	
	public ResultSet executeSqlWithResult(String sql, Object[] params) {
		//�з���ֵ��SQL Server�������ڵ��ø÷����ĳ����÷��ؽ���Ժ��ٹر����ݿ����ӣ�
		//����ResultSet�е����ݾͻᱻ���
		ResultSet rs = null;
		try {
			if (conn == null)
				getConnetion();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			// ��װSQL Server���
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}
			// ִ��SQL Server���
			rs = pstmt.executeQuery();
			// conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
}

