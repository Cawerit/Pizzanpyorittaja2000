package app;

import java.sql.*;

public class App {

<<<<<<< HEAD
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


=======
    public static void main(String[] args) {
    	
    	//final String userName = "root";
    	//final String password = "tietue";
    	
    	
    	
    	try{
    		Class.forName("com.mysql.jdbc.Driver");
    		System.out.println("Toimii");
    	} catch(Exception e){
    		System.out.println("Luokkaa ei löydy");
    	}
>>>>>>> origin/master
    	
    }
}
