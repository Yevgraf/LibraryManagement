package Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import Data.AuthorData;
import Data.AgeRangeData;
import Data.BookData;
import Data.CategoryData;
import Data.PublisherData;
import Model.AgeRange;
import Model.Author;
import Model.Book;
import Model.Category;
import Model.Publisher;
import View.CreateBookView;

public class BookController {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private BookData bookData;
    private AuthorData authorData;
    private AgeRangeData ageRangeData;
    private CategoryData categoryData;
    private PublisherData publisherData;
    private Scanner scanner;

    public BookController(BookData bookData, AuthorData authorData, AgeRangeData ageRangeData, CategoryData categoryData, PublisherData publisherData, Scanner scanner) {
        this.bookData = bookData;
        this.authorData = authorData;
        this.ageRangeData = ageRangeData;
        this.categoryData = categoryData;
        this.publisherData = publisherData;
        this.scanner = scanner;
    }


    public void createBook(String title, String subtitle, String authorName, int numPages, String categoryName, LocalDate publicationDate, String ageRangeName, String publisherName, String isbn) {
        List<Book> bookList = listBooks();

        Author author = authorData.findByName(authorName);
        if (author == null) {
            System.out.println("Autor não encontrado.");
            return;
        }

        Category category = categoryData.findByName(categoryName);
        if (category == null) {
            System.out.println("Categoria não encontrada.");
            return;
        }

        AgeRange ageRange = ageRangeData.findByName(ageRangeName);
        if (ageRange == null) {
            System.out.println("Faixa etária não encontrada.");
            return;
        }

        Publisher publisher = publisherData.findByName(publisherName);
        if (publisher == null) {
            System.out.println("Editora não encontrada.");
            return;
        }

        Book book = new Book(title, subtitle, author, numPages, category, publicationDate, ageRange, publisher, isbn);
        bookList.add(book);
        bookData.save(bookList);
    }


    public List<Book> listBooks() {
        return bookData.load();
    }

    public void removeBook(Book book) {
        List<Book> bookList = listBooks();
        boolean removed = bookList.removeIf(b -> b.getId() == book.getId());
        if (removed) {
            bookData.save(bookList);
        } else {
            System.out.println("Livro não encontrado.");
        }
    }

    public Book findBookByIdOrName(String searchTerm) {
        List<Book> books = listBooks();
        Book bookById = null;
        for (Book book : books) {
            if (Integer.toString(book.getId()).equals(searchTerm)) {
                bookById = book;
                break;
            }
        }
        if (bookById != null) {
            return bookById;
        }
        List<Book> booksByName = books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
        if (booksByName.isEmpty()) {
            return null;
        }
        if (booksByName.size() == 1) {
            return booksByName.get(0);
        }
        System.out.println("Há mais de um livro com esse nome, selecione o ID correto:");
        for (Book book : booksByName) {
            System.out.println("ID: " + book.getId() + ", Título: " + book.getTitle());
        }
        int selectedId = scanner.nextInt();
        scanner.nextLine();
        return booksByName.stream()
                .filter(book -> book.getId() == selectedId)
                .findFirst()
                .orElse(null);
    }

}
