package app;

import java.sql.*;

public class App {

    public static void main(String[] args) {	
   
    	
  	  String url = "jdbc:mysql://localhost:3306/";
	  String dbName = "pp2000";
	  String driver = "com.mysql.jdbc.Driver";
	  String userName = "root"; 
	  String password = "tietue";
	  try {
	  Class.forName(driver).newInstance();
	  Connection conn = DriverManager.getConnection(url+dbName,userName,password);
	  
	  Statement st = conn.createStatement();
	  ResultSet res = st.executeQuery("SELECT * FROM  asiakas");

	  while(res.next()){
		  int id = res.getInt("id");
		  String nimi = res.getString("nimi");
		  System.out.println(id + " " + nimi);
	  }		  
	  conn.close();
	  
	  } catch (Exception e) {
	  e.printStackTrace();
	  }


    	
    }
}
