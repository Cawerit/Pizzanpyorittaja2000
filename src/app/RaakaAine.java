package app;

public class RaakaAine {

	private String nimi;
	private double hinta;
	private int varastosaldo;
	
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

	
	
}
