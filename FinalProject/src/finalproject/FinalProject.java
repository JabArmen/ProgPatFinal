/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.sql.*;

/**
 *
 * @author Administrator
 */
public class FinalProject {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        Class.forName("org.sqlite.JDBC");

            Connection con = DriverManager.getConnection("jdbc:sqlite:C:\\SQLite\\final.db");

            Statement stmt = con.createStatement();
    }
    
}

class Librarian {
    public void addBook() {} //could be a singleton
    
    public void issueBook() {}
    
    public void returnBook() {}
    
    public void catalog() {}
    
    public void viewIssuedBooks() {}
}

class Student {
    public void searchByTitle() {}
    
    public void searchByAuthor() {}
    
    public void searchByYear() {}
    
    public void catalog() {}
    
    public void borrowBook() {}
    
    public void returnBook() {}
    
    
}
