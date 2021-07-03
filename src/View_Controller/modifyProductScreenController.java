package View_Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class modifyProductScreenController implements Initializable {
    @FXML private Label modifyProductWarningLabel;
    @FXML private TextField modifyProductIDTextField;
    @FXML private TextField modifyProductNameTextField;
    @FXML private TextField modifyProductInvTextField;
    @FXML private TextField modifyProductPriceTextField;
    @FXML private TextField modifyProductMaxTextField;
    @FXML private TextField modifyProductMinTextField;
    @FXML private TextField modifyProductSearchField;
    @FXML private TableView modifyProductTableView;
    @FXML private TableView modifyProductAssociatedTableView;
    @FXML private Button modifyProductAddButton;
    @FXML private Button modifyProductRemoveButton;
    @FXML private Button modifyProductSaveButton;
    @FXML private Button modifyProductCancelButton;
    @FXML private TableColumn partIDCol;
    @FXML private TableColumn partNameCol;
    @FXML private TableColumn invCol;
    @FXML private TableColumn priceCol;
    @FXML private TableColumn associatedIDCol;
    @FXML private TableColumn associatedNameCol;
    @FXML private TableColumn associatedInvCol;
    @FXML private TableColumn associatedPriceCol;

    private Inventory inventory = null;
    private Product selectedProduct;
    private Product tempProduct;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inventory = mainScreenController.initInventory();
        selectedProduct = mainScreenController.getSelectedProduct();
        tempProduct = new Product(
                selectedProduct.getId(),
                selectedProduct.getName(),
                selectedProduct.getPrice(),
                selectedProduct.getStock(),
                selectedProduct.getMin(),
                selectedProduct.getMax()
        );
        for (Part part: selectedProduct.getAllAssociatedParts()) {
            tempProduct.addAssociatedPart(part);
        }

        modifyProductIDTextField.setText(String.valueOf(mainScreenController.getSelectedProduct().getId()));
        modifyProductNameTextField.setText(String.valueOf(mainScreenController.getSelectedProduct().getName()));
        modifyProductInvTextField.setText(String.valueOf(mainScreenController.getSelectedProduct().getStock()));
        modifyProductPriceTextField.setText(String.valueOf(mainScreenController.getSelectedProduct().getPrice()));
        modifyProductMaxTextField.setText(String.valueOf(mainScreenController.getSelectedProduct().getMax()));
        modifyProductMinTextField.setText(String.valueOf(mainScreenController.getSelectedProduct().getMin()));

        partIDCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        invCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        associatedIDCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        associatedNameCol.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        associatedInvCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        associatedPriceCol.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        modifyProductTableView.setItems(inventory.getAllParts());
        modifyProductAssociatedTableView.setItems(tempProduct.getAllAssociatedParts());

        modifyProductWarningLabel.setText("");
    }

    /** Handles when add product button is clicked */
    public void modifyProductAddClicked(ActionEvent actionEvent) {
        if (!(modifyProductTableView.getSelectionModel().getSelectedItems().isEmpty())) {
            tempProduct.addAssociatedPart((Part) modifyProductTableView.getSelectionModel().getSelectedItem());
            modifyProductAssociatedTableView.setItems(tempProduct.getAllAssociatedParts());
        }
    }

    /** Handles when remove product button is clicked */
    public void modifyProductRemoveClicked(ActionEvent actionEvent) {
        if (!(modifyProductAssociatedTableView.getSelectionModel().getSelectedItems().isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove Part");
            alert.setContentText("Do you want to remove this part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                tempProduct.getAllAssociatedParts().remove(modifyProductAssociatedTableView.getSelectionModel().getSelectedItem());
            }
        }
    }

    /** Handles when save product button is clicked */
    public void modifyProductSaveClicked(ActionEvent actionEvent) throws IOException {
        String warning = "";
        boolean validData = true;
        String name = "";
        int inv = 0;
        double price = 0;
        int min = 0;
        int max = 0;

        if (modifyProductNameTextField.getText().trim().isEmpty()) {
            warning += "Exception: No data in name field\n";
            validData = false;
        }
        else {
            name = modifyProductNameTextField.getText();
        }
        try {
            inv = Integer.parseInt(modifyProductInvTextField.getText());
        } catch (NumberFormatException e) {
            validData = false;
            warning += "Exception: Inv is not an integer\n";
        }
        try {
            price = Double.parseDouble(modifyProductPriceTextField.getText());
        } catch (NumberFormatException e) {
            validData = false;
            warning += "Exception: Price is not a double\n";
        }
        try {
            max = Integer.parseInt(modifyProductMaxTextField.getText());
        } catch (NumberFormatException e) {
            validData = false;
            warning += "Exception: Max is not an integer\n";
        }
        try {
            min = Integer.parseInt(modifyProductMinTextField.getText());
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

        modifyProductWarningLabel.setText(warning);
        if (validData) {
            Product newProduct;
            newProduct = new Product(
                    selectedProduct.getId(),
                    name,
                    price,
                    inv,
                    min,
                    max);
            inventory.updateProduct(mainScreenController.getProductIndex(), newProduct);
            for (Part part: tempProduct.getAllAssociatedParts()) {
                newProduct.addAssociatedPart(part);
            }
            Parent mainParent = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
            Stage stage = (Stage) (((Node) actionEvent.getSource()).getScene().getWindow());
            Scene mainScene = new Scene(mainParent);
            stage.setTitle("Inventory Management System");
            stage.setScene(mainScene);
            stage.show();
        }
    }

    /** Handles when cancel button is clicked */
    public void modifyProductCancelClicked(ActionEvent actionEvent) throws IOException {
        Parent mainParent = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        Stage stage = (Stage) (((Node) actionEvent.getSource()).getScene().getWindow());
        Scene mainScene = new Scene(mainParent);
        stage.setTitle("Inventory Management System");
        stage.setScene(mainScene);
        stage.show();
    }

    public void modifyProductSearchField() {
        String query = modifyProductSearchField.getText();
        Alert alert = new Alert(Alert.AlertType.WARNING);

        ObservableList<Part> searchedParts = inventory.lookupPart(query);
        if (searchedParts.size() == 0) {
            try {
                int searchID = Integer.parseInt(query);
                if (inventory.lookupPart(searchID) != null) {
                    searchedParts.add(inventory.lookupPart(searchID));
                    modifyProductTableView.setItems(inventory.getAllParts());
                    modifyProductTableView.getSelectionModel().select(inventory.lookupPart(searchID));
                }
                else {
                    alert.setTitle("Part Not Found");
                    alert.setContentText("Your searched part was not found");
                    alert.setHeaderText("Error");
                    Optional<ButtonType> result = alert.showAndWait();
                }
            } catch (Exception e) {
                alert.setTitle("Part Not Found");
                alert.setContentText("Your searched part was not found");
                alert.setHeaderText("Error");
                Optional<ButtonType> result = alert.showAndWait();
            }
        }
        else {
            modifyProductTableView.setItems(searchedParts);
        }
    }
}
