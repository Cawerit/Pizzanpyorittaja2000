package app;

public class App {
	
	public static final Yhteys yhteys = new Yhteys();

    public static void main(String[] args) {	
    	System.out.println(Asiakas.hae());
    	System.out.println((new Asiakas("Yolo", "swag")).tallenna());
    	System.out.println(Asiakas.hae());
    }
}
