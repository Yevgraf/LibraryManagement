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
import Controller.AgeRangeController;


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

        bookController.createBook(title, subtitle, author.getName(), numPages, category.getCategoryName(), publicationDate, ageRange.getDescription(), publisher.getName(), isbn);
    }

    private String getTitle() {
        System.out.print("Título: ");
        return scanner.nextLine();
    }

    private String getSubtitle() {
        System.out.print("Subtítulo: ");
        return scanner.nextLine();
    }

    public Author getAuthor() {
        List<Author> authorList = authorController.listAuthors();
        System.out.println("Lista de autores:");
        for (int i = 0; i < authorList.size(); i++) {
            System.out.println((i+1) + ". " + authorList.get(i).getName());
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


    private int getNumPages() {
        System.out.print("Número de páginas: ");
        return Integer.parseInt(scanner.nextLine());
    }

    public Category getCategory() {
        List<Category> categoryList = categoryController.listCategories();
        System.out.println("Lista de categorias:");
        for (int i = 0; i < categoryList.size(); i++) {
            System.out.println((i+1) + ". " + categoryList.get(i).getCategoryName());
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


    private LocalDate getPublicationDate() {
        System.out.print("Data de publicação (dd/mm/aaaa): ");
        String publicationDateStr = scanner.nextLine();
        return LocalDate.parse(publicationDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public AgeRange getAgeRange() {
        List<AgeRange> ageRangeList = ageRangeController.listAgeRanges();
        System.out.println("Lista de faixas etárias:");
        for (int i = 0; i < ageRangeList.size(); i++) {
            System.out.println((i+1) + ". " + ageRangeList.get(i).getDescription());
        }
        System.out.print("Digite o número da faixa etária desejada: ");
        int selection = scanner.nextInt();
        scanner.nextLine(); // consume the remaining newline character
        if (selection < 1 || selection > ageRangeList.size()) {
            System.out.println("Seleção inválida!");
            return null;
        }
        return ageRangeList.get(selection - 1);
    }


    public Publisher getPublisher() {
        List<Publisher> publisherList = publisherController.listPublishers();
        System.out.println("Lista de editoras:");
        for (int i = 0; i < publisherList.size(); i++) {
            System.out.println((i+1) + ". " + publisherList.get(i).getName());
        }
        System.out.print("Digite o número da editora desejada: ");
        int selection = scanner.nextInt();
        scanner.nextLine(); // consume the remaining newline character
        if (selection < 1 || selection > publisherList.size()) {
            System.out.println("Seleção inválida!");
            return null;
        }
        return publisherList.get(selection - 1);
    }



    private String getIsbn() {
        System.out.print("ISBN: ");
        return scanner.nextLine();
    }

}
