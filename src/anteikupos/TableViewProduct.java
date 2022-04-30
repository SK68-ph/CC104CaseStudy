/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anteikupos;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author Admin
 */
public class TableViewProduct {
    private final SimpleStringProperty productID = new SimpleStringProperty("");
    private final SimpleStringProperty productName = new SimpleStringProperty("");
    private final SimpleStringProperty productSize = new SimpleStringProperty("");
    private final SimpleIntegerProperty productQuantity = new SimpleIntegerProperty(1);
    private final SimpleFloatProperty productTotal = new SimpleFloatProperty(0);
    
    public TableViewProduct(String productID, String productName, String productSize, int productQuantity, float productTotal) {
        setProductID (productID) ;
        setProductName (productName) ;
        setProductSize (productSize);
        setProductQuantity (productQuantity);
        setProductTotal (productTotal);
    }
    public String getProductID() { return productID.get();}
    public String getProductName() { return productName.get();}
    public String getProductSize() { return productSize.get();}
    public int getProductQuantity() { return productQuantity.get();}
    public float getProductTotal() { return productTotal.get();}
    
    public void setProductID (String productID) { this.productID.set(productID) ;}
    public void setProductName(String productName){ this.productName.set(productName);}
    public void setProductSize(String productSize) { this.productSize.set(productSize); }
    public void setProductQuantity(Integer productQuantity) { this.productQuantity.set(productQuantity); }
    public void setProductTotal(Float productTotal) { this.productTotal.set(productTotal); }
}
