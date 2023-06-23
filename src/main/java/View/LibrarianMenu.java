package View;

import Controller.LibrarianController;
import Data.LibrarianData;
import Model.Librarian;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * The view class for the librarian menu in the library system.
 */
public class LibrarianMenu {
    private Scanner scanner;
    private LibrarianController librarianController;
    private CreateLibrarianView createLibrarianView;
    private MainMenu mainMenu;

    /**
     * Constructs a new instance of the LibrarianMenu.
     *
     * @param mainMenu the main menu
     */
    public LibrarianMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        scanner = new Scanner(System.in);
        LibrarianData librarianData = new LibrarianData();
        librarianController = new LibrarianController(librarianData);
        createLibrarianView = new CreateLibrarianView(librarianController);
    }

    /**
     * Starts the librarian menu and handles user input.
     */
    public void start() {
        while (true) {
            System.out.println("Escolha uma opção:");
            System.out.println("1. Criar bibliotecário");
            System.out.println("2. Listar bibliotecários");
            System.out.println("3. Remover bibliotecário");
            System.out.println("4. Voltar");

            int option = scanner.nextInt();
            scanner.nextLine(); // consume new line

            switch (option) {
                case 1:
                    createLibrarianView.createLibrarian();
                    break;
                case 2:
                    createLibrarianView.listLibrarians();
                    break;
                case 3:
                    librarianController.deleteLibrarian();
                    break;
                case 4:
                    System.out.println("Voltar...");
                    mainMenu.displayMenu();
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

}
