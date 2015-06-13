package app;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * 
 * Interface, jonka toteuttavilla luokilla on tietokannassa vastaava taulu
 * ja jotka tarjoavat tarvittavat metodit n‰iden taulujen k‰yttˆˆn.
 *
 */
public interface Kantaolio {

	/**
	 * Muodostaa annetusta ResultSet-objektista Kantaolioita sis‰lt‰v‰n ArrayList-objektin.
	 * 
	 * @param resource ResultSet-objekti, joka sis‰lt‰‰ palvelimelta haetun datan.
	 * @param luokka Kantaolion toteuttava luokka, jota k‰ytet‰‰n uusien objektien muodostamiseen.
	 * @return Lista, joka sis‰lt‰‰ m‰‰ritetyn luokan objekteja.
	 */
	public static <T extends Kantaolio> ArrayList<T> mapData(ResultSet resource, Class<T> luokka){
		ArrayList<T> palaute = new ArrayList<>();
		try{
			while(resource.next()){
				T uusi = luokka.newInstance();
				uusi.pullData(resource);
				palaute.add(uusi);
			}
		} catch(InstantiationException e){
			System.out.println("Virhe muodostettaessa tietokannan datasta objektia.\n"
					+ "Varmista ett‰ luokka " + luokka.getName()
					+" sis‰lt‰‰ konstruktorin joka ei ota vastaan parametreja.");
		} catch(Exception e){
			System.out.println("Virhe lukiessa haun tuloksia. \n" + e.toString());
		}
		return palaute;
	}
	
	/**
	 * P‰ivitt‰‰ Kantaolio-objektin sis‰llˆn vastaamaan annettua ResultSet-objektia.
	 * 
	 * @param resource ResultSet-olio, jonka kursori on valmiiksi asetettu oikeaan kohtaan.
	 * @return true, mik‰li p‰ivitys onnistui, false jos p‰ivitysyritys aiheutti virheen. 
	 */
	boolean pullData(ResultSet resource);
	
	/**
	 * P‰ivitt‰‰ getTunniste()-metodin perusteella lˆydett‰v‰n tietokannan tietueen vastaamaan
	 * t‰t‰ Kantaoliota.
	 * 
	 * @param yhteys Yhteys-objekti, jolla Kantaolio saa yhteyden tauluun.
	 * @return true, mik‰li p‰ivitys onnistui, false jos p‰ivitysyritys aiheutti virheen. 
	 */
	boolean pushData(Yhteys yhteys);
	
}
