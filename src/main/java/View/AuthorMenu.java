package View;

import java.util.Scanner;

import Controller.AuthorController;
import Data.AuthorData;

public class AuthorMenu {
    private Scanner scanner;
    private AuthorController authorController;
    private CreateAuthorView createAuthorView;
    private MainMenu mainMenu;

    /**
     * Cria um menu de autores.
     *
     * @param mainMenu O menu principal do programa.
     */
    public AuthorMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        scanner = new Scanner(System.in);
        AuthorData authorData = new AuthorData();
        authorController = new AuthorController(authorData);
        createAuthorView = new CreateAuthorView(authorController);
    }

    /**
     * Inicia o menu de autores.
     * Permanece em um loop contínuo até que o usuário decida voltar ao menu principal.
     */
    public void start() {
        while (true) {
            System.out.println("Escolha uma opção:");
            System.out.println("1. Criar autor");
            System.out.println("2. Listar autores");
            System.out.println("3. Voltar");

            int option = scanner.nextInt();
            scanner.nextLine(); // Consome a nova linha após a leitura da opção do usuário

            switch (option) {
                case 1:
                    createAuthorView.createAuthor();
                    break;
                case 2:
                    createAuthorView.listAuthors();
                    break;
                case 3:
                    System.out.println("Voltar...");
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}
