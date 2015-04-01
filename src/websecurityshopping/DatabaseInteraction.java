/*
 * File: DatabaseInteraction.java
 * Author: @Pablo A. Arias & @Greg B. Simpson
 * Objective: Creates Database Interaction
 */
package websecurityshopping;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
//import static websecurityshopping.ShoppingFrame.user;

public class DatabaseInteraction {

    //Connection variable to MySql database
    private Connection conn = null;
    Statement stmt = null;

    // Database variables
    String dbURL, dbUserName, dbPassword;

    // Customer Table variables
    User user;
    private int customerID;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String password;
    private Date ccExpiration;
    private String creditCard;
    private boolean connected;
    
    //constructor
    public DatabaseInteraction(String dbURL, String dbUserName, String dbPassword) {
        this.dbURL = dbURL;
        this.dbUserName = dbUserName;
        this.dbPassword = dbPassword;
        this.stmt = null;
        this.conn = null;
        this.connected = false;
    }

    /*
     * Creates Connection to MySql Database
     */
    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println(dbURL);
            System.out.println("Connecting to database");
            //http://152.8.98.118/
            conn = DriverManager.getConnection(dbURL, "gregsimpson", "password1");
            //conn = DriverManager.getConnection(dbURL, "root", "");

            System.out.println("Connection Established");
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
        }
        connected = true;
    }

    /*
     * Logs User and Retrieves User Info from MySql Database
     */
    public void setUserInfo(String email, String password) {
        String sql;
        this.email = email;
        this.password = password;
        sql = "Select * FROM customer WHERE email='" + this.email + "' AND password= MD5('" + this.password + "')";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("Login Successful");
            while (rs.next()) {
                this.firstName = rs.getString("firstName");
                this.lastName = rs.getString("lastName");
                this.customerID = rs.getInt("customerID");
                //this.creditCard = rs.getString("creditCard");
                this.creditCard = "1234567890123456";
                this.ccExpiration = rs.getDate("ccExpiration");
                this.address = rs.getString("address");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Closes Database Connection
    public void close() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the customerID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the ccExpiration
     */
    public Date getCcExpiration() {
        return ccExpiration;
    }

    /**
     * @return the creditCard
     */
    public String getCreditCard() {
        return creditCard;
    }
    
    public ResultSet getOrders(int customerID) {
        ResultSet rs = null;
        String sql = "SELECT * FROM `purchases` WHERE customerID = " + customerID;
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    
    public ResultSet getProducts(){
        if(!connected) connect();
        ResultSet rs = null;
        String sql = "Select * FROM product";
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    
    public void setOrder(User user, ArrayList<Product> products, int i){
        user.setCreditCardNumber("1234567890123456");
        String sql = "INSERT INTO `products`.`purchases` (`customerID`, `productID`, `timeStamp`, `creditCard`, `name`)"
                        + " VALUES ('" + user.getCustomerID() + "', '" + i + "', NOW(), '" + user.getCreditCardNumber() + "', '" + products.get(i).getName() + "');";
        
        /*
        String sql = "INSERT INTO `products`.`purchases` (`customerID`, `productID`, `timeStamp`, `creditCard`, `name`)"
                        + " VALUES ('" + user.getCustomerID() + "', '" + i + "', NOW(), AES_ENCRYPT('" + user.getCreditCardNumber() + "','encrypt1'), '" + products.get(i).getName() + "');";
        */
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ResultSet getCustomerID(String email){
        ResultSet rs = null;
        String sql = "SELECT * FROM `customer` WHERE `email` = '" + email + "';";
        System.out.println(sql);
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException e){
            System.out.println("no user");
        }
        return rs;
    }
    
    public void addNewUser(User user, String date){
        //String date = yearExpiration + "-" + monthExpiration + "-" + "01";
        if(!connected) connect();
        //MySql sql statement to input new User into Database
        String sql = "INSERT INTO `customers`.`customer` (`customerID`, `firstName`, `lastName`, "
                + "`creditCard`, `ccExpiration`, `address`, `email`, `password`) VALUES (NULL, '"
                + user.getFirstName() + "', '" + user.getLastName() + "', AES_ENCRYPT('" + user.getCreditCardNumber() + "','encrypt1'), '" + date + "', '"
                + user.getAddress() + "','" + user.getEmail() + "', " + "MD5('" +user.getPassword()+ "')" + ");";
        System.out.println(sql);
        try {
            //Executes MySql sql statement
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
