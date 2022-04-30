/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anteikupos;

import dbconnection.connection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Nivra
 */
public class FXMLoginController implements Initializable {

    @FXML
    private TextField txtlogin;
    @FXML
    private PasswordField txtpass;
    @FXML
    private Button btnlogin;
    
    Stage dialogStage = new Stage();
    Scene scene;
    
    Connection dbconnect = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    public FXMLoginController() {
        dbconnect = connection.connectdb();
    
    }

    public static void infoBox(String infoMessage, String titleBar, String headerMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titleBar);
        alert.setHeaderText(headerMessage);
        alert.setContentText(titleBar);
        alert.showAndWait();
    
    }
    
    
    private String hashPassword(String password){
        String sql = "SELECT PASSWORD(?)";
        String hashedPass = "";
        try{ 
            preparedStatement = dbconnect.prepareStatement(sql);
            preparedStatement.setString(1, password);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            hashedPass = resultSet.getString(1);
        }catch(Exception e){
        e.printStackTrace(); 
        }
        return hashedPass;
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        String username = txtlogin.getText();
        String password = txtpass.getText();
        String hashedPassword = hashPassword(password);
        String sql = "SELECT * FROM users WHERE username = ? and password = ?";
        try{ 
            preparedStatement = dbconnect.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPassword);
            resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()) { 
                infoBox("Enter Correct Email and Password", "Failed", null);
            } else { 
                infoBox("Login Successfull", "Success", null);
                Node source = (Node) event.getSource();
                dialogStage = (Stage) source.getScene().getWindow();
                dialogStage.close();
                scene = new Scene(FXMLLoader.load(getClass().getResource("FXMLDocument.fxml")));
                dialogStage.setScene(scene);
                dialogStage.show();
            }
        }catch(Exception e){ 
            e.printStackTrace(); 
        
        }
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
