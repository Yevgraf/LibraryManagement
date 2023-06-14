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

    public void createArtist() {
        String name = getNameInput();

        try {
            artistController.createArtist(name);
            System.out.println("Artista criado e guardado com sucesso.");
        } catch (IllegalArgumentException e) {
            System.out.println("Entrada inválida: " + e.getMessage());
        }
    }

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

    public void listArtists() {
        List<Artist> artists = artistController.listArtists();
        for (Artist artist : artists) {
            System.out.println(artist);
        }
    }
}
