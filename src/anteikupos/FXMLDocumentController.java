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
    DateFormat dateFormat = new SimpleDateFormat("YYYY:MM:dd HH:mm:ss");
    
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
        // init Products from db
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
        
        txtProdCode.clear();
        txtProdName.clear();
        txtProdPrice.clear();
        prodQuantity.getValueFactory().setValue(1);
        
        calculateTotal();
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
            calculateTotal();
        }else{
            Dialog d = new Alert(Alert.AlertType.ERROR,"Please Select an Item from the List");
            d.show();
        }
    }
    
    private void calculateTotal(){
        ObservableList<TableViewProduct> data = Prodtbl.getItems();
        float total = 0f;
        for(int i = 0; i < data.size(); i++){
            total = total + (Float)data.get(i).getProductTotal();
        }
        txtTotal.setText(String.valueOf(total));
    }
    
    private void recordBill(int productId, int payment_Type_ID, int order_Quantity){
        Date today = Calendar.getInstance().getTime();        
        String dateTimeNow = dateFormat.format(today);
        String sql = "insert into orders (product_ID,payment_Type_ID,order_Quantity,order_DateTime) VALUES(?,?,?,?)";
        try{
            preparedStatement = dbconnect.prepareStatement(sql);
            preparedStatement.setInt(1, productId);
            preparedStatement.setInt(2, payment_Type_ID);
            preparedStatement.setInt(3, order_Quantity);
            preparedStatement.setString(4, dateTimeNow);
            preparedStatement.execute();
            }
            catch (Exception e){
                e.printStackTrace(); 
            }
    }
    
    private String getTransactionID(){
        try{
            Statement stmt = dbconnect.createStatement();  
            resultSet = stmt.executeQuery("SELECT MAX(order_ID) from orders");
            resultSet.first();
            String transacId = String.valueOf(resultSet.getInt(1) + 1);
            System.out.println(transacId);
            return transacId;
        }
        catch (Exception e){
            e.printStackTrace(); 
        }
        return null;
    }

    @FXML
    private void printbill(ActionEvent event) {
            String total = txtTotal.getText();
            String pay = txtTenderAmount.getText();
            String bal = txtChange.getText();
            ObservableList<TableViewProduct> data = Prodtbl.getItems();
            
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
    private void newtransaction(ActionEvent event) {
        txtProdName.clear();
        prodQuantity.getValueFactory().setValue(1);
        txtProdPrice.clear();
        txtTotal.clear();
        txtreceipt.clear();
        txtChange.clear();
        txtTenderAmount.clear();
        ObservableList<TableViewProduct> data = Prodtbl.getItems();
        data.clear();
    }

    @FXML
    private void updateView(MouseEvent event) {
        selectedIndex = Prodtbl.getSelectionModel().getSelectedIndex();
    }
    
    private void UpdateItemView(){
        txtProdName.clear();
        prodQuantity.getValueFactory().setValue(1);
        txtProdPrice.clear();
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
    


    @FXML
    private void calculateChange(KeyEvent event) {
        try{
            float change = Float.valueOf(txtTenderAmount.getText()) - Float.valueOf(txtTotal.getText());
            txtChange.setText(String.valueOf(change));
        }catch (java.lang.NumberFormatException NFE){
             System.out.println("Invalid Input." + NFE);
        }
    }

    
}
