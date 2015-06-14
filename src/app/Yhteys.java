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
	 * @return Haun tulokset sis‰lt‰v‰ ResultSet-objekti.
	 */
	public ResultSet hae(PreparedStatement statement){
		try{
			return statement.executeQuery();
		} catch(SQLException e){
			System.out.println("Virhe suoritettaessa hakua kantaan. \n" + e.toString());
			return null;
		}
	}
	/**
	 * Suorittaa annetun PreparedStatement-objektin executeUpdate-metodin
	 * @param statement PreparedStatement-objekti
	 * @return Niiden tietueiden m‰‰r‰ joihin operaatio vaikutti
	 */
	public int tallenna(PreparedStatement statement){
		try{
			return statement.executeUpdate();
		} catch(SQLException e){
			System.out.println("Virhe suoritettaessa muokkausta kantaan. \n" + e.toString());
			return 0;
		}
	}
	/**
	 * Muodostaa PreparedStatement-objektin annetulla lausekkeella
	 * T‰t‰ objektia voi myˆhemmin k‰ytt‰‰ haku- ja tallennusoperaatioissa
	 * @param sql Lauseke, joka annetaan PreparedStatementin parametriksi
	 * @return Muodostettu PreparedStatement
	 */
	public PreparedStatement getStatement(String sql){
		try{
			return this.yhteys.prepareStatement(sql);
		}catch (SQLException e){
			System.out.println("Virhe muodostaessa lauseketta.\n" + e.toString());
			return null;
		}
	}
	/**
	 * Muodostaa PreparedStatement-objektin annetulla lausekkeella
	 * T‰t‰ objektia voi myˆhemmin k‰ytt‰‰ haku- ja tallennusoperaatioissa
	 * Antamalla Statement-Interfacen vakion parametrina voi esimerkiksi m‰‰ritt‰‰
	 * ett‰ kannassa autogeneroidut arvot s‰ilytet‰‰n PreparedStatement-objektissa
	 * 
	 * @param sql Lauseke, joka annetaan PreparedStatementin parametriksi
	 * @param option Joku Statement-Interfacen staattisista vakioista
	 * @return Muodostettu PreparedStatement
	 */
	public PreparedStatement getStatement(String sql, int option){
		try{
			return this.yhteys.prepareStatement(sql, option);
		}catch (SQLException e){
			System.out.println("Virhe muodostaessa lauseketta.\n" + e.toString());
			return null;
		}
	}
	
	
	
}