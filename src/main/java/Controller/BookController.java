package Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

    private BookData bookData;
    private AuthorData authorData;
    private AgeRangeData ageRangeData;
    private CategoryData categoryData;
    private PublisherData publisherData;
    private Scanner scanner;

    public BookController(BookData bookData, AuthorData authorData, AgeRangeData ageRangeData,
                          CategoryData categoryData, PublisherData publisherData, Scanner scanner) {
        this.bookData = bookData;
        this.authorData = authorData;
        this.ageRangeData = ageRangeData;
        this.categoryData = categoryData;
        this.publisherData = publisherData;
        this.scanner = scanner;
    }

    public void createBook(String title, String subtitle, String authorName, int numPages, String categoryName,
                           LocalDate publicationDate, String ageRangeName, String publisherName, String isbn) {
        // Validations
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Título do livro inválido.");
        }
        if (numPages < 0) {
            throw new IllegalArgumentException("Número de páginas do livro inválido.");
        }
        if (publicationDate == null || publicationDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de publicação do livro inválida.");
        }
        List<Book> bookList = listBooks();
        Book bookByIsbn = bookList.stream()
                .filter(b -> b.getIsbn().equals(isbn))
                .findFirst()
                .orElse(null);

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

    public boolean removeBook(int bookId) {
        List<Book> bookList = listBooks();
        Optional<Book> bookToRemove = bookList.stream().filter(b -> b.getId() == bookId).findFirst();
        if (bookToRemove.isPresent()) {
            Book book = bookToRemove.get();
            if (book.isBorrowed()) {
                return false;
            }
            bookList.remove(book);
            bookData.save(bookList);
            return true;
        } else {
            System.out.println("Livro não encontrado.");
            return false;
        }
    }



    public Book findBookByIdOrName(String searchTerm) {
        List<Book> books = listBooks();
        Book bookByIsbn = null;
        for (Book book : books) {
            if (book.getIsbn().equals(searchTerm)) {
                bookByIsbn = book;
                break;
            }
        }
        if (bookByIsbn != null) {
            return bookByIsbn;
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
        System.out.println("Há mais de um livro com esse nome, selecione o ISBN correto:");
        for (Book book : booksByName) {
            System.out.println("ISBN: " + book.getIsbn() + ", Título: " + book.getTitle());
        }
        String selectedIsbn = scanner.nextLine();
        return booksByName.stream()
                .filter(book -> book.getIsbn().equals(selectedIsbn))
                .findFirst()
                .orElse(null);
    }


    public List<Book> searchBooksByCategory() {
        List<Category> categories = categoryData.listCategories();

        if (categories.isEmpty()) {
            System.out.println("Não há categorias cadastradas.");
            return Collections.emptyList();
        }

        System.out.println("Selecione uma categoria:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i).getCategoryName());
        }

        int selectedCategoryIndex = scanner.nextInt();
        scanner.nextLine();

        if (selectedCategoryIndex < 1 || selectedCategoryIndex > categories.size()) {
            System.out.println("Opção inválida.");
            return Collections.emptyList();
        }

        Category selectedCategory = categories.get(selectedCategoryIndex - 1);

        List<Book> books = searchBooksByCategoryWithName(selectedCategory);

        if (books.isEmpty()) {
            System.out.println("Não há livros cadastrados nesta categoria.");
            return Collections.emptyList();
        }

        System.out.println("Livros da categoria " + selectedCategory.getCategoryName() + ":");
        for (Book book : books) {
            System.out.println(book);
        }

        return books;
    }

    private List<Book> searchBooksByCategoryWithName(Category category) {
        List<Book> allBooks = bookData.listBooks();

        return allBooks.stream()
                .filter(book -> book.getCategory().getCategoryName() != null && book.getCategory().getCategoryName().equals(category.getCategoryName()))
                .distinct()
                .collect(Collectors.toList());
    }







    public List<Book> searchBooksByAuthor() {
        List<Author> authors = authorData.listAuthors();

        if (authors.isEmpty()) {
            System.out.println("Não há autores cadastrados.");
            return Collections.emptyList();
        }

        System.out.println("Selecione um autor:");
        for (int i = 0; i < authors.size(); i++) {
            System.out.println((i + 1) + ". " + authors.get(i).getName());
        }

        int selectedAuthorIndex = scanner.nextInt();
        scanner.nextLine();

        if (selectedAuthorIndex < 1 || selectedAuthorIndex > authors.size()) {
            System.out.println("Opção inválida.");
            return Collections.emptyList();
        }

        Author selectedAuthor = authors.get(selectedAuthorIndex - 1);

        List<Book> books = searchBooksByAuthorWithName(selectedAuthor.getName());

        if (books.isEmpty()) {
            System.out.println("Não há livros cadastrados para este autor.");
            return Collections.emptyList();
        }

        System.out.println("Livros do autor " + selectedAuthor.getName() + ":");
        for (Book book : books) {
            System.out.println(book);
        }

        return books;
    }

    public List<Book> searchBooksByAuthorWithName(String authorName) {
        List<Book> books = new ArrayList<>();

        List<Book> allBooks = bookData.listBooks();

        for (Book book : allBooks) {
            if (book.getAuthor().getName().equals(authorName)) {
                books.add(book);
            }
        }

        return books;
    }

    public Book isBookBorrowed(int bookId) {
        List<Book> allBooks = bookData.listBooks();
        for (Book book : allBooks) {
            if (book.getId() == bookId && book.isBorrowed()) {
                return book;
            }
        }
        return null;
    }

}
