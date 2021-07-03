//Author: Nikolas Butalid
//WGU C482 PA

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class InventorySystem extends Application {

    /** FUTURE ENHANCEMENT. A way that would be this application more useful would be the ability to upload or import
     * data from an external source like a CSV file or a database*/
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("View_Controller/mainScreen.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
