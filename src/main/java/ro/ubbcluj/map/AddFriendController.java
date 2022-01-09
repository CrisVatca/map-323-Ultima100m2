package ro.ubbcluj.map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ro.ubbcluj.map.domain.Utilizator;
import ro.ubbcluj.map.service.Service;

import java.net.URL;
import java.security.KeyException;
import java.util.ResourceBundle;

public class AddFriendController implements Initializable {

    @FXML
    private TextField textFieldPrenume;
    @FXML
    private TextField textFieldNume;

    private Service service;
    Stage dialogStage;
    Utilizator user;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setService(Stage stage, Service service1, Utilizator utilizator) {
        this.service = service1;
        this.dialogStage=stage;
        this.user=utilizator;
    }

    public Utilizator getFriend(){
        String fisrtName=textFieldPrenume.getText();
        String lastName=textFieldNume.getText();
        for (Utilizator utilizator:service.getUsers()){
            if(utilizator.getFirstName().equals(fisrtName) && utilizator.getLastName().equals(lastName)){
                return utilizator;
            }
        }
        return null;
    }

    public void handleSendRequest(ActionEvent actionEvent) throws KeyException {
        if(getFriend()==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Utilizatorul nu exista!");
            alert.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Cererea a fost trimisa");
            alert.show();
            sendRequest(getFriend());
            UserDbUtils.changeScene(actionEvent,"requests.fxml","All Requests",user.getUserName());
        }
    }

    public void sendRequest(Utilizator friend) throws KeyException {
        service.trimiteCerere(user.getUserName(),friend.getUserName());
        System.out.println("Cererea a fost trimisa");

        handleCancel();

    }

    @FXML
    public void handleBack(ActionEvent actionEvent) {
        handleCancel();
    }

    @FXML
    public void handleCancel(){
        dialogStage.close();
    }
}

