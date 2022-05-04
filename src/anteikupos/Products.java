/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anteikupos;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Admin
 */
public class Products {
    private SimpleStringProperty productID = new SimpleStringProperty("");
    private SimpleStringProperty productName = new SimpleStringProperty("");
    private SimpleFloatProperty ItemPrice_Small = new SimpleFloatProperty(0f);
    private SimpleFloatProperty ItemPrice_Medium = new SimpleFloatProperty(0f);
    private SimpleFloatProperty ItemPrice_Large = new SimpleFloatProperty(0f);
    
    public Products(int product_ID, String prod_Name, float ItemPrice_Small,float ItemPrice_Medium,float ItemPrice_Large){
        setProductID(String.valueOf(product_ID));
        setProductName(prod_Name);
        setItemPrice_Small(ItemPrice_Small);
        setItemPrice_Medium(ItemPrice_Medium);
        setItemPrice_Large(ItemPrice_Large);
    }
    
    public String getProductID() { return this.productID.get();}
    public String getProductName() { return this.productName.get();}
    public float getItemPrice_Small() { return this.ItemPrice_Small.get();}
    public float getItemPrice_Medium() { return this.ItemPrice_Medium.get();}
    public float getItemPrice_Large() { return this.ItemPrice_Large.get();}
    
    
    public void setProductID(String productID) { this.productID.set(productID);}
    public void setProductName(String prod_Name) { this.productName.set(prod_Name);}
    public void setItemPrice_Small(float ItemPrice_Small) { this.ItemPrice_Small.set(ItemPrice_Small);}
    public void setItemPrice_Medium(float ItemPrice_Medium) { this.ItemPrice_Medium.set(ItemPrice_Medium);}
    public void setItemPrice_Large(float ItemPrice_Large) { this.ItemPrice_Large.set(ItemPrice_Large);}
}
