package test;
import java.sql.*;

import joinbased.grid.*;
public class GetMetaData {
	public static void main(String[] argc){
		DataBaseIO db = new DataBaseIO();
		Object[] param = {};
		String sql = "use CoVegetation select * from dbo.Distribution";
		ResultSet rs = db.executeSqlWithResult(sql, param); 
		try {
			ResultSetMetaData rsmd = rs.getMetaData();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
