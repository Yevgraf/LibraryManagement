package View;

import java.util.List;
import java.util.Scanner;

import Controller.PublisherController;
import Data.PublisherData;
import Model.Publisher;

public class PublisherMenu {
    private Scanner scanner;
    private PublisherController publisherController;
    private CreatePublisherView createPublisherView;
    private MainMenu mainMenu;

    /**
     * Constructs a new instance of the PublisherMenu class with the specified MainMenu.
     * @param mainMenu The main menu.
     */
    public PublisherMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        scanner = new Scanner(System.in);
        PublisherData publisherData = new PublisherData();
        publisherController = new PublisherController(publisherData);
        createPublisherView = new CreatePublisherView(publisherController);
    }

    /**
     * Starts the publisher menu and handles user input.
     */
    public void start() {
        while (true) {
            System.out.println("Escolha uma opção:");
            System.out.println("1. Criar editora");
            System.out.println("2. Listar editoras");
            System.out.println("3. Voltar");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    createPublisherView.createPublisher();
                    break;
                case 2:
                    listPublishers();
                    break;
                case 3:
                    System.out.println("Voltar...");
                    mainMenu.displayMenu();
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    /**
     * Lists all the publishers.
     */
    private void listPublishers() {
        List<Publisher> publishers = publisherController.listPublishers();
        System.out.println("Lista de editoras:");
        for (Publisher publisher : publishers) {
            System.out.println(publisher.toString());
        }
    }
}
