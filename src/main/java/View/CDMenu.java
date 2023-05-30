package View;

import Controller.CDController;
import Model.Category;

import java.util.Scanner;

public class CDMenu {
    private Scanner scanner;
    private CDController cdController;
    private CreateCDView createCDView;
    private MainMenu mainMenu;

    public CDMenu(MainMenu mainMenu, CDController cdController, Scanner scanner) {
        this.mainMenu = mainMenu;
        this.cdController = cdController;
        this.scanner = scanner;
        createCDView = new CreateCDView(cdController, scanner);
    }

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
