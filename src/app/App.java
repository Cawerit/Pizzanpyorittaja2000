package app;

public class App {
	
	public static final Yhteys yhteys = new Yhteys();
	
	public static Yhteys getYhteys(){
		return yhteys;
	}

    public static void main(String[] args) {

    	Kayttoliittyma UI = new Kayttoliittyma();
    	UI.suorita();
    	
    }
}