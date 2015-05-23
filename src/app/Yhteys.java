package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class Yhteys {

	private static final String url = "jdbc:mysql://localhost:3306/";
	private static final String dbName = "pp2000";
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String userName = "root"; 
	private static final String password = "tietue";
	private Connection conn;
	
	public Yhteys(){
		try{
			Class.forName(driver).newInstance();
			this.conn = DriverManager.getConnection(url+dbName,userName,password);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public ResultSet hae(String kysely){
	
		try {
			Connection conn = this.conn;
			Statement st = conn.createStatement();
			ResultSet res = st.executeQuery(kysely);
			System.out.println("yolo");
			return res;
	  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean lisaa(String minne, String mita){
		try {
			Connection conn = this.conn;
			Statement st = conn.createStatement();
			st.executeUpdate("INSERT INTO " + minne + " VALUES (" + mita + ");");
			return true;
	  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}