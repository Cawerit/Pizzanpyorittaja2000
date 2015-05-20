package app;
import java.util.ArrayList;
import java.sql.ResultSet;


public class Asiakas {
	
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
	
	private int id;
	private String nimi;
	private String osoite;
	
	
	public static ArrayList<Asiakas> hae(){
		ResultSet tulokset = Yhteys.hae("SELECT * FROM asiakas");
	}
}
