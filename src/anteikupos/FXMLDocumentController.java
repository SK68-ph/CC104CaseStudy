/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anteikupos;

import static anteikupos.FXMLoginController.infoBox;
import dbconnection.connection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


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
    DateFormat dateFormat = new SimpleDateFormat("YYYY:MM:dd HH:mm:ss");
    
    ArrayList<Products> products = new ArrayList<Products>();
    private int selectedIndex = -1;
    @FXML
    private Button btnAddProduct;
    @FXML
    private Button btnRemoveProduct;
    @FXML
    private Button btnUpdateProduct;
    
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
        calculateTotal();
    }

    @FXML
    private void removeItem(ActionEvent event) {
        if(selectedIndex >= 0){
            ObservableList<TableViewModel> data = Prodtbl.getItems();
            data.remove(selectedIndex);
            selectedIndex = -1;
            Dialog d = new Alert(Alert.AlertType.INFORMATION,"Successfully Updated!");
            d.show();
            calculateTotal();
            
        }else{
            Dialog d = new Alert(Alert.AlertType.ERROR,"Please Select an Item from the List");
            d.show();
        }
    }
    
    private void recordBill(int productId, int payment_Type_ID, int order_Quantity){
        Date today = Calendar.getInstance().getTime();
        String dateTimeNow = dateFormat.format(today);
        String sql = "insert into orders(product_ID, payment_Type_ID,order_Quantity,order_DateTime) VALUES(?,?,?,?)";
        try{
            preparedStatement = dbconnect.prepareStatement(sql);
            preparedStatement.setInt(1, productId);
            preparedStatement.setInt(2, payment_Type_ID);
            preparedStatement.setInt(3, order_Quantity);
            preparedStatement.setString(4, dateTimeNow);
            preparedStatement.execute();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private String getTransactionID(){
        try{
            Statement stmt = dbconnect.createStatement();
            resultSet =stmt.executeQuery("SELECT MAX(order_id) from orders");
            resultSet.first();
            String transactId = String.valueOf(resultSet.getInt(1) + 1);
            return transactId;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    private void printBill(ActionEvent event) {
            String total = txtTotal.getText();
            String pay = txtTenderAmount.getText();
            String bal = txtChange.getText();
            ObservableList<TableViewModel> data = Prodtbl.getItems();
            
            txtreceipt.setText(txtreceipt.getText() + "******************************************************\n");
            txtreceipt.setText(txtreceipt.getText() + "           AnteikuCafe Shop Bill                                     \n");
            txtreceipt.setText(txtreceipt.getText() + "*******************************************************\n");
            txtreceipt.setText(txtreceipt.getText() + "Transaction ID : \t" + getTransactionID() + "\n");
            txtreceipt.setText(txtreceipt.getText() + "Product \t" + "Quantity \t \n");
            for(int i = 0; i < data.size(); i++){
                String prodname = (String)data.get(i).getProductName();
                String quantity = String.valueOf((Integer)data.get(i).getProductQuantity());
                String amount = String.valueOf((Float)data.get(i).getProductTotal()); 
                txtreceipt.setText(txtreceipt.getText() + prodname + "\t" + quantity + "\t" + amount  + "\n"  );
                recordBill(Integer.parseInt(data.get(i).getProductID()),1,data.get(i).getProductQuantity());
            }
            txtreceipt.setText(txtreceipt.getText() + "\n");     
            txtreceipt.setText(txtreceipt.getText() + "\t" + "\t" + "Subtotal :" + total + "\n");
            txtreceipt.setText(txtreceipt.getText() + "\t" + "\t" + "Tender Amount :" + pay + "\n");
            txtreceipt.setText(txtreceipt.getText() + "\t" + "\t" + "Change :" + bal + "\n");
            txtreceipt.setText(txtreceipt.getText() + "\n");
            txtreceipt.setText(txtreceipt.getText() + "*******************************************************\n");
            txtreceipt.setText(txtreceipt.getText() + "           THANK YOU COME AGAIN             \n");
    }

    @FXML
    private void newTransaction(ActionEvent event) {
        txtProductName.clear();
        productQuantity.getValueFactory().setValue(1);
        txtProductPrice.clear();
        txtTotal.clear();
        txtreceipt.clear();
        txtChange.clear();
        txtTenderAmount.clear();
        ObservableList<TableViewModel> data = Prodtbl.getItems();
        data.clear();
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
    
    private void calculateTotal(){
        ObservableList<TableViewModel> data = Prodtbl.getItems();
        float total = 0f;
        for(int i =0; i < data.size(); i++){
            total = total +(Float)data.get(i).getProductTotal();
        }
        txtTotal.setText(String.valueOf(total));
    }
    
    @FXML
    private void calculateChange(KeyEvent event) {
        try{
            float change = Float.valueOf(txtTenderAmount.getText()) - Float.valueOf(txtTotal.getText());
            txtChange.setText(String.valueOf(change));
        }catch(java.lang.NumberFormatException nfe){
            System.out.println("Invalid input" + nfe);
        }
    }
    
    private String inputBox(String title, String message){
        TextInputDialog td = new TextInputDialog(message);
        td.setHeaderText(title);
        td.showAndWait();
        return td.getResult();
    }

    @FXML
    private void addProduct(ActionEvent event) {
        String product_Name = inputBox("Enter Product Name","Enter a new product name");
        String product_Description = inputBox("Enter Product Description","Enter a new product description name");
        String itemprice_Small = inputBox("Enter Product Price","Enter the product price in small size");
        String itemprice_Medium = inputBox("Enter Product Price","Enter the product price in medium size");
        String itemprice_Large = inputBox("Enter Product Price","Enter the product price in large size");
        if(!product_Name.isEmpty() || !product_Description.isEmpty() || !itemprice_Small.isEmpty() || !itemprice_Medium.isEmpty() || !itemprice_Large.isEmpty()){
            String sql = "insert into products (product_Name,product_Description,itemprice_Small,itemprice_Medium,itemprice_Large) VALUES(?,?,?,?,?)";
            try{
                preparedStatement = dbconnect.prepareStatement(sql);
                preparedStatement.setString(1, product_Name);
                preparedStatement.setString(2, product_Description);
                preparedStatement.setFloat(3, Float.parseFloat(itemprice_Small));
                preparedStatement.setFloat(4, Float.parseFloat(itemprice_Medium));
                preparedStatement.setFloat(5, Float.parseFloat(itemprice_Large));
                preparedStatement.execute();
                infoBox("Done", "Success", null);
                LoadProducts();
            }
            catch (Exception e){
                e.printStackTrace(); 
            }
        }else {
            infoBox("Please enter correct inputs", "Failed", null);
        }
        
    }

    @FXML
    private void removeProduct(ActionEvent event) {
        String productID = inputBox("Enter ID","Enter the product id to remove");
        if(!productID.isEmpty()){
            String sql = "DELETE from products where product_ID = ?";
            try{
                preparedStatement = dbconnect.prepareStatement(sql);
                preparedStatement.setInt(1, Integer.parseInt(productID));
                preparedStatement.execute();
                infoBox("Done", "Success", null);
                LoadProducts();
            }
            catch (Exception e){
                e.printStackTrace(); 
            }
        }else {
            infoBox("Please enter correct inputs", "Failed", null);
        }
    }

    @FXML
    private void updateProductPrice(ActionEvent event) {
        
        String productID = inputBox("Enter ID","Enter the product id to update");
        String itemprice_Small = inputBox("Enter Product Price","Enter the product price in small size");
        String itemprice_Medium = inputBox("Enter Product Price","Enter the product price in medium size");
        String itemprice_Large = inputBox("Enter Product Price","Enter the product price in large size");
        if(!productID.isEmpty() || !itemprice_Small.isEmpty() || !itemprice_Medium.isEmpty() || !itemprice_Large.isEmpty()){
            String sql = "UPDATE products SET itemprice_Small = ?, itemprice_Medium = ?, itemprice_Large = ? where product_ID = ?";
            try{
                preparedStatement = dbconnect.prepareStatement(sql);
                preparedStatement.setFloat(1, Float.parseFloat(itemprice_Small));
                preparedStatement.setFloat(2, Float.parseFloat(itemprice_Medium));
                preparedStatement.setFloat(3, Float.parseFloat(itemprice_Large));
                preparedStatement.setInt(4, Integer.parseInt(productID));
                preparedStatement.execute();
                infoBox("Done", "Success", null);
                LoadProducts();
            }
            catch (Exception e){
                e.printStackTrace(); 
            }
        }else {
            infoBox("Please enter correct inputs", "Failed", null);
        }
    }

    @FXML
    private void receiveData(MouseEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        boolean isAdmin = (boolean) stage.getUserData();
        if(isAdmin){
        btnAddProduct.setDisable(false);
        btnRemoveProduct.setDisable(false);
        btnUpdateProduct.setDisable(false);
        }
    }
    
}
