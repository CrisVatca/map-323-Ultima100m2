package ro.ubbcluj.map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.ubbcluj.map.controller.MessageAlert;
import ro.ubbcluj.map.domain.Cerere;
import ro.ubbcluj.map.domain.Message;
import ro.ubbcluj.map.domain.Prietenie;
import ro.ubbcluj.map.domain.Utilizator;
import ro.ubbcluj.map.domain.validators.*;
import ro.ubbcluj.map.repository.Repository;
import ro.ubbcluj.map.repository.db.CerereDbRepository;
import ro.ubbcluj.map.repository.db.MessageDbRepository;
import ro.ubbcluj.map.repository.db.PrietenieDbRepository;
import ro.ubbcluj.map.repository.db.UtilizatorDbRepository;
import ro.ubbcluj.map.service.Service;

import java.io.IOException;
import java.net.URL;
import java.security.KeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RequestsController implements Initializable {
    private static final String url = "jdbc:postgresql://localhost:5432/db";
    private static final String username = "postgres";
    private static final String password = "compunere";

    public static Service setService(){
        UtilizatorValidator validator = new UtilizatorValidator();
        PrietenieValidator validator1 = new PrietenieValidator();
        MessageValidator validator2 = new MessageValidator();
        CerereValidator validator3 = new CerereValidator();

        Repository<Long, Cerere> repoCerere = new CerereDbRepository(url, username, password, validator3);
        Repository<Long, Utilizator> repoUtilizatori = new UtilizatorDbRepository(url, username, password, validator);
        Repository<Long, Prietenie> repoPrietenie = new PrietenieDbRepository(url, username, password, validator1);
        Repository<Long, Message> repoMessage = new MessageDbRepository(url, username, password, validator2);
        return new Service(repoUtilizatori, repoPrietenie, repoMessage, repoCerere);
    }

    @FXML
    public Button handleBack;

    @FXML
    public Button cancelRequestButton;

    @FXML
    private Button viewRequestsButton;

    @FXML
    private Button viewSentRequestsButton;

    @FXML
    TableView<Cerere> tableView;
    @FXML
    TableColumn<Cerere,String> tableColumnFromUser;
    @FXML
    TableColumn<Cerere,String> tableColumnToUser;
    @FXML
    TableColumn<Cerere,String> tableColumnStatus;
    @FXML
    TableColumn<Cerere,String> tableColumnDate;

    ObservableList<Cerere> model = FXCollections.observableArrayList();
    private Service service;
    Utilizator user;

    private Utilizator getUserByUsername(String username){
        List<Utilizator> utilizatorList= service.getUsers();
        for(Utilizator u: utilizatorList){
            if(u.getUserName().equals(username)){
                return u;
            }
        }
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        service = setService();
        user=getUserByUsername(LogInController.getUsernameField());
        initModel();
        tableColumnFromUser.setCellValueFactory(new PropertyValueFactory<Cerere, String>("userNameFrom"));
        tableColumnToUser.setCellValueFactory(new PropertyValueFactory<Cerere, String>("userNameTo"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<Cerere, String>("status"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<Cerere, String>("date"));
        tableView.setItems(model);

        handleBack.setOnAction(event -> UserDbUtils.changeScene(event, "user-profile.fxml", "UserProfile", user.getUserName()));
        viewRequestsButton.setOnAction(event -> initModel());
        viewSentRequestsButton.setOnAction(event -> getSentRequests());
        cancelRequestButton.setOnAction(event -> cancelRequest());
    }

    public void initModel() {
        List<Cerere> cereri = new ArrayList<>();
        for(Cerere cerere: service.getCereri()){
            if(cerere.getUserNameTo().equals(user.getUserName())){
                cereri.add(cerere);
            }
        }
        model.setAll(cereri);
    }

    @FXML
    public void handleAcceptRequest(ActionEvent actionEvent) throws KeyException {
        Cerere cerere = tableView.getSelectionModel().getSelectedItem();
        if(cerere!=null){
            if(cerere.getUserNameTo().equals(user.getUserName())){
                try {
                    if(cerere.getStatus().equals("pending")){
                        service.raspundereCerere(cerere.getUserNameFrom(), cerere.getUserNameTo(), true);
                        UserDbUtils.changeScene(actionEvent, "requests.fxml", "All Requests", LogInController.getUsernameField());
                    }else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Cererea nu poate fi acceptata");
                        alert.show();
                    }

                }catch (KeyException validationException){
                    System.out.println(validationException.toString());
                }
            }
        }
    }

    @FXML
    public void handleDeclineRequest(ActionEvent actionEvent) throws KeyException {
        Cerere cerere = tableView.getSelectionModel().getSelectedItem();
        if(cerere!=null){
            if(cerere.getUserNameTo().equals(user.getUserName())){
                if(cerere.getStatus().equals("pending")){
                    try {
                        service.raspundereCerere(cerere.getUserNameFrom(), cerere.getUserNameTo(), false);
                        UserDbUtils.changeScene(actionEvent, "requests.fxml", "All Requests", LogInController.getUsernameField());
                    } catch (KeyException e) {
                        e.printStackTrace();
                    }
                }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Cererea nu poate fi refuzata");
                    alert.show();
                }
            }
        }
    }

    @FXML
    public void handleAddRequest(ActionEvent actionEvent){
        showAddRequest();
    }

    public void getSentRequests(){
        List<Cerere> cereri = new ArrayList<>();
        for(Cerere cerere: service.getCereri()){
            if(cerere.getUserNameFrom().equals(user.getUserName()) && cerere.getStatus().equals("pending")){
                cereri.add(cerere);
            }
        }
        model.setAll(cereri);
    }

    public void cancelRequest(){
        Cerere cerere= tableView.getSelectionModel().getSelectedItem();
        if(cerere == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Nu a fost selectata nicio cerere");
            alert.show();
            return;
        }
        if(cerere.getUserNameFrom().equals(user.getUserName()) && cerere.getStatus().equals("pending")) {
            this.service.deleteCerere(cerere.getId());
            getSentRequests();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Nu se poate anula aceasta cerere");
            alert.show();
        }
    }

    public void showAddRequest(){
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("add-friend.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Send Friend Request");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            AddFriendController addFriendController = loader.getController();
            addFriendController.setService(dialogStage,service,user);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

