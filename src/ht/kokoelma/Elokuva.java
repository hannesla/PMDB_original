/**
 * 
 */
package ht.kokoelma;

import java.io.OutputStream;
import java.io.PrintStream;
import static ht.kanta.ElokuvaTarkistus.*;

/**
 * Luokka Yksittäisten elokuvin luontiin
 * 
 * @author Hannes Laukkanen
 * @version 23.2.2016
 * hannes.v.laukkanen@student.jyu.fi
 */
public class Elokuva {

	private int     eid;
	private String  nimi = "";
	private int     vuosi;
	private String  avainsanat = "";

	private static int seuraavaEid = 1;


	/**
	 * Täyttää olion esimerkkitiedoilla joita siihen voitaisiin syöttää
	 * @example
	 * <pre name="test">
	 * Elokuva e1 = new Elokuva(); 
	 * e1.taytaTiedoilla();
	 * e1.getNimi().matches("Interstellar [0-9]{4}") === true;
	 * String v = "" + e1.getVuosi();
	 * v.matches("[0-9]{4}") === true;
	 * e1.getAvainsanat() === "scifi, jännittävä";
	 * </pre>
	 */
	public void taytaTiedoilla() {		
		nimi = "Interstellar " + arvoValille(1000, 9999);
		vuosi = arvoValille(1900, 2016);
		avainsanat = "scifi, jännittävä";		
	}


	/**
	 * Kertoo elokuvan id:n
	 * @return elokuvan id (eli eid)
	 */
	public int getEid() {
		return eid;
	}


	/**
	 * palauttaa elokuvan avainsanat
	 * @return avainsanat
	 */
	public String getAvainsanat() {
		return avainsanat;
	}


	/**
	 * 
	 * @return jäsenen nimi
	 */
	public String getNimi() {		
		return nimi;
	}


	/**
	 * palauttaa elokuvan julkaisuvuoden
	 * @return elokuvan julkaisuvuosi
	 */
	public int getVuosi() {
		return vuosi;
	}


	/**
	 * antaa elokuvalle yksilöivän tunnuksen
	 * @return palauttaa elokuvan id:n
	 *@example
	 * <pre name="test">
	 * Elokuva test1 = new Elokuva();
	 * Elokuva test2 = new Elokuva();
	 * test1.getEid() === 0;
	 * test2.getEid() === 0;
	 * test1.rekisteroi();
	 * test2.rekisteroi();
	 * test2.getEid() - test1.getEid() == 1 === true; // testattava näin, koska testien suoritusjärjestystä ei voi tietää
	 * </pre>
	 */
	public int rekisteroi() {		
		if (eid > 0) return eid;

		eid = seuraavaEid;
		seuraavaEid++;

		return eid;
	}


	/**
	 * Tulostetaan elokuvan tiedot
	 * @param out tietovirta johon tulostetaan
	 */
	public void tulosta(PrintStream out) {
		out.println("Elokuvan nimi: " +   nimi + "\n" +
				"Valmistumisvuosi: " + vuosi + "\n\n" +
				"Ohjaaja: " + avainsanat + "\n");					
	}


	/**
	 * Tulostaa elokuvan tiedot
	 * @param os Tietovirta johon tulostetaan
	 */
	public void tulosta(OutputStream os) {
		tulosta(new PrintStream(os));		
	}	


	/**
	 * Palauttaa elokuvan tiedot tiedostoon tallennettavassa muodossa
	 * @example
	 * <pre name="test">
	 *   Elokuva test = new Elokuva();
	 *   test.asetaTiedot("5", "elokuva", "1999", "vainsanat");
	 *   "5|elokuva|1999|vainsanat|".equals(test.toString()) === true;
	 *   "5|elokuva|1939|vansanat|".equals(test.toString()) === false;
	 * </pre>
	 */
	@Override
	public String toString() {
		if (avainsanat.isEmpty()) avainsanat = " ";
		return "" + getEid()  + "|" + getNimi() + "|" + getVuosi() +
				"|" + getAvainsanat() + "|";
	}


	/**
	 * Elokuva-luokan toiminnan kokeilua varten
	 * @param args ei käytetä
	 */
	public static void main(String[] args) {
		Elokuva elokuva1 = new Elokuva(); 
		Elokuva elokuva2 = new Elokuva();

		elokuva1.tulosta(System.out);
		elokuva2.tulosta(System.out);

		elokuva1.taytaTiedoilla();
		elokuva1.rekisteroi();

		elokuva2.taytaTiedoilla();
		elokuva2.rekisteroi();

		elokuva1.tulosta(System.out);
		elokuva2.tulosta(System.out);
	}


	/**
	 * Asettaa elokuvalle tiedot
	 * @param annettuEid elokuvan tunnistusnumero
	 * @param annettuNimi elokuvan nimi
	 * @param annettuVuosi valmistumisvuosi
	 * @param annetutAvainsanat elokuvaan liittyvät avainsanat
	 */
	public void asetaTiedot(String annettuEid, String annettuNimi, String annettuVuosi, String annetutAvainsanat) {
		this.eid = Integer.parseInt(annettuEid);
		this.nimi = annettuNimi;
		this.vuosi = Integer.parseInt(annettuVuosi);
		this.avainsanat = annetutAvainsanat;
		seuraavaEid = this.eid + 1;
	}


	/**
	 * Alustaa luokan seuraavanEid:n yhdeksi
	 */
	public static void alustaSeuraavaEid() {
		seuraavaEid = 1;
	}


	/**
	 * palauttaa kenttien lkm
	 * @return kenttien lkm
	 */
	public int getKenttienLkm() {
		return 4;
	}


	/**
	 * palauttaa ensimmäisen kantän, joka on mielekäs kutsujalle tietää
	 * @return ensimmäinen mielekäs kenttä
	 */
	public int ekaKentta() {
		return 1;
	}


	/**
	 * Kertoo kutsujalle mikä tieto laitetaan mihinkin kenttään 
	 * @param kentanNumero kenttä jonka sisältöä kysytään
	 * @return kentän sisältö
	 */
	public String getKentanSisalto(int kentanNumero) {
		if (kentanNumero == 1) return nimi;						// switchiksi??
		if (kentanNumero == 2) return "Vuosi:\n" + vuosi; 
		if (kentanNumero == 3) return "Ohjaaja:\n TÄHÄN TARVITAAN OHJAAJAN HAKEMINEN"; 
		if (kentanNumero == 4) return "Avainsanat:\n" + avainsanat; 
		return "Ei sisältöä pyydetyllä arvolla " + kentanNumero;
	}


	/**
	 * Asettaa elokuvan vuoden
	 * @param vuosi joka asetetaan
	 */
	public void setVuosi(String vuosi) {
		if (vuosi.isEmpty()) return;
		this.vuosi = Integer.parseInt(vuosi);
	}


	/**
	 * asettaa elokuvan nimen
	 * @param nimi joka asetetaan
	 */
	public void setElokuvanNimi(String nimi) {
		this.nimi = nimi;		
	}


	/**
	 * asettaa elokuvan avainsanat
	 * @param avainsanat jotka asetetaan
	 */
	public void setAvainsanat(String avainsanat) {
		this.avainsanat = avainsanat + ", ";
	}
}
