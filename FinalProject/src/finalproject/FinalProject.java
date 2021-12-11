/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.*;

/**
 *
 * @author Administrator
 */
public class FinalProject {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        //creating the tables
        //can be commented out after the first run
        Class.forName("org.sqlite.JDBC");

        Connection con = DriverManager.getConnection("jdbc:sqlite:C:\\SQLite\\final.db");
        System.out.println("Opened database successfully");

        Statement stmt = con.createStatement();
        //CREATING TABLES
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
                + " SN           TEXT    NOT NULL REFERENCES Books (SN) ON DELETE CASCADE ON UPDATE CASCADE,"
                + " StId            INT     NOT NULL CONSTRAINT fk_id REFERENCES Students (ID) ON DELETE CASCADE ON UPDATE CASCADE,"
                + " StName            TEXT     NOT NULL,"
                + " StudentContact            INT     NOT NULL,"
                + " IssuedDate            TEXT"
                + ");";

        stmt.executeUpdate("Drop table if exists Issued");
        stmt.executeUpdate(createIssuedTable);
        System.out.println("Tables created\n");
        //INSERT STATEMENTS
        //students
        String sql = "INSERT INTO Students (ID,NAME,Contact) "
                + "VALUES (1, 'Paul',514);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO Students (ID,NAME,Contact) "
                + "VALUES (2, 'Giorgio',438);";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO Students (ID,NAME,Contact) "
                + "VALUES (3, 'Ralph',656);";
        stmt.executeUpdate(sql);

        //to change language
        Scanner console = new Scanner(System.in);
        String language = "";
        while (!language.equals("fr") && !language.equals("en")) {
            System.out.println("Would you like to procede in french (fr) or english (en)?");
            language = console.next();
        }

        Locale locale = new Locale(language, "CA");
        ResourceBundle res = ResourceBundle.getBundle("FinalProject/Source", locale);

        //books inserts
        Book b1 = new Book(1, "Programming", "Shahe", "Vanier", 20, 10, locale);
        b1.addBook(b1);

        Book b2 = new Book(2, "Programming 2", "Gevorg", "Vanier", 25, 7, locale);
        b2.addBook(b2);

        Book b3 = new Book(3, "HTML", "Armen", "Dawson", 30, 5, locale);
        b3.addBook(b3);

        Book b4 = new Book(4, "CSS", "People", "Abbott", 40, 10, locale);
        b4.addBook(b4);

        //issued inserts
        b1.issueBook(b1, new Student(3, "Ralph", 656, locale));
        b2.issueBook(b2, new Student(1, "Paul", 514, locale));

        ResultSet rs;
        String id = "";
        Student stu = new Student(0, "fds", 0, locale);

        //we use this to make input simpler
        if (language.equals("fr")) {
            while (!id.equals("e") && !id.equals("b")) {
                System.out.print(res.getString("login"));
                id = console.next();
            }
        } else {
            while (!id.equals("s") && !id.equals("l")) {
                System.out.print(res.getString("login"));
                id = console.next();
            }
        }

        //checks if the user is a student
        if (id.equals("s") || id.equals("e")) {
            //asks for the student's id
            boolean foundStudent = false;
            while (!foundStudent) {
                String studentNum = "";
                do {
                    System.out.print(res.getString("stuNum"));
                    studentNum = console.next();

                    if (!isNumeric(studentNum)) {
                        System.out.println(res.getString("notNum"));
                    }
                } while (!isNumeric(studentNum));
                //displays the information on that student
                rs = stmt.executeQuery("SELECT * FROM Students WHERE ID=" + studentNum);

                while (rs.next()) {
                    foundStudent = true;
                    stu = new Student(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("Contact"), locale);
                    System.out.println(res.getString("studentFound"));
                    System.out.println("");
                    System.out.print("ID = " + rs.getInt("ID"));
                    System.out.print(" | NAME = " + rs.getString("NAME"));
                    System.out.println(" | Contact = " + rs.getInt("Contact"));
                }
            }

            Controller stuController = ControllerFactory.createController("s", stu);
            String choice = "";

            while (!choice.equals("7")) {
                //asks the user to input a number from 1-7
                System.out.println(res.getString("stuChoice"));
                choice = console.next();

                switch (choice) {
                    case ("1"):
                        //search by title
                        String title = "";
                        while (title.equals("") || title.isEmpty()) {
                            System.out.println(res.getString("sop1"));
                            title = console.next();
                            stuController.getByTitle(title);
                        }
                        break;
                    case ("2"):
                        //search by author
                        String name = "";
                        while (name.equals("") || name.isEmpty()) {
                            System.out.println(res.getString("sop2"));
                            name = console.next();
                        }
                        stuController.getByName(name);
                        break;
                    case ("3"):
                        //search by publisher
                        String publisher = "";
                        while (publisher.equals("") || publisher.isEmpty()) {
                            System.out.println(res.getString("sop3"));
                            publisher = console.next();
                        }
                        stuController.getByPublisher(publisher);
                        break;
                    case ("4"):
                        //view catalogue
                        stuController.updateViewBooks();
                        stuController.updateViewIssued();
                        break;
                    case ("5"):
                        //borrow
                        String bookSN = "";
                        while (!isNumeric(bookSN)) {
                            System.out.println(res.getString("sop5"));
                            bookSN = console.next();
                        }
                        stuController.borrow(new Book(Integer.parseInt(bookSN), "", "", "", 2, 1, locale));
                        break;
                    case ("6"):
                        //return
                        String bookSNReturn = "";
                        while (!isNumeric(bookSNReturn)) {
                            System.out.println(res.getString("sop5"));
                            bookSNReturn = console.next();
                        }
                        stuController.toReturn(new Book(Integer.parseInt(bookSNReturn), "", "", "", 2, 1, locale));
                        break;
                    case ("7"):
                        //exits the application
                        System.out.println(res.getString("sop7"));
                        stmt.close();
                        stuController.con.close();
                        return;
                }
            }

        } else if (id.equals("l") || id.equals("b")) {
            Controller libController = ControllerFactory.createController("l", null);
            //choice is a string to avoid errors
            String choice = "";

            while (!choice.equals("6")) {
                //asks librarian to input a number from 1-6
                while (!isNumeric(choice)) {
                    System.out.println(res.getString("libCHoice"));
                    choice = console.next();
                }
                //turns choice into an int
                int numChoice = Integer.parseInt(choice);
                choice = "";
                switch (numChoice) {
                    case (1):
                        //adds a book
                        String[] bookArray = new String[6];
                        while (!isCorrect(bookArray)) {
                            System.out.println(res.getString("lop1"));
                            bookArray = (console.next()).split(",");
                        }

                        libController.addBook(new Book(Integer.parseInt(bookArray[0]), bookArray[1], bookArray[2], bookArray[3], Integer.parseInt(bookArray[4]),
                                Integer.parseInt(bookArray[5]), locale));
                        break;
                    case (2):
                        //issue a book
                        boolean foundStudent = false;
                        while (!foundStudent) {

                            String studentNum = "";
                            while (!isNumeric(studentNum)) {
                                System.out.println(res.getString("lop2"));
                                studentNum = console.next();
                            }
                            rs = stmt.executeQuery("SELECT * FROM Students WHERE ID=" + studentNum);

                            while (rs.next()) {
                                foundStudent = true;
                                stu = new Student(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("Contact"), locale);
                                System.out.println(res.getString("studentFound"));
                            }
                        }

                        boolean foundBook = false;
                        Book issueBook = new Book(numChoice, id, id, id, numChoice, numChoice, locale);
                        while (!foundBook) {

                            String bookSN = "";
                            while (!isNumeric(bookSN)) {
                                System.out.println(res.getString("lop2-1"));
                                bookSN = console.next();
                            }
                            rs = stmt.executeQuery("SELECT * FROM Books WHERE SN=" + bookSN);

                            while (rs.next()) {
                                foundBook = true;
                                issueBook = new Book(rs.getInt("SN"), rs.getString("Title"), rs.getString("Author"),
                                        rs.getString("Publisher"), rs.getInt("Price"), rs.getInt("Quantity"), locale);
                                System.out.println(res.getString("bookFound"));
                            }
                            libController.issueBook(issueBook, stu);
                        }
                        break;
                    case (3):
                        //return a book
                        foundStudent = false;
                        while (!foundStudent) {

                            String studentNum = "";
                            while (!isNumeric(studentNum)) {
                                System.out.println(res.getString("lop2"));
                                studentNum = console.next();
                            }

                            rs = stmt.executeQuery("SELECT * FROM Students WHERE ID=" + studentNum);

                            while (rs.next()) {
                                foundStudent = true;
                                stu = new Student(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("Contact"), locale);
                                System.out.println(res.getString("studentFound"));
                            }
                        }

                        foundBook = false;
                        issueBook = new Book(numChoice, id, id, id, numChoice, numChoice, locale);
                        while (!foundBook) {

                            String bookSN = "";
                            while (!isNumeric(bookSN)) {
                                System.out.println(res.getString("lop2-1"));
                                bookSN = console.next();
                            }

                            rs = stmt.executeQuery("SELECT * FROM Books WHERE SN=" + bookSN);

                            while (rs.next()) {
                                foundBook = true;
                                issueBook = new Book(rs.getInt("SN"), rs.getString("Title"), rs.getString("Author"),
                                        rs.getString("Publisher"), rs.getInt("Price"), rs.getInt("Quantity"), locale);
                                System.out.println(res.getString("bookFound"));
                            }
                        }
                        libController.returnBook(issueBook, stu);
                        break;

                    case (4):
                        //view the catalog
                        libController.updateViewBooks();
                        break;
                    case (5):
                        //view issued books
                        libController.updateViewIssued();
                        break;
                    case (6):
                        //exits the application
                        System.out.println(res.getString("sop7"));
                        stmt.close();
                        libController.con.close();
                        return;
                }
            }

        }

    }

    /**
     * checks if the string is a digit
     *
     * @param str the string we are checking
     * @return true or false
     */
    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * checks if the parameters for adding a book is correct
     *
     * @param arr the array with the parameters of a book
     * @return true or false
     */
    public static boolean isCorrect(String[] arr) {
        if (arr.length == 6) {
            if (isNumeric(arr[0]) && isNumeric(arr[4]) && isNumeric(arr[5])) {
                if (!arr[1].equals("") && !arr[2].equals("") && !arr[3].equals("")) {
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }
}

class ControllerFactory {

    /**
     * creates a controller class instance depending on if the user is a student
     * or librarian
     *
     * @param choice either l (librarian) or s (Student)
     * @param stu and object of type student
     * @return a Controller class instance
     * @throws Exception
     */
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
