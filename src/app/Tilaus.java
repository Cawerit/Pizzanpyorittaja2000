package app;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Tilaus implements Kantaolio{

	private int tilausnumero;
	private String kuljettajanNimi;
	private double hinta;
	private int asiakkaanId;
	private ArrayList<String> annokset;
	
	public static final String TAULU = "tilaus";
	public static final String UPDATE_SQL =
			"UPDATE " + TAULU + " SET kuljettajanNimi = ? , asiakkaanId = ?,  WHERE tilausnumero = ?";
	public static final String INSERT_SQL =
			"INSERT INTO " + TAULU + "(kuljettajanNimi, asiakkaanId) VALUES (?, ?)";
	
	
	public Tilaus(int tilausnumero, String kuljettajanNimi, double hinta, int AsiakkaanId){
		setTilausnumero(tilausnumero);
		setKuljettajanNimi(kuljettajanNimi);
		setHinta(hinta);
		setAsiakkaanId(asiakkaanId);
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

	public void setHinta(double hinta) {
		this.hinta = hinta;
	}

	public int getAsiakkaanId() {
		return asiakkaanId;
	}

	public void setAsiakkaanId(int asiakkaanId) {
		this.asiakkaanId = asiakkaanId;
	}
	
	public ArrayList<String> getAnnokset() {
		return annokset;
	}

	public void setAnnokset(ArrayList<String> annokset) {
		tallennaAnnokset(annokset);
		this.annokset = annokset;
	}

	
	@Override
	public boolean equals(Object o){
		if(o == null || !(o instanceof Tilaus)) return false;
		Tilaus a = (Tilaus) o;
		return a.getTilausnumero() == tilausnumero;
	}
		
	@Override
	public String toString(){
		return "< #" + tilausnumero + " annokset: " + haeAnnokset(tilausnumero) + ", hinta: " + haeHinta(tilausnumero) + ", asiakkaan nimi: "
				+ haeAsiakkaanNimi(tilausnumero) + ", kuljettajan nimi: " + kuljettajanNimi + ">";
	}
	
	@Override
	public boolean pullData(ResultSet resource){
		try{
			setKuljettajanNimi( resource.getString("kuljettajanNimi") );
			setHinta( resource.getDouble("hinta") );
			setTilausnumero( resource.getInt("tilausnumero") );
			return true;
		} catch( SQLException e ){
			System.out.println("Virhe asiakkaan p‰ivitt‰mises‰ \n"+ e.toString());
			return false;
		}
	}	
	
	@Override
	public boolean pushData(Yhteys yhteys){
		PreparedStatement lauseke;
		int id = getTilausnumero();
		try{
			if(id == 0){//Jos id == 0, kyseess‰ on uusi tilaus
				lauseke = yhteys.getStatement(INSERT_SQL);
			} else {
				lauseke = yhteys.getStatement(UPDATE_SQL);
				lauseke.setInt(3, tilausnumero);
			}
			lauseke.setString(1, this.kuljettajanNimi);
			lauseke.setInt(2, this.asiakkaanId);
			
			return yhteys.tallenna(lauseke) > 0;
		} catch (SQLException e){
			System.out.println("Virhe tallenteen p‰ivitt‰misess‰. \n"+e.toString());
			return false;
		}
	}
	
	/**
	 * 	Hakee kaikki annetun tilaustaulun tallenteet.
	 * 
	 * 	@return Lista, joka sis‰lt‰‰ koko tilaustaulun datan Tilaus-objekteina.
	 */
	public static ArrayList<Tilaus> haeKaikki(){
		Yhteys y = App.getYhteys();
		return Kantaolio.mapData(
				y.hae(
				y.getStatement("SELECT * FROM " + TAULU)),
				Tilaus.class);
	}
	
	/**
	 * Tallentaa tilaus-annos parin tilausKoostuu-tauluun.
	 * Annokset tulee olla tallennettuna annos-tauluun ennen kuin ne voi tallentaa tilausKoostuu-tauluun.
	 * @param annokset tallennettavat annokset
	 */
	private void tallennaAnnokset(ArrayList<String> annokset){
		Yhteys yhteys = App.getYhteys();
		for(String annos : annokset){
			//varastosaldon v‰hennys:
			try{
				ResultSet raakaAineet = Annos.haeRaakaAineet(annos);
				while(raakaAineet.next()){
					RaakaAine.vahennaSaldoa(raakaAineet.getString(1));
				}
				PreparedStatement lauseke = yhteys.getStatement("INSERT INTO tilausKoostuu VALUES('" + haeViimeisinTilausnumero() + "', '" + annos + "');");
				yhteys.tallenna(lauseke);
			} 
			catch(Exception e){
				System.out.println("Annosta ei ole menussa");
			}
		}
	}

	/**
	 * Laskee tilaukseen kuuluvien annosten hinnan.
	 * @param tilausnumero tilauksen tilausnumero 
	 * @return tilauksen hinta
	 */
	public static double haeHinta(int tilausnumero){
		ArrayList<String> annokset = haeAnnokset(tilausnumero);
		double hinta = 0;
		for(String annos : annokset){
			hinta += Annos.haeHinta(annos);
		}
		return hinta;
	}
	
	/**
	 * Hakee tilaukseen kuuluvat annokset tietokannasta. 
	 * @param tilausnumero tilauksen tilausnumero
	 * @return annosten listaesitys
	 */
	public static ArrayList<String> haeAnnokset(int tilausnumero){
		Yhteys yhteys = App.getYhteys();
		PreparedStatement lauseke = yhteys.getStatement("SELECT annoksenNimi FROM tilausKoostuu WHERE tilausnumero = '" + tilausnumero + "';");
		ResultSet rs = yhteys.hae(lauseke);
		ArrayList<String> annokset = new ArrayList<String>();
		try {
			while (rs.next()) annokset.add(rs.getString(1));
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return annokset;
	}
	
	/**
	 * Hakee viimeisen tilausnumeron tilaus-taulusta, jotta saadaan oikea tilausnumero tilausKoostuu-tauluun.
	 * @return tilaus-taulun suurin tilausnumero
	 */
	public static int haeViimeisinTilausnumero(){
		Yhteys yhteys = App.getYhteys();
		PreparedStatement lauseke = yhteys.getStatement("SELECT MAX(tilausnumero) AS tilausnumero FROM tilaus");
		ResultSet rs = yhteys.hae(lauseke);
			try {
				while (rs.next()) return rs.getInt(1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return 0;
	}
	
	/**
	 * Hakee tilausta koskevan asiakkaan nimen.
	 * @param tilausnro tilauksen tilausnumero
	 * @return asiakkaan nimi
	 */
	public static String haeAsiakkaanNimi(int tilausnro){
		int id = 0;
		Yhteys yhteys = App.getYhteys();
		PreparedStatement lauseke = yhteys.getStatement("SELECT asiakkaanId FROM tilaus WHERE tilausnumero = " + tilausnro + ";");
		ResultSet rs = yhteys.hae(lauseke);
		try {
			while(rs.next()){
				 id = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Asiakas.haeAsiakkaanNimi(id);
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
