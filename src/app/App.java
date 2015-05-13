package app;

import java.sql.*;

public class App {

    public static void main(String[] args) {
    	
    	try{
    		Class.forName("org.postgresql.Driver");
    		System.out.println("Toimii");
    	} catch(Exception e){
    		System.out.println("Luokkaa ei löydy");
    	}
    	
    }
}
