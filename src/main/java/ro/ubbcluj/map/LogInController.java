package ro.ubbcluj.map;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.util.ResourceBundle;

import java.net.URL;
import java.sql.PseudoColumnUsage;

public class LogInController implements Initializable {

    @FXML
    private Button loginButton;

    @FXML
    private Button signupButton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    public void initialize(URL location, ResourceBundle resoaurceBundle){
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                UserDbUtils.logInUser(event, usernameField.getText(), passwordField.getText());
            }
        });
        signupButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                UserDbUtils.changeScene(event, "sign-up.fxml", "SignUp", usernameField.getText());
            }
        });
    }
}
