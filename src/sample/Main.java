package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage primaryStage; // **Declare static Stage**

    private void setPrimaryStage(Stage stage) {
        Main.primaryStage = stage;
    }

    static public Stage getPrimaryStage() {
        return Main.primaryStage;
    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        setPrimaryStage(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("JavaFX Browser");
        primaryStage.setScene(new Scene(root, 1200, 620));
        root.requestFocus();
        //primaryStage.setFullScreen(true);
        primaryStage.getIcons().add(new Image("file:src/sample/appicon.png"));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
