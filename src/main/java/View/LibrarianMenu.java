package View;

import Controller.LibrarianController;
import Data.LibrarianData;
import Model.Librarian;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class LibrarianMenu {
    private Scanner scanner;
    private LibrarianController librarianController;
    private CreateLibrarianView createLibrarianView;
    private MainMenu mainMenu;

    /**
     * Cria uma instância de LibrarianMenu.
     *
     * @param mainMenu o menu principal associado ao menu do bibliotecário
     */
    public LibrarianMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        scanner = new Scanner(System.in);
        LibrarianData librarianData = new LibrarianData();
        librarianController = new LibrarianController(librarianData);
        createLibrarianView = new CreateLibrarianView(librarianController);
    }

    /**
     * Inicia o menu do bibliotecário.
     */
    public void start() {
        while (true) {
            System.out.println("Escolha uma opção:");
            System.out.println("1. Criar bibliotecário");
            System.out.println("2. Listar bibliotecários");
            System.out.println("3. Remover bibliotecário");
            System.out.println("4. Voltar");

            int option = scanner.nextInt();
            scanner.nextLine(); // consumir nova linha

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
