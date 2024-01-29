package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {

	private static Connection conn = null;
	
	public static Connection getConnection() {
		if(conn == null) {
			try {
			Properties props = loadProperties();
			String url= props.getProperty("dburl");
			conn = DriverManager.getConnection(url, props);
			}
			catch(SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
		return conn;
	}
	private static Properties loadProperties() {
		try(FileInputStream fi = new FileInputStream("db.properties")){
			Properties pt = new Properties();
			pt.load(fi);
			return pt;
		}
		catch(IOException e) {
			throw new DbException(e.getMessage());
		}
	}
	public static void closeConnection() {
	
		if(conn != null) {
			try {
			conn.close();
		}
			catch(SQLException e) {
				throw new DbException(e.getMessage());
			}	
		}
	}
	public static void closeStatement(Statement st) {
		if(st != null) {
			try {
			st.close();
			}
			catch(SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	public static void closeResultSet(ResultSet set) {
		if(set!=null) {
			try {
				set.close();
			}
			catch(SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
}
