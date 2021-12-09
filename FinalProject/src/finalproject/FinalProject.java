/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

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
        System.out.println("Opened database successfully");

        Statement stmt = con.createStatement();
        //CREATING TABLES
//        String createStudentTable = "CREATE TABLE Students "
//                + "(ID INT PRIMARY KEY     NOT NULL,"
//                + " NAME           TEXT    NOT NULL, "
//                + " Contact            INT     NOT NULL)";
//
//        stmt.executeUpdate("Drop table if exists Students");
//        stmt.executeUpdate(createStudentTable);
//
//        String createBooksTable = "CREATE TABLE Books "
//                + "(SN TEXT PRIMARY KEY     NOT NULL,"
//                + " Title           TEXT    NOT NULL, "
//                + " Author            TEXT     NOT NULL,"
//                + " Publisher            TEXT     NOT NULL,"
//                + " Price            INT     NOT NULL,"
//                + " Quantity            INT,"
//                + " Issued            INT     NOT NULL,"
//                + " Date            TEXT)";
//
//        stmt.executeUpdate("Drop table if exists Books");
//        stmt.executeUpdate(createBooksTable);
//
//        String createIssuedTable = "CREATE TABLE Issued "
//                + "(ID INT PRIMARY KEY     NOT NULL,"
//                + " SN           TEXT    NOT NULL REFERENCES Books (SN) ON DELETE CASCADE ON UPDATE CASCADE,"
//                + " StId            INT     NOT NULL CONSTRAINT fk_id REFERENCES Students (ID) ON DELETE CASCADE ON UPDATE CASCADE,"
//                + " StName            TEXT     NOT NULL,"
//                + " StudentContact            INT     NOT NULL,"
//                + " IssuedDate            TEXT"
//                + ");";
//
//        stmt.executeUpdate("Drop table if exists Issued");
//        stmt.executeUpdate(createIssuedTable);
//        System.out.println("Tables created\n");
//        //INSERT STATEMENTS
//        //students
//        String sql = "INSERT INTO Students (ID,NAME,Contact) "
//                + "VALUES (1, 'Paul',514);";
//        stmt.executeUpdate(sql);
//        sql = "INSERT INTO Students (ID,NAME,Contact) "
//                + "VALUES (2, 'Giorgio',438);";
//        stmt.executeUpdate(sql);
//        sql = "INSERT INTO Students (ID,NAME,Contact) "
//                + "VALUES (3, 'Ralph',656);";
//        stmt.executeUpdate(sql);
//
//        //books inserts
//        Book b1 = new Book(1, "Programming", "Shahe", "Vanier", 20, 10);
//        b1.addBook(b1);
//
//        Book b2 = new Book(2, "Programming 2", "Gevorg", "Vanier", 25, 7);
//        b2.addBook(b2);
//
//        Book b3 = new Book(3, "HTML", "Armen", "Dawson", 30, 5);
//        b3.addBook(b3);
//
//        Book b4 = new Book(4, "CSS", "People", "Abbott", 40, 10);
//        b4.addBook(b4);
//
//        //issued inserts
//        b1.issueBook(b1, new Student(3, "Ralph", 656));
//        b2.issueBook(b2, new Student(1, "Paul", 514));
//
//        //QUERIES
//        //students
//        stmt = con.createStatement();
//        ResultSet rs = stmt.executeQuery("SELECT * FROM Students;");
//        System.out.print("\nStudents:\n");
//        while (rs.next()) {
//            System.out.println("");
//
//            System.out.print("ID = " + rs.getInt("ID"));
//            System.out.print(" | NAME = " + rs.getString("NAME"));
//            System.out.println(" | Contact = " + rs.getInt("Contact"));
//        }
//
//        //books
//        System.out.println(Book.viewCatalog());
//
//        //issued
//        System.out.println(Book.viewIssuedBooks());

        Scanner console = new Scanner(System.in);
        ResultSet rs;
        String id = "";
        Student stu = new Student(0, "fds", 0);
        while (!id.equals("s") && !id.equals("l")) {
            System.out.print("Are you a Student or a Librarian (s or l)? ");
            id = console.next();

        }
        if (id.equals("s")) {
            boolean foundStudent = false;
            while (!foundStudent) {
                System.out.print("What is your student number? ");
                int studentNum = console.nextInt();

                rs = stmt.executeQuery("SELECT * FROM Students WHERE ID=" + studentNum);

                while (rs.next()) {
                    foundStudent = true;
                    stu = new Student(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("Contact"));
                    System.out.println("Student Found . . .");
                    System.out.println("");
                    System.out.print("ID = " + rs.getInt("ID"));
                    System.out.print(" | NAME = " + rs.getString("NAME"));
                    System.out.println(" | Contact = " + rs.getInt("Contact"));
                }
            }

            Controller stuController = ControllerFactory.createController("s", stu);
            int choice = 0;

            while (choice != 7) {
                System.out.println("\n1.\tSearch books by title\n"
                        + "2.\tSearch books by author’s name\n"
                        + "3.\tSearch by publisher\n"
                        + "4.\tView the catalogue of books (issued and available books)\n"
                        + "5.\tBorrow a book\n"
                        + "6.\tReturn a book\n"
                        + "7.\tQuit the program"
                        + "\nPlease input a number as selection");
                choice = console.nextInt();
                switch (choice) {
                    case (1):
                        System.out.println("Input Title");
                        String title = console.next();
                        stuController.getByTitle(title);
                        break;
                    case (2):
                        System.out.println("Input Author's Name");
                        String name = console.next();
                        stuController.getByName(name);
                        break;
                    case (3):
                        System.out.println("Input Publisher");
                        String publisher = console.next();
                        stuController.getByPublisher(publisher);
                        break;
                    case (4):
                        stuController.updateViewBooks();
                        stuController.updateViewIssued();
                        break;
                    case (5):
                        System.out.println("Input Selected Book's SN");
                        int bookSN = console.nextInt();
                        stuController.borrow(new Book(bookSN, "", "", "", 2, 1));
                        break;
                    case (6):
                        System.out.println("Input Selected Book's SN");
                        int bookSNReturn = console.nextInt();
                        stuController.toReturn(new Book(bookSNReturn, "", "", "", 2, 1));
                        break;
                    case (7):
                        System.out.println("You have left the library...");
                        return;
                }
            }

        } else if (id.equals("l")) {
            Controller libController = ControllerFactory.createController("l", null);
            int choice = 0;

            while (choice != 6) {
                System.out.println("\n1.\tAdd a book to the catalog of books\n"
                        + "2.\tIssue a book\n"
                        + "3.\tReturn a book\n"
                        + "4.\tView the catalogue of books (issued and available books)\n"
                        + "5.\tView issued books with information about students who has borrowed the books\n"
                        + "6.\tQuit the program\n"
                        + "\nPlease input a number as selection");
                choice = console.nextInt();
                switch (choice) {
                    case (1):
                        System.out.println("Input SN,Title,Author,Publisher,Price,Quantity\nSeparated by a comma");
                        String[] bookArray = (console.next()).split(",");
                        libController.addBook(new Book(Integer.parseInt(bookArray[0]), bookArray[1], bookArray[2], bookArray[3], Integer.parseInt(bookArray[4]),
                                Integer.parseInt(bookArray[5])));
                        break;
                    case (2):
                        boolean foundStudent = false;
                        while (!foundStudent) {

                            System.out.println("Input Student's Id");
                            int studentNum = console.nextInt();

                            rs = stmt.executeQuery("SELECT * FROM Students WHERE ID=" + studentNum);

                            while (rs.next()) {
                                foundStudent = true;
                                stu = new Student(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("Contact"));
                                System.out.println("Student Found . . .");
                            }
                        }

                        boolean foundBook = false;
                        Book issueBook = new Book(choice, id, id, id, choice, choice);
                        while (!foundBook) {
                            System.out.println("Input Book's SN");
                            int bookSN = console.nextInt();

                            rs = stmt.executeQuery("SELECT * FROM Books WHERE SN=" + bookSN);

                            while (rs.next()) {
                                foundBook = true;
                                issueBook = new Book(rs.getInt("SN"), rs.getString("Title"), rs.getString("Author"),
                                        rs.getString("Publisher"), rs.getInt("Price"), rs.getInt("Quantity"));
                                System.out.println("Book Found...");
                            }
                            libController.issueBook(issueBook, stu);
                        }
                        break;
                    case (3):
                        foundStudent = false;
                        while (!foundStudent) {

                            System.out.println("Input Student's Id");
                            int studentNum = console.nextInt();

                            rs = stmt.executeQuery("SELECT * FROM Students WHERE ID=" + studentNum);

                            while (rs.next()) {
                                foundStudent = true;
                                stu = new Student(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("Contact"));
                                System.out.println("Student Found . . .");
                            }
                        }

                        foundBook = false;
                        issueBook = new Book(choice, id, id, id, choice, choice);
                        while (!foundBook) {
                            System.out.println("Input Book's SN");
                            int bookSN = console.nextInt();

                            rs = stmt.executeQuery("SELECT * FROM Books WHERE SN=" + bookSN);

                            while (rs.next()) {
                                foundBook = true;
                                issueBook = new Book(rs.getInt("SN"), rs.getString("Title"), rs.getString("Author"),
                                        rs.getString("Publisher"), rs.getInt("Price"), rs.getInt("Quantity"));
                                System.out.println("Book Found...");
                            }
                        }
                        libController.returnBook(issueBook, stu);
                        break;

                    case (4):
                        libController.updateViewBooks();
                        break;
                    case (5):
                        libController.updateViewIssued();
                        break;
                    case (6):
                        System.out.println("You have left the library...");
                        return;
                }
            }

        }

    }
}

class ControllerFactory {

    public static Controller createController(String choice, Student stu) throws Exception {
        switch (choice.toLowerCase()) {
            case ("l"):
                return new Controller(new ViewTable());
            case ("s"):
                return new Controller(new ViewTable(), stu);
        }
        return null;
    }
}
