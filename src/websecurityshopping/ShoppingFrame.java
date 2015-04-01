/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websecurityshopping;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author gregsimpson
 */
public class ShoppingFrame extends javax.swing.JPanel implements ActionListener{
    private String dbURL = "jdbc:mysql://gregandpablo.ncat.edu:3306/products";
    private String customerDB = "jdbc:mysql://gregandpablo.ncat.edu:3306/customers";
    String dbUserName = "root";
    String dbPassword = "";
    
    //MySql database variables
    DatabaseInteraction ru_customer, ru_product;// = new DatabaseInteraction(customerDB, dbUserName, dbPassword);
    Statement stmt = null;
    private static Connection conn = null;
    static User user;

    //Array of Product Information
    private ArrayList<Product> products;
    private ArrayList<JLabel> itemImages;
    private ArrayList<JLabel> itemDescriptions;
    private ArrayList<JButton> itemButtons = new ArrayList();
    ShoppingJApplet applet;
    /**
     * Creates new form ShoppingFrame
     */
    public ShoppingFrame() {
        initComponents();
    }
    
    public ShoppingFrame(ShoppingJApplet applet) {
        initComponents();
        this.applet = applet;
    }
    
    public ShoppingFrame(User user) throws SQLException, IOException {
        ShoppingFrame.user = user;
        ru_customer = new DatabaseInteraction(customerDB, dbUserName, dbPassword);
        ru_product = new DatabaseInteraction(dbURL, dbUserName, dbPassword);
        

        welcomeLabel.setText("<html>Welcome " + user.getFirstName() + "<br/>Customer ID: " + user.getCustomerID() + "</html>");

        setItems();
        viewOrders();
        initComponents();
    }
    
    public ShoppingFrame(ShoppingJApplet applet, User user) throws SQLException, IOException {
        ShoppingFrame.user = user;
        ru_customer = new DatabaseInteraction(customerDB, dbUserName, dbPassword);
        ru_product = new DatabaseInteraction(dbURL, dbUserName, dbPassword);
        initComponents();

        welcomeLabel.setText("<html>Welcome " + user.getFirstName() + "<br/>Customer ID: " + user.getCustomerID() + "</html>");
        this.applet = applet;
        setItems();
        viewOrders();
    }
    
    public final void setItems() throws MalformedURLException, IOException, SQLException {

        products = new ArrayList();
        
        //connects to database
        //connect();
        //String sql;

        //MySql statement to retrieve products from MySql Database and Populates Products Array
        //sql = "Select * FROM product";
        try {
            ResultSet rs = ru_product.getProducts();//stmt.executeQuery(sql);

            while (rs.next()) {
                Product product = new Product(rs.getString("name"),
                        rs.getString("url"),
                        rs.getInt("itemID"),
                        rs.getDouble("price"),
                        rs.getBoolean("availability"));
                products.add(product);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Array of JLabel for Item Name and Price
        itemDescriptions = new ArrayList();
        itemDescriptions.add(descriptItem1JLabel);
        itemDescriptions.add(descriptItem2JLabel);
        itemDescriptions.add(descriptItem3JLabel);
        itemDescriptions.add(descriptItem4JLabel);
        itemDescriptions.add(descriptItem5JLabel);
        itemDescriptions.add(descriptItem6JLabel);
        itemDescriptions.add(descriptItem7JLabel);
        itemDescriptions.add(descriptItem8JLabel);

        //Array of JLabel for Item Image
        itemImages = new ArrayList();
        itemImages.add(itemImage1);
        itemImages.add(itemImage2);
        itemImages.add(itemImage3);
        itemImages.add(itemImage4);
        itemImages.add(itemImage5);
        itemImages.add(itemImage6);
        itemImages.add(itemImage7);
        itemImages.add(itemImage8);

        //Array of JButton for Buy Action
        itemButtons.add(itemBtn1);
        itemButtons.add(itemBtn2);
        itemButtons.add(itemBtn3);
        itemButtons.add(itemBtn4);
        itemButtons.add(itemBtn5);
        itemButtons.add(itemBtn6);
        itemButtons.add(itemBtn7);
        itemButtons.add(itemBtn8);

        //adds action listener to buttons for purchase
        for (int i = 0; i < itemButtons.size(); i++) {
            itemButtons.get(i).setActionCommand("button" + i);
            // wants a cast - implemented ActionListener
            itemButtons.get(i).addActionListener(this);
            //itemButtons.get(i).addActionListener((ActionListener) this);
        }

        //Displays image to itemImage Labels
        for (int i = 0; i < itemImages.size(); i++) {
            String path = products.get(i).getUrl();
            itemDescriptions.get(i).setText("<html>" + products.get(i).getName() + "<br/>$" + products.get(i).getPrice() + "</html>");

            URL url = new URL(path);
            BufferedImage image = ImageIO.read(url);
            BufferedImage newImage = getScaledImage(image, 125, 125);

            itemImages.get(i).setIcon(new ImageIcon(newImage));
        }

    }
    
    public void viewOrders() throws SQLException {

        productsComboBox.removeAllItems();
        int customerID = user.getCustomerID();
        //ru = new DatabaseInteraction(dbURL, dbUserName, dbPassword);

        char[] creditCardHidden = null;
        
        //sql statement to retrieve past orders
        //String sql = "SELECT * FROM `purchases` WHERE customerID = " + customerID;

        //Creates Purchase object in order to populate JComboBox
        try {
            ResultSet rs = ru_product.getOrders(customerID);//stmt.executeQuery(sql);
            while (rs.next()) {
                Double productPrice = rs.getDouble("productID");
                String creditCard = rs.getString("creditCard");
                Timestamp purchaseDate = rs.getTimestamp("timeStamp");
                String productName = rs.getString("name");
                Purchase purchase = new Purchase(productName, productPrice, creditCard, purchaseDate);
                productsComboBox.addItem(purchase);

                //hides credit carder number except last 4 digits
                creditCardHidden = creditCard.toCharArray();
                for (int i = 0; i < creditCard.length() - 4; i++) {
                    creditCardHidden[i] = '*';
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseInteraction.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static BufferedImage getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
        private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        itemImage3 = new javax.swing.JLabel();
        itemImage2 = new javax.swing.JLabel();
        itemImage5 = new javax.swing.JLabel();
        itemImage6 = new javax.swing.JLabel();
        itemImage4 = new javax.swing.JLabel();
        itemImage8 = new javax.swing.JLabel();
        itemImage7 = new javax.swing.JLabel();
        descriptItem1JLabel = new javax.swing.JLabel();
        descriptItem2JLabel = new javax.swing.JLabel();
        descriptItem3JLabel = new javax.swing.JLabel();
        descriptItem4JLabel = new javax.swing.JLabel();
        descriptItem5JLabel = new javax.swing.JLabel();
        descriptItem6JLabel = new javax.swing.JLabel();
        descriptItem7JLabel = new javax.swing.JLabel();
        descriptItem8JLabel = new javax.swing.JLabel();
        itemBtn1 = new javax.swing.JButton();
        itemBtn2 = new javax.swing.JButton();
        itemBtn3 = new javax.swing.JButton();
        itemBtn4 = new javax.swing.JButton();
        itemBtn5 = new javax.swing.JButton();
        itemBtn6 = new javax.swing.JButton();
        itemBtn7 = new javax.swing.JButton();
        itemBtn8 = new javax.swing.JButton();
        welcomeLabel = new javax.swing.JLabel();
        itemImage1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        exitButton = new javax.swing.JButton();
        productsComboBox = new javax.swing.JComboBox();
        viewOrderBtn = new javax.swing.JButton();

        jLabel1.setText("jLabel1");

        descriptItem1JLabel.setText("1");
        descriptItem2JLabel.setText("2");
        descriptItem3JLabel.setText("3");
        descriptItem4JLabel.setText("4");
        descriptItem5JLabel.setText("5");
        descriptItem6JLabel.setText("6");

        descriptItem7JLabel.setText("7");

        descriptItem8JLabel.setText("8");

        itemBtn1.setText("Buy");

        itemBtn2.setText("Buy");

        itemBtn3.setText("Buy");

        itemBtn4.setText("Buy");

        itemBtn5.setText("Buy");

        itemBtn6.setText("Buy");

        itemBtn7.setText("Buy");

        itemBtn8.setText("Buy");

        //welcomeLabel.setText("Welcome Label");

        jLabel2.setText("Your Orders");

        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        productsComboBox.setMaximumRowCount(20);
        productsComboBox.setToolTipText("");

        viewOrderBtn.setText("View Order Details");
        viewOrderBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewOrderBtnActionPerformed(evt);
            }
        });

        //javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        //getContentPane().setLayout(layout);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(itemImage1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(itemImage2, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(itemImage3, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(itemImage4, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(descriptItem1JLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(183, 183, 183)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(descriptItem2JLabel)
                                    .addComponent(descriptItem6JLabel)
                                    .addComponent(itemBtn6))
                                .addGap(90, 90, 90)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(descriptItem7JLabel)
                                    .addComponent(itemBtn7)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(itemImage5, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(itemBtn1))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(itemImage6, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(41, 41, 41)
                                        .addComponent(itemBtn2)))
                                .addGap(27, 27, 27)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(itemBtn3)
                                    .addComponent(descriptItem3JLabel)
                                    .addComponent(itemImage7, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(descriptItem4JLabel)
                                    .addComponent(itemBtn4)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(descriptItem8JLabel)
                                    .addComponent(itemBtn8)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(itemImage8, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(descriptItem5JLabel)
                    .addComponent(itemBtn5)
                    .addComponent(welcomeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(viewOrderBtn)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addGap(121, 121, 121))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(productsComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(exitButton))
                            .addGap(22, 22, 22)))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(welcomeLabel)
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(itemImage2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemImage1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemImage4, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemImage3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(productsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                //.addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(descriptItem1JLabel)
                                    .addComponent(descriptItem2JLabel)
                                    .addComponent(descriptItem3JLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(itemBtn1)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(itemBtn3)
                                        .addComponent(itemBtn2))))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(descriptItem4JLabel)
                                    .addComponent(viewOrderBtn))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(itemBtn4)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(itemImage6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(itemImage5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(itemImage8, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(itemImage7, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(descriptItem5JLabel)
                    .addComponent(descriptItem6JLabel)
                    .addComponent(descriptItem7JLabel)
                    .addComponent(descriptItem8JLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemBtn5)
                    .addComponent(itemBtn6)
                    .addComponent(itemBtn7)
                    .addComponent(itemBtn8)
                    .addComponent(exitButton))
                //.addContainerGap(29, Short.MAX_VALUE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

    }// </editor-fold>                        

    /*
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        descriptItem3JLabel = new javax.swing.JLabel();
        descriptItem4JLabel = new javax.swing.JLabel();
        descriptItem5JLabel = new javax.swing.JLabel();
        descriptItem6JLabel = new javax.swing.JLabel();
        descriptItem7JLabel = new javax.swing.JLabel();
        descriptItem8JLabel = new javax.swing.JLabel();
        itemBtn1 = new javax.swing.JButton();
        itemBtn2 = new javax.swing.JButton();
        itemBtn3 = new javax.swing.JButton();
        itemBtn4 = new javax.swing.JButton();
        itemBtn5 = new javax.swing.JButton();
        itemBtn6 = new javax.swing.JButton();
        itemBtn7 = new javax.swing.JButton();
        itemImage3 = new javax.swing.JLabel();
        itemBtn8 = new javax.swing.JButton();
        itemImage2 = new javax.swing.JLabel();
        welcomeLabel = new javax.swing.JLabel();
        itemImage5 = new javax.swing.JLabel();
        itemImage1 = new javax.swing.JLabel();
        itemImage6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        itemImage4 = new javax.swing.JLabel();
        exitButton = new javax.swing.JButton();
        itemImage8 = new javax.swing.JLabel();
        productsComboBox = new javax.swing.JComboBox();
        itemImage7 = new javax.swing.JLabel();
        viewOrderBtn = new javax.swing.JButton();
        descriptItem1JLabel = new javax.swing.JLabel();
        descriptItem2JLabel = new javax.swing.JLabel();

        descriptItem3JLabel.setText("3");

        descriptItem4JLabel.setText("4");

        descriptItem5JLabel.setText("5");

        descriptItem6JLabel.setText("6");

        descriptItem7JLabel.setText("7");

        descriptItem8JLabel.setText("8");

        itemBtn1.setText("Buy");

        itemBtn2.setText("Buy");

        itemBtn3.setText("Buy");

        itemBtn4.setText("Buy");

        itemBtn5.setText("Buy");

        itemBtn6.setText("Buy");

        itemBtn7.setText("Buy");

        itemBtn8.setText("Buy");

        welcomeLabel.setText("Welcome Label");

        jLabel2.setText("Your Orders");

        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        productsComboBox.setMaximumRowCount(20);
        productsComboBox.setToolTipText("");

        viewOrderBtn.setText("View Order Details");
        viewOrderBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewOrderBtnActionPerformed(evt);
            }
        });

        descriptItem1JLabel.setText("1");

        descriptItem2JLabel.setText("2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(itemImage1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(itemImage2, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(itemImage3, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(itemImage4, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(descriptItem1JLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(183, 183, 183)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(descriptItem2JLabel)
                                    .addComponent(descriptItem6JLabel)
                                    .addComponent(itemBtn6))
                                .addGap(90, 90, 90)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(descriptItem7JLabel)
                                    .addComponent(itemBtn7)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(itemImage5, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(itemBtn1))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(itemImage6, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(41, 41, 41)
                                        .addComponent(itemBtn2)))
                                .addGap(27, 27, 27)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(itemBtn3)
                                    .addComponent(descriptItem3JLabel)
                                    .addComponent(itemImage7, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(descriptItem4JLabel)
                                    .addComponent(itemBtn4)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(descriptItem8JLabel)
                                    .addComponent(itemBtn8)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(itemImage8, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(descriptItem5JLabel)
                    .addComponent(itemBtn5)
                    .addComponent(welcomeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(viewOrderBtn)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addGap(121, 121, 121))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(productsComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(exitButton))
                            .addGap(22, 22, 22)))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(welcomeLabel)
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(itemImage2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemImage1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemImage4, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemImage3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(productsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(descriptItem1JLabel)
                                    .addComponent(descriptItem2JLabel)
                                    .addComponent(descriptItem3JLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(itemBtn1)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(itemBtn3)
                                        .addComponent(itemBtn2))))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(descriptItem4JLabel)
                                    .addComponent(viewOrderBtn))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(itemBtn4)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(itemImage6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(itemImage5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(itemImage8, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(itemImage7, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(descriptItem5JLabel)
                    .addComponent(descriptItem6JLabel)
                    .addComponent(descriptItem7JLabel)
                    .addComponent(descriptItem8JLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemBtn5)
                    .addComponent(itemBtn6)
                    .addComponent(itemBtn7)
                    .addComponent(itemBtn8)
                    .addComponent(exitButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    */
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < itemButtons.size(); i++) {
            //Gets specific button
            if (e.getActionCommand().equals("button" + i)) {

                //MySql Statement to create a new Purchase Entry
                ru_product.setOrder(user, products, i);
                JOptionPane.showMessageDialog(null, "You Have Purchased " + products.get(i).getName());
                //updates Orders to JComboBox
                try {
                    viewOrders();
                } catch (SQLException ex) {
                    Logger.getLogger(ShoppingFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }
    
    
    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_exitButtonActionPerformed

    private void viewOrderBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewOrderBtnActionPerformed
        Purchase purchase = (Purchase) productsComboBox.getSelectedItem();

        JOptionPane.showMessageDialog(null, "<html>"
            + "Product Name: " + purchase.getProductName() + "<br/>"
            + "Price: " + purchase.getProductPrice() + "<br/>"
            + " Date: " + purchase.getPurchaseDate() + "<br/>"
            + "Credit Card: " + purchase.getCreditCard()
            + "</html>"
        );
    }//GEN-LAST:event_viewOrderBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel descriptItem1JLabel;
    private javax.swing.JLabel descriptItem2JLabel;
    private javax.swing.JLabel descriptItem3JLabel;
    private javax.swing.JLabel descriptItem4JLabel;
    private javax.swing.JLabel descriptItem5JLabel;
    private javax.swing.JLabel descriptItem6JLabel;
    private javax.swing.JLabel descriptItem7JLabel;
    private javax.swing.JLabel descriptItem8JLabel;
    private javax.swing.JButton exitButton;
    private javax.swing.JButton itemBtn1;
    private javax.swing.JButton itemBtn2;
    private javax.swing.JButton itemBtn3;
    private javax.swing.JButton itemBtn4;
    private javax.swing.JButton itemBtn5;
    private javax.swing.JButton itemBtn6;
    private javax.swing.JButton itemBtn7;
    private javax.swing.JButton itemBtn8;
    private javax.swing.JLabel itemImage1;
    private javax.swing.JLabel itemImage2;
    private javax.swing.JLabel itemImage3;
    private javax.swing.JLabel itemImage4;
    private javax.swing.JLabel itemImage5;
    private javax.swing.JLabel itemImage6;
    private javax.swing.JLabel itemImage7;
    private javax.swing.JLabel itemImage8;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JComboBox productsComboBox;
    private javax.swing.JButton viewOrderBtn;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JLabel jLabel1;
}
