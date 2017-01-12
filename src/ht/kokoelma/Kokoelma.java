/**
 * 
 */
package ht.kokoelma;

import java.io.PrintStream;

import ht.kanta.ElokuvaTarkistus;

/** 
 * Luokka joka huolehtii tietokannan hallinnasta välittäjämetodien avulla
 * 
 * @author Hannes Laukkanen
 * @version 24.2.2016
 * hannes.v.laukkanen@student.jyu.fi
 */
public class Kokoelma {
	private String kokoelmanPolku;
	private final Elokuvat elokuvat = new Elokuvat();
	private final Henkilot henkilot = new Henkilot();
	private final Roolit   roolit   = new Roolit();
	private final Relaatiot relaatiot  = new Relaatiot();
	private Elokuva elokuvaKasittelyssa = null; //TODO: olisiko parempi ratkaisu, jossa käsiteltävä elokuva on käyttöliittymän asia(?)


	/**
	 * Lisää Elokuvan kokoelmaan
	 * @param elokuva listäätävä elokuva
	 * @return Elokuva joka lisättiin
	 * @throws SailoException elokuvaa ei voi lisätä
	 */
	public Elokuva add(Elokuva elokuva) throws SailoException {
		return elokuvat.add(elokuva);
	}


	/**
	 * Lisää Elokuvan kokoelmaan
	 * @param relaatio listäätävä elokuva
	 * @return Relaatio joka lisättiin
	 * @throws SailoException elokuvaa ei voi lisätä
	 */
	public Relaatio add(Relaatio relaatio) throws SailoException {
		return relaatiot.add(relaatio);
	}


	/**
	 * Lisää Henkilon kokoelmaan
	 * @param henkilo lisättävä henkilo
	 * @return palautaa viitteen lisättyyn henkilöön
	 * @throws SailoException Henkiloa ei voi lisätä
	 */
	public Henkilo add(Henkilo henkilo) throws SailoException {
		return henkilot.add(henkilo);
	}


	/**
	 * Kertoo kuinka monta elokuvaa elokuvat luokassa
	 * @return elokuvien lukumäärä
	 */
	public int getElokuvienLkm() {
		return elokuvat.getLkm();
	}


	/**
	 * Palauttaa elokuvan parametrina saamastaan paikasta
	 * @param i indeksi josta elokuva haetaan
	 * @return elokuva joka paikasta löytyi
	 */
	public Elokuva getElokuva(int i) {
		return elokuvat.get(i);
	}


	/**
	 * Kokeilllaan kokoelma-luokan käyttöä
	 * @param args ei käytetä
	 */
	public static void main(String[] args) {
		Kokoelma kokoelma = new Kokoelma();

		Elokuva elokuva1 = new Elokuva(); 
		Elokuva elokuva2 = new Elokuva();

		Henkilo henkilo1 = new Henkilo().taytaTiedoilla().rekisteroi();
		Henkilo henkilo2 = new Henkilo().taytaTiedoilla().rekisteroi();

		Rooli rooli1  = new Rooli("rooli").taytaTiedoilla().rekisteroi();
		Rooli rooli2  = new Rooli("toinenRollii").taytaTiedoilla().rekisteroi();

		Relaatio relaatio1  = new Relaatio().liitosElokuva().liitosHenkilo().liitosRooli();
		Relaatio relaatio2  = new Relaatio().liitosElokuva().liitosHenkilo().liitosRooli();

		elokuva1.taytaTiedoilla();
		elokuva1.rekisteroi();

		elokuva2.taytaTiedoilla();
		elokuva2.rekisteroi();

		System.out.println("Kokeillaan elokuvia:");

		try {
			kokoelma.add(elokuva1);

			kokoelma.add(elokuva2);

			for (int i = 0; i < kokoelma.getElokuvienLkm(); i++) { // TODO: huono ratkaisu, korvataan jossain vaiheessa fiksummalla
				Elokuva elokuva = kokoelma.getElokuva(i);
				elokuva.tulosta(System.out);
			}
		} catch (SailoException e) {
			System.err.println(e.getMessage());
		}

		System.out.println("\nKokeillaan henkiloita:");

		try {
			kokoelma.add(henkilo1);

			kokoelma.add(henkilo2);

			for (int i = 0; i < kokoelma.getHenkiloidenLkm(); i++) { // TODO: huono ratkaisu, korvataan jossain vaiheessa fiksummalla
				Henkilo henkilo = kokoelma.getHenkilo(i);
				henkilo.tulosta(System.out);
			}
		} catch (SailoException e) {
			System.err.println(e.getMessage());
		}

		System.out.println("\nKokeillaan rooleja");

		try {
			kokoelma.add(rooli1);

			kokoelma.add(rooli2);

			for (int i = 0; i < kokoelma.getRoolienLkm(); i++) { // TODO: huono ratkaisu, korvataan jossain vaiheessa fiksummalla
				Rooli rooli = kokoelma.getRooli(i);
				rooli.tulosta(System.out);
			}
		} catch (Exception e) { // TODO: mieti onko sopiva catch
			System.err.println(e.getMessage());
		}

		System.out.println("\nKokeillaan relaatioita");

		try {
			kokoelma.add(relaatio1);

			kokoelma.add(relaatio2);

			for (int i = 0; i < kokoelma.getRelaatioidenLkm(); i++) { // TODO: huono ratkaisu, korvataan jossain vaiheessa fiksummalla
				Relaatio rel = kokoelma.getRelaatio(i);
				rel.tulosta(System.out);
			}
		} catch (Exception e) { // TODO: mieti onko sopiva catch
			System.err.println(e.getMessage());
		}
	}


	/**
	 * Palauttaa relaation annetusta indeksistä
	 * @param i indeksi josta haetaan
	 * @return viite relaatioon
	 */
	public Relaatio getRelaatio(int i) {
		return relaatiot.get(i);
	}


	/**
	 * palauttaa relaatioiden lukumäärän
	 * @return relaatioiden lukumäärä
	 */
	public int getRelaatioidenLkm() {
		return relaatiot.getLkm();
	}


	/**
	 * Lisää roolin tietokantaan
	 * @param rooli lisättävä rooli
	 */
	public void add(Rooli rooli) {
		roolit.add(rooli);
	}


	/**
	 * palauttaa roolin pyydetystä indeksistä
	 * @param i indeksi josta roolia haetaan
	 * @return roolin viite
	 */
	public Rooli getRooli(int i) {
		return roolit.get(i);
	}


	/**
	 * Paluttaa roolien lukumäärän
	 * @return roolien lukumäärä
	 */
	public int getRoolienLkm() {
		return roolit.getLkm();
	}


	/**
	 * Palauttaa henkilön viitteen
	 * @param indeksi paikka josta henkilöä haetaan
	 * @return henkilön viite
	 */
	public Henkilo getHenkilo(int indeksi) {
		return henkilot.get(indeksi);
	}


	/**
	 * Palauttaa henkilöiden lukumäärän
	 * @return henkilöiden lukumäärä
	 */
	public int getHenkiloidenLkm() {
		return henkilot.getLkm();
	}


	/**
	 * tallentaa kokoelman tiedot
	 * @param tallennusPolku polku johon tiedostot tallennetaan
	 * @throws SailoException jos tallennus ei onnistunut
	 */
	public void tallenna(String tallennusPolku) throws SailoException {
		
		// Tähän tarvittaisiin lukko, ettei kaksi käyttääjää tallentaisi
		// samaan aikaan
		elokuvat.tallenna(tallennusPolku);
		henkilot.tallenna(tallennusPolku);
		roolit.tallenna(tallennusPolku);
		relaatiot.tallenna(tallennusPolku);
	}


	/**
	 * lukee tiedostot annetusta polusta
	 * @param polku polku josta luetaan
	 * @throws SailoException jos tiedostoa ei ole
	 */
	public void lueTiedostot(String polku) throws SailoException {		
		kokoelmanPolku = polku;
		
		elokuvat.lueTiedosto(polku);
		henkilot.lueTiedosto(polku);
		relaatiot.lueTiedosto(polku);		
	}
	
	
	/**
	 * palauttaa kokoelman polun
	 * @return kokoelman polku
	 */
	public String getKokoelmanPolku() {
		return kokoelmanPolku;
	}


	/**
	 * alustaa tietokannan seuraavaa käyttäjää varten
	 * @return viite alustettuun Kokoelmaan
	 */
	public Kokoelma alusta() {
		elokuvat.alusta();
		henkilot.alusta();
		relaatiot.alusta();
		return new Kokoelma();
	}


	/**
	 * Valitsee elokuvan kokoelmasta valmiiksi kasittelyä varten
	 * @param valittu elokuva joka valitaan kasittelyyn
	 */
	public void valitseElokuva(Elokuva valittu) {
		elokuvaKasittelyssa = valittu;
	}


	/**
	 * kertoo kasittelyssa olevan elokuvan
	 * @return elokuva jota kasitellään
	 */
	public Elokuva getElokuvaKasittelyssa() {
		return elokuvaKasittelyssa;
	}


	/**
	 * asettaa tiedot, jotta henkilö liittyy elokuvaan 
	 * @param henkilonNimi Henkilön nimi
	 * @param eid elokuvan id johon liitetään
	 * @param rid henkilön roolin id elokuvassa
	 * @example
	 * <pre name="test">
	 * Kokoelma k = new Kokoelma();
	 * k.getHenkiloidenLkm() === 0;
	 * k.setHenkilo("Henkilö", 1, 1);
	 * k.getHenkiloidenLkm() === 1;
	 * k.setHenkilo("Henkilö", 65, 54);
	 * k.getHenkiloidenLkm() === 1;
	 * k.setHenkilo("Toinen Henkilö", 65, 54);
	 * k.getHenkiloidenLkm() === 2;
	 * </pre>
	 */
	public void setHenkilo(String henkilonNimi, int eid, int rid) {						
		
		// Tähän tarvittaisiin lukko, voisi mennä indeksit ja viitteet väärin
		// jos kaksi käyttäjää lisäisi samaan aikaan
		Henkilo henkilo = henkilot.etsiHenkilo(henkilonNimi);

		if (henkilo == null) {

			henkilo = new Henkilo(henkilonNimi).rekisteroi();

			try {
				henkilot.add(henkilo);
			} catch (SailoException e) { // TODO: Poikkeus nousemaan käyttöliittymään
				System.err.println(e.getMessage()); 
			}
		}

		Relaatio relaatio = relaatiot.etsiRelaatio(eid, henkilo.getHid(), rid);

		if (relaatio != null) return;

		relaatio = new Relaatio();

		if (rid == 1) relaatiot.poista(eid, rid); // TODO: ratkaisu jossa selvitettäisiin erikseen, onko rid sellainen että niitä voi olla vain yksi per elokuva

		relaatio.asetaTiedot("" + eid, "" + henkilo.getHid(), "" + rid);

		relaatiot.add(relaatio);
	}


	/**
	 * Asettaa elokuvalle vuoden
	 * @param vuosi joka asetetaan
	 */
	public void setVuosi(String vuosi) {
		elokuvaKasittelyssa.setVuosi(vuosi);		
	}


	/**
	 * Asettaa elokuvalle nimen
	 * @param nimi joka asetetaan
	 */
	public void setElokuvanNimi(String nimi) {
		elokuvaKasittelyssa.setElokuvanNimi(nimi);		
	}


	/**
	 * Poistaa henkilön elokuvasta
	 * @param poistettavanNimi henkilön nimi
	 * @param rooliJostaPoistetaan rooli josta poistetaan
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException
	 * Relaatio r = new Relaatio();
	 * r.asetaTiedot("" + 1, "" + 2, "" + 3);
	 * Henkilo h = new Henkilo();
	 * h.asetaTiedot("" + 2, "henkilo");
	 * Kokoelma k = new Kokoelma();
	 * k.add(r);
	 * k.add(h);
	 * Elokuva e = new Elokuva();
	 * e.asetaTiedot("" + 1, "ihan sama", "" + 1999, "anneainsanat");
	 * k.valitseElokuva(e);
	 * k.getRelaatioidenLkm() === 1;
 	 * k.poistaHenkiloElokuvasta("ei tämä henkilö", 3); #THROWS NullPointerException
 	 * k.getRelaatioidenLkm() === 1;
 	 * k.poistaHenkiloElokuvasta("henkilo", 3);
	 * k.getRelaatioidenLkm() === 0;
	 * </pre>
	 */
	public void poistaHenkiloElokuvasta(String poistettavanNimi, int rooliJostaPoistetaan) {
		Henkilo poistettava = henkilot.etsiHenkilo(poistettavanNimi);
		relaatiot.poista(elokuvaKasittelyssa.getEid(), poistettava.getHid(), rooliJostaPoistetaan);
	}


	/**
	 * Poistaa elokuvan
	 * @param poistettava elokuva joka poistetaan
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException
	 *  Kokoelma k = new Kokoelma();
	 * 	Elokuva el = new Elokuva();
	 * 	el.asetaTiedot("" + 1, "nimi", "" + 1999, "avc");
	 * 	k.add(el);
	 * 	Relaatio rel = new Relaatio(); 
	 * 	rel.asetaTiedot("" + 1, "" + 43, "" + 2);
	 *  k.add(rel);
	 *	k.getRelaatioidenLkm() === 1;
	 *  k.getElokuvienLkm() === 1;
	 *  k.poistaElokuva(el);
	 *	k.getElokuvienLkm() === 0;
	 *  k.getRelaatioidenLkm() === 0;
	 * </pre>
	 */
	public void poistaElokuva(Elokuva poistettava) {			
		relaatiot.poista(poistettava.getEid());
		elokuvat.poista(poistettava);
	}


	/**
	 * Pyyttää tarkistamaan, onko elokuvan nimi oikeanlainen
	 * @param ehdotus nimi jota on ehdotettu
	 * @return null jos sopiva, muuten syy miksi ei ole sopiva
	 */
	public String tarkistaNimi(String ehdotus) {
		return ElokuvaTarkistus.tarkistaNimi(ehdotus);
	}


	/**
	 * Pyytää tarkistamaan, onko elokuvalle ehdotettu vuosi sopiva
	 * @param ehdotus vuosi jota ehdotetaan
	 * @return null jos sopiva, muuten syy miksi ei ole sopiva
	 */
	public String tarkistaVuosi(String ehdotus) {
		return ElokuvaTarkistus.tarkistaVuosi(ehdotus);
	}


	/**
	 * Selvittää kuka on ohjannut elokuvan
	 * @param elokuva jonka ohjaajaa selvitetään
	 * @return ohjaajan nimi
	 */
	public String selvitaOhjaaja(Elokuva elokuva) {

		int roolinId = roolit.roolinId("ohjaaja");

		if (roolinId == 0) return null;

		Relaatio r = relaatiot.etsiRelaatio(elokuva.getEid(), roolinId);

		Henkilo etsitty = henkilot.etsiHenkilo(r.getHid());

		return etsitty.getNimi();
	}


	/**
	 * Tulostaa tiivistetyt tiedot eolokuvasta tietovirtaan
	 * @param os tietovirta johon tulostetaan
	 * @param tulostettava elokuva jonka tiedot tulostetaan
	 */
	public void tulostaTiivistelma(PrintStream os, Elokuva tulostettava) {
		os.println("Elokuvan nimi:\n"+   tulostettava.getNimi() + "\n\n" +
				"Valmistumisvuosi:\n" + tulostettava.getVuosi() + "\n\n" +
				"Ohjaaja:\n" + selvitaOhjaaja(tulostettava) + "\n\n" + 
				"Avainsanat:\n" + tulostettava.getAvainsanat());
	}


	/**
	 * etsii henkilön nimen perusteella
	 * @param nimi henkilön nimi
	 * @return viite henkilöön tai null jos ei löydy
	 */
	public Henkilo getHenkilo(String nimi) {
		return henkilot.etsiHenkilo(nimi);
	}
}
