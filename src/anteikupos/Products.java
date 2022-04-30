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
        
        public Products(int product_ID, String prod_Name, float ItemPrice_Small, float ItemPrice_Medium,float ItemPrice_Large){
            setProductId(String.valueOf(product_ID));
            setProductName(prod_Name);
            setItemPriceSmall(ItemPrice_Small);
            setItemPriceMedium(ItemPrice_Medium);
            setItemPriceLarge(ItemPrice_Large);
        }
        
        
        public String getProductId() {
             return _product_ID;
        }
        
        public String getProductName() {
             return _prod_Name;
        }
        
        public float getItemPriceSmall() {
            return _ItemPrice_Small;
        }
        public float getItemPriceMedium() {
            return _ItemPrice_Medium;
        }
        public float getItemPriceLarge() {
            return _ItemPrice_Large;
        }
        
        public void setProductId(String product_ID) {
            this._product_ID = product_ID;
        }
        
        public void setProductName(String prod_Name) {
            this._prod_Name = prod_Name;
        }
        public void setItemPriceSmall(float ItemPrice_Small) {
            this._ItemPrice_Small = ItemPrice_Small;
        }
        public void setItemPriceMedium(float ItemPrice_Medium) {
            this._ItemPrice_Medium = ItemPrice_Medium;
        }
        public void setItemPriceLarge(float ItemPrice_Large) {
            this._ItemPrice_Large = ItemPrice_Large;
        }
        
}
