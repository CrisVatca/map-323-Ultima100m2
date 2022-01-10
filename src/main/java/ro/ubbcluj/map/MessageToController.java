package ro.ubbcluj.map;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class MessageToController implements Initializable {

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
    private Button backButton;

    @FXML
    private TextArea messageTextArea;

    @FXML
    private TextField messageToField;

    @FXML
    private Button sendMessageButton;

    public static Long Id;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backButton.setOnAction(event -> UserDbUtils.changeScene(event, "user-profile.fxml", "UserProfile", LogInController.getUsernameField()));
        sendMessageButton.setOnAction(event -> sendMessage());
    }


    public void sendMessage(){
        Utilizator utilizatorTo = service.getUserAfterUserName(messageToField.getText());
        Utilizator utilizatorFrom = service.getUserAfterUserName(LogInController.getUsernameField());
        boolean isFriend = false;
        for(Utilizator u: service.prieteniiUnuiUtilizator(utilizatorFrom.getId()).keySet()) {
            if (u.equals(utilizatorTo))
                isFriend = true;
        }
        if(!isFriend) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Utilizatorul nu va este prieten!");
            alert.show();
            return;
        }
        String message = messageTextArea.getText();
        LocalDateTime data = LocalDateTime.now();
        service.addMessage(utilizatorFrom.getId(), utilizatorTo.getId(), message,data, Id);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Mesajul a fost trimis!");
        alert.show();
    }

}
