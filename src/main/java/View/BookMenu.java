package View;

import Controller.BookController;
import Data.*;
import Model.Book;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class BookMenu {
    private Scanner scanner;
    private BookController bookController;
    private CreateBookView createBookView;
    private MainMenu mainMenu;
    private DateTimeFormatter dateFormatter;

    public BookMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        scanner = new Scanner(System.in);
        dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        BookData bookData = new BookData();
        AuthorData authorData = new AuthorData();
        AgeRangeData ageRangeData = new AgeRangeData();
        CategoryData categoryData = new CategoryData();
        PublisherData publisherData = new PublisherData();
        bookController = new BookController(bookData, authorData, ageRangeData, categoryData, publisherData, scanner);
        createBookView = new CreateBookView(bookController, scanner);
    }
    public void displayMenu() {
        int choice = -1;
        while (choice != 0) {
            System.out.println("==== Livros ====");
            System.out.println("1. Listar livros");
            System.out.println("2. Adicionar livro");
            System.out.println("3. Remover livro");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    listBooks();
                    break;
                case 2:
                    createBookView.createBook();
                    break;
                case 3:
                    removeBook();
                    break;
                case 0:
                    mainMenu.displayMenu();
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        }
    }

    private void listBooks() {
        List<Book> bookList = bookController.listBooks();
        if (bookList.isEmpty()) {
            System.out.println("Nenhum livro encontrado.");
        } else {
            System.out.println("==== Lista de livros ====");
            for (Book book : bookList) {
                System.out.println(book.toString());
            }
        }
    }

    private void removeBook() {
        System.out.print("Digite o ISBN ou nome do livro que deseja remover: ");
        String searchTerm = scanner.nextLine();
        Book book = bookController.findBookByIdOrName(searchTerm);
        if (book == null) {
            System.out.println("Livro não encontrado.");
        } else {
            bookController.removeBook(book);
            System.out.println("Livro removido com sucesso.");
        }
    }

}
