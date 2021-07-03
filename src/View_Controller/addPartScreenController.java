package View_Controller;

import Model.InHousePart;
import Model.Inventory;
import Model.OutsourcedPart;
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
import java.util.ResourceBundle;

public class addPartScreenController implements Initializable {
    @FXML private Label addPartWarningLabel;
    @FXML private Label machineLabel;
    @FXML private  RadioButton addPartInHouseRadio;
    @FXML private  RadioButton addPartOutsourcedRadio;
    @FXML private  Button addPartSaveButton;
    @FXML private  Button addPartCancelButton;
    @FXML private  TextField addPartMinTextField;
    @FXML private  TextField addPartIDTextField;
    @FXML private  TextField addPartNameTextField;
    @FXML private  TextField addPartInvTextField;
    @FXML private  TextField addPartPriceTextField;
    @FXML private  TextField addPartMaxTextField;
    @FXML private  TextField addPartMachineIDTextField;

    private Inventory inventory = null;
    private ToggleGroup addPartToggleGroup;
    private boolean isInHouse;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isInHouse = true;
        inventory = mainScreenController.initInventory();

        addPartToggleGroup = new ToggleGroup();
        this.addPartInHouseRadio.setToggleGroup(addPartToggleGroup);
        this.addPartOutsourcedRadio.setToggleGroup(addPartToggleGroup);
        addPartToggleGroup.selectToggle(addPartInHouseRadio);
        addPartWarningLabel.setText("");
    }

    /** Handles when user clicks save button. */
    public void addPartSaveClicked(ActionEvent actionEvent) throws IOException {
        String warning = "";
        boolean validData = true;
        String name = "";
        String companyName = "";
        int inv = 0;
        double price = 0;
        int min = 0;
        int max = 0;
        int machineID = 0;

        if (addPartNameTextField.getText().trim().isEmpty()) {
            warning += "Exception: No data in name field\n";
            validData = false;
        }
        else {
            name = addPartNameTextField.getText();
        }
        try {
            inv = Integer.parseInt(addPartInvTextField.getText());
        } catch (NumberFormatException e) {
            validData = false;
            warning += "Exception: Inv is not an integer\n";
        }
        try {
            price = Double.parseDouble(addPartPriceTextField.getText());
        } catch (NumberFormatException e) {
            validData = false;
            warning += "Exception: Price is not a double\n";
        }
        try {
            max = Integer.parseInt(addPartMaxTextField.getText());
        } catch (NumberFormatException e) {
            validData = false;
            warning += "Exception: Max is not an integer\n";
        }
        try {
            min = Integer.parseInt(addPartMinTextField.getText());
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
                machineID = Integer.parseInt(addPartMachineIDTextField.getText());
            } catch (NumberFormatException e) {
                warning += "Exception: Machine ID is not an integer\n";
                validData = false;
            }
        }
        else {
            if (addPartMachineIDTextField.getText().trim().isEmpty()) {
                warning += "Exception: No data in company field\n";
                validData = false;
            }
            else {
                companyName = addPartMachineIDTextField.getText();
            }
        }
        addPartWarningLabel.setText(warning);
        if (validData) {
            if (isInHouse) {
                inventory.addPart(new InHousePart(
                        mainScreenController.getPartAutoID(),
                        name,
                        price,
                        inv,
                        min,
                        max,
                        machineID));
            } else {
                inventory.addPart(new OutsourcedPart(
                        mainScreenController.getPartAutoID(),
                        name,
                        price,
                        inv,
                        min,
                        max,
                        companyName));
            }
            Parent mainParent = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
            Stage stage = (Stage) (((Node) actionEvent.getSource()).getScene().getWindow());
            Scene mainScene = new Scene(mainParent);
            stage.setTitle("Inventory Management System");
            stage.setScene(mainScene);
            stage.show();
        }
    }


    /** Handles when user clicks cancel button. */
    public void addPartCancelClicked(ActionEvent actionEvent) throws IOException {
        Parent mainParent = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        Stage stage = (Stage) (((Node) actionEvent.getSource()).getScene().getWindow());
        Scene mainScene = new Scene(mainParent);
        stage.setTitle("Inventory Management System");
        stage.setScene(mainScene);
        stage.show();
    }

    /** Handles when user selects a radio button. */
    public void addPartRadioSelected(ActionEvent actionEvent) {
        if (this.addPartToggleGroup.getSelectedToggle().equals(addPartInHouseRadio)) {
            machineLabel.setText("Machine ID");
            isInHouse = true;
        }
        else {
            machineLabel.setText("Company Name");
            isInHouse = false;
        }
    }


}
