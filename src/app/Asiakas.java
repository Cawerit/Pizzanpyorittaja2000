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
	
	@Override
	public String toString(){
		return "< #" + id + " nimi: " + nimi + ", osoite: " + osoite + " >";
	}
	
	public Asiakas(int id, String nimi, String osoite){
		setId(id);
		setNimi(nimi);
		setOsoite(osoite);
	}
	
	
	public static ArrayList<Asiakas> hae(){
		ResultSet tulokset = App.yhteys.hae("SELECT * FROM asiakas");
		ArrayList<Asiakas> kaikki = new ArrayList<>();
		try{
			while(tulokset.next()){
				kaikki.add(
						new Asiakas(
							tulokset.getInt("id"),
							tulokset.getString("nimi"),
							tulokset.getString("osoite")
						)
				);
			}
		} catch(Exception e){
			System.out.println(e);
		}
		return kaikki;
	}
}