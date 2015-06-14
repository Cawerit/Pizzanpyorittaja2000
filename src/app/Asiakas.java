package app;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class Asiakas implements Kantaolio {
	
	/**
	 * Kannan taulu josta asiakkaita voi hakea
	 */
	public static final String TAULU = "asiakas";
	/**
	 * Tallennusoperaatioihin käytettävä PreparedStatement
	 */
	private static final PreparedStatement INSERT_STATEMENT =
			App.getYhteys().getStatement("INSERT INTO " + TAULU + "(nimi, osoite) VALUES (? , ?)");
	/**
	 * Kaikkien asiakkaiden hakemiseen käytettävä PreparedStatement
	 */
	private static final PreparedStatement HAE_KAIKKI_STATEMENT = 
			App.getYhteys().getStatement("SELECT * FROM " + TAULU);
	/**
	 * Tietyn nimisten asiakkaiden hakemiseen käytettävä PreparedStatement
	 */
	private static final PreparedStatement HAE_NIMELLA_STATEMENT =
			App.getYhteys().getStatement("SELECT * FROM " + TAULU + " WHERE nimi=?");
	/**
	 * Tietyn asiakkaan hakemiseen käytettävä PreparedStatement
	 */
	private static final PreparedStatement HAE_IDLLA_STATEMENT =
			App.getYhteys().getStatement("SELECT * FROM " + TAULU + " WHERE id=?");
		
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
			System.out.println("Virhe asiakkaan päivittämisesä \n"+ e.toString());
			return false;
		}
	}	
	@Override
	public boolean pushData(Yhteys yhteys){
		PreparedStatement lauseke;
		try{
			lauseke = INSERT_STATEMENT;
			lauseke.setString(1, this.nimi);
			lauseke.setString(2, this.osoite);
			return yhteys.tallenna(lauseke) > 0;
		} catch (SQLException e){
			System.out.println("Virhe tallenteen päivittämisessä. \n"+e.toString());
			return false;
		}
	}
	/**
	 * 	Hakee kaikki annetun asiakastaulun tallenteet.
	 * 
	 * 	@return Lista, joka sisältää koko asiakastaulun datan Asiakas-objekteina.
	 */
	public static ArrayList<Asiakas> haeKaikki(){
		Yhteys y = App.getYhteys();
		return Kantaolio.mapData(
				y.hae(HAE_KAIKKI_STATEMENT),
				Asiakas.class);
	}
	/**
	 * Hakee asiakastaulusta kaikki asiakkaat joilla on annettu nimi
	 * @param nimi Asiakkaalle annettu nimi
	 * @return Lista osumista muodostetuista Asiakas-objekteista
	 */
	public static ArrayList<Asiakas> haeNimella(String nimi){
		Yhteys y = App.getYhteys();
		try{
			HAE_NIMELLA_STATEMENT.clearParameters();
			HAE_NIMELLA_STATEMENT.setString(1, nimi);
		} catch(SQLException e){ e.printStackTrace(); }
		return Kantaolio.mapData(y.hae(HAE_NIMELLA_STATEMENT), Asiakas.class);
	}
	/**
	 * Hakee taulusta asiakkaan jolla on annettu id
	 * @param id Asiakkaan id
	 * @return Osumasta muodostettu Asiakas-objekti tai null, jos osumia ei löytynyt
	 */
	public static Asiakas haeIdlla(int id){
		Yhteys y = App.getYhteys();
		try{
			HAE_IDLLA_STATEMENT.clearParameters();
			HAE_IDLLA_STATEMENT.setInt(1, id);
		} catch(SQLException e){ e.printStackTrace(); }
		ArrayList<Asiakas> osumat =  Kantaolio.mapData(y.hae(HAE_IDLLA_STATEMENT), Asiakas.class);
		return osumat.size() >= 1 ? osumat.get(0) : null;
	}
}




