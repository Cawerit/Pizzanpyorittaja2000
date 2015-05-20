package app;

import java.sql.ResultSet;
import java.sql.SQLException;


public class App {
	
	public static final Yhteys yhteys = new Yhteys();

    public static void main(String[] args) {	
   
  	  	ResultSet res = yhteys.hae("Select * from asiakas");
  	  	try {
			while(res.next()){
				int id = res.getInt("id");
				String nimi = res.getString("nimi");
				System.out.println(id + "\t" + nimi);


			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
