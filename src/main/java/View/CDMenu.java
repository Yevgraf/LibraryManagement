package View;

import Controller.ArtistController;
import Controller.CDController;

import java.util.Scanner;

public class CDMenu {
    private Scanner scanner;
    private CDController cdController;
    private CreateCDView createCDView;

    private ArtistController artistController;
    private MainMenu mainMenu;

    /**
     * Cria um menu para CDs.
     *
     * @param mainMenu      O menu principal do programa.
     * @param cdController  O controlador de CDs.
     * @param scanner       O objeto Scanner para entrada de dados.
     */
    public CDMenu(MainMenu mainMenu, CDController cdController, Scanner scanner) {
        this.mainMenu = mainMenu;
        this.cdController = cdController;
        this.scanner = scanner;

        // Inicializa o objeto artistController
        this.artistController = new ArtistController();

        createCDView = new CreateCDView(cdController, artistController, scanner);
    }

    /**
     * Exibe o menu de CDs.
     * Permanece em um loop contínuo até que o usuário escolha a opção de voltar.
     */
    public void displayMenu() {
        int choice = -1;
        while (choice != 0) {
            System.out.println("==== CDs ====");
            System.out.println("1. Adicionar CD");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createCDView.createCD();
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
}
