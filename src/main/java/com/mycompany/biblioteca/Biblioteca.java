package com.mycompany.biblioteca;

import java.util.ArrayList;
import java.util.List;

import Model.Book;
import View.CreateBookView;

public class Biblioteca {
    private static List<Book> books = new ArrayList<Book>();

    public static void main(String[] args) {
        while (true) {
            System.out.println("Welcome to the Library!");
            System.out.println("1. Add book");
            System.out.println("2. List books");
            System.out.println("3. Quit");

            int choice = Integer.parseInt(System.console().readLine());

            switch (choice) {
                case 1:
                    CreateBookView createBookView = new CreateBookView();
                    Book book = createBookView.createBook();
                    books.add(book);
                    System.out.println("Book added successfully!");
                    break;
                case 2:
                    for (Book b : books) {
                        System.out.println(b);
                    }
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please choose again.");
                    break;
            }
        }
    }
}
