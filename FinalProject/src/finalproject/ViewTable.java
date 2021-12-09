/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalproject;

import java.util.Map;

/**
 *
 * @author armen
 */
public class ViewTable {

    public void viewCatalog() throws Exception {
        System.out.println(Book.viewCatalog());
    }

    public void viewIssuedBooks() throws Exception {
        System.out.println(Book.viewIssuedBooks());
    }

    public void searchPublisher(String publisher, Student s) throws Exception {
        System.out.println(s.searchBookByPublisher(publisher));
    }

    public void searchName(String name, Student s) throws Exception {
        System.out.println(s.searchBookByName(name));
    }

    public void searchTitle(String title, Student s) throws Exception {
        System.out.println(s.searchBookByTitle(title));

    }
}
