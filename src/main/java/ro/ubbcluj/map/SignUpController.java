package ro.ubbcluj.map;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @FXML
    private Button registerButton;

    @FXML
    private Button closeButton;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registerButton.setOnAction(event -> {
            if(passwordField.getText().equals(confirmPasswordField.getText())){
                UserDbUtils.signUpUser(event, firstNameField.getText(), lastNameField.getText(), usernameField.getText(),
                        passwordField.getText());
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Parolele nu coincid!");
                alert.show();
            }
        });
        closeButton.setOnAction(event -> UserDbUtils.changeScene(event, "log-in.fxml", "LogIn", usernameField.getText()));
    }
}
