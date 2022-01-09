package ro.ubbcluj.map;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable {
    @FXML
    private Button letsGoButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        letsGoButton.setOnAction(event -> UserDbUtils.changeScene(event, "log-in.fxml", "LogIn", null));
    }
}
