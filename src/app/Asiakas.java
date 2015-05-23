package app;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Asiakas {
	
	private int id;
	private String nimi;
	private String osoite;
	
	public Asiakas(String nimi, String osoite){
		setNimi(nimi);
		setOsoite(osoite);
	}
	
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
	
	public boolean tallenna(){
		return App.yhteys.lisaa("ASIAKAS (nimi, osoite)", ("\"" + nimi + "\"," + osoite + "\""));
	}
	
	@Override
	public String toString(){
		return "< #" + id + " nimi: " + nimi + ", osoite: " + osoite + " >";
	}
	
	public static ArrayList<Asiakas> hae(){
		ResultSet tulokset = App.yhteys.hae("SELECT * FROM asiakas");
		ArrayList<Asiakas> kaikki = new ArrayList<>();
		try{
			while(tulokset.next()){
				Asiakas uusi = new Asiakas(
						tulokset.getString("nimi"),
						tulokset.getString("osoite")
					);
				uusi.setId(tulokset.getInt("id"));
				kaikki.add(uusi);
			}
		} catch(Exception e){
			System.out.println(e);
		}
		return kaikki;
	}
}
