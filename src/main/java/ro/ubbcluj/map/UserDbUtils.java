package ro.ubbcluj.map;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

import static ro.ubbcluj.map.utils.MD5.getMd5;

public class UserDbUtils {

    private static final String url = "jdbc:postgresql://localhost:5432/db";
    private static final String username = "postgres";
    private static final String password = "compunere";

    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username1) {
        Parent root = null;

        if (username1 != null) {
            try {
                FXMLLoader loader = new FXMLLoader(UserDbUtils.class.getResource(fxmlFile));
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                root = FXMLLoader.load(Objects.requireNonNull(UserDbUtils.class.getResource(fxmlFile)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        assert root != null;
        stage.setScene(new Scene(root, 500, 450));
    }


    public static void logInUser(ActionEvent event, String username1, String password1) {
        String sql = "select password from utilizatori where user_name=?";
        try(Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, username1);
            ResultSet resultSet = ps.executeQuery();

            if(!resultSet.isBeforeFirst()){
                System.out.println("Utilizatorul nu a fost gasit in baza de date!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Credentialele sunt gresite!");
                alert.show();
            }else{
                while (resultSet.next()){
                    String password2 = resultSet.getString("password");
                    if(password2.equals(getMd5(password1))){
                        changeScene(event, "user-profile.fxml", "Profile", username1);
                    } else {
                        System.out.println("Parola nu corespunde!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Credentialele sunt gresite!");
                        alert.show();
                    }
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void signUpUser(ActionEvent event, String firstName, String lastName, String username1, String password1){
        String sqlCheckUserExist = "select * from utilizatori where user_name=?";
        String sqlInsert = "insert into utilizatori(first_name, last_name, user_name, password) values (?, ?, ?, ?)";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement psCheck = connection.prepareStatement(sqlCheckUserExist);
            PreparedStatement psInsert = connection.prepareStatement(sqlInsert)){
            psCheck.setString(1, username1);
            ResultSet resultSet = psCheck.executeQuery();
            if(resultSet.isBeforeFirst()){
                System.out.println("Utilizatorul exista deja!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Cont deja creat!");
                alert.show();
            }else{
                psInsert.setString(1, firstName);
                psInsert.setString(2, lastName);
                psInsert.setString(3, username1);
                psInsert.setString(4, getMd5(password1));
                psInsert.executeUpdate();

                changeScene(event, "log-in.fxml", "LogIn", username1);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}