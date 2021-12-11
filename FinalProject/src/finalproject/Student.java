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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.*;

/**
 *
 * @author armen
 */
public class Student {

    private int stId;
    private String name;
    private int contactNumber;
    private static Connection con;
    private Locale loc;
    private static ResourceBundle res;

    public Student(int stId, String name, int contactNumber, Locale loc) throws Exception {
        this.stId = stId;
        this.name = name;
        this.contactNumber = contactNumber;
        this.con = DBConnection.getSingleInstance();
        this.loc = loc;
        res = ResourceBundle.getBundle("FinalProject/Source", loc);
    }

    /**
     * searches for a book with the title the student input
     *
     * @param title the title of the book the student is looking for
     * @return a list of all the books with that title
     * @throws Exception
     */
    public List<Book> searchBookByTitle(String title) throws Exception {
        Statement stmt = con.createStatement();

        ArrayList<Book> list = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Books WHERE Title = '" + title + "';");

        System.out.print(res.getString("searchByTitle1"));
        while (rs.next()) {
            list.add(new Book(Integer.parseInt(rs.getString("SN")), rs.getString("Title"), rs.getString("Author"), rs.getString("Publisher"), rs.getInt("Price"), rs.getInt("Quantity"), loc));
        }
        if (list.isEmpty()) {
            System.out.println(res.getString("noBook"));
        }

        return list;
    }

    /**
     * searches for a book with the author the student input
     *
     * @param name the author of the book the student is looking for
     * @return a list of all the books with that author
     * @throws Exception
     */
    public List<Book> searchBookByName(String name) throws Exception {

        Statement stmt = con.createStatement();

        ArrayList<Book> list = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Books WHERE Author = '" + name + "';"); // where author = 'shahe'

        System.out.print(res.getString("searchByName1"));
        while (rs.next()) {
            list.add(new Book(Integer.parseInt(rs.getString("SN")), rs.getString("Title"), rs.getString("Author"), rs.getString("Publisher"), rs.getInt("Price"), rs.getInt("Quantity"), loc));
        }

        if (list.isEmpty()) {
            System.out.println(res.getString("noBook"));
        }

        return list;
    }

    /**
     * searches for a book with the publisher the student input
     *
     * @param publisher the publisher of the book the student is looking for
     * @return a list of all the books with that publisher
     * @throws Exception
     */
    public List<Book> searchBookByPublisher(String publisher) throws Exception {

        Statement stmt = con.createStatement();

        ArrayList<Book> list = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Books WHERE Publisher = '" + publisher + "';");

        System.out.print(res.getString("searchByPub1"));
        while (rs.next()) {
            list.add(new Book(Integer.parseInt(rs.getString("SN")), rs.getString("Title"), rs.getString("Author"), rs.getString("Publisher"), rs.getInt("Price"), rs.getInt("Quantity"), loc));
        }
        if (list.isEmpty()) {
            System.out.println(res.getString("noBook"));
        }
        return list;
    }

    /**
     * makes a map object with all the information from the table books
     *
     * @return a map
     * @throws Exception
     */
    public Map<String, String> viewCatalog() throws Exception {
        NumberFormat currencyForm = NumberFormat.getCurrencyInstance(loc);

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
     * adds the book the student is borrowing to the Issued table
     *
     * @param b the book the student is borrowing
     * @return true or false
     * @throws Exception
     */
    public boolean borrowBook(Book b) throws Exception {

        Statement stmt = con.createStatement();
        ResultSet rs;
        System.out.print(res.getString("issueBook1"));
        boolean inData = false;
        rs = stmt.executeQuery("SELECT * FROM Issued WHERE SN = " + b.getSN() + " AND StId = " + this.getStId() + ";");
        while (rs.next()) {
            inData = true;
        }
        if (inData) {
            System.out.println(res.getString("issueBook2"));
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
                + "VALUES (%d,'%s',%d,'%s',%d,'%s');", newId, b.getSN(), this.getStId(), this.getName(), this.getContactNumber(), nowDate);
        stmt.executeUpdate(sql);
        System.out.println(res.getString("issueBook4"));

        sql = "UPDATE Books SET Quantity = " + quantity + " WHERE SN = " + b.getSN() + ";";
        stmt.executeUpdate(sql);
        sql = "UPDATE Books SET Issued = " + issued + " WHERE SN = " + b.getSN() + ";";
        stmt.executeUpdate(sql);
        return true;
    }

    /**
     * removes the book the student is returning from the table Issued
     *
     * @param b the book the student is returning
     * @return ture or false
     * @throws Exception
     */
    public boolean returnBook(Book b) throws Exception {

        Statement stmt = con.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT * FROM Issued WHERE SN LIKE " + b.getSN() + ";");
        System.out.print(res.getString("returnBook1"));
        int quantity = 0;
        int issued = 0;
        while (rs.next()) {
            if (rs.getInt("StId") == (this.getStId())) {
                System.out.println(res.getString("returnBook2"));
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
