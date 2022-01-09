package ro.ubbcluj.map;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private final Label welcomeText;

    public HelloController(Label welcomeText) {
        this.welcomeText = welcomeText;
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}