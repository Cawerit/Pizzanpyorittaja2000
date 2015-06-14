package app;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Annos implements Kantaolio{

	private String nimi;
	private double valmistuskustannukset;
	private ArrayList<String> raakaAineet;
	
	public static final String TAULU = "annos";
	
	private static final String UPDATE_SQL =
			"UPDATE " + TAULU + " SET valmistuskustannukset= ? WHERE nimi = ?";
	private static final String INSERT_SQL =
			"INSERT INTO " + TAULU + "(nimi, valmistuskustannukset) VALUES (? , ?)";
	private static final PreparedStatement NIMELLA_HAKU_STATEMENT =
			 App.getYhteys().getStatement("SELECT * FROM " + TAULU + " WHERE nimi=?");

	public Annos(String nimi, double valmistuskustannukset, ArrayList<String> raakaAineet){
		setNimi(nimi);
		setValmistuskustannukset(valmistuskustannukset);
		setRaakaAineet(raakaAineet);
	}

	public Annos(){
		
	}
	
	public String getNimi() {
		return nimi;
	}

	public void setNimi(String nimi) {
		this.nimi = nimi;
	}

	public double getValmistuskustannukset() {
		return valmistuskustannukset;
	}

	public void setValmistuskustannukset(double valmistuskustannukset) {
		this.valmistuskustannukset = valmistuskustannukset;
	}

	public ArrayList<String> getRaakaAineet() {
		return raakaAineet;
	}

	public void setRaakaAineet(ArrayList<String> raakaAineet) {
		tallennaRaakaAineet(raakaAineet);
		this.raakaAineet = raakaAineet;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null || !(o instanceof Annos)) return false;
		Annos a = (Annos) o;
		return a.getNimi().equals(getNimi()) && a.getValmistuskustannukset() == getValmistuskustannukset();
	}
		
	@Override
	public String toString(){
		ArrayList<String> raakaAineet = new ArrayList<String>();
		ResultSet raakaAineSet = haeRaakaAineet(nimi);
		try {
			while(raakaAineSet.next()){
				raakaAineet.add(raakaAineSet.getString(1)); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "< nimi: " + nimi + ", hinta: " + haeHinta(nimi) + ", Raaka-aineet: " +  raakaAineet.toString() + " >";
	}

	@Override
	public boolean pullData(ResultSet resource){
		try{
			setNimi( resource.getString("nimi") );
			setValmistuskustannukset( resource.getDouble("valmistuskustannukset") );
			return true;
		} catch( SQLException e ){
			System.out.println("Virhe annoksen päivittämisessä \n"+ e.toString());
			return false;
		}
	}	
	
	@Override
	public boolean pushData(Yhteys yhteys){
		PreparedStatement lauseke;
    	ArrayList<Annos> lista = haeKaikki();
		try{
			lauseke = yhteys.getStatement(INSERT_SQL);
			lauseke.setString(1, this.nimi);
			lauseke.setDouble(2, this.valmistuskustannukset);
			//Jos annos löytyy tietokannasta, tehdään päivitysoperaatio :
			for(Annos annos : lista){
				if(this.equals(annos)){
					lauseke = yhteys.getStatement(UPDATE_SQL);
					lauseke.setDouble(1, this.valmistuskustannukset);
					lauseke.setString(2, this.nimi);
				}
			}
			return yhteys.tallenna(lauseke) > 0;
			} catch (SQLException e){
				System.out.println("Virhe tallenteen päivittämisessä. \n"+e.toString());
				return false;
			}
	}
	
	/**
	 * Hakee kaikki annokset tietokannasta
	 * @return annosten listaesitys
	 */
	public static ArrayList<Annos> haeKaikki(){
		Yhteys y = App.getYhteys();
		return Kantaolio.mapData(
				y.hae(
				y.getStatement("SELECT * FROM " + TAULU)),
				Annos.class);
	}
	
	/**
	 * Tallentaa annos-raaka-aine parin annosKoostuu-tauluun.
	 * Raaka-aineet tulee olla tallennettuna raaka_aine-tauluun ennen kuin ne voi tallentaa AnnosKoostuu-tauluun.
	 * @param raakaAineet tallennettavat raaka-aineet
	 */
	private void tallennaRaakaAineet(ArrayList<String> raakaAineet){
		Yhteys yhteys = App.getYhteys();
		for(String aine : raakaAineet){
			try{
				PreparedStatement lauseke = yhteys.getStatement("INSERT INTO annosKoostuu VALUES('" + this.nimi + "', '" + aine + "');");
				yhteys.tallenna(lauseke);
			} 
			catch(Exception e){
				System.out.println("Raaka-ainetta ei ole varastossa");
			}
		}
	}
	
	/**
	 * Laskee annoksen valmistuskustannukset ja jokaisen siihen kuuluvan raaka-aineen hinnan yhteen.
	 * @param annoksenNimi annoksen nimi
	 * @return annoksen hinta
	 */
	public static double haeHinta(String annoksenNimi){
		double hinta = haeValmistuskustannukset(annoksenNimi);
		ResultSet raakaAineet = haeRaakaAineet(annoksenNimi);
		try {
			while(raakaAineet.next()){
				hinta += RaakaAine.haeHinta(raakaAineet.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hinta;
	}
	
	/**
	 * Hakee valmistuskustannukset tietokannasta annoksen nimen perusteella.
	 * @param nimi annoksen nimi
	 * @return annoksen valmistuskustannukset
	 */
	public static double haeValmistuskustannukset(String nimi){
		Yhteys yhteys = App.getYhteys();
		PreparedStatement lauseke = yhteys.getStatement("SELECT valmistuskustannukset FROM annos WHERE nimi='" + nimi + "';");
		ResultSet rs = yhteys.hae(lauseke);
		double valmistuskustannukset;
		try {
			while(rs.next()){
				valmistuskustannukset = (rs.getDouble(1));
				return valmistuskustannukset;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0.0;
	}
	public static Annos haeNimella(String nimi){
		Yhteys y = App.getYhteys();
		try{ NIMELLA_HAKU_STATEMENT.setString(1, nimi);}
		catch(SQLException e){ e.printStackTrace(); }
		ArrayList<Annos> tulos = Kantaolio.mapData(y.hae(NIMELLA_HAKU_STATEMENT), Annos.class);
		return tulos.size() >= 1 ? tulos.get(0) : null;
	}
	
	/**
	 * Hakee annokseen kuuluvat raaka-aineet tietokannasta.
	 * @param annoksenNimi annoksen nimi
	 * @return raaka-aineiden ResultSet
	 */
	public static ResultSet haeRaakaAineet(String annoksenNimi){
		Yhteys yhteys = App.getYhteys();
		PreparedStatement lauseke = yhteys.getStatement("SELECT raakaAineenNimi FROM annosKoostuu WHERE annoksenNimi = '" + annoksenNimi + "';");
		return yhteys.hae(lauseke);
	}
}
