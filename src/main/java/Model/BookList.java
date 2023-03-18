package Model;

import java.util.ArrayList;

public class BookList {
    private ArrayList<Book> books;

    public BookList() {
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        this.books.add(book);
    }

    public void listBooks() {
        System.out.println("List of books:");
        for (Book book : this.books) {
            System.out.println("Title: " + book.getTitle());
            System.out.println("Author: " + book.getAuthor());
            System.out.println("Publication year: " + book.getPublicationYear());
            System.out.println();
        }
    }
}
