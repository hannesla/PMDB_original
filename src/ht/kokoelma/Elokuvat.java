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

/**
 * Hallinnoi elokuvien viitteit‰ 
 * 
 * @author Hannes Laukkanen
 * @version 23.2.2016
 * hannes.v.laukkanen@student.jyu.fi
 */
public class Elokuvat {
	private int maxElokuvia = 5;
	private int lkm;
	private Elokuva[] alkiot = new Elokuva[maxElokuvia];


	/**
	 * Lis‰‰ elokuvan elokuvien taulukkoon
	 * @param elokuva elokuva joka lis‰t‰‰n
	 * @return palauttaa k‰sitellyn elokuvan
	 * @throws SailoException Poikkeus ilmoittaa, jos taulukkoon yritet‰‰n lis‰t‰ alkiota silloin, kun se on jo t‰ynn‰
	 *@example
	 * <pre name="test">
	 * #THROWS SailoException
	 * Elokuvat hen = new Elokuvat();
	 * hen.getLkm() === 0;
	 * hen.add(new Elokuva());
	 * hen.getLkm() === 1;
	 * hen.add(new Elokuva());
	 * hen.getLkm() === 2;
	 * while (hen.getLkm() <= hen.getMaksimi()-1)  {
	 *  	hen.add(new Elokuva());
	 * }
	 * hen.add(new Elokuva());
	 * </pre>
	 */
	public Elokuva add(Elokuva elokuva) throws SailoException {
		
		// T‰h‰n tarvittaisiin lukko, voisi menn‰ indeksit ja viitteet v‰‰rin
		// jos kaksi k‰ytt‰j‰‰ lis‰isi samaan aikaan
		if (lkm >= alkiot.length) {
			maxElokuvia += 5;

			Elokuva[] alkiotUusi = new Elokuva[maxElokuvia];

			for (int i = 0; i < alkiot.length; i++) {
				alkiotUusi[i] = alkiot[i];
			}

			alkiot = alkiotUusi;
		}

		elokuva.rekisteroi();
		alkiot[lkm] = elokuva;
		lkm++;

		return elokuva;
	}


	/**
	 * Palauttaa paljon mahtuu maksimissaan elokuvia nyt k‰ytˆss‰ olevaan taulukkoon
	 * @return alkioiden maksimim‰‰r‰
	 */
	public int getMaksimi() {
		return maxElokuvia;
	}


	/**
	 * Palatauttaa elokuvien lukum‰‰r‰n taulukossa
	 * @return elokuvien lukum‰‰r‰
	 */
	public int getLkm() {
		return lkm;
	}


	/**
	 * Palauttaa parametrina saamastaan kohdasta taulukossa lˆytyv‰n elokuvan viitteen
	 * @param i paikka josta elokuvaa haetaan
	 * @return paikasta lˆytyneen elokuvan viite
	 * @throws IndexOutOfBoundsException jos i ei ole sopiva taulukon paikka
	 * @example
	 * <pre name="test">
	 * #THROWS IndexOutOfBoundsException	  
	 * Elokuvat hen = new Elokuvat();
	 * hen.get(0);  #THROWS IndexOutOfBoundsException
	 * Elokuva h = new Elokuva();
	 * try {
	 * 	hen.add(h);
	 * } catch (Exception e) {
	 * 	e.getMessage();
	 * }
	 * Elokuva k = new Elokuva();
	 * hen.get(0) == h === true;
	 * hen.get(0) == k === false;
	 * hen.get(1);  #THROWS IndexOutOfBoundsException
	 * </pre>
	 */
	public Elokuva get(int i) throws IndexOutOfBoundsException {
		if (i < 0 || i > lkm - 1) throw new IndexOutOfBoundsException("Pyydetyss‰ indeksiss‰ ei alkioita: " + i);

		return alkiot[i];
	}


	/**
	 * Elovat luokan kokeilua varten
	 * @param args ei k‰ytet‰
	 */
	public static void main(String[] args) {
		Elokuvat elokuvia = new Elokuvat();

		Elokuva elokuva1 = new Elokuva(); 
		Elokuva elokuva2 = new Elokuva();
		Elokuva elokuva3 = new Elokuva();

		elokuva1.taytaTiedoilla();
		elokuva1.rekisteroi();

		elokuva2.taytaTiedoilla();
		elokuva2.rekisteroi();

		elokuva3.taytaTiedoilla();
		elokuva3.rekisteroi();

		try {
			elokuvia.add(elokuva1);


			elokuvia.add(elokuva2);


			System.out.println("---> T‰st‰ l‰htee kokeilu 1:\n");

			for (int i = 0; i < elokuvia.getLkm(); i++) {
				Elokuva elokuva = elokuvia.get(i);
				elokuva.tulosta(System.out);
			}

			elokuvia.add(elokuva3);

			System.out.println("---> T‰st‰ l‰htee kokeilu 2:\n");

			for (int i = 0; i < elokuvia.getLkm(); i++) { // TODO: huono ratkaisu, korvataan jossain vaiheessa fiksummalla
				Elokuva elokuva = elokuvia.get(i);
				elokuva.tulosta(System.out);
			}

		} catch (SailoException e) {
			System.err.println(e.getMessage());
		}
	}


	/**
	 * Tallentaa elokuvien tiedot annettuun polkuun
	 * @param tallennusPolku polku johon tallennetaan
	 * @throws SailoException jos tallennuksessa tapahtuu pokkeuksia
	 * @example
	 * <pre name="test">
	 * // tallennus testattu samassa lueTiedoston() kanssa 
	 * </pre>
	 */
	public void tallenna(String tallennusPolku) throws SailoException {
		String tiedNimi = tallennusPolku + "\\" + "elokuvat.dat";
		try (PrintStream fo = new PrintStream(new FileOutputStream(tiedNimi, false))) {
			for (Elokuva elokuva : alkiot) {
				if (elokuva == null) break;
				fo.printf(elokuva.toString() + "\n");
			}
		}catch ( FileNotFoundException ex ) {
			throw new SailoException("Tiedoston " + tiedNimi + " avaamisessa tapahtui virhe");
		}
	}


	/**
	 * lukee elokuvat.dat tiedoston annetusta polusta
	 * @param polku mist‰ luetaan
	 * @throws SailoException jos tiedostoa ei ole
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException
	 * #import java.io.File;
	 * Elokuvat test = new Elokuvat();
	 * test.testiTallennus();
	 * Elokuva yks = new Elokuva(); yks.rekisteroi(); yks.taytaTiedoilla();
	 * Elokuva kaks = new Elokuva(); kaks.rekisteroi(); kaks.taytaTiedoilla();
	 * test.tallenna("test"); 
	 * test.add(yks); test.add(kaks);
	 * test.tallenna("test");
	 * test.alusta();
	 * test.lueTiedosto("test2");
	 * test.get(0); #THROWS IndexOutOfBoundsException // varmistaa ett‰ nyt on "auki" eri tiedosto
	 * test.alusta();
	 * test.lueTiedosto("test");
	 * yks.toString().equals(test.get(0).toString()) === true;
	 * kaks.toString().equals(test.get(1).toString()) === true;
	 * kaks.toString().equals(test.get(0).toString()) === false; // varmistaa ettei l‰pimeno johdu testiss‰ olevista ongelmista
	 * test.poistaTestitiedostot();
	 * </pre>
	 */
	public void lueTiedosto(String polku) throws SailoException {
		alkiot = new Elokuva[maxElokuvia];
		String tiedNimi = polku + "\\" + "elokuvat.dat";

		try (Scanner fi = new Scanner(new FileInputStream(new File(tiedNimi)))) {

			while (fi.hasNext()) {
				String s = fi.nextLine();
					
				String[] palat = s.split("\\|");
				//		if (palat.length < 2) continue;

				Elokuva elokuva = new Elokuva();

				try {
					this.add(elokuva); 
				} catch (SailoException e) { // ei heit‰ en‰‰ t‰t‰ pokkeusta, koska taulukko on dynaaminen
					System.err.println("Elokuvan lis‰‰minen ei onnistunut: " + e.getMessage());
				}

				elokuva.asetaTiedot(palat[0], palat[1], palat[2], palat[3]);
			}
		} catch ( FileNotFoundException e ) {
			throw new SailoException("Tiedostoa polussa: " + tiedNimi + " ei ole.");
		}
	}


	/**
	 * Alustaa elokuvat k‰ytett‰v‰ksi seuraavaa k‰ytt‰j‰‰ varten
	 */
	public void alusta() {
		Elokuva.alustaSeuraavaEid();
		lkm = 0;
		alkiot = new Elokuva[maxElokuvia];		
	}	


	/**
	 * Metodi avustamaan tiedostojen lukemisen ja tallentamisen testaamista
	 */
	public void testiTallennus() {
		File polku = new File("test");
		File polku2 = new File("test2");

		polku.mkdir();
		polku2.mkdir();

		File ftied = new File("test\\elokuvat.dat");
		File ftied2 = new File("test2\\elokuvat.dat");

		try {
			ftied.createNewFile();
			ftied2.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}	

	/**
	 * Poistaa testeiss‰ k‰ytetyt tiedostot
	 */
	public void poistaTestitiedostot() {
		File polku = new File("test");
		File polku2 = new File("test2");

		File ftied = new File("test\\elokuvat.dat");
		File ftied2 = new File("test2\\elokuvat.dat");

		ftied.delete();
		ftied2.delete();
		polku.delete();
		polku2.delete();
	}


	/**
	 * Poistaa elokuvan elokuvista
	 * @param poistettava elokuva joka poistetaan
	 *@example
	 * <pre name="test">
	 * #THROWS SailoException
	 * Elokuvat el = new Elokuvat();
	 * Elokuva e = new Elokuva();
	 * Elokuva e2 = new Elokuva();
	 * e.asetaTiedot("" + 23, "Testi Elokuva", "" + 1999, "avainsanat");
	 * e2.asetaTiedot("" + 24, "Testi Elokuva", "" + 1999, "avainsanat");
	 * el.add(e);
	 * el.add(e2);
	 * el.getLkm() === 2;
	 * el.poista(e);
	 * el.getLkm() === 1;
	 * el.poista(e);
	 * el.getLkm() === 1; 
	 * el.poista(e2);
	 * el.getLkm() === 0;
	 * </pre>
	 */
	public void poista(Elokuva poistettava) {		
				
		Elokuva[] muutettu = new Elokuva[maxElokuvia];
		
		int i = 0;
		int j = 0;
		Boolean loytyi = false;
		
		while(i < lkm) {
			if (alkiot[i].getEid() == poistettava.getEid()) {
				i++;
				loytyi = true;
				continue;
			}
			
			muutettu[j] = alkiot[i];
			
			i++;
			j++;
		}

		if (loytyi) {
			alkiot = muutettu;
			lkm--;
		}
	}
}