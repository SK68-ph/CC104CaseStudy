/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anteikupos;

/**
 *
 * @author Admin
 */
public class Products {
    private String _product_ID;
    private String _prod_Name;
    private float _ItemPrice_Small;
    private float _ItemPrice_Medium;
    private float _ItemPrice_Large;
    
    public Products(int product_ID, String prod_Name, float ItemPrice_Small,float ItemPrice_Medium,float ItemPrice_Large){
        setProductID(String.valueOf(product_ID));
        setProductName(prod_Name);
        setItemPrice_Small(ItemPrice_Small);
        setItemPrice_Medium(ItemPrice_Medium);
        setItemPrice_Large(ItemPrice_Large);
    }
    
    public String getProductID() { return this._product_ID;}
    public String getProductName() { return this._prod_Name;}
    public float getItemPrice_Small() { return this._ItemPrice_Small;}
    public float getItemPrice_Medium() { return this._ItemPrice_Medium;}
    public float getItemPrice_Large() { return this._ItemPrice_Large;}
    
    
    public void setProductID(String productID) { this._product_ID = (productID);}
    public void setProductName(String prod_Name) { this._prod_Name = (prod_Name);}
    public void setItemPrice_Small(float ItemPrice_Small) { this._ItemPrice_Small = (ItemPrice_Small);}
    public void setItemPrice_Medium(float ItemPrice_Medium) { this._ItemPrice_Medium = (ItemPrice_Medium);}
    public void setItemPrice_Large(float ItemPrice_Large) { this._ItemPrice_Large = (ItemPrice_Large);}
}
