package Model;

import java.util.ArrayList;

public class BookList {
    private ArrayList<Book> books;

    public BookList() {
        books = new ArrayList<Book>();
    }

    public void add(Book book) {
        books.add(book);
    }

    public void remove(Book book) {
        books.remove(book);
    }

    public void show() {
        if (books.isEmpty()) {
            System.out.println("No books have been added yet.");
        } else {
            for (Book book : books) {
                System.out.println("Title: " + book.getTitle());
                System.out.println("Author: " + book.getAuthor().getName());
                System.out.println("Birth date: " + book.getAuthor().getBirthDate());
                System.out.println("Nationality: " + book.getAuthor().getNationality());
                System.out.println("Publication date: " + book.getPublicationDate());
                System.out.println("Genre: " + book.getGenre());
                System.out.println("Age range: " + book.getAgeRange());
                System.out.println("ISBN: " + book.getIsbn());
                System.out.println();
            }
        }
    }
}