package View;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import Controller.*;
import Model.AgeRange;
import Model.Author;
import Model.Category;
import Model.Publisher;

public class CreateBookView {
    private BookController bookController;
    private AuthorController authorController;
    private AgeRangeController ageRangeController;
    private CategoryController categoryController;
    private PublisherController publisherController;
    private Scanner scanner;

    public CreateBookView(BookController bookController, AuthorController authorController, AgeRangeController ageRangeController, CategoryController categoryController, PublisherController publisherController, Scanner scanner) {
        this.bookController = bookController;
        this.authorController = authorController;
        this.ageRangeController = ageRangeController;
        this.categoryController = categoryController;
        this.publisherController = publisherController;
        this.scanner = scanner;
    }

    /**
     * Prompts the user to enter the details of a new book and creates it using the bookController.
     */
    public void createBook() {
        System.out.println("Criação de novo livro:");

        String title = getTitle();
        String subtitle = getSubtitle();
        Author author = getAuthor();
        int numPages = getNumPages();
        Category category = getCategory();
        LocalDate publicationDate = getPublicationDate();
        AgeRange ageRange = getAgeRange();
        Publisher publisher = getPublisher();
        String isbn = getIsbn();
        int quantity = getQuantity();

        bookController.createBook(title, subtitle, author.getName(), numPages, category.getCategoryName(), publicationDate, ageRange.getDescription(), publisher.getName(), isbn, quantity);
    }

    /**
     * Prompts the user to enter the title of the book.
     *
     * @return The title of the book.
     */
    private String getTitle() {
        System.out.print("Título: ");
        return scanner.nextLine();
    }

    /**
     * Prompts the user to enter the subtitle of the book.
     *
     * @return The subtitle of the book.
     */
    private String getSubtitle() {
        System.out.print("Subtítulo: ");
        return scanner.nextLine();
    }

    /**
     * Displays the list of authors and prompts the user to select an author.
     *
     * @return The selected author.
     */
    public Author getAuthor() {
        List<Author> authorList = authorController.listAuthors();
        System.out.println("Lista de autores:");
        for (int i = 0; i < authorList.size(); i++) {
            System.out.println((i + 1) + ". " + authorList.get(i).getName());
        }
        System.out.print("Digite o número do autor desejado: ");
        int selection = scanner.nextInt();
        scanner.nextLine(); // consume the remaining newline character
        if (selection < 1 || selection > authorList.size()) {
            System.out.println("Seleção inválida!");
            return null;
        }
        return authorList.get(selection - 1);
    }

    /**
     * Prompts the user to enter the number of pages of the book.
     *
     * @return The number of pages of the book.
     */
    private int getNumPages() {
        System.out.print("Número de páginas: ");
        return Integer.parseInt(scanner.nextLine());
    }

    /**
     * Prompts the user to enter the quantity of books.
     *
     * @return The quantity of books.
     */
    private int getQuantity() {
        System.out.print("Número de livros: ");
        return Integer.parseInt(scanner.nextLine());
    }

    /**
     * Displays the list of categories and prompts the user to select a category.
     *
     * @return The selected category.
     */
    public Category getCategory() {
        List<Category> categoryList = categoryController.listCategories();
        System.out.println("Lista de categorias:");
        for (int i = 0; i < categoryList.size(); i++) {
            System.out.println((i + 1) + ". " + categoryList.get(i).getCategoryName());
        }
        System.out.print("Digite o número da categoria desejada: ");
        int selection = scanner.nextInt();
        scanner.nextLine(); // consume the remaining newline character
        if (selection < 1 || selection > categoryList.size()) {
            System.out.println("Seleção inválida!");
            return null;
        }
        return categoryList.get(selection - 1);
    }

    /**
     * Prompts the user to enter the publication date of the book.
     *
     * @return The publication date of the book.
     */
    private LocalDate getPublicationDate() {
        System.out.print("Data de publicação (dd/mm/aaaa): ");
        String publicationDateStr = scanner.nextLine();
        return LocalDate.parse(publicationDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /**
     * Displays the list of age ranges and prompts the user to select an age range.
     *
     * @return The selected age range.
     */
    public AgeRange getAgeRange() {
        List<AgeRange> ageRangeList = ageRangeController.listAgeRanges();
        System.out.println("Lista de faixas etárias:");
        for (int i = 0; i < ageRangeList.size(); i++) {
            System.out.println((i + 1) + ". " + ageRangeList.get(i).getDescription());
        }
        System.out.print("Digite o número da faixa etária desejada: ");
        int selection = scanner.nextInt();
        scanner.nextLine();
        if (selection < 1 || selection > ageRangeList.size()) {
            System.out.println("Seleção inválida!");
            return null;
        }
        return ageRangeList.get(selection - 1);
    }

    /**
     * Displays the list of publishers and prompts the user to select a publisher.
     *
     * @return The selected publisher.
     */
    public Publisher getPublisher() {
        List<Publisher> publisherList = publisherController.listPublishers();
        System.out.println("Lista de editoras:");
        for (int i = 0; i < publisherList.size(); i++) {
            System.out.println((i + 1) + ". " + publisherList.get(i).getName());
        }
        System.out.print("Digite o número da editora desejada: ");
        int selection = scanner.nextInt();
        scanner.nextLine();
        if (selection < 1 || selection > publisherList.size()) {
            System.out.println("Seleção inválida!");
            return null;
        }
        return publisherList.get(selection - 1);
    }

    /**
     * Prompts the user to enter the ISBN of the book.
     *
     * @return The ISBN of the book.
     */
    private String getIsbn() {
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();

        try {
            validateIsbn(isbn);
        } catch (IllegalArgumentException e) {
            System.out.println("Entrada inválida: " + e.getMessage());
            return getIsbn(); // Prompt again if validation fails
        }

        return isbn;
    }

    /**
     * Validates the ISBN format.
     *
     * @param isbn The ISBN to validate.
     * @throws IllegalArgumentException if the ISBN is invalid.
     */
    private void validateIsbn(String isbn) {
        if (isbn.length() != 13) {
            throw new IllegalArgumentException("O ISBN deve ter 13 dígitos.");
        }

        try {
            Long.parseLong(isbn);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("O ISBN deve ser um número válido.");
        }
    }
}
