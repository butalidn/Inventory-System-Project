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
    private int partID;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isInHouse = true;
        inventory = mainScreenController.initInventory();
        partID = mainScreenController.getPartAutoID();

        addPartToggleGroup = new ToggleGroup();
        this.addPartInHouseRadio.setToggleGroup(addPartToggleGroup);
        this.addPartOutsourcedRadio.setToggleGroup(addPartToggleGroup);
        addPartToggleGroup.selectToggle(addPartInHouseRadio);
    }

    /** Handles when user clicks save button. */
    public void addPartSaveClicked(ActionEvent actionEvent) throws IOException {
        boolean filledOut = false;

        if (!(addPartNameTextField.getText().equals("") ||
            addPartPriceTextField.getText().equals("") ||
            addPartInvTextField.getText().equals("") ||
            addPartPriceTextField.getText().equals("") ||
            addPartMinTextField.getText().equals("") ||
            addPartMaxTextField.getText().equals("") ||
            addPartMachineIDTextField.getText().equals(""))) {
            filledOut = true;
        }
        else {
            System.out.println("Fill out the form.");
        }
        try {
            if (filledOut) {
                if (isInHouse) {
                    inventory.addPart(new InHousePart(
                            partID,
                            addPartNameTextField.getText(),
                            Double.parseDouble(addPartPriceTextField.getText()),
                            Integer.parseInt(addPartInvTextField.getText()),
                            Integer.parseInt(addPartMinTextField.getText()),
                            Integer.parseInt(addPartMaxTextField.getText()),
                            Integer.parseInt(addPartMachineIDTextField.getText())));
                } else {
                    inventory.addPart(new OutsourcedPart(
                            partID ,
                            addPartNameTextField.getText(),
                            Double.parseDouble(addPartPriceTextField.getText()),
                            Integer.parseInt(addPartInvTextField.getText()),
                            Integer.parseInt(addPartMinTextField.getText()),
                            Integer.parseInt(addPartMaxTextField.getText()),
                            addPartMachineIDTextField.getText()));
                }
                Parent mainParent = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
                Stage stage = (Stage) (((Node) actionEvent.getSource()).getScene().getWindow());
                Scene mainScene = new Scene(mainParent);
                stage.setTitle("Inventory Management System");
                stage.setScene(mainScene);
                stage.show();
            }
        } catch (Exception e) {
            System.out.println("Whoops try again");
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
