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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Hallinnoi tietokaannassa olevia relaatioita
 * 
 * @author Hannes Laukkanen
 * @version Apr 19, 2016
 * hannes.v.laukkanen@student.jyu.fi
 */
public class Relaatiot {
	private List<Relaatio> relaatiot = new ArrayList<Relaatio>();


	/**
	 * Pääohjelmaa käytetään luokan kokeilemiseen
	 * @param args ei käytetä
	 */
	public static void main(String[] args) {
		Relaatiot relaatiot = new Relaatiot();

		relaatiot.add(new Relaatio().liitosElokuva().liitosHenkilo().liitosRooli());
		relaatiot.add(new Relaatio().liitosElokuva().liitosHenkilo().liitosRooli());

		for (int i = 0; i < relaatiot.getLkm(); i++) {
			System.out.println("Testataan relaatiot-luokkaa");
			relaatiot.get(i).tulosta(System.out);
		}
	}


	/**
	 * palauttaa relaatioiden lukumäärän
	 * @return relaatioiden lukumäärä
	 */
	public int getLkm() {
		return relaatiot.size();
	}


	/**
	 * palauttaa pyydetyn relaation viitteen
	 * @param i indeksi josta pyydetään
	 * @return relaation viite
	 * @throws IndexOutOfBoundsException jos epäkelpo indeksi
	 * @example
	 * <pre name="test">
	 * #THROWS IndexOutOfBoundsException
	 * Relaatiot rel = new Relaatiot();
	 * rel.get(0); #THROWS IndexOutOfBoundsException
	 * Relaatio r = new Relaatio();
	 * rel.add(r);
	 * rel.get(0) == r === true;
	 * rel.get(0) == new Relaatio() === false; 
	 * rel.get(1); #THROWS IndexOutOfBoundsException
	 * </pre>
	 */
	public Relaatio get(int i) throws IndexOutOfBoundsException {
		if (i < 0 || i > getLkm() - 1) throw new IndexOutOfBoundsException();

		return relaatiot.get(i);
	}


	/**
	 * Lisää relaation listaan
	 * @param relaatio lisättävä relaatio
	 * @return lisätty relaatio
	 * @example
	 * <pre name="test">
	 * Relaatiot rel = new Relaatiot();
	 * rel.getLkm() === 0;
	 * rel.add(new Relaatio());
	 * rel.getLkm() === 1;
	 * </pre> 
	 */
	public Relaatio add(Relaatio relaatio) {
		relaatiot.add(relaatio);

		return relaatio;
	}


	/**
	 * Tallentaa relaatiot annettuun polkuun
	 * @param polku johon tallennetaan
	 * @throws SailoException jos tellantamisessa ongelmia
	 * @example
	 * <pre name="test">
	 * // tallennus testattu samassa lueTiedoston() kanssa 
	 * </pre>
	 */
	public void tallenna(String polku) throws SailoException {
		String tiedNimi = polku + "\\" + "relaatiot.dat";
		try (PrintStream fo = new PrintStream(new FileOutputStream(tiedNimi, false))) {            	
			for (Relaatio rela : relaatiot) {
				if (rela == null) break;
				fo.printf(rela.toString() + "\n");
			}
		} catch (FileNotFoundException ex) {
			throw new SailoException("Tiedoston " + tiedNimi + " avaamisessa tapahtui virhe");
		}

	}


	/**
	 * Lukee relaatiot.dat tiedoston annetusta polusta
	 * @param polku polku josta luetaan
	 * @throws SailoException jos tiedostoa ei ole
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException
	 * #import java.io.File;
	 * Relaatiot test = new Relaatiot();
	 * test.testiTallennus();
	 * Relaatio yks = new Relaatio();
	 * Relaatio kaks = new Relaatio();
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
	 * test.poistaTestitiedostot();
	 * </pre>
	 */
	public void lueTiedosto(String polku) throws SailoException {
		relaatiot = new ArrayList<Relaatio>();

		String tiedNimi = polku + "\\" + "relaatiot.dat";

		try (Scanner fi = new Scanner(new FileInputStream(new File(tiedNimi)))) {
			while ( fi.hasNext() ) {

				String s = fi.nextLine();
				String[] palat = s.split("\\|");

				Relaatio relaatio = new Relaatio();        		

				this.add(relaatio);

				relaatio.asetaTiedot(palat[0], palat[1], palat[2]);


			}
		} catch ( FileNotFoundException e ) {
			throw new SailoException("Tiedostoa polussa: " + tiedNimi + " ei ole.");
		}
	}

	/**
	 * Alustaa relaatio seuraavaa käyttäjää varten
	 */
	public void alusta() {
		relaatiot = new ArrayList<Relaatio>();		
	}


	/**
	 * Metodi avustamaan tiedostojen lukemisen ja tallentamisen testaamista
	 */
	public void testiTallennus() {
		File polku = new File("test");
		File polku2 = new File("test2");

		polku.mkdir();
		polku2.mkdir();

		File ftied = new File("test\\relaatiot.dat");
		File ftied2 = new File("test2\\relaatiot.dat");

		try {
			ftied.createNewFile();
			ftied2.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}	


	/**
	 * Poistaa testeissä käytetyt tiedostot
	 */
	public void poistaTestitiedostot() {
		File polku = new File("test");
		File polku2 = new File("test2");

		File ftied = new File("test\\relaatiot.dat");
		File ftied2 = new File("test2\\relaatiot.dat");

		ftied.delete();
		ftied2.delete();
		polku.delete();
		polku2.delete();
	}


	/**
	 * Etsii relaation jossa id:t vastaavat kutsun parametreja
	 * @param eid elokuvan id
	 * @param hid henkilön id
	 * @param rid roolin id
	 * @return löytynyt relaatio tai null jos ei löytynyt
	 * @example
	 * <pre name="test">
	 * Relaatio r = new Relaatio().asetaTiedot("" + 1, "" + 2, "" + 3);
	 * Relaatio r2 = new Relaatio().asetaTiedot("" + 4, "" + 5, "" + 6);
	 * Relaatiot rel = new Relaatiot();
	 * rel.add(r);
	 * rel.add(r2);
	 * Relaatio e = rel.etsiRelaatio(1, 2, 3);
	 * e == r === true;
	 * e == r2 === false;
	 * rel.etsiRelaatio(7, 2, 3) === null;
	 * </pre>
	 */
	public Relaatio etsiRelaatio(int eid, int hid, int rid) {

		for (Relaatio relaatio : relaatiot) {
			if (relaatio.getEid() == eid && relaatio.getHid() == hid && relaatio.getRid() == rid) return relaatio;
		}

		return null;
	}


	/**
	 * Poistaa relaation, jonka id:t vastaavat parametrina saatuja
	 * @param eid elokuva id
	 * @param hid henkilö id
	 * @param rid rooli id
	 * @example
	 * <pre name="test">
	 * Relaatio r = new Relaatio().asetaTiedot("" + 1, "" + 2, "" + 3);
	 * Relaatio r2 = new Relaatio().asetaTiedot("" + 4, "" + 5, "" + 6);
	 * Relaatiot rel = new Relaatiot();
	 * rel.add(r);
	 * rel.add(r2);
	 * rel.getLkm() === 2;
	 * rel.poista(1, 2, 3);
	 * rel.getLkm() === 1;
	 * </pre>
	 */
	public void poista(int eid, int hid, int rid) {

		int i = -1;
		boolean loytyi = false;

		for (Relaatio relaatio : relaatiot) {
			if (relaatio.getEid() == eid && relaatio.getHid() == hid && relaatio.getRid() == rid) {
				i = relaatiot.indexOf(relaatio);
				loytyi = true;
			}		
		}

		if (loytyi) relaatiot.remove(i);
	}


	/**
	 * poistaa relaation elokuvan id:n ja roolin id:n perusteella (eli henkilö poistetaan suorittamasta
	 * roolia elokuvassa)
	 * @param eid elokuvan id
	 * @param rid roolin id
	 * @example
	 * <pre name="test">
	 * Relaatio r = new Relaatio().asetaTiedot("" + 1, "" + 2, "" + 3);
	 * Relaatio r2 = new Relaatio().asetaTiedot("" + 4, "" + 5, "" + 6);
	 * Relaatiot rel = new Relaatiot();
	 * rel.add(r);
	 * rel.add(r2);
	 * rel.getLkm() === 2;
	 * rel.poista(1, 3);
	 * rel.getLkm() === 1;
	 * </pre>
	 */
	public void poista(int eid, int rid) {

		int i = -1;
		boolean loytyi = false;

		for (Relaatio relaatio : relaatiot) {
			if (relaatio.getEid() == eid && relaatio.getRid() == rid) {
				i = relaatiot.indexOf(relaatio);
				loytyi = true;
			}		
		}

		if (loytyi) relaatiot.remove(i);

	}


	/**
	 * poistaa kaikki elokuvaan liittyvät relaatiot
	 * @param eid elokuvan id
	 * @example
	 * <pre name="test">
	 * Relaatio r2 = new Relaatio().asetaTiedot("" + 4, "" + 5, "" + 6);
	 * Relaatio r = new Relaatio().asetaTiedot("" + 1, "" + 2, "" + 3);
	 * Relaatio r3 = new Relaatio().asetaTiedot("" + 1, "" + 5, "" + 6);
	 * Relaatiot rel = new Relaatiot();
	 * rel.add(r);
	 * rel.add(r2);
	 * rel.add(r3);
	 * rel.getLkm() === 3;
	 * rel.poista(1);
	 * rel.getLkm() === 1;
	 * </pre>
	 */
	public void poista(int eid) { // Ongelmana oli, että poiston jälkeen siirtyi seuraavaan indeksiin hypäten yhden yli

		int jaljella = relaatiot.size();
		int i = 0;

		while (i < jaljella) {		
			if (relaatiot.get(i).getEid() == eid) {
				relaatiot.remove(relaatiot.get(i));
				jaljella--;
				continue;
			}
			
			i++;
		}
	}

	
	/**
	 * Etsii relaation elokuvan id:n ja roolin id:n perusteella
	 * @param eid elokuvan id
	 * @param rid roolin id
	 * @return löytynyt relaatio tai null jos relaatiota ei ole 
	 * <pre name="test">
	 * Relaatio r = new Relaatio().asetaTiedot("" + 1, "" + 2, "" + 3);
	 * Relaatio r2 = new Relaatio().asetaTiedot("" + 4, "" + 5, "" + 6);
	 * Relaatiot rel = new Relaatiot();
	 * rel.add(r);
	 * rel.add(r2);
	 * Relaatio e = rel.etsiRelaatio(1, 3);
	 * e == r === true;
	 * e == r2 === false;
	 * rel.etsiRelaatio(7, 3) === null;
	 * </pre>
	 */
	public Relaatio etsiRelaatio(int eid, int rid) {
		for (Relaatio r : relaatiot) {
			if (r.getEid() == eid && r.getRid() == rid) return r;
		}

		return null;
	}
}