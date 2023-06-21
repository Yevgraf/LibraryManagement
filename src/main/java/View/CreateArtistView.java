package View;

import Controller.ArtistController;
import Model.Artist;

import java.util.List;
import java.util.Scanner;

public class CreateArtistView {
    private Scanner scanner;
    private ArtistController artistController;

    /**
     * Cria uma instância da classe CreateArtistView.
     *
     * @param controller O controlador de artistas.
     */
    public CreateArtistView(ArtistController controller) {
        scanner = new Scanner(System.in);
        artistController = controller;
    }

    /**
     * Permite a criação de um novo artista.
     * Solicita o nome do artista ao usuário e chama o controlador para criar o artista correspondente.
     * Exibe uma mensagem de sucesso ou de erro, caso ocorra uma exceção.
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
     * Obtém a entrada do usuário para o nome do artista.
     * Valida se o nome não está vazio e solicita novamente caso esteja.
     *
     * @return O nome do artista inserido pelo usuário.
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
     * Lista todos os artistas existentes.
     * Obtém a lista de artistas do controlador e os exibe no console.
     */
    public void listArtists() {
        List<Artist> artists = artistController.listArtists();
        for (Artist artist : artists) {
            System.out.println(artist);
        }
    }

    /**
     * Inicia o menu de criação de artistas.
     * Exibe um menu com opções para criar artistas, listar artistas ou voltar ao menu principal.
     * Solicita a escolha do usuário e realiza a ação correspondente.
     * Ao escolher "Voltar", retorna ao menu principal.
     */
    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("Escolha uma opção:");
            System.out.println("1. Criar artista");
            System.out.println("2. Listar artistas");
            System.out.println("3. Voltar");

            int option = scanner.nextInt();
            scanner.nextLine(); // consumir nova linha

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
