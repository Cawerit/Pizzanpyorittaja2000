package app;



public class App {
	
	public static final Yhteys yhteys = new Yhteys();

    public static void main(String[] args) {	
   
    	Asiakas asiakas = new Asiakas();
    	asiakas.lisaaAsiakas("INSERT INTO asiakas " + "VALUES (3, 'martti', 'martintie 30')", yhteys);
    	asiakas.haeTiedot(yhteys);
    	
    	
    }
}
