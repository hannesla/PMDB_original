/**
 * 
 */
package ht.fxPmdb;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import ht.kokoelma.Henkilo;

import ht.kokoelma.Kokoelma;
import ht.kokoelma.Relaatio;
import ht.kokoelma.SailoException;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Näyttää enemmän tietoja elokuvasta ja mahdollistaa tietojen muokkauksen
 * 
 * Ohjelmasta puuttuvat ominaisuudet:
 * - henkilöihin liittyvät oikeellisuustarkistukset (nimiin ei numeroita tai erikoismerkkejä)
 * - ikkunoiden sulkemisnapit
 * - avainnojen tulosteesta ',' pois lopusta
 * 
 * @author handu
 * @version Feb 12, 2016
 * hannes.v.laukkanen@student.jyu.fi
 */
public class TiedotController implements ModalControllerInterface<Kokoelma>, Initializable {

	// Tekstikentät
	@FXML private GridPane gridTiedot;	
	@FXML private TextField textNimi;
	@FXML private TextField textVuosi;
	@FXML private TextArea textNayttelijat;
	@FXML private TextArea textAvainsanat;
	@FXML private TextArea textKasikirjoittajat;
	@FXML private TextField textOhjaaja;

	// painikkeet
	@FXML private Button buttonEditNimi;
	@FXML private Button buttonEditVuosi;
	@FXML private Button buttonEditOhjaaja;


	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		alusta();
	}	

	@FXML void muokkaaAvainsanatHandler() {
		avainsanojenMuokkaus();
	}


	@FXML void muokkaaKasikirjoittajatHandler() {
		kasikirjoittajienMuokkaus();
	}


	@FXML void muokkaaNayttelijatHandler() {
		nayttelijoidenMuokkaus();
	}


	@FXML void muokkaaNimiHandler() {
		muokkaaNimi();
		textNimi.requestFocus();
		buttonEditNimi.setDisable(true);
	}


	@FXML void vahdaNimi() {
		kasittelyssa.setElokuvanNimi(textNimi.getText());
	}

	@FXML void vaihdaOhjaaja() {
		if (ohjaaja.isEmpty()) ohjaaja = " ";

		kasittelyssa.setHenkilo(ohjaaja, kasittelyssa.getElokuvaKasittelyssa().getEid(), 1);
	}


	@FXML void vaihdaVuosi() {  	
		kasittelyssa.setVuosi(textVuosi.getText());
	}

	@FXML void muokkaaOhjaajaHandler() {	
		muokkaaOhjaaja();
		textOhjaaja.requestFocus();
		buttonEditOhjaaja.setDisable(true);
	}


	@FXML void muokkaaVuosiHandler() {
		muokkaaVuosi();
		textVuosi.requestFocus();
		buttonEditVuosi.setDisable(true);
	}


	@FXML void ohjaajaMuistiin() {
		ohjaaja = textOhjaaja.getText();
	}

	// =========================================================================================
	// Oma koodi	

	private Kokoelma kasittelyssa = null;
	private String ohjaaja;							 // tarvitaan, koska muuten ohjaajan muokkaaminen
	// ja sen jälkeen uudessa ikkunassa lisättävien
	// tietojen lisääminen ei onnistuisi

	private void alusta() {
		//
	}


	/**
	 * Muuttaa vuosi kentän muokattavaksi
	 */
	private void muokkaaVuosi() {
		textVuosi.setEditable(true);	
	}


	/**
	 * Muuttaa ohjaaja kentän muokattavaksi
	 */
	private void muokkaaOhjaaja() {    	   	
		textOhjaaja.setEditable(true);	
	}


	/**
	 * Muuttaa nimi kentän muokattavaksi
	 */
	private void muokkaaNimi() {
		textNimi.setEditable(true);
	}


	/**
	 * Avaa ikkunan josta voidaan lisätä ja poistaa tietoja
	 */
	private void nayttelijoidenMuokkaus() {

		String[] pilkottu = textNayttelijat.getText().split("\n");

		List<String> tiedot = new ArrayList<>();

		for (int i = 0; i < pilkottu.length; i++) {
			tiedot.add(pilkottu[i]);
		}

		List<String> uudet = LisaysJaPoistoController.lisaaTaiPoista(null, tiedot); // tiedoista löytyy tämän suorituksen jälkeen tieto poistetuista

		for (String uusi : uudet) {			
			try {
				Henkilo henk = kasittelyssa.getHenkilo(uusi);

				if (henk == null) {
					henk = kasittelyssa.add(new Henkilo(uusi).rekisteroi());					
				}
				
				kasittelyssa.add(new Relaatio().asetaTiedot("" + kasittelyssa.getElokuvaKasittelyssa().getEid(), "" + henk.getHid(), "" + 2));;
			} catch (SailoException e) {
				System.err.println(e.getMessage());
			}
		}


		if (!tiedot.isEmpty()) {
			for (String poistettu : tiedot) {
				kasittelyssa.poistaHenkiloElokuvasta(poistettu, 2);
			}	
		}		

		asetaTiedot();
	}


	/**
	 * Muokkaa käsikirjoittajia
	 */
	public void kasikirjoittajienMuokkaus() {

		String[] pilkottu = textKasikirjoittajat.getText().split("\n");

		List<String> tiedot = new ArrayList<>();

		for (int i = 0; i < pilkottu.length; i++) {
			tiedot.add(pilkottu[i]);
		}

		List<String> uudet = LisaysJaPoistoController.lisaaTaiPoista(null, tiedot); // tiedoista löytyy tämän suorituksen jälkeen tieto poistetuista

		for (String uusi : uudet) {			
			try {
				Henkilo henk = kasittelyssa.getHenkilo(uusi);

				if (henk == null) {
					henk = kasittelyssa.add(new Henkilo(uusi).rekisteroi());					
				}
				
				kasittelyssa.add(new Relaatio().asetaTiedot("" + kasittelyssa.getElokuvaKasittelyssa().getEid(), "" + henk.getHid(), "" + 3));				
			} catch (SailoException e) {
				System.err.println(e.getMessage());
			}
		}

		if (!tiedot.isEmpty()) {
			for (String poistettu : tiedot) {
				kasittelyssa.poistaHenkiloElokuvasta(poistettu, 3);
			}	
		}		

		asetaTiedot();		
	}    


	/**
	 * Muokkaa avainsanoja
	 */
	public void avainsanojenMuokkaus() {

		String[] pilkottu = textAvainsanat.getText().split(", ");

		List<String> tiedot = new ArrayList<>();

		for (int i = 0; i < pilkottu.length; i++) {
			tiedot.add(pilkottu[i]);
		}

		List<String> uudet = LisaysJaPoistoController.lisaaTaiPoista(null, tiedot); 

		for (String uusi : uudet) {			
			kasittelyssa.getElokuvaKasittelyssa().setAvainsanat(kasittelyssa.getElokuvaKasittelyssa().getAvainsanat() + uusi);
		}

		for (int i = 0; i < tiedot.size(); i++) {
			kasittelyssa.getElokuvaKasittelyssa().setAvainsanat(kasittelyssa.getElokuvaKasittelyssa().getAvainsanat().replaceFirst(tiedot.get(i) + ", ", ""));
		}

		asetaTiedot();		
	}


	/**
	 * Luo ikkunan
	 * @param modalityStage modaalisuus
	 * @param kasiteltava oletus käyttäjä
	 * @return ikkunan vaikutus
	 */
	public static Kokoelma elokuvanTiedot(Stage modalityStage, Kokoelma kasiteltava ){		
		return ModalController.showModal(
				TiedotController.class.getResource("TiedotView.fxml"),
				"Elokuvan tiedot", modalityStage, kasiteltava);
	}


	/**
	 * Palauttaa vastauksen
	 */
	@Override
	public Kokoelma getResult() {
		vaihdaOhjaaja();
		tarkistaNimi();
		tarkistaVuosi();

		return kasittelyssa;
	}


	private void tarkistaVuosi() {
		String ongelma = kasittelyssa.tarkistaVuosi(textVuosi.getText());
		if (ongelma == null) return;

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Vuodessa ongelma");
		alert.setHeaderText(null);
		alert.setContentText(ongelma);

		alert.showAndWait();

		kasittelyssa.setVuosi("" + 1895);
	}


	/**
	 * kysyy kokoelmalta, onko nimi sopiva kokoelmaan
	 */
	public void tarkistaNimi() {

		String ongelma = kasittelyssa.tarkistaNimi(textNimi.getText());
		if (ongelma == null) return;

		vaadiNimi(ongelma);	
	}


	/**
	 * Avaa ikkunan jossa käyttäjää pyydetään antamaan sopiva nimi elokuvalle
	 * @param ongelma miksi aiemmin annettu nimi ei kelpaa
	 */
	public void vaadiNimi(String ongelma) {

		String nimi = "";

		while(nimi.equals("")) {
			TextInputDialog dialog = new TextInputDialog("");
			dialog.setHeaderText(null);
			dialog.setTitle(ongelma);
			dialog.setContentText("Elokuvan nimi:");
			Optional<String> answer = dialog.showAndWait();

			if (!answer.isPresent()) nimi = "nimetön elokuva";
			else nimi = answer.get();		
		}

		kasittelyssa.getElokuvaKasittelyssa().setElokuvanNimi(nimi);		
	}


	/**
	 * Asettaa kursorin nimi kenttään
	 */
	@Override
	public void handleShown() {
		//	System.out.println(kasittelyssa.toString());
		textNimi.requestFocus();		
	}


	/**
	 * ottaa kokoelman kasitteleltavaksi ja asettaa nykyiset tiedot
	 */
	@Override
	public void setDefault(Kokoelma auki) {
		kasittelyssa = auki;

		if (kasittelyssa.getElokuvaKasittelyssa().getNimi().isEmpty()) kaikkiMuokattaviksi();

		asetaTiedot();

		ohjaaja = textOhjaaja.getText();
	}


	private void kaikkiMuokattaviksi() {		
		muokkaaVuosiHandler();
		muokkaaOhjaaja();
		muokkaaNimiHandler();
		buttonEditOhjaaja.setDisable(true);
	}


	/**
	 * asettaa kokoelmassa olevat tämän hetkiset tiedot ikkunaan
	 */
	public void asetaTiedot() {
		textNimi.setText(kasittelyssa.getElokuvaKasittelyssa().getNimi());
		textVuosi.setText("" + kasittelyssa.getElokuvaKasittelyssa().getVuosi());
		if (ohjaaja == null) textOhjaaja.setText(selvitaOhjaaja());
		else textOhjaaja.setText(ohjaaja);

		textNayttelijat.setText(selvitaNayttelijat());
		textKasikirjoittajat.setText(selvitaKasikirjoittajat());
		textAvainsanat.setText(selvitaAvainsanat());
	}


	/**
	 * selvittää elokuvan avainsanat
	 * @return avainsanat
	 */
	public String selvitaAvainsanat() {
		return kasittelyssa.getElokuvaKasittelyssa().getAvainsanat();
	}


	/**
	 * selvittää elokuvan käsikirjoittajat
	 * @return käsikirjoittajat
	 */
	public String selvitaKasikirjoittajat() {
		StringBuilder nayttelijat = new StringBuilder("");
		List<Integer> sopivatNayttelijat = new ArrayList<>(); 

		for (int i = 0; i < kasittelyssa.getRelaatioidenLkm(); i++) {
			if (kasittelyssa.getRelaatio(i).getRid() == 3 &&
					kasittelyssa.getRelaatio(i).getEid() == kasittelyssa.getElokuvaKasittelyssa().getEid()) sopivatNayttelijat.add(kasittelyssa.getRelaatio(i).getHid());
		}

		for (int i = 0; i < kasittelyssa.getHenkiloidenLkm(); i++) {
			if (sopivatNayttelijat.contains(kasittelyssa.getHenkilo(i).getHid())) nayttelijat.append(kasittelyssa.getHenkilo(i).getNimi() + "\n");
		}

		return nayttelijat.toString();
	}


	/**
	 * selvittää elokuvan näyttelijät
	 * @return näyttelijät
	 */
	public String selvitaNayttelijat() {
		StringBuilder nayttelijat = new StringBuilder("");
		List<Integer> sopivatNayttelijat = new ArrayList<>(); 

		for (int i = 0; i < kasittelyssa.getRelaatioidenLkm(); i++) {
			if (kasittelyssa.getRelaatio(i).getRid() == 2 &&
					kasittelyssa.getRelaatio(i).getEid() == kasittelyssa.getElokuvaKasittelyssa().getEid()) sopivatNayttelijat.add(kasittelyssa.getRelaatio(i).getHid());
		}

		for (int i = 0; i < kasittelyssa.getHenkiloidenLkm(); i++) {
			if (sopivatNayttelijat.contains(kasittelyssa.getHenkilo(i).getHid())) nayttelijat.append(kasittelyssa.getHenkilo(i).getNimi() + "\n");
		}

		return nayttelijat.toString();
	}


	/**
	 * selvittää elokuvan ohjaajan
	 * @return elokuvan ohjaaja
	 */
	public String selvitaOhjaaja() {

		int eid = kasittelyssa.getElokuvaKasittelyssa().getEid();
		int hid = -1;
		boolean loytyiko = false;

		for (int i = 0; i < kasittelyssa.getRelaatioidenLkm(); i++) {
			if (kasittelyssa.getRelaatio(i).getEid() == eid && kasittelyssa.getRelaatio(i).getRid() == 1) {
				hid = kasittelyssa.getRelaatio(i).getHid();
				loytyiko = true;
				break;
			}
		}

		if (!loytyiko) return "";

		for (int i = 0; i < kasittelyssa.getHenkiloidenLkm(); i++) {
			if (kasittelyssa.getHenkilo(i).getHid() == hid) return kasittelyssa.getHenkilo(i).getNimi();
		}		

		return "";
	}
}
