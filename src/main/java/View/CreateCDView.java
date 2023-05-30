package View;

import Controller.CDController;
import Model.Category;

import java.util.List;
import java.util.Scanner;

public class CreateCDView {
    private CDController cdController;
    private Scanner scanner;

    public CreateCDView(CDController cdController, Scanner scanner) {
        this.cdController = cdController;
        this.scanner = scanner;
    }

    public void createCD() {
        System.out.println("Criar novo CD:");
        System.out.print("Título: ");
        String title = scanner.nextLine();
        System.out.print("Artista: ");
        String artist = scanner.nextLine();
        System.out.print("Ano de lançamento: ");
        int releaseYear = scanner.nextInt();
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

            try {
                cdController.createCD(title, artist, releaseYear, numTracks, selectedCategory.getCategoryName(), quantity);
            } catch (IllegalArgumentException e) {
                System.out.println("Erro ao criar o CD: " + e.getMessage());
            }
        } else {
            System.out.println("Categoria inválida. Operação cancelada.");
        }
    }

}
