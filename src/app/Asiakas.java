package app;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class Asiakas implements Kantaolio {
	
	public static final String TAULU = "asiakas";
	public static final String UPDATE_SQL =
			"UPDATE " + TAULU + " SET nimi = ? , osoite = ? WHERE id = ?";
	public static final String INSERT_SQL =
			"INSERT INTO " + TAULU + "(nimi, osoite) VALUES (? , ?)";
		
	private int id;
	private String nimi;
	private String osoite;
	
	public Asiakas(String nimi, String osoite){
		setNimi(nimi);
		setOsoite(osoite);
	}
	public Asiakas(int id){
		setId(id);
	}
	public Asiakas(){}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNimi() {
		return nimi;
	}
	public void setNimi(String nimi) {
		this.nimi = nimi;
	}
	public String getOsoite() {
		return osoite;
	}
	public void setOsoite(String osoite) {
		this.osoite = osoite;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null || !(o instanceof Asiakas)) return false;
		Asiakas a = (Asiakas) o;
		return a.getNimi().equals(getNimi()) && a.getOsoite().equals(getOsoite());
	}
		
	@Override
	public String toString(){
		return "< #" + id + " nimi: " + nimi + ", osoite: " + osoite + " >";
	}
	
	@Override
	public boolean pullData(ResultSet resource){
		try{
			setNimi( resource.getString("nimi") );
			setOsoite( resource.getString("osoite") );
			setId( resource.getInt("id") );
			return true;
		} catch( SQLException e ){
			System.out.println("Virhe asiakkaan p‰ivitt‰mises‰ \n"+ e.toString());
			return false;
		}
	}	
	@Override
	public boolean pushData(Yhteys yhteys){
		PreparedStatement lauseke;
		int id = getId();
		try{
			if(id == 0){//Jos id == 0, kyseess‰ on uusi asiakas
				lauseke = yhteys.getStatement(INSERT_SQL);
			} else {
				lauseke = yhteys.getStatement(UPDATE_SQL);
				lauseke.setInt(3, id);
			}
			lauseke.setString(1, this.nimi);
			lauseke.setString(2, this.osoite);
			
			return yhteys.tallenna(lauseke) > 0;
		} catch (SQLException e){
			System.out.println("Virhe tallenteen p‰ivitt‰misess‰. \n"+e.toString());
			return false;
		}
	}
	/**
	 * 	Hakee kaikki annetun asiakastaulun tallenteet.
	 * 
	 * 	@return Lista, joka sis‰lt‰‰ koko asiakastaulun datan Asiakas-objekteina.
	 */
	public static ArrayList<Asiakas> haeKaikki(){
		Yhteys y = App.getYhteys();
		return Kantaolio.mapData(
				y.hae(
				y.getStatement("SELECT * FROM " + TAULU)),
				Asiakas.class);
	}
}




