package View;

import java.util.List;
import java.util.Scanner;

import Controller.AgeRangeController;
import Data.AgeRangeData;
import Model.AgeRange;

public class AgeRangeMenu {
    private Scanner scanner;
    private AgeRangeController ageRangeController;
    private CreateAgeRangeView createAgeRangeView;
    private MainMenu mainMenu;

    /**
     * Cria um menu de faixas etárias.
     *
     * @param mainMenu O menu principal do programa.
     */
    public AgeRangeMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        scanner = new Scanner(System.in);
        AgeRangeData ageRangeData = new AgeRangeData();
        ageRangeController = new AgeRangeController(ageRangeData);
        createAgeRangeView = new CreateAgeRangeView(ageRangeController);
    }

    /**
     * Inicia o menu de faixas etárias.
     * Permanece em um loop contínuo até que o usuário decida voltar ao menu principal.
     */
    public void start() {
        while (true) {
            System.out.println("Escolha uma opção:");
            System.out.println("1. Criar faixa etária");
            System.out.println("2. Listar faixas etárias");
            System.out.println("3. Voltar");

            int option = scanner.nextInt();
            scanner.nextLine(); // Consumir o caractere de nova linha

            switch (option) {
                case 1:
                    createAgeRangeView.createAgeRange();
                    break;
                case 2:
                    createAgeRangeView.listAgeRanges();
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
}
