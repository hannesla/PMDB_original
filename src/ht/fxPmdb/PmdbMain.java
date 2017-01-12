package ht.fxPmdb;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * P��ohjelma PMDB:lle eli elokuvatietokannalle
 * 
 * Ohjelmasta puuttuvat ominaisuudet:
 * - synkronointi tekem�tt�
 * - haku n�yttelij�iden nimill�
 * - haku avainsanoilla
 * - haku k�sikirjoittajilla
 * - paperille tulostaminen 
 * - henkil�ihin liittyv�t oikeellisuustarkistukset (nimiin ei numeroita tai erikoismerkkej�)
 * - ikkunoiden sulkemisnapit
 * 
 * @author Hannes Laukkanen
 * @version Jan 29, 2016
 * hannes.v.laukkanen@student.jyu.fi
 */
public class PmdbMain extends Application {
	
	@Override
	public void start(Stage primaryStage) {
				
		try {
			FXMLLoader ldr = new FXMLLoader(getClass().getResource("PmdbGUIView.fxml"));
			
			final Pane root = ldr.load();						
			
			final Scene scene = new Scene(root);			
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setTitle("PMDB");
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * K�ynnistet��n PMDB-ohjelma
	 * @param args ei k�ytet�
	 */
	public static void main(String[] args) {
		launch(args);
	}
}