package app;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class Kayttoliittyma {

	private Scanner sc;
	public static String HAE_KAIKKI = "SELECT * FROM ?";
	
	public Kayttoliittyma(){
		sc = new Scanner( System.in );
		sano("Tervetuloa ohjelmaan Pizzanpyörittäjä 2000");	
	}
	
	public void suorita(){
		while(true){		
			sano("", "", "Valitse vaihtoehto kirjoittamalla numero ja painamalla enter:");
			int osio = kysyInt("\t",
					"1: Asiakkaat",
					"2: Varasto",
					"3: Tilaukset",
					"4: Lopeta ohjelma");
			
			switch(osio){
				case 1:
					asiakasCtrl();
					break;
				case 4:
					return;
				default:
					sano("Toimintoa ei tunneta");
					break;
			}
		}	
	}
	
	private void asiakasCtrl(){
		while(true){
			sano("", "");
			int osio = kysyInt("\t\t", "1: Lue kaikki asiakkaat", "4: Palaa takaisin");
			
			switch(osio){
			
				case 1:
					sano(Asiakas.haeKaikki().toArray());
					break;
			
				case 4:
					return;
					
				default:
					sano("Toimintoa ei tunneta");
					break;
			
			}
			
		}
	}
	
    /*
     * 	Tulostaa näytölle annetut lauseet
     * 
     * 	Ensimmäinen parametri kirjoitetaan jokaisen rivin alkuun, loput omina riveinään.
     */
    private static void sano(String... txt){
    	if(txt.length == 1){
    		System.out.println(txt[0]);
    		return;
    	}
    	for(int i=1, n=txt.length; i<n; i++){
    		System.out.println(txt[0] + txt[i]);
    	}
    }
    private static void sano(Object... obj){
    	for(Object o : obj){
    		System.out.println(o);
    	}
    }
    
    private int kysyInt(String... txt){
    	return Integer.parseInt(kysyStr(txt));
    }
    private String kysyStr(String... txt){
    	sano(txt);
    	return sc.nextLine();
    }
    
}
