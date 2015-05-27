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
    			"Matti Meik�l�inen", "Orimattila",
    			"Hilda-Heikki", "Alayl�h�rm�"
    			};
    	Yhteys y = getYhteys();

    	ArrayList<Asiakas> lista = y.haeTaulu(Asiakas.TAULU, Asiakas.class);
    	
    	for(int i=0, n=testi.length; i<n; i=i+2){
    		
    		Asiakas a = new Asiakas(testi[i], testi[i+1]);
    		int listassa = lista.indexOf(a);
    		//Jos asiakas on jo lis�tty palvelimelle, p�ivitet��n data eik� lis�t� uutta
    		if(listassa > -1) a = lista.get(listassa);
    		
    		boolean onnistui = a.pushData(y);
    		System.out.println("Asiakkaan " + a + " p�ivitys " + (onnistui ? "onnistui." : "ep�onnistui."));
    	}
    	System.out.println("Haetaan taulun sis�lt�... \n");
    	
    	System.out.println(lista.toString().replaceAll(">,", ">,\n"));

    	//Raaka-ainetestit
    	RaakaAine r1 = new RaakaAine("Tomaatti", 0.59, 50);
    	RaakaAine r2 = new RaakaAine("Juusto", 0.89, 100);
    	
    	boolean onnistui = r1.pushData(y);
		System.out.println("\n" + "Raaka-aineen " + r1.getNimi() + " p�ivitys " + (onnistui ? "onnistui." : "ep�onnistui."));
    	
		onnistui = r2.pushData(y);
		System.out.println("Raaka-aineen " + r2.getNimi() + " p�ivitys " + (onnistui ? "onnistui." : "ep�onnistui."));
		
		System.out.println(y.haeTaulu(RaakaAine.TAULU, RaakaAine.class).toString().replaceAll(">,", ">,\n"));
    	
    	
    }
}