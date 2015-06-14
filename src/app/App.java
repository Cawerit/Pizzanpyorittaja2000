package app;

public class App {
	
	public static final Yhteys yhteys = new Yhteys();
	
	/**
	 * Palauttaa tietokantayhteyden mahdollistaen tietokantaoperaatiot
	 * @return yhteys tietokantaan
	 */
	public static Yhteys getYhteys(){
		return yhteys;
	}
	
	/**
	 * Käynnistää käyttöliittymän.
	 * @param args
	 */
    public static void main(String[] args) {

    	Kayttoliittyma UI = new Kayttoliittyma();
    	UI.suorita();
    	
    }
}