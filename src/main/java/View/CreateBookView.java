package View;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    private Author getAuthor() {
        System.out.println("Autor:");
        System.out.println("-- Escreva 1 para listar --");
        String authorName = scanner.nextLine();
        Author author;
        if (authorName.equals("1")) {
            authorController.listAuthorsView();
            System.out.print("Nome do autor: ");
            authorName = scanner.nextLine();
            author = authorController.findByName(authorName);
        } else {
            author = authorController.findByName(authorName);
            if (author == null) {
                System.out.println("Não foi encontrado nenhum autor com o nome '" + authorName + "'.");
                return getAuthor();
            }
        }
        return author;
    }

    private int getNumPages() {
        System.out.print("Número de páginas: ");
        return Integer.parseInt(scanner.nextLine());
    }

    private Category getCategory() {
        System.out.println("Categoria:");
        System.out.println("-- Escreva 1 para listar --");
        String categoryName = scanner.nextLine();
        Category category;
        if (categoryName.equals("1")) {
            categoryController.listCategoriesView();
            System.out.print("Nome da categoria: ");
            categoryName = scanner.nextLine();
            category = categoryController.findByName(categoryName);
        } else {
            category = categoryController.findByName(categoryName);
            if (category == null) {
                System.out.println("Não foi encontrada nenhuma categoria com o nome '" + categoryName + "'.");
                return getCategory();
            }
        }
        return category;
    }

    private LocalDate getPublicationDate() {
        System.out.print("Data de publicação (dd/mm/aaaa): ");
        String publicationDateStr = scanner.nextLine();
        return LocalDate.parse(publicationDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private AgeRange getAgeRange() {
        System.out.println("Faixa etária:");
        System.out.println("-- Escreva 1 para listar --");
        String ageRangeName = scanner.nextLine();
        AgeRange ageRange;
        if (ageRangeName.equals("1")) {
            ageRangeController.listAgeRangesView();
            System.out.print("Faixa etária: ");
            ageRangeName = scanner.nextLine();
            ageRange = ageRangeController.findByDescription(ageRangeName);
        } else {
            ageRange = ageRangeController.findByDescription(ageRangeName);
            if (ageRange == null) {
                System.out.println("Não foi encontrada nenhuma faixa etária com o nome '" + ageRangeName + "'.");
                return getAgeRange();
            }
        }
        return ageRange;
    }

    private Publisher getPublisher() {
        System.out.println("Editora:");
        System.out.println("-- Escreva 1 para listar --");
        String publisherName = scanner.nextLine();
        Publisher publisher;
        if (publisherName.equals("1")) {
            publisherController.listPublishersView();
            System.out.print("Nome da editora: ");
            publisherName = scanner.nextLine();
            publisher = publisherController.findByName(publisherName);
        } else {
            publisher = publisherController.findByName(publisherName);
            if (publisher == null) {
                System.out.println("Não foi encontrada nenhuma editora com o nome '" + publisherName + "'.");
                return getPublisher();
            }
        }
        return publisher;
    }

    private String getIsbn() {
        System.out.print("ISBN: ");
        return scanner.nextLine();
    }


}
