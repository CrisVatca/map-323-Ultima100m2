package ro.ubbcluj.map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class StartApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("start.fxml")));
        primaryStage.setTitle("Start");
        primaryStage.setScene(new Scene(root, 500, 450));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}