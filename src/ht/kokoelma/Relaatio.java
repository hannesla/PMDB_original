/**
 * 
 */
package ht.kokoelma;

import java.io.PrintStream;

/**
 * Luokka jolla yhdistetään elokuvat ja henkilöt sekä kunkin henkilön roolit elokuvissa
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
	 * Kokeillaan Relaatio luokkaa luokan pääohjelmassa 
	 * @param args ei käytetä
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
	 * Relaatioon liittyvän henkilön id
	 * @return henkilön id
	 */
	public Relaatio liitosHenkilo() {
		hid = ht.kanta.ElokuvaTarkistus.arvoValille(1, 10);
		return this;
	}


	/**
	 * Relaatioon liittyvän elokuvan id
	 * @return elokuvan id
	 */
	public Relaatio liitosElokuva() {
		eid = ht.kanta.ElokuvaTarkistus.arvoValille(1, 10);
		return this;
	}


	/**
	 * relaatioon liittyvän henkilön roolin id
	 * @return roolin id
	 */
	public Relaatio liitosRooli() {
		rid = ht.kanta.ElokuvaTarkistus.arvoValille(1, 10);
		return this;
	}


	/**
	 * Asetetaan elokuvan, henkilön ja henkilön roolin id arvo relaatioon
	 * @param eid elokuvan id
	 * @param hid henkilön id
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
	 * Palauttaa henkilön id:n 
	 * @return henkilön id
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
	 * @param uusiHid henkilön id
	 * @param uusiRid roolin id
	 * @return tämän relaation
	 */
	public Relaatio asetaTiedot(String uusiEid, String uusiHid, String uusiRid) { // TODO: parametrit inteiksi
		this.eid = Integer.parseInt(uusiEid);
		this.hid = Integer.parseInt(uusiHid);
		this.rid = Integer.parseInt(uusiRid);		

		return this;
	}
}
