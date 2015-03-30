/*
 * File: Product.java
 * Author: @Pablo A. Arias & @Greg B. Simpson
 * Objective: Product Class
 * Objective: Used to create Products objects for Shopping Frame
 */
package websecurityshopping;

/**
 *
 * @Pablo A. Arias & @Greg B. Simpson
 */
public class Product {

    //class variables
    private String name, url;
    private int itemID;
    private Double price;
    private Boolean availability;

    //default constructor
    public Product() {
        this.name = null;
        this.url = null;
        this.itemID = 0;
        this.price = 0.0;
        this.availability = false;
    }

    //contructor with variable initiation
    public Product(String name, String url, int itemID, Double price, Boolean availability) {
        this.name = name;
        this.url = url;
        this.itemID = itemID;
        this.price = price;
        this.availability = availability;
    }

    /**
     * @return the itemID
     */
    public int getItemID() {
        return itemID;
    }

    /**
     * @param itemID the itemID to set
     */
    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * @return the availability
     */
    public Boolean getAvailability() {
        return availability;
    }

    /**
     * @param availability the availability to set
     */
    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }
    
    @Override
    public String toString(){
        return this.name;
    }
}
