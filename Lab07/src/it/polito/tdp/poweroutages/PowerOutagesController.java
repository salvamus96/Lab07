package it.polito.tdp.poweroutages;
import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.poweroutages.model.Model;
import it.polito.tdp.poweroutages.model.Nerc;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class PowerOutagesController {
	
	Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtYears"
    private TextField txtYears; // Value injected by FXMLLoader

    @FXML // fx:id="txtHours"
    private TextField txtHours; // Value injected by FXMLLoader

    @FXML // fx:id="btnWorst"
    private Button btnWorst; // Value injected by FXMLLoader

    @FXML // fx:id="boxNerc"
    private ChoiceBox<Nerc> boxNerc; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doAnalysis(ActionEvent event) {
    	this.txtResult.clear();
    	
    	Nerc nerc = this.boxNerc.getValue();
    	if(nerc == null) {
    		this.txtResult.appendText("Selezionare un NERC!");
    		return ;
    	}
    	
    	if (this.txtYears.getText().isEmpty()) {
    		this.txtResult.appendText("Inserire un intervallo massimo di anni per l'analisi!");
    		return ;
    	}
    	
    	if (this.txtHours.getText().isEmpty()) {
    		this.txtResult.appendText("Inserire un intervallo massimo di ore per l'analisi!");
    		return ;
    	}
		
    	try {
    		int maxYears = Integer.parseInt(this.txtYears.getText());
    		int maxHours = Integer.parseInt(this.txtHours.getText());
    		
    		this.txtResult.appendText(model.analizza(nerc, maxYears, maxHours));
        	
    	}catch(NumberFormatException e) {	
    		this.txtResult.appendText("Formato intervallo massimo delle ore o degli anni non ammesso!\n");
    	}
    	
    
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtYears != null : "fx:id=\"txtYears\" was not injected: check your FXML file 'PowerOutages.fxml'.";
        assert txtHours != null : "fx:id=\"txtHours\" was not injected: check your FXML file 'PowerOutages.fxml'.";
        assert btnWorst != null : "fx:id=\"btnWorst\" was not injected: check your FXML file 'PowerOutages.fxml'.";
        assert boxNerc != null : "fx:id=\"boxNerc\" was not injected: check your FXML file 'PowerOutages.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'PowerOutages.fxml'.";

     	this.txtResult.setStyle("-fx-font-family: monospace");

    }

	public void setModel(Model model) {
		this.model = model;
		this.boxNerc.getItems().addAll(model.getNercList());
	}

}
