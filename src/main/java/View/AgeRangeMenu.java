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

    public AgeRangeMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        scanner = new Scanner(System.in);
        AgeRangeData ageRangeData = new AgeRangeData();
        ageRangeController = new AgeRangeController(ageRangeData);
        createAgeRangeView = new CreateAgeRangeView(ageRangeController);
    }

    public void start() {
        while (true) {
            System.out.println("Escolha uma opção:");
            System.out.println("1. Criar faixa etária");
            System.out.println("2. Listar faixas etárias");
            System.out.println("3. Voltar");

            int option = scanner.nextInt();
            scanner.nextLine();

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
