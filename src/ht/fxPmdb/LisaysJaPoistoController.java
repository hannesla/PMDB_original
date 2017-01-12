/**
 * 
 */
package ht.fxPmdb;


import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * T�st� ikkunasta lis�t��n ja poistetaan sis�lt�j�
 * 
 * Parannettavaa:
 * - Kun lis�t��n avainsanoja silloin kun niit� ei ennalta ole, listaan j�� aluksi tyhj� avainsana
 *   (poistuu kun sulkee ikkunan ja avaa uudelleen)
 * 
 * @author Hannes Laukkanen
 * @version Feb 12, 2016
 * hannes.v.laukkanen@student.jyu.fi 
 */
public class LisaysJaPoistoController implements ModalControllerInterface<List<String>>, Initializable {
	@FXML private ListChooser listChooserSisalto;


	@FXML void lisaaHandler() {
		lisaa();
	}

	@FXML void poistaHandler() {
		poista();
	}


	// =========================================================================================
	// Oma koodi	

	private List<String> kasittelyssa;
	private List<String> uudet = new ArrayList<String>();
	private List<String> poistetut = new ArrayList<String>();
	private ListView<String> listSisallot = new ListView<String>();		
	private ObservableList<String> listdataSisallot = FXCollections.observableArrayList(); 


	@Override
	public void initialize(URL location, ResourceBundle resources) {

		BorderPane parent = (BorderPane)listChooserSisalto.getParent();

		parent.setCenter(listSisallot);		

		parent.setCenter(listSisallot);		
		listSisallot.setCellFactory( p -> new CellString() );

		listSisallot.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			//	naytaElokuva();
		});
	}


	@Override
	public List<String> getResult() {
		kasittelyssa.clear();

		for (String poistettu: poistetut) {
			kasittelyssa.add(poistettu);
		}

		return uudet;
	}

	@Override
	public void handleShown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefault(List<String> kasittelyssa) {
		this.kasittelyssa = kasittelyssa;
		hae();
	}
	
	
	/**
	 * sulkee dialogin, ei avaa tiedostoa
	 */
	@FXML private void handleCancel() {
		System.out.println("b��");
	}
	

	/**
	 * Avaa dialogin, johon k�ytt�j� voi kirjoittaa lis�tt�v�n tiedon
	 */
	private void lisaa() {	
		TextInputDialog dialog = new TextInputDialog("kirjoita...");
		dialog.setHeaderText(null);
		dialog.setTitle("Lis��minen");
		dialog.setContentText("Lis�tt�v�:");
		Optional<String> answer = dialog.showAndWait();
		
		if (!answer.isPresent()) return;  // jos k�ytt�j� painaa cancel

		kasittelyssa.add(answer.get());
		uudet.add(answer.get());

		hae();

	}


	/**
	 * Poistaa valittuna olevan tiedon
	 */
	public void poista() {
		String sisaltoKohdalla = listSisallot.getSelectionModel().getSelectedItem();

		kasittelyssa.remove(sisaltoKohdalla);

		poistetut.add(sisaltoKohdalla);		

		hae();
	}


	/**
	 * Luo ikkunan jossa k�sitell��n poistamisia ja lis��misi�
	 * @param modalityStage modaalisuus
	 * @param kasittelyssa oletus k�ytt�j�
	 * @return ikkunan vaikutuksen
	 */
	public static List<String> lisaaTaiPoista(Stage modalityStage, List<String> kasittelyssa ){
		return ModalController.showModal(
				LisaysJaPoistoController.class.getResource("LisaysJaPoistoView.fxml"),
				"Lis�� tai poista", modalityStage, kasittelyssa);
	}


	/**
	 * hakee henkil�t listaan
	 */
	public void hae() {		

		listdataSisallot.clear();
		listSisallot.setItems(listdataSisallot);

		//		String[] tiedot = kasittelyssa.split("\n");

		for (int i = 0; i < kasittelyssa.size(); i++) {
			if (kasittelyssa.get(i).length() > 0) listdataSisallot.add(kasittelyssa.get(i));
		}	
	}	


	/**
	 * Sis�lt�jen n�ytt�misen listassa hoitava luokka
	 */
	public static class CellString extends ListCell<String> {

		@Override 
		protected void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);
			setText(empty ? "" : item);
		}

	}
}
