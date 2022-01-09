package ro.ubbcluj.map;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ro.ubbcluj.map.domain.Cerere;
import ro.ubbcluj.map.domain.Message;
import ro.ubbcluj.map.domain.Prietenie;
import ro.ubbcluj.map.domain.Utilizator;
import ro.ubbcluj.map.domain.validators.CerereValidator;
import ro.ubbcluj.map.domain.validators.MessageValidator;
import ro.ubbcluj.map.domain.validators.PrietenieValidator;
import ro.ubbcluj.map.domain.validators.UtilizatorValidator;
import ro.ubbcluj.map.repository.Repository;
import ro.ubbcluj.map.repository.db.CerereDbRepository;
import ro.ubbcluj.map.repository.db.MessageDbRepository;
import ro.ubbcluj.map.repository.db.PrietenieDbRepository;
import ro.ubbcluj.map.repository.db.UtilizatorDbRepository;
import ro.ubbcluj.map.service.Service;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserProfileController implements Initializable {

    private static final String url = "jdbc:postgresql://localhost:5432/db";
    private static final String username = "postgres";
    private static final String password = "compunere";
    Repository<Long, Utilizator> repoUtilizator = new UtilizatorDbRepository(url, username, password,
            new UtilizatorValidator());
    Repository<Long, Prietenie> repoPrietenie = new PrietenieDbRepository(url, username, password,
            new PrietenieValidator());
    Repository<Long, Message> repoMessage = new MessageDbRepository(url, username, password,
            new MessageValidator());
    Repository<Long, Cerere> repoCerere = new CerereDbRepository(url, username, password,
            new CerereValidator());
    Service service = new Service(repoUtilizator, repoPrietenie, repoMessage, repoCerere);

    @FXML
    private Button friendRequestButton;

    @FXML
    private Button myFriendsButton;

    @FXML
    private Button signOutButton;

    @FXML
    private Label nameLabel;

    @FXML
    private Label usernameLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String usernameLog = LogInController.getUsernameField();
        Utilizator utilizator = getUserByUsername(usernameLog);
        if (utilizator != null) {
            nameLabel.setText(utilizator.getFirstName() + " " + utilizator.getLastName());
            usernameLabel.setText(utilizator.getUserName());
        } else {
            System.out.println("Utilizatorul e null!");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Utilizator inexistent!");
            alert.show();
        }
        signOutButton.setOnAction(event -> UserDbUtils.changeScene(event, "log-in.fxml", "LogIn", usernameLabel.getText()));
        friendRequestButton.setOnAction(event -> UserDbUtils.changeScene(event, "requests.fxml", "All Requests", usernameLabel.getText()));
        myFriendsButton.setOnAction(event -> {
            UserDbUtils.changeScene(event, "find-user.fxml", "FindUser", usernameLabel.getText());
        });
    }

    private Utilizator getUserByUsername(String username){
        List<Utilizator> utilizatorList= service.getUsers();
        for(Utilizator u: utilizatorList){
            if(u.getUserName().equals(username)){
                return u;
            }
        }
        return null;
    }
}
