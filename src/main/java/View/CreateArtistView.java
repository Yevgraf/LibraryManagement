package View;

import Controller.ArtistController;
import Model.Artist;

import java.util.List;
import java.util.Scanner;

public class CreateArtistView {
    private Scanner scanner;
    private ArtistController artistController;

    public CreateArtistView(ArtistController controller) {
        scanner = new Scanner(System.in);
        artistController = controller;
    }

    /**
     * Prompts the user to enter the name of a new artist and creates it using the artistController.
     */
    public void createArtist() {
        String name = getNameInput();

        try {
            artistController.createArtist(name);
            System.out.println("Artista criado e guardado com sucesso.");
        } catch (IllegalArgumentException e) {
            System.out.println("Entrada inválida: " + e.getMessage());
        }
    }

    /**
     * Prompts the user to enter the name of the artist.
     * Keeps prompting until a non-empty name is entered.
     *
     * @return The name of the artist.
     */
    private String getNameInput() {
        String name;
        do {
            System.out.print("Nome: ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("O nome não pode estar vazio.");
            }
        } while (name.isEmpty());
        return name;
    }

    /**
     * Lists all the artists using the artistController.
     */
    public void listArtists() {
        List<Artist> artists = artistController.listArtists();
        for (Artist artist : artists) {
            System.out.println(artist);
        }
    }

    /**
     * Starts the artist menu loop, allowing the user to create artists, list artists, or go back.
     * The loop continues until the user chooses to go back.
     */
    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("Escolha uma opção:");
            System.out.println("1. Criar artista");
            System.out.println("2. Listar artistas");
            System.out.println("3. Voltar");

            int option = scanner.nextInt();
            scanner.nextLine(); // consume new line

            switch (option) {
                case 1:
                    createArtist();
                    break;
                case 2:
                    listArtists();
                    break;
                case 3:
                    System.out.println("Voltar...");
                    running = false;
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
        MainMenu mainMenu = new MainMenu();
        mainMenu.displayMenu();
    }
}

