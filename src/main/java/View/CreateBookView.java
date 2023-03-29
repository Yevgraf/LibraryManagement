package View;

import java.time.LocalDate;
import java.util.Scanner;

import Controller.BookController;

public class CreateBookView {
    private BookController bookController;
    private Scanner scanner;

    public CreateBookView(BookController bookController, Scanner scanner) {
        this.bookController = bookController;
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

        System.out.print("Número de páginas: ");
        int numPages = Integer.parseInt(scanner.nextLine());

        System.out.print("Nome da categoria: ");
        String categoryName = scanner.nextLine();

        System.out.print("Data de publicação (dd/mm/aaaa): ");
        String publicationDateStr = scanner.nextLine();
        LocalDate publicationDate = LocalDate.parse(publicationDateStr, BookController.DATE_FORMATTER);

        System.out.print("Faixa etária: ");
        String ageRangeName = scanner.nextLine();

        System.out.print("Nome da editora: ");
        String publisherName = scanner.nextLine();

        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();

        bookController.createBook(title, subtitle, authorName, numPages, categoryName, publicationDate, ageRangeName, publisherName, isbn);
    }
}
