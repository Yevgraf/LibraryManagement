package View;

import Controller.CDController;
import Model.CD;

import java.util.List;
import java.util.Scanner;

public class SearchCDsView {
    private final CDController cdController;
    private final Scanner scanner;

    public SearchCDsView(CDController cdController, Scanner scanner) {
        this.cdController = cdController;
        this.scanner = scanner;
    }

    public void searchCDs() {
        int option = -1;
        while (option != 0) {
            System.out.println("Escolha uma opção de busca:");
            System.out.println("1 - Procurar por ID ou título");
            System.out.println("2 - Procurar por categoria");
            System.out.println("3 - Procurar por artista");
            System.out.println("0 - Voltar");

            option = scanner.nextInt();
            scanner.nextLine(); // consume newline character

            switch (option) {
                case 1:
                    searchCDByIdOrTitle();
                    break;
                case 2:
                    searchCDsByCategory();
                    break;
                case 3:
                    searchCDsByArtist();
                    break;
                case 0:
                    System.out.println("Voltar ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida, tente novamente");
                    break;
            }
        }
    }

    private void searchCDByIdOrTitle() {
        System.out.println("Digite o título ou ID do CD para procurar:");
        String searchTerm = scanner.nextLine();

        CD result = cdController.findCDByIdOrTitle(searchTerm);

        if (result == null) {
            System.out.println("CD não encontrado");
        } else {
            System.out.println("CD encontrado:");
            System.out.println(result);
        }
    }

    private void searchCDsByCategory() {
        System.out.println("Categorias:");

        List<CD> result = cdController.searchCDsByCategory();

        if (result.isEmpty()) {
            System.out.println("Nenhum CD encontrado nesta categoria.");
        } else {
            for (CD cd : result) {
                System.out.println(cd);
            }
        }
    }

    private void searchCDsByArtist() {
        System.out.println("Artistas:");

        List<CD> result = cdController.searchCDsByArtist();

        if (result.isEmpty()) {
            System.out.println("Nenhum CD encontrado para este artista.");
        } else {
            for (CD cd : result) {
                System.out.println(cd);
            }
        }
    }
}
