package app;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Tilaus implements Kantaolio{

	private int tilausnumero;
	private String kuljettajanNimi;
	private double hinta;
	private Asiakas asiakas;
	private ArrayList<Annos> annokset;
	
	/**
	 * Hinta joka lisätään jokaisen tilauksen valmistuskuluihin
	 * jotta tehdään myös tuottoa.
	 */
	public static final double MARGINAALI = 1.0;
	
	public static final String TAULU = "tilaus";
	
	private static final String INSERT_SQL =
			"INSERT INTO " + TAULU + "(kuljettajanNimi, asiakkaanId, tilausnumero) VALUES (?, ?, ?)";
	
	private static final PreparedStatement HAE_ANNOKSET_STATEMENT =
			App.getYhteys().getStatement("SELECT * FROM " + Annos.TAULU + " WHERE nimi IN "
			+"(SELECT annoksenNimi FROM tilausKoostuu WHERE tilausnumero = ? )");
	
	
	public Tilaus(String kuljettajanNimi, Asiakas asiakas, ArrayList<Annos> annokset){
		setKuljettajanNimi(kuljettajanNimi);
		setAsiakas(asiakas);
		if(annokset == null) setAnnokset();
		else setAnnokset(annokset);
		setHinta();
	}
	
	public Tilaus(){	
	}

	public int getTilausnumero() {
		return tilausnumero;
	}
	public void setTilausnumero(int tilausnumero) {
		this.tilausnumero = tilausnumero;
	}
	public String getKuljettajanNimi() {
		return kuljettajanNimi;
	}
	public void setKuljettajanNimi(String kuljettajanNimi) {
		this.kuljettajanNimi = kuljettajanNimi;
	}
	public double getHinta() {
		return hinta;
	}
	public void setHinta() {
		this.hinta = MARGINAALI;
		for(Annos a : annokset){
			this.hinta += a.getValmistuskustannukset();
		}
	}
	public void setHinta(double hinta){
		this.hinta = hinta;
	}
	public Asiakas getAsiakas() {
		return asiakas;
	}
	public void setAsiakas(Asiakas asiakas) {
		this.asiakas = asiakas;
	}
	
	public ArrayList<Annos> getAnnokset() {	
		return annokset;
	}
	
	/**
	 * Asettaa tälle tilaukselle kuuluvat annokset
	 * @param annokset Lista Tilaus-objektille kuuluvista annoksista		   
	 */
	public void setAnnokset(ArrayList<Annos> annokset) {
		this.annokset = annokset;
		//Lasketaan myös hinta tuotteelle tämän kautta, koska hinta on riippuvainen annoksista
		this.setHinta();		
	}
	/**
	 * Hakee tälle tilaukselle kuuluvat annokset tilausKoostuu-taulusta ja asettaa tuloksena
	 * saadut annokset tämän objektin annoksiksi
	 * @param annokset Lista Tilaus-objektille kuuluvista annoksista		   
	 */
	public void setAnnokset(){
		try{
			HAE_ANNOKSET_STATEMENT.clearParameters();
			HAE_ANNOKSET_STATEMENT.setInt(1, tilausnumero);
		} catch(SQLException virhe){ virhe.printStackTrace(); }
		setAnnokset(Kantaolio.mapData(App.getYhteys().hae(HAE_ANNOKSET_STATEMENT), Annos.class));
	}
	
	
	@Override
	public boolean equals(Object o){
		if(o == null || !(o instanceof Tilaus)) return false;
		Tilaus a = (Tilaus) o;
		return a.getTilausnumero() == tilausnumero;
	}
		
	@Override
	public String toString(){
		return "< #" + tilausnumero + " annokset: " + getAnnokset() + ", hinta: " + getHinta() + ", asiakkaan nimi: "
				+ asiakas.getNimi() + ", kuljettajan nimi: " + kuljettajanNimi + ">";
	}
	@Override
	public boolean pullData(ResultSet resource){
		try{
			setKuljettajanNimi( resource.getString("kuljettajanNimi") );
			setHinta( resource.getDouble("hinta") );
			setTilausnumero( resource.getInt("tilausnumero") );
			setAsiakas( Asiakas.haeIdlla(resource.getInt("asiakkaanId")) );
			setAnnokset();
			return true;
		} catch( SQLException e ){
			System.out.println("Virhe asiakkaan päivittämisesä \n"+ e.toString());
			return false;
		}
	}	
	/**
	 * Tallentaa tämän Tilaus-objektin Tilaus-kantaan
	 * ja tämän Tilaus-objektin annokset tilausKoostuu-kantaan
	 */
	@Override
	public boolean pushData(Yhteys yhteys){
		PreparedStatement lauseke;
		try{
			//Tallennetaan itse objekti. PreparedStatement.RETURN_GENERATED_KEYS tarkoittaa,
			//että automaattisesti generoitu tilausnumero saadaan selville
			lauseke = yhteys.getStatement(INSERT_SQL, PreparedStatement.RETURN_GENERATED_KEYS);
			lauseke.setString(1, kuljettajanNimi);
			lauseke.setInt(2, asiakas.getId());
			lauseke.setInt(3, tilausnumero);
			boolean tallennusOnnistui = yhteys.tallenna(lauseke) > 0;
			
			ResultSet generoidut = lauseke.getGeneratedKeys();
			if(generoidut != null && generoidut.next()){
				setTilausnumero(generoidut.getInt(1));
			}
			
			//Tallennetaan annokset tilausKoostuu-tauluun
			StringBuilder valmistelu = new StringBuilder("INSERT INTO tilausKoostuu VALUES ");
			for(int i=0, n=annokset.size(); i<n; i++){
				valmistelu.append('(').append(tilausnumero).append(",?),");
			}
			//Poistetaan viimeinen turha pilkku
			valmistelu.deleteCharAt(valmistelu.length()-1);
			lauseke = yhteys.getStatement(valmistelu.toString());
			for(int i=0, n=annokset.size(); i<n; i++){
				lauseke.setString(i+1, annokset.get(i).getNimi());
			}
			boolean annosTallennusOnnistui = yhteys.tallenna(lauseke) >= annokset.size();
			
			return tallennusOnnistui && annosTallennusOnnistui;
			
		} catch(SQLException virhe){
			virhe.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 	Hakee kaikki annetun tilaustaulun tallenteet.
	 * 
	 * 	@return Lista, joka sisältää koko tilaustaulun datan Tilaus-objekteina.
	 */
	public static ArrayList<Tilaus> haeKaikki(){
		Yhteys y = App.getYhteys();
		return Kantaolio.mapData(
				y.hae(
				y.getStatement("SELECT * FROM " + TAULU)),
				Tilaus.class);
	}	
	/**
	 * Hakee tilausta koskevan asiakkaan nimen
	 * @return asiakkaan nimi
	 */
	public String haeAsiakkaanNimi(){
		return getAsiakas().getNimi();
	}
	
	/**
	 * Poistaa tilausnumeroa vastaavan tilauksen tietokannasta
	 * @param tilausnro tilauksen tilausnumero
	 */
	public static void poistaTilaus(int tilausnro){
		Yhteys yhteys = App.getYhteys();
		PreparedStatement lauseke = yhteys.getStatement("DELETE FROM tilaus WHERE tilausnumero = " + tilausnro + ";");
		yhteys.tallenna(lauseke);
	}
}
