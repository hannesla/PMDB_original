/**
 * 
 */
package ht.kokoelma;

import java.io.PrintStream;

/**
 * Luokka jolla luodaan henkil�it�
 * 
 * @author Hannes Laukkanen
 * @version Mar 9, 2016
 * hannes.v.laukkanen@student.jyu.fi
 */
public class Henkilo {

	private int hid;
	private String nimi;

	private static int seuraavaHid = 1;


	/**
	 * Luo henkil�n ilman ett� asettaa attribuutteja
	 */
	public Henkilo() {
		//
	}


	/**
	 * Luo henkil�n nimell� varustettuna
	 * @param nimi henkil�n nimi
	 */
	public Henkilo(String nimi) {
		this.nimi = nimi;
	}


	/**
	 * Antaa henkilolle yksiloivan numeron
	 * @return palauttaa k�sitellyn Henkilon viitteen (jotta voidaan ketjuttaa metodeja)
	 * @example
	 * <pre name="test">
	 * Henkilo test1 = new Henkilo();
	 * Henkilo test2 = new Henkilo();
	 * test1.getHid() === 0;
	 * test2.getHid() === 0;
	 * test1.rekisteroi();
	 * test2.rekisteroi();
	 * test2.getHid() - test1.getHid() == 1 === true; // testattava n�in, koska testien suoritusj�rjestyst� ei voi tiet��
	 * </pre>
	 */
	public Henkilo rekisteroi() {
		this.hid = seuraavaHid;

		seuraavaHid++;

		return this;
	}


	/**
	 * T�ytt�� olion esimerkkitiedoilla joita siihen voitaisiin sy�tt��
	 * @return viitteen k�siteltyyn henkil��n
	 * @example
	 * <pre name="test">	 * 
	 * Henkilo e1 = new Henkilo(); 
	 * e1.taytaTiedoilla();
	 * e1.getNimi().matches("Hannes Hoo [0-9]{4}") === true;
	 * </pre>
	 */
	public Henkilo taytaTiedoilla() {
		this.nimi = "Hannes Hoo " + ht.kanta.ElokuvaTarkistus.arvoValille(1000, 9999);
		return this;
	}


	/**
	 * P��ohjelmaa k�ytet��n henkilo-luokan kokeiluun
	 * @param args ei k�ytet�
	 */
	public static void main(String[] args) {

		Henkilo h = new Henkilo();
		Henkilo k = new Henkilo();

		h.taytaTiedoilla().rekisteroi();
		k.taytaTiedoilla().rekisteroi();

		h.tulosta(System.out);	
		k.tulosta(System.out);
	}


	/**
	 * Tulostaa henkilon tiedot
	 * @param out tietovirta johon tulostetaan
	 */
	public void tulosta(PrintStream out) {
		out.println(nimi + " " + hid);
	}


	/**
	 * palauttaa henkil�n tunnusnumeron
	 * @return tunnusnumero
	 */
	public int getHid() {
		return hid;
	}


	/**
	 * palauttaa henkil�n nimen
	 * @return henkil�n nimi
	 */
	public String getNimi() {
		return nimi;
	}


	/**
	 * Palauttaa henkil�n tiedot tiedostoon tallennettavassa muodossa
	 * @example
	 * <pre name="test">
	 *   Henkilo test = new Henkilo();
	 *   test.asetaTiedot("5", "Brad Pitt");
	 *   "5|Brad Pitt|".equals(test.toString()) === true;
	 *   "5|elokuva|1939|vansanat|".equals(test.toString()) === false;
	 * </pre>
	 */
	@Override
	public String toString() {
		return "" + getHid()  + "|" + getNimi() + "|";
	}


	/**
	 * asettaa henkil�n tiedot
	 * @param uusiHid henkil�n tunnitus numero
	 * @param uusiNimi henkil�n nimi
	 */
	public void asetaTiedot(String uusiHid, String uusiNimi) {
		this.hid = Integer.parseInt(uusiHid);
		this.nimi = uusiNimi;

		seuraavaHid += 1;
	}


	/**
	 * alustaa henkil�t seuraavksi ladattavaa k�ytt�j�nime� varten
	 */
	public static void alustaSeuraavaHid() {
		seuraavaHid = 1;
	}
}
