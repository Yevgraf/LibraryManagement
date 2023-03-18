
package View;

import java.util.Scanner;

import Controller.AuthorController;
import Data.AuthorData;

public class AuthorMenu {
    private Scanner scanner;
    private AuthorController authorController;
    private CreateAuthorView createAuthorView;
    private MainMenu mainMenu;

    public AuthorMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        scanner = new Scanner(System.in);
        AuthorData authorData = new AuthorData();
        authorController = new AuthorController(authorData);
        createAuthorView = new CreateAuthorView(authorController);
    }

    public void start() {
        while (true) {
            System.out.println("Escolha uma opção:");
            System.out.println("1. Criar autor");
            System.out.println("2. Listar autores");
            System.out.println("3. Voltar");

            int option = scanner.nextInt();
            scanner.nextLine(); // consume new line

            switch (option) {
                case 1:
                    createAuthorView.createAuthor();
                    break;
                case 2:
                    createAuthorView.listAuthors();
                    break;
                case 3:
                    System.out.println("Voltando...");
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}