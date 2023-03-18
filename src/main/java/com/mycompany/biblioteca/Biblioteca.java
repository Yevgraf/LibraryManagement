package com.mycompany.biblioteca;

import java.util.Scanner;

import Model.Book;
import Model.BookList;
import View.CreateBookView;

public class Biblioteca {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BookList bookList = new BookList();

        while (true) {
            System.out.println("Select an option:");
            System.out.println("1 - Add book");
            System.out.println("2 - List books");
            System.out.println("0 - Quit");

            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 1) {
                CreateBookView createBookView = new CreateBookView(scanner);
                Book book = createBookView.show();
                bookList.addBook(book);
                System.out.println("Book added successfully!");
            } else if (option == 2) {
                bookList.listBooks();
            } else if (option == 0) {
                break;
            } else {
                System.out.println("Invalid option, try again.");
            }

            System.out.println();
        }
    }
}
