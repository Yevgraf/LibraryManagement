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

    /**
     * Creates a new book with the specified details and saves it.
     *
     * @param title           The title of the book.
     * @param subtitle        The subtitle of the book.
     * @param authorName      The name of the author of the book.
     * @param numPages        The number of pages in the book.
     * @param categoryName    The name of the category to which the book belongs.
     * @param publicationDate The publication date of the book.
     * @param ageRangeName    The name of the age range for which the book is suitable.
     * @param publisherName   The name of the publisher of the book.
     * @param isbn            The ISBN (International Standard Book Number) of the book.
     * @param quantity        The quantity of books to be created.
     * @throws IllegalArgumentException if any of the input parameters are invalid or if a book with the same ISBN already exists.
     */
    public void createBook(String title, String subtitle, String authorName, int numPages, String categoryName,
                           LocalDate publicationDate, String ageRangeName, String publisherName, String isbn, int quantity) {
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
        List<Book> bookList = bookData.listBooks();
        Book bookByIsbn = bookList.stream()
                .filter(b -> b.getIsbn().equals(isbn))
                .findFirst()
                .orElse(null);
        if (bookByIsbn != null) {
            throw new IllegalArgumentException("Já existe um livro com esse ISBN.");
        }

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

        Book book = new Book();
        book.setTitle(title);
        book.setSubtitle(subtitle);
        book.setAuthor(author);
        book.setNumPages(numPages);
        book.setCategory(category);
        book.setPublicationDate(publicationDate);
        book.setAgeRange(ageRange);
        book.setPublisher(publisher);
        book.setIsbn(isbn);
        book.setQuantity(quantity);

        bookData.save(book);

        System.out.println("Livro criado com sucesso.");
    }


    public List<Book> listBooks() {
        return bookData.load();
    }

    public boolean removeBook(Book book) {
        return bookData.deleteBook(book.getId());
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

    /**
     * Searches for books based on a selected category and displays the results.
     *
     * @return A list of books belonging to the selected category.
     */
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


    /**
     * Searches for books belonging to the specified category by category name.
     *
     * @param category The category for which to search books.
     * @return A list of books belonging to the specified category.
     */
    private List<Book> searchBooksByCategoryWithName(Category category) {
        List<Book> allBooks = bookData.listBooks();

        return allBooks.stream()
                .filter(book -> book.getCategory().getCategoryName() != null && book.getCategory().getCategoryName().equals(category.getCategoryName()))
                .distinct()
                .collect(Collectors.toList());
    }


    /**
     * Searches for books written by a selected author and displays the results.
     *
     * @return A list of books written by the selected author.
     */

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


}
