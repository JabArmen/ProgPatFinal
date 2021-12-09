/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author armen
 */
public class Student {

    private int stId;
    private String name;
    private int contactNumber;
    private static Connection con;

    public Student(int stId, String name, int contactNumber) throws Exception {
        this.stId = stId;
        this.name = name;
        this.contactNumber = contactNumber;
        this.con = DBConnection.getSingleInstance();
    }

    public List<Book> searchBookByTitle(String title) throws Exception {
        Statement stmt = con.createStatement();

        ArrayList<Book> list = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Books WHERE Title = '" + title + "';");

        System.out.print("\nCatalog By Title:\n");
        while (rs.next()) {
            list.add(new Book(Integer.parseInt(rs.getString("SN")), rs.getString("Title"), rs.getString("Author"), rs.getString("Publisher"), rs.getInt("Price"), rs.getInt("Quantity")));
        }
        if (list.isEmpty()) {
            System.out.println("No Book has been found!");
        }

        return list;
    }

    public List<Book> searchBookByName(String name) throws Exception {

        Statement stmt = con.createStatement();

        ArrayList<Book> list = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Books WHERE Author = '" + name + "';"); // where author = 'shahe'

        System.out.print("\nCatalog By Author:\n");
        while (rs.next()) {
            list.add(new Book(Integer.parseInt(rs.getString("SN")), rs.getString("Title"), rs.getString("Author"), rs.getString("Publisher"), rs.getInt("Price"), rs.getInt("Quantity")));
        }

        if (list.isEmpty()) {
            System.out.println("No Book has been found!");
        }

        return list;
    }

    public List<Book> searchBookByPublisher(String publisher) throws Exception {

        Statement stmt = con.createStatement();

        ArrayList<Book> list = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Books WHERE Publisher = '" + publisher + "';");

        System.out.print("\nCatalog By Publisher:\n");
        while (rs.next()) {
            list.add(new Book(Integer.parseInt(rs.getString("SN")), rs.getString("Title"), rs.getString("Author"), rs.getString("Publisher"), rs.getInt("Price"), rs.getInt("Quantity")));
        }
        if (list.isEmpty()) {
            System.out.println("No Book has been found!");
        }
        return list;
    }

    public Map<String, String> viewCatalog() throws Exception {

        Statement stmt = con.createStatement();

        HashMap<String, String> map = new HashMap<>();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Books;");
        System.out.print("\nCatalog:\n");
        while (rs.next()) {
            map.put(rs.getString("SN"), " | Title = " + rs.getString("Title")
                    + " | Author = " + rs.getString("Author") + " | Publisher = " + rs.getString("Publisher") + " | Price = " + rs.getInt("Price")
                    + " | Quantity = " + rs.getInt("Quantity") + " | Issued = " + rs.getInt("Issued") + " | Date = " + rs.getString("Date") + "\n\n");
        }

        return map;
    }

    public boolean borrowBook(Book b) throws Exception {

        Statement stmt = con.createStatement();
        ResultSet rs;
        System.out.print("\nIssue Book Request:\n");
        boolean inData = false;
        rs = stmt.executeQuery("SELECT * FROM Issued WHERE SN = " + b.getSN() + " AND StId = " + this.getStId() + ";");
        while (rs.next()) {
            inData = true;
        }
        if (inData) {
            System.out.println("You have already borrowed this book!Request Denied!");
            return false;
        }
        inData = false;
        rs = stmt.executeQuery("SELECT * FROM Books WHERE SN = " + b.getSN() + ";");

        int quantity = 0;
        int issued = 0;
        while (rs.next()) {
            inData = true;
            issued = rs.getInt("Issued") + 1;
            quantity = rs.getInt("Quantity") - 1;
            if (rs.getInt("Quantity") <= 0) {
                System.out.println("The requested book is not Available!Request Denied!");
                return false;
            }
        }
        if (!inData) {
            return false;
        }
        int newId = 100;
        rs = stmt.executeQuery("SELECT ID FROM Issued;");
        while (rs.next()) {
            newId = rs.getInt("ID") + 1;

        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        String nowDate = dtf.format(now);

        String sql = String.format("INSERT INTO Issued (ID,SN,StId,StName,StudentContact,IssuedDate) "
                + "VALUES (%d,'%s',%d,'%s',%d,'%s');", newId, b.getSN(), this.getStId(), this.getName(), this.getContactNumber(), nowDate);
        stmt.executeUpdate(sql);
        System.out.println("Request Accepted");

        sql = "UPDATE Books SET Quantity = " + quantity + " WHERE SN = " + b.getSN() + ";";
        stmt.executeUpdate(sql);
        sql = "UPDATE Books SET Issued = " + issued + " WHERE SN = " + b.getSN() + ";";
        stmt.executeUpdate(sql);
        return true;
    }

    public boolean returnBook(Book b) throws Exception {

        Statement stmt = con.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT * FROM Issued WHERE SN LIKE " + b.getSN() + ";");
        System.out.print("\nReturn Book Request:\n");
        int quantity = 0;
        int issued = 0;
        while (rs.next()) {
            if (rs.getInt("StId") == (this.getStId())) {
                System.out.println("The book will be returned!");
                stmt.executeUpdate("DELETE FROM Issued WHERE SN LIKE " + b.getSN() + " AND StId LIKE " + this.getStId() + ";");
            }
        }

        rs = stmt.executeQuery("SELECT * FROM Books WHERE SN LIKE " + b.getSN() + ";");

        while (rs.next()) {
            quantity = rs.getInt("Quantity") + 1;
            issued = rs.getInt("Issued") - 1;
        }

        String sql = "UPDATE Books SET Quantity = " + quantity + " WHERE SN = " + b.getSN() + ";";
        stmt.executeUpdate(sql);
        sql = "UPDATE Books SET Issued = " + issued + " WHERE SN = " + b.getSN() + ";";
        stmt.executeUpdate(sql);
        return true;
    }

    public int getStId() {
        return stId;
    }

    public String getName() {
        return name;
    }

    public int getContactNumber() {
        return contactNumber;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.stId;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Student other = (Student) obj;
        return this.stId == other.stId;
    }

    @Override
    public String toString() {
        return "Student{" + "stId=" + stId + ", name=" + name + ", contactNumber=" + contactNumber + '}';
    }
}
