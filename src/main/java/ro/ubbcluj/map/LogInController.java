package ro.ubbcluj.map;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable {

    @FXML
    private Button loginButton;

    @FXML
    private Button signupButton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private static String stringUsernameField;

    public void initialize(URL location, ResourceBundle resoaurceBundle){
        loginButton.setOnAction(event -> {
            stringUsernameField = usernameField.getText();
            UserDbUtils.logInUser(event, usernameField.getText(), passwordField.getText());
        });
        signupButton.setOnAction(event -> UserDbUtils.changeScene(event, "sign-up.fxml", "SignUp", usernameField.getText()));
    }

    public static String getUsernameField(){
        return stringUsernameField;
    }
}
