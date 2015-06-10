package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;


public class Yhteys {

	private static final String URL = "jdbc:mysql://localhost:3306/";
	private static final String KANTA = "pp2000";
	private static final String AJURI = "com.mysql.jdbc.Driver";
	private static final String KAYTTAJA = "root"; 
	private static final String SALASANA = "tietue";
	private Connection yhteys;
	
	public Yhteys(){
		try{
			Class.forName(AJURI).newInstance();
			this.yhteys = DriverManager.getConnection(URL+KANTA, KAYTTAJA, SALASANA);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Suorittaa annetun PreparedStetementin mukaisen haun palvelimelle.
	 * @param statement PreparedStatement-objekti joka voidaan suorittaa.
	 * @return Haun tulokset sisältävä ResultSet-objekti.
	 */
	public ResultSet hae(PreparedStatement statement){
		try{
			return statement.executeQuery();
		} catch(SQLException e){
			System.out.println("Virhe suoritettaessa hakua palvelimelle. \n" + e.toString());
			return null;
		}
	}
	
	public int tallenna(PreparedStatement statement){
		try{
			return statement.executeUpdate();
		} catch(SQLException e){
			System.out.println("Virhe suoritettaessa hakua palvelimelle. \n" + e.toString());
			return 0;
		}
	}
	
	public PreparedStatement getStatement(String sql){
		try{
			return this.yhteys.prepareStatement(sql);
		}catch (SQLException e){
			System.out.println("Virhe muodostaessa lauseketta.\n" + e.toString());
			return null;
		}
	}
	
	
	
}