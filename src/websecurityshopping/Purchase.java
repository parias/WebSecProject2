/*
 * File: Purchase.java
 * Author: @Pablo A. Arias & @Greg B. Simpson
 * Objective: Puchase Class
 * Objective: Used to create new purchase objects for the Shopping Frame
 */
package websecurityshopping;

import java.sql.Timestamp;

/**
 *
 * @author Pablo
 */
public class Purchase {
    
    //Class Variables
    private Timestamp purchaseDate = null;
    private String creditCard = null;
    private String productName = null;
    private Double productPrice = null;
    
    //Constructor
    public Purchase(String productName, Double productPrice, String creditCard, Timestamp purchaseDate){
        this.productName = productName;
        this.productPrice = productPrice;
        this.creditCard = creditCard;
        this.purchaseDate = purchaseDate;
    }

    /**
     * @return the purchaseDate
     */
    public Timestamp getPurchaseDate() {
        return purchaseDate;
    }

    /**
     * @param purchaseDate the purchaseDate to set
     */
    public void setPurchaseDate(Timestamp purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    /**
     * @return the creditCard
     */
    public String getCreditCard() {
        return creditCard;
    }

    /**
     * @param creditCard the creditCard to set
     */
    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the productPrice
     */
    public Double getProductPrice() {
        return productPrice;
    }

    /**
     * @param productPrice the productPrice to set
     */
    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }
    
    /*
    * toString method, used for JComboBox to display orderName
    */
    @Override
    public String toString(){
        return this.productName;
    }

}
