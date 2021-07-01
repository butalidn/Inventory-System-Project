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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class addProductScreenController implements Initializable {

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
    private int productID;
    private ObservableList<Part> associatedParts;
    //private Product selectedProduct;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inventory = mainScreenController.initInventory();
        productID = mainScreenController.getProductAutoID();
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

        addProductTableView.setItems(inventory.getAllParts());
        /*if (selectedProduct.getAllAssociatedParts().size() != 0) {

        } */
    }

    /** Handles when add product button is clicked. */
    public void addProductAddPartClicked(ActionEvent actionEvent) {
        /* selectedProduct.addAssociatedPart((Part) addProductTableView.getSelectionModel().getSelectedItem());
         */
        associatedParts.add(addProductTableView.getSelectionModel().getSelectedItem());
        addProductAssociatedTableView.setItems(associatedParts);
    }

    /** Handles when remove product button is clicked. */
    public void addProductRemoveClicked(ActionEvent actionEvent) {
    }

    /** Handles when save product button is clicked. */
    public void addProductSaveClicked(ActionEvent actionEvent) {
        boolean filledOut = false;

        if (!(addProductNameTextField.getText().equals("") ||
                addProductInvTextField.getText().equals("") ||
                addProductPriceTextField.getText().equals("") ||
                addProductMaxTextField.getText().equals("") ||
                addProductMinTextField.getText().equals(""))) {
            filledOut = true;
        }
        else {
            System.out.println("Fill out the form.");
        }
        try {
            if (filledOut) {
                Product saveProduct = new Product(
                        productID ,
                        addProductNameTextField.getText(),
                        Double.parseDouble(addProductPriceTextField.getText()),
                        Integer.parseInt(addProductInvTextField.getText()),
                        Integer.parseInt(addProductMinTextField.getText()),
                        Integer.parseInt(addProductMaxTextField.getText()));
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
        } catch (Exception e) {
            System.out.println("Whoops try again");
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

        ObservableList<Part> searchedParts = inventory.lookupPart(query);
        if (searchedParts.size() == 0) {
            try {
                int searchID = Integer.parseInt(query);
                if (inventory.lookupPart(searchID) != null) {
                    searchedParts.add(inventory.lookupPart(searchID));
                    addProductTableView.setItems(inventory.getAllParts());
                    addProductTableView.getSelectionModel().select(searchID - 1);
                }
                else {
                    System.out.println("Error: part not found");
                }
            } catch (Exception e) {
                System.out.println("Error: part not found");
            }
        }
        else {
            addProductTableView.setItems(searchedParts);
        }
    }
}
