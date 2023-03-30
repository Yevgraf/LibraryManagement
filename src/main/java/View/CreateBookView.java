package View;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import Controller.*;
import Data.AgeRangeData;
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

        System.out.print("Título: ");
        String title = scanner.nextLine();

        System.out.print("Subtítulo: ");
        String subtitle = scanner.nextLine();

        System.out.print("Nome do autor: ");
        String authorName = scanner.nextLine();
        Author author = authorController.findByName(authorName);
        if (author == null) {
            System.out.println("Não foi encontrado nenhum autor com o nome '" + authorName + "'.");
            return;
        }

        System.out.print("Número de páginas: ");
        int numPages = Integer.parseInt(scanner.nextLine());

        System.out.print("Nome da categoria: ");
        String categoryName = scanner.nextLine();
        Category category = categoryController.findByName(categoryName);
        if (category == null) {
            System.out.println("Não foi encontrada nenhuma categoria com o nome '" + categoryName + "'.");
            return;
        }

        System.out.print("Data de publicação (dd/mm/aaaa): ");
        String publicationDateStr = scanner.nextLine();
        LocalDate publicationDate = LocalDate.parse(publicationDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));


        System.out.print("Faixa etária: ");
        String ageRangeName = scanner.nextLine();
        AgeRangeController ageRangeController = new AgeRangeController(new AgeRangeData());


        AgeRange ageRange = ageRangeController.findByDescription(ageRangeName);
        if (ageRange == null) {
            System.out.println("Não foi encontrada nenhuma faixa etária com o nome '" + ageRangeName + "'.");
            return;
        }

        System.out.print("Nome da editora: ");
        String publisherName = scanner.nextLine();
        Publisher publisher = publisherController.findByName(publisherName);
        if (publisher == null) {
            System.out.println("Não foi encontrada nenhuma editora com o nome '" + publisherName + "'.");
            return;
        }

        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();

        bookController.createBook(title, subtitle, author.getName(), numPages, category.getCategoryName(), publicationDate, ageRange.getDescription(), publisher.getName(), isbn);
    }

}
