package app;

import java.util.ArrayList;

public class App {
	
	public static final Yhteys yhteys = new Yhteys();
	
	public static Yhteys getYhteys(){
		return yhteys;
	}

    public static void main(String[] args) {
    	
    	String[] testi = new String[]{
    			"Jaakko Jaakonpoika", "Tietie2",
    			"Matti Meikäläinen", "Orimattila",
    			"Hilda-Heikki", "Alaylähärmä"
    			};
    	Yhteys y = getYhteys();

    	ArrayList<Asiakas> lista = y.haeTaulu(Asiakas.TAULU, Asiakas.class);
    	
    	for(int i=0, n=testi.length; i<n; i=i+2){
    		
    		Asiakas a = new Asiakas(testi[i], testi[i+1]);
    		int listassa = lista.indexOf(a);
    		//Jos asiakas on jo lisätty palvelimelle, päivitetään data eikä lisätä uutta
    		if(listassa > -1) a = lista.get(listassa);
    		
    		boolean onnistui = a.pushData(y);
    		System.out.println("Asiakkaan " + a + " päivitys " + (onnistui ? "onnistui." : "epäonnistui."));
    	}
    	System.out.println("Haetaan taulun sisältö... \n");
    	
    	System.out.println(lista.toString().replaceAll(">,", ">,\n"));

    }
}