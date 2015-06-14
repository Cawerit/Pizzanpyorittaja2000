package app;

import java.util.ArrayList;
import java.util.Scanner;


public class Kayttoliittyma {

	private Yhteys yhteys = App.getYhteys();
	private Scanner sc;
	public static String HAE_KAIKKI = "SELECT * FROM ?";
	
	public Kayttoliittyma(){
		sc = new Scanner( System.in );
		sano("Tervetuloa ohjelmaan Pizzanpyörittäjä 2000");	
	}
	
	/**
	 * Ohjelman aloitusvalikko
	 */
	public void suorita(){
		while(true){		
			sano("", "", "Valitse vaihtoehto kirjoittamalla numero ja painamalla enter:");
			int osio = kysyInt("\t",
					"1: Asiakkaat",
					"2: Varasto",
					"3: Menu",
					"4: Tilaukset",
					"5: Lopeta ohjelma");
			
			switch(osio){
				case 1:
					asiakasCtrl();
					break;
				case 2:
					varastoCtrl();
					break;
				case 3:
					menuCtrl();
					break;
				case 4:
					tilausCtrl();
					break;
				case 5:
					return;
				default:
					sano("Toimintoa ei tunneta");
					break;
			}
		}	
	}

			/**
			 * Toiminnot asiakkaidenhallintaan
			 */
			private void asiakasCtrl(){
		while(true){
			sano("", "");
			int osio = kysyInt("\t\t", "1: Lue kaikki asiakkaat", "2: Lisää uusi asiakas", "4: Palaa takaisin");
			
			switch(osio){
			
				case 1:
					sano(Asiakas.haeKaikki().toArray());
					break;
				case 2:
					Asiakas uusi = new Asiakas();
					uusi.setNimi(kysyStr("\t\t", "Anna asiakkaan nimi"));
					uusi.setOsoite(kysyStr("\t\t", "Anna asiakkaan osoite"));
					uusi.pushData(yhteys);
					//Poistetaan asiakas-olio, jotta voidaan luoda uusia asiakkaita
					uusi = null;
					System.out.println("\t\tAsiakas tallennettu tietokantaan");
					break;
				case 4:
					return;
					
				default:
					sano("Toimintoa ei tunneta");
					break;
			
			}
			
		}
	}
	
	/**
	 * Toiminnot varastonhallintaan
	 */
	private void varastoCtrl(){
		while(true){
			sano("", "");
			int osio = kysyInt("\t\t", "1: Lue varaston sisältö", "2: Lisää tuote varastoon", "4: Palaa takaisin");
			
			switch(osio){
			
				case 1:
					sano(RaakaAine.haeKaikki().toArray());
					break;
				case 2: 
					RaakaAine uusi = new RaakaAine();
					uusi.setNimi(kysyStr("\t\t", "Anna tuotteen nimi"));
					uusi.setHinta(kysyDouble("\t\t", "Anna tuotteen hinta"));
					uusi.setVarastosaldo(kysyInt("\t\t", "Anna tuotteen varastosaldo"));
					uusi.pushData(yhteys);
					//Poistetaan olio, jotta voidaan luoda uusia raaka-aineita
					uusi = null;
					System.out.println("\t\tTuote lisätty varastoon");
					break;
				case 4:
					return;
					
				default:
					sano("Toimintoa ei tunneta");
					break;
			
			}
			
		}
	}
	
	/**
	 * Toiminnot ruokalistan hallintaan
	 */
	private void menuCtrl(){
		while(true){
			sano("", "");
			int osio = kysyInt("\t\t", "1: Lue kaikki annokset", "2: Luo uusi annos", "4: Palaa takaisin");
			
			switch(osio){
			
				case 1:
					sano(Annos.haeKaikki().toArray());
					break;
				case 2: 
					Annos uusi = new Annos();
					uusi.setNimi(kysyStr("\t\t", "Anna annoksen nimi"));
					uusi.setValmistuskustannukset(kysyDouble("\t\t", "Anna annoksen valmistuskustannukset"));
					Boolean jatketaanko = true;
					ArrayList<String> raakaAineet = new ArrayList<String>();
					while(jatketaanko){
						String raakaAine = kysyStr("\t\t", "Lisää raaka-aine tai tallenna annos painamalla 4");
						if(!raakaAine.equals("4")){
							raakaAineet.add(raakaAine);
						} else {
							jatketaanko = false;
						}
					}
					uusi.pushData(yhteys);
					uusi.setRaakaAineet(raakaAineet);
					//Poistetaan olio, jotta voidaan luoda uusia annoksia
					uusi = null;
					System.out.println("\t\tAnnos tallennettu");
					break;
				case 4:
					return;
					
				default:
					sano("Toimintoa ei tunneta");
					break;
			
			}
			
		}
	}
	
	/**
	 * Toiminnot tilausten hallintaan
	 */
	private void tilausCtrl(){
		
		tilaus_ctrl_loop:
		while(true){
			sano("", "");
			int osio = kysyInt("\t\t", "1: Lue kaikki tilaukset", "2: Luo uusi tilaus", "3: Poista tilaus",  "4: Palaa takaisin");
			
			switch(osio){
				case 1:
					sano(Tilaus.haeKaikki().toArray());
					break;
				case 2: 
					
					ArrayList<Asiakas> haetutAsiakkaat = Asiakas.haeNimella(kysyStr("\t\t", "Anna Asiakkaan nimi"));
					Asiakas valittuAsiakas;
					
					switch(haetutAsiakkaat.size()){
						case 0:
							//Jos nimi ei tuottanut tuloksia, ilmoitetaan asiasta ja palataan valikkoon
							sano("\t", "Nimellä ei löytynyt yhtään asiakasta");
							continue tilaus_ctrl_loop;
							
						case 1://Jee, vain yksi nimi
							valittuAsiakas = haetutAsiakkaat.get(0);
							break;
						
						default://Muutoin on liikaa vaihtoehtoja
							sano("\t", "Nimellä löytyi useita asiakkaita:");
							for(int i=0, n=haetutAsiakkaat.size(); i<n; i++){
								sano("\t" + i + ": " + haetutAsiakkaat.get(i).toString());
							}
							int index = kysyInt("\t", "Valitse yksi listalta");
							valittuAsiakas = haetutAsiakkaat.get(index);							
							break;
					}
					
					ArrayList<Annos> annokset = new ArrayList<Annos>();
					while(true){
						String seuraavaksi = kysyStr("\t\t", "Lisää annos tai tallenna tilaus painamalla 4");
						if(!seuraavaksi.equals("4")){
							Annos haettu = Annos.haeNimella(seuraavaksi);
							if(haettu == null){
								sano("\t", "Annosta ei löydetty annetulla nimellä! Yritä uudelleen.");
								continue;
							} else{
								annokset.add(haettu);
							}
						} else {
							break;
						}
					}
					//Luodaan ja tallennetaan tilaus
					Tilaus uusi = new Tilaus(
							kysyStr("\t\t", "Kuka kuljettaa tilauksen?"),
							valittuAsiakas,
							annokset
							);
					uusi.pushData(App.getYhteys());
					sano("\tTilaus " + uusi.toString() + " tallennettu.");
					
					break;
					
				case 3:
					int tilausnumero = kysyInt("Anna poistettavan tilauksen tilausnumero");
					Tilaus.poistaTilaus(tilausnumero);
					break;
				case 4:
					return;
					
				default:
					sano("Toimintoa ei tunneta");
					break;
			
			}
			
		}
	}
	
	/**
	 * Tulostaa näytölle annetut lauseet 
	 * Ensimmäinen parametri kirjoitetaan jokaisen rivin alkuun, loput omina riveinään.
	 * @param txt tulostettava teksti
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
    
    /**
     * Kysyy käyttäjältä numeroa
     * @param txt kysymys
     * @return vastaus
     */
    private int kysyInt(String... txt){
    	return Integer.parseInt(kysyStr(txt));
    }
    
    /**
     * Kysyy käyttäjältä desimaaliluvun
     * @param txt kysymys
     * @return vastaus
     */
    private double kysyDouble(String... txt){
    	return Double.parseDouble(kysyStr(txt));
    }
    
    /**
     * Kysyy käyttäjältä merkkijonon
     * @param txt kysymys
     * @return vastaus
     */
    private String kysyStr(String... txt){
    	sano(txt);
    	return sc.nextLine();
    }
    
}
