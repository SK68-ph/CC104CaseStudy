/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anteikupos;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleFloatProperty;
/**
 *
 * @author Admin
 */
public class CashierModel {
    private final SimpleStringProperty productID = new SimpleStringProperty("");
    private final SimpleStringProperty productName = new SimpleStringProperty("");
    private final SimpleStringProperty productSize = new SimpleStringProperty("");
    private final SimpleIntegerProperty productQuantity = new SimpleIntegerProperty(0);
    private final SimpleFloatProperty productTotal = new SimpleFloatProperty(0f);
    
    public CashierModel(String productID, String productName, String productSize, int productQuantity, float productTotal) {
        setProductID(productID);
        setProductName(productName);
        setProductSize(productSize);
        setProductQuantity(productQuantity);
        setProductTotal(productTotal);
    }
    
    public String getProductID() { return this.productID.get();}
    public String getProductName() { return this.productName.get();}
    public String getProductSize() { return this.productSize.get();}
    public int getProductQuantity() { return this.productQuantity.get();}
    public float getProductTotal() { return this.productTotal.get();}
    
    public void setProductID(String productID) { this.productID.set(productID);}
    public void setProductName(String productName) { this.productName.set(productName);}
    public void setProductSize(String productSize) { this.productSize.set(productSize);}
    public void setProductQuantity(int productQuantity) { this.productQuantity.set(productQuantity);}
    public void setProductTotal(float productTotal) { this.productTotal.set(productTotal);}
}
