package ro.ubbcluj.map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FindUserController implements Initializable {

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
    private TableColumn<Utilizator, String> firstNameColumn;

    @FXML
    private TableView<Utilizator> friendsTable;

    @FXML
    private TableColumn<Utilizator, String> lastNameColumn;

    @FXML
    private Button searchButton;

    @FXML
    private Button backButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField searchFriendField;

    @FXML
    private TableColumn<Utilizator, String> usernameColumn;

    ObservableList<Utilizator> model = FXCollections.observableArrayList();

    Utilizator utilizator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initModel();
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("lastName"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("userName"));
        friendsTable.setItems(model);
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                UserDbUtils.changeScene(event, "user-profile.fxml", "UserProfile", utilizator.getUserName());
            }
        });
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getFriendFromTable(searchFriendField.getText());
            }
        });
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Utilizator prieten = getSelectedUser();
                if(prieten==null){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Nu a fost selectat niciun prieten!");
                    alert.show();
                }
                else{
                    service.deleteFriend(utilizator.getId(),prieten.getId());
                    getAllFriends();
                }
            }
        });
    }

    private void initModel() {
        utilizator = getUserByUsername(LogInController.getUsernameField());
        List<Utilizator> utilizatorList = new ArrayList<>(service.prieteniiUnuiUtilizator(utilizator.getId()).keySet());
        model.setAll(utilizatorList);

    }

    private Utilizator getSelectedUser(){
        int index = friendsTable.getSelectionModel().getSelectedIndex();
        if(index<0)
            return null;
        return friendsTable.getItems().get(index);
    }

    private void getAllFriends() {
        List<Utilizator> utilizatori = new ArrayList<>(service.prieteniiUnuiUtilizator(utilizator.getId()).keySet());
        model.setAll(utilizatori);
    }

    private void getFriendFromTable(String input){
        List<Utilizator> utilizatori = new ArrayList<>(service.prieteniiUnuiUtilizator(utilizator.getId()).keySet());
        model.setAll(utilizatori.stream()
                .filter(u->{return u.getLastName().startsWith(input);})
                .collect(Collectors.toList()));
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
