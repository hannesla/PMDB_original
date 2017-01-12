/**
 * 
 */
package ht.kokoelma;

import java.io.PrintStream;

/**
 * Luokka jolla yhdistet��n elokuvat ja henkil�t sek� kunkin henkil�n roolit elokuvissa
 * 
 * @author Hannes Laukkanen
 * @version Mar 9, 2016
 * hannes.v.laukkanen@student.jyu.fi
 */
public class Relaatio {
	private int eid;
	private int hid;
	private int rid;


	/**
	 * Kokeillaan Relaatio luokkaa luokan p��ohjelmassa 
	 * @param args ei k�ytet�
	 */
	public static void main(String[] args) {
		Relaatio h = new Relaatio();
		Relaatio k = new Relaatio();

		h.liitosElokuva().liitosHenkilo().liitosRooli();
		k.liitosElokuva().liitosHenkilo().liitosRooli();

		h.tulosta(System.out);	
		k.tulosta(System.out);	
	}


	/**
	 * Tulostaa Relaation tiedot
	 * @param out tuolostettava tietovirta
	 */
	public void tulosta(PrintStream out) {		
		out.println("Eid: " + eid + "\n" +
				"Hid: " + hid + "\n" + 
				"Rid: " + rid + "\n");		
	}


	/**
	 * Relaatioon liittyv�n henkil�n id
	 * @return henkil�n id
	 */
	public Relaatio liitosHenkilo() {
		hid = ht.kanta.ElokuvaTarkistus.arvoValille(1, 10);
		return this;
	}


	/**
	 * Relaatioon liittyv�n elokuvan id
	 * @return elokuvan id
	 */
	public Relaatio liitosElokuva() {
		eid = ht.kanta.ElokuvaTarkistus.arvoValille(1, 10);
		return this;
	}


	/**
	 * relaatioon liittyv�n henkil�n roolin id
	 * @return roolin id
	 */
	public Relaatio liitosRooli() {
		rid = ht.kanta.ElokuvaTarkistus.arvoValille(1, 10);
		return this;
	}


	/**
	 * Asetetaan elokuvan, henkil�n ja henkil�n roolin id arvo relaatioon
	 * @param eid elokuvan id
	 * @param hid henkil�n id
	 * @param rid roolin id
	 */
	public void set(int eid, int hid, int rid) {
		this.eid = eid;
		this.hid = hid;
		this.rid = rid;		
	}


	/**
	 * palauttaa elokuvan id:n
	 * @return elokuvan id
	 */
	public int getEid() {
		return eid;
	}


	/**
	 * Palauttaa henkil�n id:n 
	 * @return henkil�n id
	 */
	public int getHid() {
		return hid;
	}


	/**
	 * Palauttaa relaation tiedot tiedostoon tallennettavassa muodossa
	 * @example
	 * <pre name="test">
	 *   Relaatio test = new Relaatio();
	 *   test.asetaTiedot("5", "34", "100");
	 *   "5|34|100|".equals(test.toString()) === true;
	 *   "5|elokuva|1939|vansanat|".equals(test.toString()) === false;
	 * </pre>
	 */
	@Override
	public String toString() {
		return "" + getEid()  + "|" + getHid() + "|" + getRid() + "|";
	}


	/**
	 * palautaa relaation roolin id:n
	 * @return roolin id
	 */
	public int getRid() {
		return rid;
	}


	/**
	 * asettaa relaation tiedot
	 * @param uusiEid elokuvan id
	 * @param uusiHid henkil�n id
	 * @param uusiRid roolin id
	 * @return t�m�n relaation
	 */
	public Relaatio asetaTiedot(String uusiEid, String uusiHid, String uusiRid) { // TODO: parametrit inteiksi
		this.eid = Integer.parseInt(uusiEid);
		this.hid = Integer.parseInt(uusiHid);
		this.rid = Integer.parseInt(uusiRid);		

		return this;
	}
}
