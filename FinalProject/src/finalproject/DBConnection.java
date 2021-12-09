/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalproject;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author armen
 */
public class DBConnection {

    private static Connection con;

    public static Connection getSingleInstance() throws Exception {
        if (con == null) {
            con = getConnection();
        }
        return con;

    }

    private static Connection getConnection() throws Exception {
        String url = "jdbc:sqlite:C:\\SQLite\\final.db";
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(url);
        return con;
    }
}
