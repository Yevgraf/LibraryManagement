package View;

import java.util.Scanner;

import Controller.PublisherController;

public class CreatePublisherView {
    private Scanner scanner;
    private PublisherController publisherController;

    /**
     * Cria uma instância de CreatePublisherView.
     *
     * @param publisherController o controlador de editoras associado à view
     */
    public CreatePublisherView(PublisherController publisherController) {
        scanner = new Scanner(System.in);
        this.publisherController = publisherController;
    }

    /**
     * Executa o processo de criação de uma nova editora.
     */
    public void createPublisher() {
        String name = getNameInput();
        String address = getAddressInput();

        if (name != null && address != null) {
            publisherController.createPublisher(name, address);
            System.out.println("Editora criada com sucesso.");
        } else {
            System.out.println("Entrada inválida. Certifique-se de preencher todos os campos corretamente.");
        }
    }

    private String getNameInput() {
        System.out.print("Nome: ");
        String name = scanner.nextLine().trim();
        return !name.isEmpty() ? name : null;
    }

    private String getAddressInput() {
        System.out.print("Morada: ");
        String address = scanner.nextLine().trim();
        return !address.isEmpty() ? address : null;
    }
}
