/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author Admin
 */
public class connection {
    Connection conn = null;
    public static Connection connectdb() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String host, database, user, password;
            host = "localhost";
            database = "pos";
            user = "root";
            password = "";
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database, user, password);
            return conn;
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    
    }
}
