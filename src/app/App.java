package app;

import java.sql.*;

public class App {

    public static void main(String[] args) {
    	
    	//final String userName = "root";
    	//final String password = "tietue";
    	
    	
    	
    	try{
    		Class.forName("com.mysql.jdbc.Driver");
    		System.out.println("Toimii");
    	} catch(Exception e){
    		System.out.println("Luokkaa ei löydy");
    	}
    	
    }
}
