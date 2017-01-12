/**
 * 
 */
package ht.kokoelma;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import fi.jyu.mit.fxgui.Dialogs;

/**
 * Tällä hallinnoidaan henkiloita
 * 
 * @author Hannes Laukkanen
 * @version Mar 9, 2016
 * hannes.v.laukkanen@student.jyu.fi
 */
public class Henkilot {

	private int maxHenkiloita = 5;
	private int lkm;
	private Henkilo[] alkiot = new Henkilo[maxHenkiloita];


	/**
	 * Kokeillaan Henkilot-luokkaa
	 * @param args ei käytetä
	 */
	public static void main(String[] args) {
		Henkilot nayttelijat = new Henkilot();

		try {
			nayttelijat.add(new Henkilo().taytaTiedoilla().rekisteroi());
			nayttelijat.add(new Henkilo().taytaTiedoilla().rekisteroi());
		} catch (SailoException e) {
			Dialogs.showMessageDialog("Tapahtui virhe uuden henkilön lisäämisessä" + e.getMessage());
			return;
		}

		for (int i = 0; i < nayttelijat.getLkm(); i++) {
			nayttelijat.get(i).tulosta(System.out);
		}
	}


	/**
	 * Palauttaa paljon mahtuu maksimissaan henkilöitä nyt käytössä olevaan taulukkoon
	 * @return alkioiden maksimimäärä
	 */
	public int getMaksimi() {
		return maxHenkiloita;
	}


	/**
	 * palauttaa kuinka monta henkilöä olio kattaa
	 * @return henkilöden määrä
	 */
	public int getLkm() {
		return lkm;
	}


	/**
	 * palauttaa viitteen henkilöön pyydetystä paikasta
	 * @param i indeksi josta henkilöä pyydetään
	 * @return viite henkilöön
	 * @throws IndexOutOfBoundsException jos indeksi on alle 0 tai suurempi kuin alkioita taulukossa
	 * @example
	 * <pre name="test">
	 * #THROWS IndexOutOfBoundsException	  
	 * Henkilot hen = new Henkilot();
	 * hen.get(0);  #THROWS IndexOutOfBoundsException
	 * Henkilo h = new Henkilo();
	 * try {
	 * 	hen.add(h);
	 * } catch (Exception e) {
	 * 	e.getMessage();
	 * }
	 * Henkilo k = new Henkilo();
	 * hen.get(0) == h === true;
	 * hen.get(0) == k === false;
	 * hen.get(1);  #THROWS IndexOutOfBoundsException
	 * </pre>
	 */
	public Henkilo get(int i) throws IndexOutOfBoundsException {
		if (i < 0 || i > this.lkm - 1) throw new IndexOutOfBoundsException("Indeksi on epäkelpo");

		return alkiot[i];		
	}


	/**
	 * Lisää henkilön
	 * @param henkilo henkilö joka lisätään
	 * @return palauttaa viitteen käsiteltyyn henkilöön
	 * @throws SailoException jos taulukkoon ei mahdu enempää henkilöitä
	 *@example
	 * <pre name="test">
	 * #THROWS SailoException
	 * Henkilot hen = new Henkilot();
	 * hen.getLkm() === 0;
	 * hen.add(new Henkilo());
	 * hen.getLkm() === 1;
	 * hen.add(new Henkilo());
	 * hen.getLkm() === 2;
	 * while (hen.getLkm() <= hen.getMaksimi()-1)  {
	 *  	hen.add(new Henkilo());
	 * }
	 * hen.add(new Henkilo());
	 * </pre>
	 */
	public Henkilo add(Henkilo henkilo) throws SailoException {
		
		// Tähän tarvittaisiin lukko, voisi mennä indeksit ja viitteet väärin
		// jos kaksi käyttäjää lisäisi samaan aikaan
		if (lkm >= alkiot.length) {
			maxHenkiloita += 5;

			Henkilo[] alkiotUusi = new Henkilo[maxHenkiloita];

			for (int i = 0; i < alkiot.length; i++) {
				alkiotUusi[i] = alkiot[i];
			}

			alkiot = alkiotUusi;
		}	

		alkiot[lkm] = henkilo;		
		lkm++;

		return henkilo;
	}


	/**
	 * tallentaa henkilöt annettuun polkuun
	 * @param tallennusPolku polku johon tallennetaan
	 * @throws SailoException jos tallentamisessa ongelmia
	 * @example
	 * <pre name="test">
	 * // tallennus testattu samassa lueTiedoston() kanssa 
	 * </pre>
	 */
	public void tallenna(String tallennusPolku) throws SailoException {
		String tiedNimi = tallennusPolku + "\\" + "henkilot.dat";
		try (PrintStream fo = new PrintStream(new FileOutputStream(tiedNimi, false))) {
			for (Henkilo henkilo : alkiot) {
				if (henkilo == null) break;
				fo.printf(henkilo.toString() + "\n");
			}
		} catch (FileNotFoundException ex) {
			throw new SailoException("Tiedoston " + tiedNimi + " avaamisessa tapahtui virhe");
		}
	}


	/**
	 * lukee henkilöt annetusta polusta
	 * @param polku polku josta luetaan
	 * @throws SailoException jos tiedostoa ei ole
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException
	 * #import java.io.File;
	 * Henkilot test = new Henkilot();
	 * test.testiTallennus();
	 * Henkilo yks = new Henkilo(); yks.rekisteroi(); yks.taytaTiedoilla();
	 * Henkilo kaks = new Henkilo(); kaks.rekisteroi(); kaks.taytaTiedoilla();
	 * test.tallenna("test"); 
	 * test.add(yks); test.add(kaks);
	 * test.tallenna("test");
	 * test.alusta();
	 * test.lueTiedosto("test2");
	 * test.get(0); #THROWS IndexOutOfBoundsException // varmistaa että nyt on "auki" eri tiedosto
	 * test.alusta();
	 * test.lueTiedosto("test");
	 * yks.toString().equals(test.get(0).toString()) === true;
	 * kaks.toString().equals(test.get(1).toString()) === true;
	 * kaks.toString().equals(test.get(0).toString()) === false; // varmistaa ettei läpimeno johdu testissä olevista ongelmista
	 * test.poistaTestitiedostot();
	 * </pre>
	 */
	public void lueTiedosto(String polku) throws SailoException {	        
		alkiot = new Henkilo[maxHenkiloita];
		String tiedNimi = polku + "\\" + "henkilot.dat";

		try (Scanner fi = new Scanner(new FileInputStream(new File(tiedNimi)))) {                  
			while (fi.hasNext()) {
				String s = fi.nextLine();
				String[] palat = s.split("\\|");

				Henkilo henkilo = new Henkilo();

				try {
					this.add(henkilo);
				} catch (SailoException e) {
					System.err.println("Tiedoston avaaminen ei onnistunut: " + e.getMessage());
				}

				henkilo.asetaTiedot(palat[0], palat[1]);

			}
		} catch ( FileNotFoundException e ) {
			throw new SailoException("Tiedostoa polussa: " + tiedNimi + " ei ole.");
		}
	}
	

	/**
	 * Alustaa henkilöt seuraavaa käyttäjän latausta varten
	 */
	public void alusta() {
		Henkilo.alustaSeuraavaHid();
		lkm = 0;
		alkiot = new Henkilo[maxHenkiloita];		
	}


	/**
	 * Metodi avustamaan tiedostojen lukemisen ja tallentamisen testaamista
	 */
	public void testiTallennus() {
		File polku = new File("test");
		File polku2 = new File("test2");

		polku.mkdir();
		polku2.mkdir();

		File ftied = new File("test\\henkilot.dat");
		File ftied2 = new File("test2\\henkilot.dat");

		try {
			ftied.createNewFile();
			ftied2.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}	


	/**
	 * Poistaa testeissä käytetyt tiedostot
	 */
	public void poistaTestitiedostot() {
		File polku = new File("test");
		File polku2 = new File("test2");

		File ftied = new File("test\\henkilot.dat");
		File ftied2 = new File("test2\\henkilot.dat");

		ftied.delete();
		ftied2.delete();
		polku.delete();
		polku2.delete();
	}


	/**
	 * selvittää löytyykö henkilö henkilöistä
	 * @param henkilo henkilön nimi
	 * @return löytyikö vai ei
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException
	 * Henkilot hen = new Henkilot();
	 * hen.add(new Henkilo("Testi Henkilö"));
	 * hen.loytyyko("Testi Henkilö") === true;
	 * hen.loytyyko("Joku Muu") === false;
	 * </pre>
	 */
	public boolean loytyyko(String henkilo) {

		for (int i = 0; i < lkm; i++) {
			if (henkilo.equals(get(i).getNimi())) return true;
		}

		return false;
	}


	/**
	 * Etstii henkilöä nimeltä
	 * @param nimi henkilön nimi
	 * @return löytynyt henkilö tai null jos ei löytynyt
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException
	 * Henkilot hen = new Henkilot();
	 * Henkilo h = new Henkilo("Testi Henkilö");
	 * hen.add(h);
	 * hen.etsiHenkilo("Testi Henkilö") == h === true;
	 * hen.etsiHenkilo("Joku Muu") === null;
	 * </pre>
	 */
	public Henkilo etsiHenkilo(String nimi) {		
		for (int i = 0; i < lkm; i++) {
			if (nimi.equals(get(i).getNimi())) return get(i);
		}

		return null;
	}


	/**
	 * etsii henkilön id:n perusteella
	 * @param hid henkilön id
	 * @return löytynyt henkilö tai null jos ei löytynyt
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException
	 * Henkilot hen = new Henkilot();
	 * Henkilo h = new Henkilo();
	 * h.asetaTiedot("" + 5, "Testi Henkilö");
	 * hen.add(h);
	 * hen.etsiHenkilo(5) == h === true;
	 * hen.etsiHenkilo(5) == new Henkilo("Ei Tämä") === false;
	 * </pre>
	 */
	public Henkilo etsiHenkilo(int hid) {

		for (int i = 0; i < lkm; i++) {
			if (alkiot[i].getHid() == hid) return get(i);
		}

		return null;
	}
}