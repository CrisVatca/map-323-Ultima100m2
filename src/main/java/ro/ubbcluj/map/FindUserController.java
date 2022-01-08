package ro.ubbcluj.map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import ro.ubbcluj.map.domain.Utilizator;

import java.net.URL;
import java.util.ResourceBundle;

public class FindUserController implements Initializable {

    @FXML
    private TableColumn<Utilizator, String> firstNameColumn;

    @FXML
    private TableView<Utilizator> friendsTable;

    @FXML
    private TableColumn<Utilizator, String> lastNameColumn;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchFriendField;

    @FXML
    private TableColumn<Utilizator, String> usernameColumn;

    ObservableList<Utilizator> model = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
