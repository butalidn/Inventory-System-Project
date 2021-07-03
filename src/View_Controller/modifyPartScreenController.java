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
    @FXML private Label modifyPartWarningLabel;
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

        modifyPartWarningLabel.setText("");

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
        String warning = "";
        boolean validData = true;
        String name = "";
        String companyName = modifyPartMachineIDTextField.getText();
        int inv = 0;
        double price = 0;
        int min = 0;
        int max = 0;
        int machineID = 0;

        if (modifyPartNameTextField.getText().trim().isEmpty()) {
            warning += "Exception: No data in name field\n";
            validData = false;
        }
        else {
            name = modifyPartNameTextField.getText();
        }
        try {
            inv = Integer.parseInt(modifyPartInvTextField.getText());
        } catch (NumberFormatException e) {
            validData = false;
            warning += "Exception: Inv is not an integer\n";
        }
        try {
            price = Double.parseDouble(modifyPartPriceTextField.getText());
        } catch (NumberFormatException e) {
            validData = false;
            warning += "Exception: Price is not a double\n";
        }
        try {
            max = Integer.parseInt(modifyPartMaxTextField.getText());
        } catch (NumberFormatException e) {
            validData = false;
            warning += "Exception: Max is not an integer\n";
        }
        try {
            min = Integer.parseInt(modifyPartMinTextField.getText());
            if (max < min) {
                warning += "Error: Min must be smaller than Max\n";
                validData = false;
            }
            if ((inv > max) || (inv < min )) {
                warning += "Error: Inv must be larger than Min\nand smaller than Max\n";
                validData = false;
            }
        } catch (NumberFormatException e) {
            validData = false;
            warning += "Exception: Min is not an integer\n";
        }
        if (isInHouse) {
            try {
                machineID = Integer.parseInt(modifyPartMachineIDTextField.getText());
            } catch (NumberFormatException e) {
                warning += "Exception: Machine ID is not an integer\n";
                validData = false;
            }
        }
        else {
            if (companyName.trim().isEmpty()) {
                warning += "Exception: No data in company field\n";
                validData = false;
            }
        }
        modifyPartWarningLabel.setText(warning);
        if (validData) {
            if (isInHouse == true) {
                newPart = new InHousePart(
                        selectedPart.getId(),
                        name,
                        price,
                        inv,
                        min,
                        max,
                        machineID);
            }
            else {
                newPart = new OutsourcedPart(
                        selectedPart.getId(),
                        name,
                        price,
                        inv,
                        min,
                        max,
                        companyName);
            }
            inventory.updatePart(mainScreenController.getPartIndex(), newPart);

            Parent mainParent = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
            Stage stage = (Stage) (((Node) actionEvent.getSource()).getScene().getWindow());
            Scene mainScene = new Scene(mainParent);
            stage.setTitle("Inventory Management System");
            stage.setScene(mainScene);
            stage.show();
        }
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
