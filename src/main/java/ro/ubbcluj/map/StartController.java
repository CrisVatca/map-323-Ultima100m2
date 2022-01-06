package ro.ubbcluj.map;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
        letsGoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                UserDbUtils.changeScene(event, "log-in.fxml", "LogIn", null);
            }
        });
    }
}
