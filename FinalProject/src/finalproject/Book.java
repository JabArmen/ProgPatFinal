/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.*;

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
    private static Locale loc;
    private static ResourceBundle res;

    public Book(int SN, String title, String author, String publisher, int price, int qte, Locale loc) throws Exception {
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
        this.loc = loc;
        res = ResourceBundle.getBundle("FinalProject/Source", loc);

    }

    /**
     * adds a book to the database
     *
     * @param book a book class instance
     * @throws Exception
     */
    public void addBook(Book book) throws Exception {
        Connection con = DBConnection.getSingleInstance();

        Statement stmt = con.createStatement();

        String sql;
        sql = String.format("INSERT INTO Books (SN,Title,Author,Publisher,Price,Quantity,Issued,Date) VALUES (%s,'%s','%s','%s',%d,%d,%d,'%s');", book.getSN(), book.getTitle(),
                book.getAuthor(), book.getPublisher(), book.getPrice(),
                book.getQte(), book.getIssuedQte(), book.getDateOfPurchase());
        stmt.executeUpdate(sql);
    }

    /**
     * adds the book a student is borrowing to the issued table
     *
     * @param b the book the student wants to borrow
     * @param s the student borrowing it
     * @return true or false
     * @throws Exception
     */
    public boolean issueBook(Book b, Student s) throws Exception {
        Connection con = DBConnection.getSingleInstance();
        Statement stmt = con.createStatement();
        ResultSet rs;
        System.out.print(res.getString("issueBook1"));
        boolean inData = false;
        rs = stmt.executeQuery("SELECT * FROM Issued WHERE SN = " + b.getSN() + " AND StId = " + s.getStId() + ";");
        while (rs.next()) {
            inData = true;
        }
        if (inData) {
            System.out.println(res.getString("issueBook2"));
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
                System.out.println(res.getString("issueBook3"));
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
        System.out.println(res.getString("issueBook4"));

        sql = "UPDATE Books SET Quantity = " + quantity + " WHERE SN = " + b.getSN() + ";";
        stmt.executeUpdate(sql);
        sql = "UPDATE Books SET Issued = " + issued + " WHERE SN = " + b.getSN() + ";";
        stmt.executeUpdate(sql);
        return true;
    }

    /**
     * removes the book a student is returning from the issued table
     *
     * @param b the book the student is returning
     * @param s the student thats returning the book
     * @return true or false
     * @throws Exception
     */
    public boolean returnBook(Book b, Student s) throws Exception {
        Connection con = DBConnection.getSingleInstance();
        Statement stmt = con.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT * FROM Issued WHERE SN LIKE " + b.getSN() + ";");
        System.out.print(res.getString("returnBook1"));
        int quantity = 0;
        int issued = 0;
        boolean borrowedBook = false;
        while (rs.next()) {
            if (rs.getInt("StId") == (s.getStId())) {
                borrowedBook = true;
                System.out.println(res.getString("returnBook2"));
                stmt.executeUpdate("DELETE FROM Issued WHERE SN LIKE " + b.getSN() + " AND StId LIKE " + s.getStId() + ";");
            }
        }

        if (!borrowedBook) {
            System.out.println(res.getString("returnBook3"));
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

    /**
     * makes a map object with all the information from the table books
     *
     * @return a map
     * @throws Exception
     */
    public static Map<String, String> viewCatalog() throws Exception {
        NumberFormat currencyForm = NumberFormat.getCurrencyInstance(loc);

        Connection con = DBConnection.getSingleInstance();
        Statement stmt = con.createStatement();

        HashMap<String, String> map = new HashMap<>();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Books;");
        System.out.print(res.getString("viewCatalog1"));
        while (rs.next()) {
            map.put(rs.getString("SN"), " | Title = " + rs.getString("Title")
                    + " | Author = " + rs.getString("Author") + " | Publisher = " + rs.getString("Publisher") + " | Price = " + currencyForm.format(rs.getInt("Price"))
                    + " | Quantity = " + rs.getInt("Quantity") + " | Issued = " + rs.getInt("Issued") + " | Date = " + rs.getString("Date") + "\n\n");
        }

        return map;
    }

    /**
     * makes a map object with all the information from the table issued
     *
     * @return a map
     * @throws Exception
     */
    public static Map<String, String> viewIssuedBooks() throws Exception {
        Connection con = DBConnection.getSingleInstance();
        Statement stmt = con.createStatement();

        HashMap<String, String> map = new HashMap<>();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Issued;");
        System.out.print(res.getString("viewIssued1"));
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
