package app;

public class Tilaus {

	private int tilausnumero;
	private String kuljettajanNimi;
	private double hinta;
	private int asiakkaanId;
	
	private Tilaus(int tilausnumero, String kuljettajanNimi, double hinta, int AsiakkaanId){
		setTilausnumero(tilausnumero);
		setKuljettajanNimi(kuljettajanNimi);
		setHinta(hinta);
		setAsiakkaanId(asiakkaanId);
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
	
	
	
}
