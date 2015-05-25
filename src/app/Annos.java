package app;

public class Annos {

	private String nimi;
	private double valmistuskustannukset;
	
	public Annos(String nimi, double valmistuskustannukset){
		setNimi(nimi);
		setValmistuskustannukset(valmistuskustannukset);
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
	
	
}
