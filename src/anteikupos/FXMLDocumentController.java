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
    private TableView<TableViewModel> Prodtbl;
    @FXML
    private TextField txtProductCode;
    @FXML
    private TextField txtProductName;
    @FXML
    private TextField txtProductPrice;
    @FXML
    private Spinner<Integer> productQuantity;
    @FXML
    private ChoiceBox<String> productSizes;
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
    private int selectedIndex = -1;
    
    private void LoadProducts(){
        try{
            Statement stmt = dbconnect.createStatement();
            resultSet = stmt.executeQuery("select * from products");
            while(resultSet.next()){
                int productID = resultSet.getInt("product_ID");
                String productName = resultSet.getString("product_Name");
                float itempriceSmall = resultSet.getFloat("itemprice_Small");
                float itempriceMedium = resultSet.getFloat("itemprice_Medium");
                float itempriceLarge = resultSet.getFloat("itemprice_Large");
                Products product = new Products(productID,productName,itempriceSmall,itempriceMedium,itempriceLarge);
                products.add(product);
            }
            stmt.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // init spinner
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,99,1);
        productQuantity.setValueFactory(valueFactory);
        // init selection
        String sizes[] = { "Small", "Medium", "Large"};
        productSizes.getItems().addAll((FXCollections.observableArrayList(sizes)));
        productSizes.getSelectionModel().selectFirst();
        // init db
        dbconnect = connection.connectdb();
        LoadProducts();
    }    
    
    @FXML
    private void addItem(ActionEvent event) {
        String productID = txtProductCode.getText();
        String productName = txtProductName.getText();
        String productSize = productSizes.getSelectionModel().getSelectedItem();
        Integer prodQuantity = productQuantity.getValue();
        Float productTotal = Float.parseFloat(txtProductPrice.getText()) * prodQuantity;
        
        ObservableList<TableViewModel> data = Prodtbl.getItems();
        data.add(new TableViewModel(productID,productName,productSize,prodQuantity,productTotal));
        
        txtProductCode.clear();
        txtProductName.clear();
        productQuantity.getValueFactory().setValue(1);
        txtProductPrice.clear();
    }

    @FXML
    private void removeItem(ActionEvent event) {
        if(selectedIndex >= 0){
            ObservableList<TableViewModel> data = Prodtbl.getItems();
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
    private void printBill(ActionEvent event) {
        
    }

    @FXML
    private void newTransaction(ActionEvent event) {
        
    }
    
    private void UpdateItemView(){
        txtProductName.clear();
        productQuantity.getValueFactory().setValue(1);
        txtProductPrice.clear();
        for(Products i: products){
            if(i.getProductID().equals(txtProductCode.getText())){
                txtProductName.setText(i.getProductName());
                float price = 0f;
                switch(productSizes.getValue()){
                    case "Small":
                        price = i.getItemPrice_Small();
                        break;
                    case "Medium":
                        price = i.getItemPrice_Medium();
                        break;
                    case "Large":
                        price = i.getItemPrice_Large();
                        break;
                }
                txtProductPrice.setText(String.valueOf(price));
            }
        
        }
    }

    @FXML
    private void updateProductInfo(KeyEvent event) {
        UpdateItemView();
    }

    @FXML
    private void onSizeChanged(ActionEvent event) {
        UpdateItemView();
    }

    @FXML
    private void updateTableViewIndex(MouseEvent event) {
        selectedIndex = Prodtbl.getSelectionModel().getSelectedIndex();
    }
    
}
