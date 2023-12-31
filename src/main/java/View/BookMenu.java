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
        CDData cdData = new CDData();  // Instantiate CDData
        bookController = new BookController(bookData, authorData, ageRangeData, categoryData, publisherData, scanner);
        authorController = new AuthorController(authorData);
        ageRangeController = new AgeRangeController(ageRangeData);
        categoryController = new CategoryController(categoryData);
        publisherController = new PublisherController(publisherData);
        CDController cdController = new CDController(cdData, categoryData);  // Instantiate CDController
        createBookView = new CreateBookView(bookController, authorController, ageRangeController, categoryController, publisherController, scanner);
        reservationController = new ReservationController(reservationData, memberData, bookData, cdData);  // Pass cdData to ReservationController
    }

    /**
     * Displays the book menu and handles user input.
     */
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

    /**
     * Lists all the books in the database.
     */
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

    /**
     * Removes a book from the database.
     */
    public void removeBook() {
        List<Book> bookList = bookController.listBooks();

        if (bookList.isEmpty()) {
            System.out.println("Nenhum livro encontrado na base de dados.");
            return;
        }

        displayBookList(bookList);

        int selectedIndex = getSelectedBookIndex();
        if (selectedIndex >= 1 && selectedIndex <= bookList.size()) {
            Book selectedBook = bookList.get(selectedIndex - 1);
            boolean removed = bookController.removeBook(selectedBook);
            if (removed) {
                showSuccessMessage(selectedBook.getTitle());
            } else {
                showFailureMessage(selectedBook.getTitle());
            }
        } else {
            System.out.println("Índice inválido. Operação cancelada.");
        }
    }

    /**
     * Displays the list of books.
     *
     * @param bookList The list of books to display.
     */
    public void displayBookList(List<Book> bookList) {
        if (bookList.isEmpty()) {
            System.out.println("Nenhum livro encontrado na base de dados.");
            return;
        }

        System.out.println("Lista de Livros:");
        for (int i = 0; i < bookList.size(); i++) {
            Book book = bookList.get(i);
            System.out.println((i + 1) + ". " + book.getTitle());
        }
    }

    /**
     * Gets the index of the book selected by the user.
     *
     * @return The index of the selected book.
     */
    public int getSelectedBookIndex() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Insira o índice do livro: ");

        int selectedIndex = -1;
        while (selectedIndex < 1) {
            if (scanner.hasNextInt()) {
                selectedIndex = scanner.nextInt();
                if (selectedIndex < 1) {
                    System.out.println("Índice inválido. Insira um valor maior que zero.");
                }
            } else {
                System.out.println("Entrada inválida. Insira um número inteiro correspondente ao índice do livro.");
                scanner.next();
            }
        }
        return selectedIndex;
    }

    /**
     * Displays a success message for book removal.
     *
     * @param bookTitle The title of the removed book.
     */
    public void showSuccessMessage(String bookTitle) {
        System.out.println("O livro \"" + bookTitle + "\" foi removido com sucesso.");
    }

    /**
     * Displays a failure message for book removal.
     *
     * @param bookTitle The title of the book that failed to be removed.
     */
    public void showFailureMessage(String bookTitle) {
        System.out.println("Falha ao remover o livro \"" + bookTitle + "\".");
    }

}
