/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websecurityshopping;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.WindowConstants;

/**
 *
 * @author gregsimpson
 */
public class Panel extends JPanel{
    //ArrayList<String> url;
    JScrollPane scroll;
    GridLayout grid;
    private final int columns = 3;
    private int rows;
    private DatabaseInteraction db;
    String dbURL = "jdbc:mysql://gregandpablo.ncat.edu:3306/products";
    private String customerDB =  "jdbc:mysql://gregandpablo.ncat.edu:3306/customers";
    JPanel panel = new JPanel();
    ArrayList<Product> products;
    User user;
    
    public Panel(){
        user = null;   
        init_components();
    }
    
    public Panel(User user){
        this.user = user; 
        init_components();
    }
    
    public void init_components(){
        int allProductTotal = getAllProducts();
        rows = (int) Math.ceil((double)allProductTotal/(double)columns);
        panel.setLayout(grid = new GridLayout(rows, columns, 1,1));
        try {
            for(int x=0;x<products.size();x++){
                URL url = new URL(products.get(x).getUrl());
                BufferedImage image = ImageIO.read(url);
                BufferedImage newImage = getScaledImage(image, 125, 125);
                ImageIcon imageIcon = new ImageIcon(newImage);
                JLabel label = new JLabel(imageIcon);
                label.addMouseListener(new MouseAdapter(){
                    public void mouseClicked(MouseEvent e) {
                      System.out.println("Click at: " + e.getPoint());
                    }
                 });
                JPanel inside_panel = new JPanel();
                inside_panel.setLayout(new BorderLayout());
                inside_panel.add(new JLabel(products.get(x).getName()), BorderLayout.NORTH);
                inside_panel.add(label, BorderLayout.CENTER);
                inside_panel.add(new JLabel("$"+products.get(x).getPrice()), BorderLayout.SOUTH);
                panel.add(inside_panel);
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(Panel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Panel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        scroll = new JScrollPane(panel);
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        JLabel topLabel;
        if(user == null)
            topLabel = new JLabel("<html>Welcome " + "user.getFirstName()" + "<br/>Customer ID: " + "user.getCustomerID()" + "</html>");
        else
            topLabel = new JLabel("<html>Welcome " + user.getFirstName() + "<br/>Customer ID: " + user.getCustomerID() + "</html>");

        JButton button = new JButton("Cart");
        topPanel.add(topLabel, BorderLayout.WEST);
        String[] box = {"Orders"};
        topPanel.add(new JComboBox(box), BorderLayout.CENTER);
        topPanel.add(button, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        //setSize(500,500);
        //setVisible(true);
    }
    
    public int getAllProducts(){
        db = new DatabaseInteraction(dbURL, "","");
        db.connect();
        try {
            ResultSet rs = db.getProducts();//stmt.executeQuery(sql);

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
        db.close();
        return products.size();
    }
    
    
    public static BufferedImage getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }
    
}
