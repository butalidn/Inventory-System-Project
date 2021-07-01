package View_Controller;

import Model.*;
import com.sun.scenario.effect.impl.prism.PrDrawable;
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

public class mainScreenController implements Initializable {

    @FXML private Label warningLabel;
    @FXML private TableColumn<Part, Integer> partIDCol;
    @FXML private TableColumn<Part, String> partNameCol;
    @FXML private TableColumn<Part, Integer> partInvCol;
    @FXML private TableColumn<Part, Double> partPriceCol;
    @FXML private TableColumn<Product, Integer> productIDCol;
    @FXML private TableColumn<Product, String> productNameCol;
    @FXML private TableColumn<Product, Integer> productInvCol;
    @FXML private TableColumn<Product, Double> productPriceCol;
    @FXML private TextField partsTextField;
    @FXML private TextField productsTextField;
    @FXML private TableView<Product> productsTableView;
    @FXML private TableView<Part> partsTableView;
    @FXML private Button addPartButton;
    @FXML private Button modifyPartButton;
    @FXML private Button deletePartButton;
    @FXML private Button addProductButton;
    @FXML private Button modifyProductButton;
    @FXML private Button deleteProductButton;
    @FXML private Button exitButton;

    private static Inventory inventory = new Inventory();
    private static Inventory dataInit = null;
    private static Part selectedPart;
    private static Product selectedProduct;
    private static int partAutoID;
    private static int productAutoID;
    private static int partIndex;
    private static int productIndex;
    //private ObservableList<Part> searchedParts;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partIDCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        productIDCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        productInvCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        partsTableView.setItems(inventory.getAllParts());
        productsTableView.setItems(inventory.getAllProducts());
        partsTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        productsTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        warningLabel.setText("");
    }

    public static Inventory initInventory() {
        return dataInit;
    }

    /** Handles when add part button is clicked. */
    public void addPartClicked(ActionEvent actionEvent) throws IOException {
        dataInit = inventory;
        Parent addPartParent = FXMLLoader.load(getClass().getResource("addPartScreen.fxml"));
        Stage stage = (Stage) (((Node)actionEvent.getSource()).getScene().getWindow());
        Scene addPartScene = new Scene(addPartParent);
        stage.setTitle("Add Part");
        stage.setScene(addPartScene);
        stage.show();
    }

    /** Handles when add part button is clicked. */
    public void modifyPartClicked(ActionEvent actionEvent) throws IOException {
        dataInit = inventory;
        selectedPart = partsTableView.getSelectionModel().getSelectedItem();
        partIndex = partsTableView.getSelectionModel().getSelectedIndex();
        Parent modifyPartParent = FXMLLoader.load(getClass().getResource("modifyPartScreen.fxml"));
        Stage stage = (Stage) (((Node)actionEvent.getSource()).getScene().getWindow());
        Scene modifyPartScene = new Scene(modifyPartParent);
        stage.setTitle("Modify Part");
        stage.setScene(modifyPartScene);
        stage.show();
    }

    /** Handles when add part button is clicked. */
    public void deletePartClicked(ActionEvent actionEvent) throws IOException {
        if (!(partsTableView.getSelectionModel().getSelectedItems().isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Part");
            alert.setContentText("Do you want to delete this part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                (inventory.getAllParts()).remove(partsTableView.getSelectionModel().getSelectedItem());
                this.searchPartTextFieldTyped();
            }
        }
    }

    /** Handles when add part button is clicked. */
    public void addProductClicked(ActionEvent actionEvent) throws IOException {
        dataInit = inventory;
        selectedProduct = productsTableView.getSelectionModel().getSelectedItem();
        Parent addProductParent = FXMLLoader.load(getClass().getResource("addProductScreen.fxml"));
        Stage stage = (Stage) (((Node) actionEvent.getSource()).getScene().getWindow());
        Scene addProductScene = new Scene(addProductParent);
        stage.setTitle("Add Product");
        stage.setScene(addProductScene);
        stage.show();
    }

    /** Handles when modify part button is clicked. */
    public void modifyProductClicked(ActionEvent actionEvent) throws IOException {
        dataInit = inventory;
        selectedProduct = productsTableView.getSelectionModel().getSelectedItem();
        productIndex = productsTableView.getSelectionModel().getSelectedIndex();
        Parent modifyProductParent = FXMLLoader.load(getClass().getResource("modifyProductScreen.fxml"));
        Stage stage = (Stage) (((Node)actionEvent.getSource()).getScene().getWindow());
        Scene modifyProductScene = new Scene(modifyProductParent);
        stage.setTitle("Modify Product");
        stage.setScene(modifyProductScene);
        stage.show();
    }

    /** Handles when delete product button is clicked. */
    public void deleteProductClicked(ActionEvent actionEvent) throws IOException {
        selectedProduct = productsTableView.getSelectionModel().getSelectedItem();
        if (!(productsTableView.getSelectionModel().getSelectedItems().isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Product");
            alert.setContentText("Do you want to delete this product?");
            Optional<ButtonType> result = alert.showAndWait();

            if (selectedProduct.getAllAssociatedParts().size() > 0) {
                warningLabel.setText("This product has parts\n");
            }
            else {
                if (result.get() == ButtonType.OK) {
                    (inventory.getAllProducts()).remove(productsTableView.getSelectionModel().getSelectedItem());
                    this.searchProductTextFieldTyped();
                }
            }
        }
    }


    /** Handles when exit button is clicked. */
    public void exitClicked(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public static int getPartAutoID() {
        partAutoID += 1;
        return partAutoID;
    }

    public static int getProductAutoID() {
        productAutoID += 1;
        return productAutoID;
    }

    public static Part getSelectedPart() {
        return selectedPart;
    }

    public static Product getSelectedProduct() {
        return selectedProduct;
    }

    public static void setSelectedPart(Part newSelectedPart) {
        mainScreenController.selectedPart = newSelectedPart;
        inventory.addPart(selectedPart);
    }

    public static int getPartIndex() {
        return partIndex;
    }

    public static int getProductIndex() {
        return productIndex;
    }

    public void searchPartTextFieldTyped() {
        String query = partsTextField.getText();

        ObservableList<Part> searchedParts = inventory.lookupPart(query);
        if (searchedParts.size() == 0) {
            try {
                int searchID = Integer.parseInt(query);
                if (inventory.lookupPart(searchID) != null) {
                    searchedParts.add(inventory.lookupPart(searchID));
                    partsTableView.setItems(inventory.getAllParts());
                    partsTableView.getSelectionModel().select(searchID - 1);
                }
                else {
                    System.out.println("Error: part not found");
                }
            } catch (Exception e) {
                System.out.println("Error: part not found");
            }
        }
        else {
            partsTableView.setItems(searchedParts);
        }
    }

    public void searchProductTextFieldTyped() {
        String query = productsTextField.getText();

        ObservableList<Product> searchedProducts = inventory.lookupProduct(query);
        if (searchedProducts.size() == 0) {
            try {
                int searchID = Integer.parseInt(query);
                if (inventory.lookupProduct(searchID) != null) {
                    searchedProducts.add(inventory.lookupProduct(searchID));
                    productsTableView.setItems(inventory.getAllProducts());
                    productsTableView.getSelectionModel().select(searchID - 1);
                } else {
                    System.out.println("Error: product not found");
                }
            } catch (Exception e) {
                System.out.println("Error: product not found");
            }
        } else {
            productsTableView.setItems(searchedProducts);
        }
    }
}
