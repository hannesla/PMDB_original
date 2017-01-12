/**
 * 
 */
package ht.kanta;

/**
 * Aliojelmia joita tietokanta hy�dynt��
 *  
 *   Puuttuvat ominaisuudet:
 * - henkil�ihin liittyv�t oikeellisuustarkistukset (nimiin ei numeroita tai erikoismerkkej�)
 *  
 * @author Hannes Laukkanen
 * @version 23.2.2016
 * hannes.v.laukkanen@student.jyu.fi
 */
public class ElokuvaTarkistus {	
	
	
	/**
	 * Arpoo luvun annetulle v�lille
	 * @param alaraja luvun on oltava t�t� suurempi
	 * @param ylaraja luvun on oltava t�t� pienempi
	 * @return arvottu luku
	 */
	public static int arvoValille(int alaraja, int ylaraja) {
		double n = (ylaraja - alaraja) * Math.random() + alaraja;		
		return (int)Math.round(n);
	}

	
	/**
	 * tarkistaa onko elokuvan nimi sopiva tietokantaan
	 * @param ehdotus nimi jota on ehdotettu
	 * @return null jos ok, muuten syy miksi ei ole sopiva
	 * @example
	 * <pre name="test">
	 * tarkistaNimi("") === "Elokuvalla on oltava nimi";
	 * tarkistaNimi("Mit� tahansa muuta") === null;
	 * </pre>
	 */
	public static String tarkistaNimi(String ehdotus) {
		if (ehdotus.isEmpty()) return "Kaikki kent�t on annettava";
		if (!ehdotus.matches("[A-Za-z0-9 \t]+")) return "Ainoastaan " + 
				"kirjaimet, numerot ja v�lily�nnit sallittuja";
		return null;
		
	}

	
	/**
	 * Tarkistaa onko ehdotettu valmistumisvuosi sopiva tietokantaan
	 * @param ehdotus vuosi jota ehdotettu
	 * @return null jos ok, muuten syy miksi ei kelpaa
	 * @example
	 * <pre name="test">
	 * tarkistaVuosi("1999") === null;
	 * tarkistaVuosi("1") === "vuoden on oltava v�lilt� 1895 - 2025";
	 * tarkistaVuosi("2026") === "vuoden on oltava v�lilt� 1895 - 2025";
	 * tarkistaVuosi("2025") === null;
	 * tarkistaVuosi("1895") === null;
	 * </pre>
	 */
	public static String tarkistaVuosi(String ehdotus) {		
		int vuosi = Integer.parseInt(ehdotus);
		
		if (vuosi < 1895 || vuosi > 2025) return "vuoden on oltava v�lilt� 1895 - 2025";
		
		return null;
	}
}