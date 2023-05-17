package View;

import Controller.*;
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
    private AuthorController authorController;
    private AgeRangeController ageRangeController;
    private CategoryController categoryController;
    private PublisherController publisherController;
    private ReservationController reservationController;


    public BookMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        scanner = new Scanner(System.in);
        dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        BookData bookData = new BookData();
        AuthorData authorData = new AuthorData();
        AgeRangeData ageRangeData = new AgeRangeData();
        CategoryData categoryData = new CategoryData();
        PublisherData publisherData = new PublisherData();
        ReservationData reservationData = new ReservationData();
        MemberData memberData = new MemberData();
        bookController = new BookController(bookData, authorData, ageRangeData, categoryData, publisherData, scanner);
        authorController = new AuthorController(authorData);
        ageRangeController = new AgeRangeController(ageRangeData);
        categoryController = new CategoryController(categoryData);
        publisherController = new PublisherController(publisherData);
        createBookView = new CreateBookView(bookController, authorController, ageRangeController, categoryController, publisherController, scanner);
        reservationController = new ReservationController(reservationData, memberData, bookData);

    }

    public void displayMenu() {
        int choice = -1;
        while (choice != 0) {
            System.out.println("==== Livros ====");
            System.out.println("1. Adicionar livro");
            System.out.println("2. Listar livros");
            System.out.println("3. Remover livro");
            System.out.println("4. Procurar livro");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createBookView.createBook();
                    break;
                case 2:
                    listBooks();
                    break;
                case 3:
                    removeBook();
                    break;
                case 4:
                    SearchBooksView searchBooksView = new SearchBooksView(bookController, scanner);
                    searchBooksView.searchBooks();
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
        System.out.print("Digite o nome do livro que deseja remover: ");
        String name = scanner.nextLine();

        if (reservationController.isBookBorrowed(name)) {
            System.out.println("Este livro não pode ser removido, pois está atualmente emprestado ou reservado.");
        } else if (bookController.removeBook(name)) {
            System.out.println("Livro removido com sucesso.");
        } else {
            System.out.println("Não foi possível remover o livro.");
        }
    }



}
