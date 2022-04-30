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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Nivra
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label label;
    @FXML
    private TableView<TableViewProduct> Prodtbl;
    @FXML
    private TextField txtProdCode;
    @FXML
    private TextField txtProdName;
    @FXML
    private TextField txtProdPrice;
    @FXML
    private Spinner<Integer> prodQuantity;
    @FXML
    private ChoiceBox<String> prodSize;
    @FXML
    private TextArea txtreceipt;
    @FXML
    private TextField txtTotal;
    @FXML
    private TextField txtTenderAmount;
    @FXML
    private TextField txtChange;
    
    Connection dbconnect = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    ArrayList<Products> products = new ArrayList<Products>();
    
    private void handleButtonAction(ActionEvent event) {
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // initialize spinner
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 1);
        prodQuantity.setValueFactory(valueFactory);
        // init selection
        String st[] = { "Small", "Medium", "Large"};
        prodSize.getItems().addAll((FXCollections.observableArrayList(st)));
        prodSize.getSelectionModel().selectFirst();
        // init db
        dbconnect = connection.connectdb();
        
        LoadProducts();
    }    
    
    private void LoadProducts(){
        try{ 
            Statement stmt = dbconnect.createStatement();  
            resultSet = stmt.executeQuery("SELECT * FROM products");
            while(resultSet.next()) { 
                int product_ID = resultSet.getInt("product_ID");
                String product_Name = resultSet.getString("product_Name");
                float itemprice_Small = resultSet.getFloat("itemprice_Small");
                float itemprice_Medium = resultSet.getFloat("itemprice_Medium");
                float itemprice_Large = resultSet.getFloat("itemprice_Large");
                Products product = new Products(product_ID,product_Name,itemprice_Small,itemprice_Medium,itemprice_Large);
                products.add(product);
            }
            stmt.close();
            resultSet.close();
        }catch (Exception e){
            e.printStackTrace(); 
        }
    }

    @FXML
    private void addItem(ActionEvent event) {
        String productID = txtProdCode.getText();
        String productName = txtProdName.getText();
        String productSize = prodSize.getSelectionModel().getSelectedItem();
        Integer productQuantity = prodQuantity.getValue();
        Float productTotal = Float.parseFloat(txtProdPrice.getText()) * productQuantity;
        
        ObservableList<TableViewProduct> data = Prodtbl.getItems();
        data.add(new TableViewProduct(productID,productName,productSize,productQuantity,productTotal));
        
        txtProdCode.setText("");
        txtProdName.setText("");
        txtProdPrice.setText("");
        prodQuantity.getValueFactory().setValue(1);
    }
    
    int selectedIndex = -1;
    @FXML
    private void removeItem(ActionEvent event) {
        if(selectedIndex >= 0){
            ObservableList<TableViewProduct> data = Prodtbl.getItems();
            data.remove(selectedIndex);
            selectedIndex = -1;
            Dialog d = new Alert(Alert.AlertType.INFORMATION,"Successfully Updated!");
            d.show();
            
        }else{
            Dialog d = new Alert(Alert.AlertType.ERROR,"Please Select an Item from the List");
            d.show();
        }
    }

    @FXML
    private void printbill(ActionEvent event) {
    }

    @FXML
    private void newtransaction(ActionEvent event) {
    }

    @FXML
    private void updateView(MouseEvent event) {
        selectedIndex = Prodtbl.getSelectionModel().getSelectedIndex();
    }
    
    private void UpdateItemView(){
        txtProdName.setText("");
        prodQuantity.getValueFactory().setValue(1);
        txtProdPrice.setText("");
        for (Products i: products) {
            if(i.getProductId().equals(txtProdCode.getText())){
                txtProdName.setText(i.getProductName());
                
                float price = 0f;
                switch(prodSize.getValue()){
                    case "Small":
                        price = i.getItemPriceSmall();
                        break;
                    case "Medium":
                        price = i.getItemPriceMedium();
                        break;
                    case "Large":
                        price = i.getItemPriceLarge();
                        break;
                }
                txtProdPrice.setText(String.valueOf(price));
            }
        }
        
        
    }
    @FXML
    private void updateInfo(KeyEvent event) {
        UpdateItemView();
    }

    @FXML
    private void controllerMethod(ActionEvent event) {
        UpdateItemView();
    }

    
}
