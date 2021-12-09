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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author armen
 */
public class Book implements Comparable<Object> {

    private int SN;
    private String title;
    private String author;
    private String publisher;
    private int price;
    private int qte;
    private int issuedQte;
    private String DateOfPurchase;
    private Connection con;

    public Book(int SN, String title, String author, String publisher, int price, int qte) throws Exception {
        this.SN = SN;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
        this.qte = qte;
        this.issuedQte = 0;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        this.DateOfPurchase = dtf.format(now);
        this.con = DBConnection.getSingleInstance();
    }

    public void addBook(Book book) throws Exception {
        Connection con = DBConnection.getSingleInstance();

        Statement stmt = con.createStatement();

        String sql;
        sql = String.format("INSERT INTO Books (SN,Title,Author,Publisher,Price,Quantity,Issued,Date) VALUES (%s,'%s','%s','%s',%d,%d,%d,'%s');", book.getSN(), book.getTitle(),
                book.getAuthor(), book.getPublisher(), book.getPrice(),
                book.getQte(), book.getIssuedQte(), book.getDateOfPurchase());
        stmt.executeUpdate(sql);
    }

    public boolean issueBook(Book b, Student s) throws Exception {
        Connection con = DBConnection.getSingleInstance();
        Statement stmt = con.createStatement();
        ResultSet rs;
        System.out.print("\nIssue Book Request:\n");
        boolean inData = false;
        rs = stmt.executeQuery("SELECT * FROM Issued WHERE SN = " + b.getSN() + " AND StId = " + s.getStId() + ";");
        while (rs.next()) {
            inData = true;
        }
        if (inData) {
            System.out.println("You have already borrowed this book!Request Denied!");
            return false;
        }
        rs = stmt.executeQuery("SELECT * FROM Books WHERE SN = " + b.getSN() + ";");

        int quantity = 0;
        int issued = 0;
        inData = false;
        while (rs.next()) {
            inData = true;
            issued = rs.getInt("Issued") + 1;
            quantity = rs.getInt("Quantity") - 1;
            if (rs.getInt("Quantity") <= 0) {
                System.out.println("The requested book is not Available! Request Denied!");
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
                + "VALUES (%d,'%s',%d,'%s',%d,'%s');", newId, b.getSN(), s.getStId(), s.getName(), s.getContactNumber(), nowDate);
        stmt.executeUpdate(sql);
        System.out.println("Request Accepted");

        sql = "UPDATE Books SET Quantity = " + quantity + " WHERE SN = " + b.getSN() + ";";
        stmt.executeUpdate(sql);
        sql = "UPDATE Books SET Issued = " + issued + " WHERE SN = " + b.getSN() + ";";
        stmt.executeUpdate(sql);
        return true;
    }

    public boolean returnBook(Book b, Student s) throws Exception {
        Connection con = DBConnection.getSingleInstance();
        Statement stmt = con.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT * FROM Issued WHERE SN LIKE " + b.getSN() + ";");
        System.out.print("\nReturn Book Request:\n");
        int quantity = 0;
        int issued = 0;
        boolean borrowedBook = false;
        while (rs.next()) {
            if (rs.getInt("StId") == (s.getStId())) {
                borrowedBook = true;
                System.out.println("The book will be returned!");
                stmt.executeUpdate("DELETE FROM Issued WHERE SN LIKE " + b.getSN() + " AND StId LIKE " + s.getStId() + ";");
            }
        }

        if (!borrowedBook) {
            System.out.println("You haven't borrowed this book!");
            return false;
        }
        int newId = 100;
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

    public static Map<String, String> viewCatalog() throws Exception {
        Connection con = DBConnection.getSingleInstance();
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

    public static Map<String, String> viewIssuedBooks() throws Exception {
        Connection con = DBConnection.getSingleInstance();
        Statement stmt = con.createStatement();

        HashMap<String, String> map = new HashMap<>();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Issued;");
        System.out.print("\nIssued Books:\n");
        while (rs.next()) {
            map.put(rs.getString("SN"), "ID = " + rs.getInt("ID") + " | SN = " + rs.getString("SN")
                    + " | StId= " + rs.getString("StId") + " | StName = " + rs.getString("StName") + " | StudentContact = " + rs.getString("StudentContact")
                    + " | IssuedDate = " + rs.getString("IssuedDate") + "\n\n");
        }

        return map;
    }

    public int getPrice() {
        return price;
    }

    public int getSN() {
        return SN;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getQte() {
        return qte;
    }

    public int getIssuedQte() {
        return issuedQte;
    }

    public String getDateOfPurchase() {
        return DateOfPurchase;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.SN;
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
        final Book other = (Book) obj;
        if (this.SN != other.SN) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Book: |" + " SN: " + SN + " | Title: "
                + title + " | Author: " + author + " | Publisher: " + publisher
                + " | Qte:" + qte + " | IssuedQte=" + issuedQte + " | DateOfPurchase: " + DateOfPurchase + "\n";
    }

    @Override
    public int compareTo(Object o) {
        return this.SN - ((Book) o).getSN();
    }
}
