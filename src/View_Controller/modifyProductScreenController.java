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
import java.util.ResourceBundle;

public class modifyProductScreenController implements Initializable {
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
    //private ObservableList<Part> associatedParts;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inventory = mainScreenController.initInventory();
        selectedProduct = mainScreenController.getSelectedProduct();

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
        modifyProductAssociatedTableView.setItems(selectedProduct.getAllAssociatedParts());
    }

    /** Handles when add product button is clicked */
    public void modifyProductAddClicked(ActionEvent actionEvent) {
        selectedProduct.addAssociatedPart((Part)modifyProductTableView.getSelectionModel().getSelectedItem());
        modifyProductAssociatedTableView.setItems(selectedProduct.getAllAssociatedParts());
    }

    /** Handles when remove product button is clicked */
    public void modifyProductRemoveClicked(ActionEvent actionEvent) {
        selectedProduct.deleteAssociatedPart((Part)modifyProductAssociatedTableView.getSelectionModel().getSelectedItem());
    }

    /** Handles when save product button is clicked */
    public void modifyProductSaveClicked(ActionEvent actionEvent) throws IOException {
        Product newProduct;
        newProduct = new Product(
                    selectedProduct.getId(),
                    modifyProductNameTextField.getText(),
                    Double.parseDouble(modifyProductPriceTextField.getText()),
                    Integer.parseInt(modifyProductInvTextField.getText()),
                    Integer.parseInt(modifyProductMinTextField.getText()),
                    Integer.parseInt(modifyProductMaxTextField.getText()));
        inventory.updateProduct(mainScreenController.getProductIndex(), newProduct);
        for (Part part: selectedProduct.getAllAssociatedParts()) {
            newProduct.addAssociatedPart(part);
        }

        Parent mainParent = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        Stage stage = (Stage) (((Node) actionEvent.getSource()).getScene().getWindow());
        Scene mainScene = new Scene(mainParent);
        stage.setTitle("Inventory Management System");
        stage.setScene(mainScene);
        stage.show();
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
}
