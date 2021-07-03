package Model;

import View_Controller.mainScreenController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    private ObservableList<Part> allParts;
    private ObservableList<Product> allProducts;

    public Inventory() {
        allParts = FXCollections.observableArrayList();
        allProducts = FXCollections.observableArrayList();
        this.addPart(new InHousePart(mainScreenController.getPartAutoID(),"Tire", 39.99, 32, 20, 50,2));
        this.addPart(new InHousePart(mainScreenController.getPartAutoID(),"Hood", 299.00, 25, 10, 40,2));
        this.addPart(new OutsourcedPart(mainScreenController.getPartAutoID(),"Bolt",1.99 , 1000, 30, 1500,"Ace Hardware"));
        this.addPart(new OutsourcedPart(mainScreenController.getPartAutoID(),"Nail", 1.50, 1500, 1000, 2500,"Home Depot"));
        this.addPart(new OutsourcedPart(mainScreenController.getPartAutoID(),"Ball", 3, 50, 25, 100,"Dick's Sporting Goods"));
        this.addProduct(new Product(mainScreenController.getProductAutoID(),"Car", 3999.99, 7, 2, 14));
        this.addProduct(new Product(mainScreenController.getProductAutoID(),"Bike", 150.00, 25, 2, 50));
        this.addProduct(new Product(mainScreenController.getProductAutoID(),"House", 10000, 4, 2, 10));
        this.addProduct(new Product(mainScreenController.getProductAutoID(),"Phone", 499, 45, 10, 100));
        this.addProduct(new Product(mainScreenController.getProductAutoID(),"Desk", 149.99, 40, 40, 45));
    }

    public void addPart(Part newPart) {
        this.getAllParts().add(newPart);
    }

    public void addProduct(Product newProduct) {
        this.allProducts.add(newProduct);
    }

    public Part lookupPart(int partID) {
        ObservableList<Part> searchedPart = FXCollections.observableArrayList();

        for (Part part: allParts) {
            if (part.getId() == partID) {
                return part;
            }
        }
        return null;
    }

    public Product lookupProduct(int productID) {
        ObservableList<Product> searchProduct = FXCollections.observableArrayList();

        for (Product product: allProducts) {
            if (product.getId() == productID) {
                return product;
            }
        }
        return null;
     }

    public ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> searchedPart = FXCollections.observableArrayList();

        for (Part part: allParts) {
            if ((part.getName().toLowerCase()).contains(partName.toLowerCase())) {
                searchedPart.add(part);
            }
        }

        return searchedPart;
    }

    public ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> searchedProduct = FXCollections.observableArrayList();

        for (Product product: allProducts) {
            if ((product.getName().toLowerCase()).contains(productName.toLowerCase())) {
                searchedProduct.add(product);
            }
        }
        return searchedProduct;
    }

    public void updatePart(int index, Part selectedPart) {
        allParts.set(index,selectedPart);
    }

    public void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    public boolean deletePart(Part selectedPart) {
        if (allParts.contains(selectedPart)) {
            allProducts.remove(selectedPart);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean deleteProduct(Product selectedProduct) {
        if (allProducts.contains(selectedProduct)) {
            allProducts.contains(selectedProduct);
            return true;
        }
        else {
            return false;
        }
    }

    public ObservableList<Part> getAllParts() {
        return allParts;
    }

    public ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
