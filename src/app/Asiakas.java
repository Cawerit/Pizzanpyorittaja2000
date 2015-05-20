package app;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Asiakas {

	private int id;
	private String nimi;
	private String osoite;
	
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
	
	public void lisaaAsiakas(String kasky, Yhteys yhteys){
		
		yhteys.päivitä(kasky);
	}
	
	
	public void haeTiedot(Yhteys yhteys){
  	  	ResultSet res = yhteys.hae("Select * from asiakas");
  	  	try {
			while(res.next()){
				int id = res.getInt("id");
				String nimi = res.getString("nimi");
				System.out.println(id + "\t" + nimi);

				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	}
	

