package app;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class RaakaAine implements Kantaolio{
	
	public static final String TAULU = "raaka_aine";
	public static final String UPDATE_SQL =
			"UPDATE " + TAULU + " SET hinta = ? , varastosaldo = ? WHERE nimi = ?";
	public static final String INSERT_SQL =
			"INSERT INTO " + TAULU + "(nimi, hinta, varastosaldo) VALUES (? , ? , ?)";

	private String nimi;
	private double hinta;
	private int varastosaldo;
	
	public RaakaAine(){
		
	}
	
	public RaakaAine(String nimi, double hinta, int varastosaldo){
		setNimi(nimi);
		setHinta(hinta);
		setVarastosaldo(varastosaldo);
	}
	
	public String getNimi() {
		return nimi;
	}
	public void setNimi(String nimi) {
		this.nimi = nimi;
	}
	public double getHinta() {
		return hinta;
	}
	public void setHinta(double hinta) {
		this.hinta = hinta;
	}
	public int getVarastosaldo() {
		return varastosaldo;
	}
	public void setVarastosaldo(int varastosaldo) {
		this.varastosaldo = varastosaldo;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null || !(o instanceof RaakaAine)) return false;
		RaakaAine a = (RaakaAine) o;
		return a.getNimi().equals(getNimi()) && a.getHinta() == getHinta() && a.getVarastosaldo() == getVarastosaldo();
	}
		
	@Override
	public String toString(){
		return "< nimi: " + nimi + ", hinta: " + hinta + ", varastosaldo: " + varastosaldo + " >";
	}

	@Override
	public boolean pullData(ResultSet resource){
		try{
			setNimi( resource.getString("nimi") );
			setHinta( resource.getDouble("hinta") );
			setVarastosaldo( resource.getInt("varastosaldo") );
			return true;
		} catch( SQLException e ){
			System.out.println("Virhe raaka-aineen p‰ivitt‰misess‰ \n"+ e.toString());
			return false;
		}
	}	
	@Override
	public boolean pushData(Yhteys yhteys){
		PreparedStatement lauseke;
    	ArrayList<RaakaAine> lista = haeKaikki();
		try{
			lauseke = yhteys.getStatement(INSERT_SQL);
			lauseke.setString(1, this.nimi);
			lauseke.setDouble(2, this.hinta);
			lauseke.setInt(3, this.varastosaldo);
			//Jos raaka-aine lˆytyy tietokannasta, tehd‰‰n p‰ivitysoperaatio :
			for(RaakaAine aine : lista){
				if(this.nimi.equals(aine.getNimi())){
					lauseke = yhteys.getStatement(UPDATE_SQL);
					lauseke.setDouble(1, this.hinta);
					lauseke.setInt(2, this.varastosaldo);
					lauseke.setString(3, this.nimi);
				}
			}
			return yhteys.tallenna(lauseke) > 0;
			} catch (SQLException e){
				System.out.println("Virhe tallenteen p‰ivitt‰misess‰. \n"+e.toString());
				return false;
			}
	}
	
	/**
	 * Hakee kaikki raaka-aineet tietokannasta
	 * @return raaka-aineiden listaesitys
	 */
	public static ArrayList<RaakaAine> haeKaikki(){
		Yhteys y = App.getYhteys();
		return Kantaolio.mapData(
				y.hae(
				y.getStatement("SELECT * FROM " + TAULU)),
				RaakaAine.class);
	}

	/**
	 * V‰hent‰‰ raaka-aineen varastosaldoa yhdell‰.
	 * @param raakaAine raaka-aine jonka saldoa v‰hennet‰‰n
	 */
	public static void vahennaSaldoa(String raakaAine){
		Yhteys yhteys = App.getYhteys();
		PreparedStatement lauseke = yhteys.getStatement("UPDATE raaka_aine SET varastosaldo = varastosaldo -1 WHERE nimi = "
														+ "'" + raakaAine + "';");
		yhteys.tallenna(lauseke);
	}
	
	/**
	 * Hakee raaka-aineen hinnan tietokannasta nimen perusteella.
	 * @param nimi raaka-aineen nimi
	 * @return raaka aineen hinta
	 */
	public static double haeHinta(String nimi){
		Yhteys yhteys = App.getYhteys();
		PreparedStatement lauseke = yhteys.getStatement("SELECT hinta FROM raaka_aine WHERE nimi='" + nimi + "';");
		try {
			ResultSet rs = yhteys.hae(lauseke);
			while(rs.next()){
				double hinta = rs.getDouble(1);
				return hinta;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0.0;
	}
}
			