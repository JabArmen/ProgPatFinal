/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.util.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author shahe
 */
public class ControllerIT {

    public ControllerIT() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of addBook method, of class Controller.
     */
    @Test
    public void testAddBook() throws Exception {
        System.out.println("addBook");
        Book b = new Book(90, "test", "tester", "JUNIT", 15, 5, Locale.CANADA);
        Student s = new Student(1, "", 0, Locale.CANADA);
        Controller instance = new Controller(new ViewTable());
        List<Book> lBook = s.searchBookByTitle("test");
        if (lBook.isEmpty()) {
            instance.addBook(b);
        }
        List<Book> actual = s.searchBookByPublisher("JUNIT");
        assertEquals(b, actual.get(0));
    }

    /**
     * Test of issueBook method, of class Controller.
     */
    @Test
    public void testIssueBook() throws Exception {
        System.out.println("issueBook");
        Book b = new Book(1, null, null, null, 0, 0, Locale.CANADA);
        Student s = new Student(1, null, 0, Locale.CANADA);
        Controller instance = new Controller(new ViewTable());
        instance.returnBook(b, s);

        boolean expResult = true;
        boolean result = instance.issueBook(b, s);
        assertEquals(expResult, result);

    }

    /**
     * Test of borrow method, of class Controller.
     */
    @Test
    public void testBorrow() throws Exception {
        System.out.println("borrow");
        Book b = new Book(1, null, null, null, 0, 0, Locale.CANADA);
        Student stu = new Student(3, "", 0, Locale.CANADA);
        Controller instance = new Controller(new ViewTable(), stu);
        instance.toReturn(b);

        boolean expResult = true;
        boolean result = instance.borrow(b);
        assertEquals(expResult, result);
    }

    /**
     * Test of returnBook method, of class Controller.
     */
    @Test
    public void testReturnBook() throws Exception {
        System.out.println("returnBook");
        Book b = new Book(90, null, null, null, 0, 0, Locale.CANADA);
        Student s = new Student(91, null, 0, Locale.CANADA);
        Controller instance = new Controller(new ViewTable());
        instance.issueBook(b, s);
        boolean expResult = true;
        boolean result = instance.returnBook(b, s);
        assertEquals(expResult, result);
    }

    /**
     * Test of toReturn method, of class Controller.
     */
    @Test
    public void testToReturn() throws Exception {
        System.out.println("toReturn");
        Book b = new Book(90, "", "", "", 0, 0, Locale.CANADA);
        Student stu = new Student(91, "", 0, Locale.CANADA);
        Controller instance = new Controller(new ViewTable(), stu);
        instance.borrow(b);

        boolean expResult = true;
        boolean result = instance.toReturn(b);
        assertEquals(expResult, result);
    }

    /**
     * Test of updateViewBooks method, of class Controller.
     */
    @Test
    public void testUpdateViewBooks() throws Exception {
        System.out.println("updateViewBooks");
        Controller instance = new Controller(new ViewTable());
        Map<String, String> actual = Book.viewCatalog();
        Map<String, String> expected = new HashMap<>();

        expected.put("1", "");
        expected.put("2", "");
        expected.put("3", "");
        expected.put("4", "");
        expected.put("90", "");
        assertEquals(expected.keySet(), actual.keySet());
    }

    /**
     * Test of updateViewIssued method, of class Controller.
     */
    @Test
    public void testUpdateViewIssued() throws Exception {
        System.out.println("updateViewIssued");
        Controller instance = new Controller(new ViewTable());
        Map<String, String> actual = Book.viewIssuedBooks();
        Map<String, String> expected = new HashMap<>();

        expected.put("1", "");
        expected.put("2", "");
        assertEquals(expected.keySet(), actual.keySet());
    }

    /**
     * Test of getByPublisher method, of class Controller.
     */
    @Test
    public void testGetByPublisher() throws Exception {
        System.out.println("getByPublisher");
        String publisher = "Dawson";
        Book b = new Book(3, "HTML", "Armen", "Dawson", 30, 5, Locale.CANADA);
        Student s = new Student(1, null, 0, Locale.CANADA);
//        Controller instance = new Controller(new ViewTable());
        List<Book> actual = s.searchBookByPublisher(publisher);
//        instance.getByPublisher(publisher);
        assertEquals(b, actual.get(0));

    }

    /**
     * Test of getByName method, of class Controller.
     */
    @Test
    public void testGetByName() throws Exception {
        System.out.println("getByName");
        String name = "Armen";
        Book b = new Book(3, "HTML", "Armen", "Dawson", 30, 5, Locale.CANADA);
        Student s = new Student(1, null, 0, Locale.CANADA);
        List<Book> actual = s.searchBookByName(name);
//        Controller instance = null;
//        instance.getByName(name);
        assertEquals(b, actual.get(0));
    }

    /**
     * Test of getByTitle method, of class Controller.
     */
    @Test
    public void testGetByTitle() throws Exception {
        System.out.println("getByTitle");
        String title = "HTML";
        Book b = new Book(3, "HTML", "Armen", "Dawson", 30, 5, Locale.CANADA);
        Student s = new Student(1, null, 0, Locale.CANADA);
        List<Book> actual = s.searchBookByTitle(title);
//        Controller instance = null;
//        instance.getByTitle(title);
        assertEquals(b, actual.get(0));
    }

}
