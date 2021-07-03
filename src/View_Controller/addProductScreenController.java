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

public class addProductScreenController implements Initializable {

    @FXML private Label addProductWarningLabel;
    @FXML private TableColumn<Part, Integer> partIDCol;
    @FXML private TableColumn<Part, String> partNameCol;
    @FXML private TableColumn<Part, Integer> invCol;
    @FXML private TableColumn<Part, Double> priceCol;
    @FXML private TableColumn<Part, Integer> associatedIDCol;
    @FXML private TableColumn<Part, String> associatedNameCol;
    @FXML private TableColumn<Part, Integer> associatedInvCol;
    @FXML private TableColumn<Part, Double> associatedPriceCol;
    @FXML private Button addProductAddPartButton;
    @FXML private Button addProductRemoveButton;
    @FXML private Button addProductSaveButton;
    @FXML private Button addProductCancelButton;
    @FXML private TextField addProductInvTextField;
    @FXML private TextField addProductIDTextField;
    @FXML private TextField addProductNameTextField;
    @FXML private TextField addProductPriceTextField;
    @FXML private TextField addProductMaxTextField;
    @FXML private TextField addProductMinTextField;
    @FXML private TextField addProductSearchField;
    @FXML private TableView<Part> addProductTableView;
    @FXML private TableView<Part> addProductAssociatedTableView;

    private Inventory inventory = null;
    private ObservableList<Part> associatedParts;
    //private Product selectedProduct;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inventory = mainScreenController.initInventory();
        associatedParts = FXCollections.observableArrayList();
        //selectedProduct = mainScreenController.getSelectedProduct();

        partIDCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        invCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        associatedIDCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        associatedNameCol.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        associatedInvCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        associatedPriceCol.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        addProductWarningLabel.setText("");

        addProductTableView.setItems(inventory.getAllParts());
        /*if (selectedProduct.getAllAssociatedParts().size() != 0) {

        } */
    }

    /** Handles when add product button is clicked. */
    public void addProductAddPartClicked(ActionEvent actionEvent) {
        if (!(addProductTableView.getSelectionModel().getSelectedItems().isEmpty())) {
            associatedParts.add(addProductTableView.getSelectionModel().getSelectedItem());
            addProductAssociatedTableView.setItems(associatedParts);
        }
    }

    /** Handles when remove product button is clicked. */
    public void addProductRemoveClicked(ActionEvent actionEvent) {
        if (!(addProductAssociatedTableView.getSelectionModel().getSelectedItems().isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove Part");
            alert.setContentText("Do you want to remove this part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                associatedParts.remove(addProductTableView.getSelectionModel().getSelectedItem());
            }
        }
    }

    /** Handles when save product button is clicked. */
    public void addProductSaveClicked(ActionEvent actionEvent) throws IOException {
        String warning = "";
        boolean validData = true;
        String name = "";
        int inv = 0;
        double price = 0;
        int min = 0;
        int max = 0;

        if (addProductNameTextField.getText().trim().isEmpty()) {
            warning += "Exception: No data in name field\n";
            validData = false;
        }
        else {
            name = addProductNameTextField.getText();
        }
        try {
            inv = Integer.parseInt(addProductInvTextField.getText());
        } catch (NumberFormatException e) {
            validData = false;
            warning += "Exception: Inv is not an integer\n";
        }
        try {
            price = Double.parseDouble(addProductPriceTextField.getText());
        } catch (NumberFormatException e) {
            validData = false;
            warning += "Exception: Price is not a double\n";
        }
        try {
            max = Integer.parseInt(addProductMaxTextField.getText());
        } catch (NumberFormatException e) {
            validData = false;
            warning += "Exception: Max is not an integer\n";
        }
        try {
            min = Integer.parseInt(addProductMinTextField.getText());
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

        addProductWarningLabel.setText(warning);
        if (validData) {
            Product saveProduct = new Product(
                    mainScreenController.getProductAutoID() ,
                    name,
                    price,
                    inv,
                    min,
                    max);
            inventory.addProduct(saveProduct);
            for (Part part: associatedParts) {
                saveProduct.addAssociatedPart(part);
            }
            Parent mainParent = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
            Stage stage = (Stage) (((Node) actionEvent.getSource()).getScene().getWindow());
            Scene mainScene = new Scene(mainParent);
            stage.setTitle("Inventory Management System");
            stage.setScene(mainScene);
            stage.show();
        }
    }

    /** Handles when cancel button is clicked. */
    public void addProductCancelClicked(ActionEvent actionEvent) throws IOException {
        Parent mainParent = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        Stage stage = (Stage) (((Node) actionEvent.getSource()).getScene().getWindow());
        Scene mainScene = new Scene(mainParent);
        stage.setTitle("Inventory Management System");
        stage.setScene(mainScene);
        stage.show();
    }

    public void addProductSearchFieldTyped() {
        String query = addProductSearchField.getText();
        Alert alert = new Alert(Alert.AlertType.WARNING);

        ObservableList<Part> searchedParts = inventory.lookupPart(query);
        if (searchedParts.size() == 0) {
            try {
                int searchID = Integer.parseInt(query);
                if (inventory.lookupPart(searchID) != null) {
                    searchedParts.add(inventory.lookupPart(searchID));
                    addProductTableView.setItems(inventory.getAllParts());
                    addProductTableView.getSelectionModel().select(inventory.lookupPart(searchID));
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
            addProductTableView.setItems(searchedParts);
        }
    }
}
