package ro.ubbcluj.map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ro.ubbcluj.map.domain.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MessageFromController implements Initializable {

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

    ObservableList<MessageGui> model = FXCollections.observableArrayList();

    Utilizator utilizator;

    @FXML
    private TableColumn<MessageGui, String> messageColumn;

    @FXML
    private TableColumn<MessageGui, String> messageToColumn;

    @FXML
    private Button backButton;

    @FXML
    private TableColumn<MessageGui, LocalDateTime> dateColumn;

    @FXML
    private Button deleteMessageButton;

    @FXML
    private TableColumn<MessageGui, String> messageFromColumn;

    @FXML
    private TableView<MessageGui> messageView;

    @FXML
    private Button replyButton;

    public static Long replyId = 0L;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initModel();
        messageFromColumn.setCellValueFactory(new PropertyValueFactory<MessageGui, String>("from"));
        messageToColumn.setCellValueFactory(new PropertyValueFactory<MessageGui, String>("to"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<MessageGui, LocalDateTime>("data"));
        messageColumn.setCellValueFactory(new PropertyValueFactory<MessageGui, String>("message"));
        messageView.setItems(model);
        backButton.setOnAction(event -> UserDbUtils.changeScene(event, "user-profile.fxml", "UserProfile",
                LogInController.getUsernameField()));
        deleteMessageButton.setOnAction(event -> deleteMessage());
        replyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(messageView.getSelectionModel().getSelectedItem() == null){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Nu a fost selectat niciun mesaj!");
                    alert.show();
                    return;
                }

                UserDbUtils.changeScene(event, "message-to.fxml", "MessageTo",
                        LogInController.getUsernameField());
                sendId();
            }
        });
    }

    private void initModel() {
        utilizator = service.getUserAfterUserName(LogInController.getUsernameField());
        model.setAll(service.getMessagesOf(utilizator));

    }

    public void deleteMessage(){
        MessageGui messageGui = messageView.getSelectionModel().getSelectedItem();
        if(messageGui == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Nu a fost selectat niciun mesaj!");
            alert.show();
            return;
        }
        service.deleteMessage(messageGui.getId());
        initModel();
    }

    public void sendId(){
        MessageGui messageGui = messageView.getSelectionModel().getSelectedItem();
        if(messageGui!=null)
            MessageToController.Id = messageGui.getId();
    }

}
