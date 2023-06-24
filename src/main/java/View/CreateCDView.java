package View;

import Controller.ArtistController;
import Controller.CDController;
import Model.Artist;
import Model.Category;

import java.util.List;
import java.util.Scanner;

/**
 * The CreateCDView class represents the view for creating a new CD.
 */
public class CreateCDView {
    private CDController cdController;
    private ArtistController artistController;
    private Scanner scanner;

    /**
     * Constructs a CreateCDView object.
     *
     * @param cdController      the CDController object
     * @param artistController  the ArtistController object
     * @param scanner           the Scanner object for user input
     */
    public CreateCDView(CDController cdController, ArtistController artistController, Scanner scanner) {
        this.cdController = cdController;
        this.artistController = artistController;
        this.scanner = scanner;
    }

    /**
     * Prompts the user to enter the details of a new CD and creates it.
     * Displays appropriate messages based on the outcome.
     */
    public void createCD() {
        System.out.println("Criar novo CD:");
        System.out.print("Título: ");
        String title = scanner.nextLine();

        List<Artist> artists = artistController.listArtists();
        System.out.println("Artistas disponíveis:");
        for (int i = 0; i < artists.size(); i++) {
            Artist artist = artists.get(i);
            System.out.println((i + 1) + ". " + artist.getName());
        }

        System.out.print("Artista (escolha o número): ");
        int artistIndex = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        if (artistIndex >= 1 && artistIndex <= artists.size()) {
            Artist selectedArtist = artists.get(artistIndex - 1);

            System.out.print("Ano de lançamento: ");
            String releaseYearInput = scanner.nextLine();
            int releaseYear;

            try {
                releaseYear = Integer.parseInt(releaseYearInput);
            } catch (NumberFormatException e) {
                System.out.println("Ano de lançamento inválido. Operação cancelada.");
                return;
            }

            System.out.print("Número de faixas: ");
            int numTracks = scanner.nextInt();
            scanner.nextLine();

            List<Category> categories = cdController.listCategories();
            System.out.println("Categorias disponíveis:");
            for (int i = 0; i < categories.size(); i++) {
                Category category = categories.get(i);
                System.out.println((i + 1) + ". " + category.getCategoryName());
            }

            System.out.print("Categoria (escolha o número): ");
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();

            if (categoryIndex >= 1 && categoryIndex <= categories.size()) {
                Category selectedCategory = categories.get(categoryIndex - 1);

                System.out.print("Quantidade: ");
                int quantity = scanner.nextInt();
                scanner.nextLine();

                try {
                    cdController.createCD(title, selectedArtist, releaseYear, numTracks, selectedCategory, quantity);
                } catch (IllegalArgumentException e) {
                    System.out.println("Erro ao criar o CD: " + e.getMessage());
                }
            } else {
                System.out.println("Categoria inválida. Operação cancelada.");
            }
        } else {
            System.out.println("Artista inválido. Operação cancelada.");
        }
    }
}
