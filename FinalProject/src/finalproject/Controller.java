/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalproject;

import java.sql.Connection;
import java.util.Map;

/**
 *
 * @author armen
 */
public class Controller {

    ViewTable view;
    Connection con;
    Student stu;

    //student constructor
    public Controller(ViewTable view, Student stu) throws Exception {
        this.view = view;
        this.stu = stu;
        con = DBConnection.getSingleInstance();
    }

    public Controller(ViewTable view) throws Exception {
        this.view = view;
        this.con = DBConnection.getSingleInstance();
    }

    public void addBook(Book b) throws Exception {
        b.addBook(b);
    }

    public boolean issueBook(Book b, Student s) throws Exception {
        return b.issueBook(b, s);
    }

    public boolean borrow(Book b) throws Exception {
        return this.stu.borrowBook(b);
    }

    public boolean returnBook(Book b, Student s) throws Exception {
        return b.returnBook(b, s);
    }

    public boolean toReturn(Book b) throws Exception {
        return this.stu.returnBook(b);
    }

    public void updateViewBooks() throws Exception {
        view.viewCatalog();
    }

    public void updateViewIssued() throws Exception {
        view.viewIssuedBooks();
    }

    public void getByPublisher(String publisher) throws Exception {
        view.searchPublisher(publisher, this.stu);
    }

    public void getByName(String name) throws Exception {
        view.searchName(name, this.stu);
    }

    public void getByTitle(String title) throws Exception {
        view.searchTitle(title, this.stu);
    }
}
