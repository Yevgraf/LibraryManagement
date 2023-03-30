package View;

import java.util.Scanner;

import Controller.PublisherController;

public class CreatePublisherView {
    private Scanner scanner;
    private PublisherController publisherController;

    public CreatePublisherView(PublisherController publisherController) {
        scanner = new Scanner(System.in);
        this.publisherController = publisherController;
    }

    public void createPublisher() {
        System.out.print("Nome: ");
        String name = scanner.nextLine();

        System.out.print("Morada: ");
        String address = scanner.nextLine();

        publisherController.createPublisher(name, address);

        System.out.println("Editora criada com sucesso.");
    }
}
