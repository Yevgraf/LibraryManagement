package View;

import java.util.List;
import java.util.Scanner;

import Controller.PublisherController;
import Data.PublisherData;
import Model.Publisher;

/**
 * A classe PublisherMenu representa o menu de opções relacionadas às editoras de livros.
 */
public class PublisherMenu {
    private Scanner scanner;
    private PublisherController publisherController;
    private CreatePublisherView createPublisherView;
    private MainMenu mainMenu;

    /**
     * Construtor da classe PublisherMenu.
     * Inicializa o scanner, o controlador de editoras, a visualização de criação de editoras e o menu principal.
     * @param mainMenu O menu principal da aplicação.
     */
    public PublisherMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        scanner = new Scanner(System.in);
        PublisherData publisherData = new PublisherData();
        publisherController = new PublisherController(publisherData);
        createPublisherView = new CreatePublisherView(publisherController);
    }

    /**
     * Inicia o menu de editoras.
     * Exibe as opções disponíveis e permite ao usuário interagir com elas.
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
     * Lista todas as editoras cadastradas.
     */
    private void listPublishers() {
        List<Publisher> publishers = publisherController.listPublishers();
        System.out.println("Lista de editoras:");
        for (Publisher publisher : publishers) {
            System.out.println(publisher.toString());
        }
    }
}
