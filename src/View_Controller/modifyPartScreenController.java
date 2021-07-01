package View_Controller;

import Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;;

public class modifyPartScreenController implements Initializable {
    @FXML private Label machineLabel;
    @FXML private RadioButton inHouseRadio;
    @FXML private RadioButton outsourcedRadio;
    @FXML private Button modifyPartSaveButton;
    @FXML private Button modifyPartCancelButton;
    @FXML private TextField modifyPartMinTextField;
    @FXML private TextField modifyPartIDTextField;
    @FXML private TextField modifyPartNameTextField;
    @FXML private TextField modifyPartInvTextField;
    @FXML private TextField modifyPartPriceTextField;
    @FXML private TextField modifyPartMaxTextField;
    @FXML private TextField modifyPartMachineIDTextField;

    private Inventory inventory = null;
    private boolean isInHouse;
    private Part selectedPart;
    private ToggleGroup modifyPartToggleGroup;

    /** RUNTIME ERROR. Had a ClassCastException error. Tried to cast the selectedPart object from the main controller as an
     In-House part. If the user added a part as an Outsourced part, it would be incompatible with a In-House Part cas0. Fixed by removing
     In-House cast and changing private member to a Part object instead of In-House and Outsourced Parts.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inventory = mainScreenController.initInventory();
        selectedPart = mainScreenController.getSelectedPart();

        modifyPartToggleGroup = new ToggleGroup();
        this.inHouseRadio.setToggleGroup(modifyPartToggleGroup);
        this.outsourcedRadio.setToggleGroup(modifyPartToggleGroup);

        modifyPartIDTextField.setText(String.valueOf(mainScreenController.getSelectedPart().getId()));
        modifyPartNameTextField.setText(String.valueOf(mainScreenController.getSelectedPart().getName()));
        modifyPartInvTextField.setText(String.valueOf(mainScreenController.getSelectedPart().getStock()));
        modifyPartPriceTextField.setText(String.valueOf(mainScreenController.getSelectedPart().getPrice()));
        modifyPartMaxTextField.setText(String.valueOf(mainScreenController.getSelectedPart().getMax()));
        modifyPartMinTextField.setText(String.valueOf(mainScreenController.getSelectedPart().getMin()));

        if (selectedPart instanceof InHousePart) {
            modifyPartMachineIDTextField.setText(String.valueOf(((InHousePart) mainScreenController.getSelectedPart()).getMachineID()));
            modifyPartToggleGroup.selectToggle(inHouseRadio);
            isInHouse = true;
        }
        else {
            modifyPartMachineIDTextField.setText(((OutsourcedPart) mainScreenController.getSelectedPart()).getCompanyName());
            modifyPartToggleGroup.selectToggle(outsourcedRadio);
            machineLabel.setText("Company Name");
            isInHouse = false;
        }
    }

    /** Handles when user clicks save button. */
    public void modifyPartSaveClicked(ActionEvent actionEvent) throws IOException {
        Part newPart;
        if (isInHouse == true) {
            newPart = new InHousePart(
                    selectedPart.getId(),
                    modifyPartNameTextField.getText(),
                    Double.parseDouble(modifyPartPriceTextField.getText()),
                    Integer.parseInt(modifyPartInvTextField.getText()),
                    Integer.parseInt(modifyPartMinTextField.getText()),
                    Integer.parseInt(modifyPartMaxTextField.getText()),
                    Integer.parseInt(modifyPartMachineIDTextField.getText()));
        }
        else {
            newPart = new OutsourcedPart(
                    selectedPart.getId(),
                    modifyPartNameTextField.getText(),
                    Double.parseDouble(modifyPartPriceTextField.getText()),
                    Integer.parseInt(modifyPartInvTextField.getText()),
                    Integer.parseInt(modifyPartMinTextField.getText()),
                    Integer.parseInt(modifyPartMaxTextField.getText()),
                    modifyPartMachineIDTextField.getText());
        }
        inventory.updatePart(mainScreenController.getPartIndex(), newPart);

        Parent mainParent = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        Stage stage = (Stage) (((Node) actionEvent.getSource()).getScene().getWindow());
        Scene mainScene = new Scene(mainParent);
        stage.setTitle("Inventory Management System");
        stage.setScene(mainScene);
        stage.show();
    }

    /** Handles when user clicks cancel button. */
    public void modifyPartCancelClicked(ActionEvent actionEvent) throws IOException {
        Parent mainParent = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        Stage stage = (Stage) (((Node) actionEvent.getSource()).getScene().getWindow());
        Scene mainScene = new Scene(mainParent);
        stage.setTitle("Inventory Management System");
        stage.setScene(mainScene);
        stage.show();
    }

    /** Handles when user selects radio button. */
    public void modifyPartRadioSelected(ActionEvent actionEvent) throws IOException {
        if (modifyPartToggleGroup.getSelectedToggle().equals(inHouseRadio)) {
            machineLabel.setText("Machine ID");
            isInHouse = true;
        }
        else {
            machineLabel.setText("Company Name");
            isInHouse = false;
        }
    }
}
