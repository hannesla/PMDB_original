package ht.fxPmdb;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.TextAreaOutputStream;

import ht.kokoelma.Elokuva;
import ht.kokoelma.Kokoelma;
import ht.kokoelma.SailoException;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

/**
 * PMDB:n k�ytt�liittym�n kontrolleri
 * 
 * Ikkunasta puuttuvat ominaisuudet:
 * - haku n�yttelij�iden nimill�
 * - haku avainsanoilla
 * - haku k�sikirjoittajilla
 * - paperille tulostaminen 
 * - isot ja pienet kirjaimet merkityksett�miksi hakuun
 * 
 * @author Hannes Laukkanen
 * @version Jan 29, 2016
 * hannes.v.laukkanen@student.jyu.fi
 */
public class PmdbGUIController implements Initializable{

	@FXML private ListChooser chooserElokuvat;
	@FXML private GridPane paneElokuvantiedot;

	// Hakukent�t
	@FXML private TextField textHakuNimella;
	@FXML private TextField textHakuVuodella;
	@FXML private TextField textHakuOhjaajalla;


	@FXML void jarjestaNimienMukaanHandler() {
		jarjesta("nimi");
	}


	@FXML void jarjestaVuosienMukaanHandler() {
		jarjesta("vuosi");
	}


	@FXML void haeEhdoillaHandler() {
		hae();
	}


	@FXML void avaaHandler() {
		avaaTiedostoDialogi();
	}

	@FXML void lisaaHandler() {
		avaaUusiElokuvaIkkuna();
		//uusiElokuva();
	}

	@FXML void muokkaaHandler() {
		avaaElokuvaIkkuna();
	}

	@FXML void ohjeHandler() {
		avaaWiki();
	}

	@FXML void poistaHandler() {
		poistaElokuva();
	}

	@FXML void suljeHandler() {
		sulje();
	}

	@FXML void tallennaHandler() {
		tallenna();
	}

	@FXML void tiedotHandler() {
		avaaElokuvaIkkuna();
	}

	@FXML void tietojaHandler() {
		avaaTietojaIkkuna();
	}

	@FXML void tulostaHandler(){
		tulosta();
	}

	@FXML void tyhjennaHakuHandler() {
		tyhjennaHaku();
	}

	//===========================================================================================    
	// T�st� eteenp�in k�ytt�liittym��n suoraan liittym�t�n koodi  

	private Kokoelma kokoelma;
	private ListView<Elokuva> listElokuvat = new ListView<Elokuva>();		
	private ObservableList<Elokuva> listdataElokuvat = FXCollections.observableArrayList(); 
	private TextArea areaElokuvantiedot = new TextArea();
	private Elokuva elokuvaKohdalla = null;
	private String kayttaja = "default";
	private String kayttajatKansio = "kayttajat";
	private String jarjestysperuste = "id";


	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		alusta();
	}


	private void luoKayttajatKansio() {
		File dir = new File(kayttajatKansio);
		dir.mkdir();
	}


	private void alusta() {

		BorderPane parent = (BorderPane)chooserElokuvat.getParent(); // kertoo mink� p��lle chooserElokuvat laitettu
		BorderPane gridParent = (BorderPane)paneElokuvantiedot.getParent();

		gridParent.setCenter(areaElokuvantiedot);
		areaElokuvantiedot.setFont(new Font("Helvetica", 16));
		areaElokuvantiedot.setEditable(false);

		parent.setCenter(listElokuvat);		
		listElokuvat.setCellFactory( p -> new CellElokuva() );

		listElokuvat.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			naytaElokuva();
		});

		tyhjennaHaku();

		setPmdb(new Kokoelma());
		luoKayttajatKansio();
		avaaTiedostoDialogi();
	}


	/**
	 * Elokuvien n�ytt�misen listassa hoitava luokka
	 */
	public static class CellElokuva extends ListCell<Elokuva> {

		@Override 
		protected void updateItem(Elokuva item, boolean empty) {
			super.updateItem(item, empty);
			setText(empty ? "" : item.getNimi());
		}

	}


	/**
	 * Tyhjent�� hakukent�t ja tulokset
	 */
	public void tyhjennaHaku() {
		textHakuNimella.clear();
		textHakuVuodella.clear();
		textHakuOhjaajalla.clear();
	}


	/**
	 * Poistaa kohditettuna olevan elokuvan
	 */
	public void poistaElokuva() {
		kokoelma.poistaElokuva(elokuvaKohdalla);

		hae();	
	}


	/**
	 * Avaa ikkunan, jonka kautta voidaan avata tietokantatiedosto
	 * @return onnisuiko vai ei
	 */
	public boolean avaaTiedostoDialogi() {		

		String annettuTunnus = KirjautuminenController.kysyTunnus(null, kayttaja);

		if (annettuTunnus == null) Platform.exit(); 

		kayttaja = annettuTunnus;

		File polku = new File(kayttajatKansio + "\\" + kayttaja);
		polku.mkdir();

		lueTiedostot(kayttaja);

		return true;  
	}

	/**
	 * Avaa tietokanna k�ytt�j�n antaman k�ytt�j�tunnuksen mukaan
	 * @param nimi k�ytt�n nimi
	 */
	protected void lueTiedostot(String nimi) { 
		kokoelma = kokoelma.alusta();		

		kayttaja = nimi;

		try {
			kokoelma.lueTiedostot(kayttajatKansio + "\\" + kayttaja);
		} catch (SailoException e) {
			System.out.println("Tiedostoa ei luettu, koska: " + e.getMessage());
		}

		hae();
		/**
		String virhe = "Avaisi k�ytt�j�n nimen mukaan elokuvatietokannan";  // TODO: t�h�n oikea tiedoston lukeminen 
		// if (virhe != null)  
		Dialogs.showMessageDialog(virhe);
		 */ 
	} 


	/**
	 * Avaa ikkunan josta voidaan lis�t�, muokata ja poistaa elokuvia
	 */
	public void avaaElokuvaIkkuna() {
		if (elokuvaKohdalla == null || elokuvaKohdalla.getEid() == 0) return; 
		kokoelma.valitseElokuva(elokuvaKohdalla);

		TiedotController.elokuvanTiedot(null, kokoelma);

		hae();

		listElokuvat.getSelectionModel().select(kokoelma.getElokuvaKasittelyssa());
	}


	/**
	 * avaa elokuvan tiedot ikkunan uuden elokuvan lis��mist� varten
	 */
	public void avaaUusiElokuvaIkkuna() {		

		try {
			elokuvaKohdalla = kokoelma.add(new Elokuva());
		} catch (SailoException e) {
			System.out.println(e.getMessage());
		}

		kokoelma.valitseElokuva(elokuvaKohdalla);
		TiedotController.elokuvanTiedot(null, kokoelma);

		hae();

		listElokuvat.getSelectionModel().select(kokoelma.getElokuvaKasittelyssa());		
	}


	/**
	 * Avaa ohjelman k�ytt�ohjeen
	 */
	public void avaaWiki() {
		Desktop desktop = Desktop.getDesktop();
		try {
			URI uri = new URI("https://trac.cc.jyu.fi/projects/ohj2ht/wiki/k2016/suunnitelmat/PMDB");
			desktop.browse(uri);
		} catch (URISyntaxException e) {
			return;
		} catch (IOException e) {
			return;
		}
	}


	/**
	 * Sulkee ohjelman
	 */
	public void sulje() {
		// Dialogs.showMessageDialog("Kysyisi tallennetaanko ennen sulkemista ja sulkisi ohjelman");
		Platform.exit();
	}


	/**
	 * Tallentaa avoimen tietokannan tiedot
	 */
	public void tallenna() {		
		try {
			kokoelma.tallenna(kayttajatKansio + "\\" + kayttaja);
		} catch (SailoException e) {
			System.err.println("Tallentaminen ei onnistunut: " + e.getMessage());
		}
	}


	/**
	 * Avaa ikkunan josta n�kyy tarkemmat tiedot elokuvasta
	 */
	public void avaaTietojaIkkuna() {
		Dialogs.showMessageDialog(
				"Personal Movie Database\n" +
						"Tekij�: Hannes Laukkanen\n" +
						"Jyv�skyl�n yliopiston Ohjelmointi 2 -kurssin harjoitusty�\n\n" + 
						"Ohjelma johon voi tallentaa tietoja katsomistaan elokuvista."
				);
	}


	/**
	 * Tulostaa valittuna olevan elokuvan tiedot
	 */
	public void tulosta() {
		Dialogs.showMessageDialog("Tulostaisi valitun elokuvan tiedot");		
	}


	/**
	 * asettaa ohjelman k�ytt�m�ksi kokoelmaksi parametrina saadun kokoelman
	 * @param kokoelma kokoelma joka laitetaan
	 */
	public void setPmdb(Kokoelma kokoelma) {
		this.kokoelma = kokoelma;		
	}


	/**
	 * Luo elokuvan editointia varten
	 */
	protected void uusiElokuva() { // TODO: pit�nee siirt�� Kokoelman puolelle n�iden k�sittely
		Elokuva uusi = new Elokuva(); // TODO: Aukaisee dialogin
		uusi.rekisteroi(); // TODO: rekister�i vasta kun tullaan dialogista
		uusi.taytaTiedoilla();

		try {
			kokoelma.add(uusi);
		} catch (SailoException e) {
			Dialogs.showMessageDialog("Tapahtui virhe uuden elokuvan lis��misess�: \n" + e.getMessage());
			return;
		}

		hae();
	}


	/**
	 * hakee elokuvat listaan
	 */
	public void hae() {
		listdataElokuvat.clear();
		listElokuvat.setItems(listdataElokuvat);

		for (int i = 0; i < kokoelma.getElokuvienLkm(); i++) {
			Elokuva elokuva = kokoelma.getElokuva(i);

			boolean ehdotTayttyy = tarkistaEhdot(elokuva);

			if (ehdotTayttyy){
				listdataElokuvat.add(elokuva);
			}
		}

		if (jarjestysperuste.equals("nimi")) jarjesta("nimi");
		if (jarjestysperuste.equals("vuosi")) jarjesta("vuosi");

		listElokuvat.getSelectionModel().select(0);

		if (listElokuvat.getSelectionModel().getSelectedIndex() == -1) areaElokuvantiedot.setText("");
	}


	/**
	 * tarkistaa onko elokuva hakuehdot t�ytt�v�
	 * @param elokuva jonka sopivuutta tarkastellaan
	 * @return t�ytt��k� ehdot
	 */
	public boolean tarkistaEhdot(Elokuva elokuva) {
		if (!elokuva.getNimi().contains(textHakuNimella.getText())) return false;
		if (!("" + elokuva.getVuosi()).contains(textHakuVuodella.getText())) return false;		

		String ohjaaja = kokoelma.selvitaOhjaaja(elokuva);

		if (!ohjaaja.contains(textHakuOhjaajalla.getText())) return false;		

		return true;
	}


	/**
	 * N�ytt�� listasta valitun elokuvan tiedot
	 */
	public void naytaElokuva() {
		elokuvaKohdalla = listElokuvat.getSelectionModel().getSelectedItem();

		if (elokuvaKohdalla == null) return;
		areaElokuvantiedot.setText("");

		try (PrintStream os = TextAreaOutputStream.getTextPrintStream(areaElokuvantiedot)) {
			kokoelma.tulostaTiivistelma(os, elokuvaKohdalla);
		}
	}


	/**
	 * v�litt�� tiedon asiaan kuuluvalle metodille, ett� lista pit�isi j�rjest�� jollakin perusteella
	 * @param jarjestysPeruste peruste jonka mukaan j�rjestet��n
	 */
	public void jarjesta(String jarjestysPeruste) {
		if ("nimi".equals(jarjestysPeruste)) jarjestaNimenMukaan();
		if ("vuosi".equals(jarjestysPeruste)) jarjestaVuodenMukaan();
	}


	/**
	 * j�rjest�� listassa olevat elokuvat nimen mukaan
	 */
	public void jarjestaNimenMukaan() {

		jarjestysperuste = "nimi";

		ObservableList<Elokuva> jarjestetty = FXCollections.observableArrayList();

		while (!listdataElokuvat.isEmpty()) {
			Elokuva seuraava = ensimmainenAakkosissa();			
			jarjestetty.add(seuraava);
			listdataElokuvat.remove(seuraava);			
		}

		listdataElokuvat.clear();

		int i = 0;

		while (!jarjestetty.isEmpty()) {
			listdataElokuvat.add(jarjestetty.get(i));
			jarjestetty.remove(i);
		}		
	}


	/**
	 * Selvitt�� listassa olevista elokuvista aakkosissa ensimm�isen
	 * @return aakkosissa ensimm�inen elokuva
	 */
	private Elokuva ensimmainenAakkosissa() {

		Elokuva ensimmainen = listdataElokuvat.get(0);

		for (Elokuva e : listdataElokuvat) {

			int lyhyemmanPituus;

			if (ensimmainen.getNimi().length() < e.getNimi().length()) lyhyemmanPituus = ensimmainen.getNimi().length(); 
			else lyhyemmanPituus = e.getNimi().length();

			for (int i = 0; i < lyhyemmanPituus; i++) {
				if (e.getNimi().charAt(i) < ensimmainen.getNimi().charAt(i)) {
					ensimmainen = e;
					break;				
				}

				if (e.getNimi().charAt(i) > ensimmainen.getNimi().charAt(i)) {
					break;
				}
			}			
		}

		return ensimmainen;
	}


	private void jarjestaVuodenMukaan() {
		jarjestysperuste = "vuosi";

		ObservableList<Elokuva> jarjestetty = FXCollections.observableArrayList();

		while (!listdataElokuvat.isEmpty()) {
			Elokuva vanhin = vanhinElokuva();
			jarjestetty.add(vanhin);
			listdataElokuvat.remove(vanhin);			
		}

		listdataElokuvat.clear();

		int i = 0;

		while (!jarjestetty.isEmpty()) {
			listdataElokuvat.add(jarjestetty.get(i));
			jarjestetty.remove(i);
		}		
	}


	/**
	 * selvitt�� listan vanhimman elokuvan
	 * @return vanhin elokuva
	 */
	public Elokuva vanhinElokuva() {

		Elokuva vanhin = listdataElokuvat.get(0);

		for (Elokuva e : listdataElokuvat) {
			if (e.getVuosi() < vanhin.getVuosi()) vanhin = e;
		}

		return vanhin;
	}
}