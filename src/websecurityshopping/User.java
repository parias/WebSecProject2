/*
 * File: User.java
 * Author: @Pablo A. Arias & @Greg B. Simpson
 * Objective: User Class
 */
package websecurityshopping;

import java.util.Date;

public class User {

    //class variables
    private String firstName, lastName, address, password, email;
    private String creditCardNumber;
    private Date expiration;
    private int customerID;

    //Contructor for User
    public User(String firstName, String lastName, String creditCardNumber, Date expiration,
            String address, String password, String email) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.creditCardNumber = creditCardNumber;
        this.address = address;
        this.password = password;
        this.email = email;
        this.expiration = expiration;
    }

    //User Contructor for limited User information
    public User(String firstName, String lastName, String creditCardNumber, Date expiration) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.creditCardNumber = creditCardNumber;
        this.expiration = expiration;
    }
    

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the creditCardNumber
     */
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    /**
     * @param creditCardNumber the creditCardNumber to set
     */
    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the expiration
     */
    public Date getExpiration() {
        return expiration;
    }

    /**
     * @param expiration the expiration to set
     */
    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    /**
     * @return the customerID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * @param customerID the customerID to set
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

}
