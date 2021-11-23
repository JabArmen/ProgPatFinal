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
    public static void main(String[] args) throws Exception {
        Class.forName("org.sqlite.JDBC");

        Connection con = DriverManager.getConnection("jdbc:sqlite:C:\\SQLite\\final.db");

        Statement stmt = con.createStatement();

        String createStudentTable = "CREATE TABLE Students "
                + "(ID INT PRIMARY KEY     NOT NULL,"
                + " NAME           TEXT    NOT NULL, "
                + " Contact            INT     NOT NULL)";

        stmt.executeUpdate("Drop table if exists Students");
        stmt.executeUpdate(createStudentTable);

        String createBooksTable = "CREATE TABLE Books "
                + "(SN TEXT PRIMARY KEY     NOT NULL,"
                + " Title           TEXT    NOT NULL, "
                + " Author            TEXT     NOT NULL,"
                + " Publisher            TEXT     NOT NULL,"
                + " Price            INT     NOT NULL,"
                + " Quantity            INT,"
                + " Issued            INT     NOT NULL,"
                + " Date            TEXT)";

        stmt.executeUpdate("Drop table if exists Books");
        stmt.executeUpdate(createBooksTable);

        String createIssuedTable = "CREATE TABLE Issued "
                + "(ID INT PRIMARY KEY     NOT NULL,"
                + " SN           TEXT    NOT NULL, "
                + " StId            INT     NOT NULL,"
                + " StName            TEXT     NOT NULL,"
                + " StudentContact            INT     NOT NULL,"
                + " IssuedDate            INT,"
                + " FOREIGN KEY(SN) REFERENCES Books(SN),"
                + "FOREIGN KEY(StId) REFERENCES Students(ID))";

        stmt.executeUpdate("Drop table if exists Issued");
        stmt.executeUpdate(createIssuedTable);
    }

}

class Librarian {

    public void addBook() {
    } //could be a singleton

    public void issueBook() {
    }

    public void returnBook() {
    }

    public void catalog() {
    }

    public void viewIssuedBooks() {
    }
}

class Student {

    public void searchByTitle() {
    }

    public void searchByAuthor() {
    }

    public void searchByYear() {
    }

    public void catalog() {
    }

    public void borrowBook() {
    }

    public void returnBook() {
    }

}

class Book {

    private int SN;
    private String title;
    private String author;
    private String publisher;
    private int qte;
    private int issuedQte;
    private String DateOfPurchase;

    public Book(int SN, String title, String author, String publisher, int qte, int issuedQte, String DateOfPurchase) {
        this.SN = SN;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.qte = qte;
        this.issuedQte = issuedQte;
        this.DateOfPurchase = DateOfPurchase;
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
        return "Book:\n" + "SN: " + SN + "\nTitle: "
                + title + "\nAuthor: " + author + "\nPublisher: " + publisher
                + "\nQte:" + qte + " ,IssuedQte=" + issuedQte + "\nDateOfPurchase: " + DateOfPurchase;
    }
}
