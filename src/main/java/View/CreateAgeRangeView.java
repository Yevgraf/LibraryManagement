package View;

import java.util.List;
import java.util.Scanner;

import Controller.AgeRangeController;
import Model.AgeRange;

public class CreateAgeRangeView {
    private Scanner scanner;
    private AgeRangeController ageRangeController;

    public CreateAgeRangeView(AgeRangeController controller) {
        scanner = new Scanner(System.in);
        ageRangeController = controller;
    }

    public void createAgeRange() {
        System.out.print("Descrição: ");
        String description = scanner.nextLine();

        try {
            ageRangeController.createAgeRange(description);
            System.out.println("Faixa etária criada e guardada com sucesso.");
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao criar a faixa etária: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void listAgeRanges() {
        List<AgeRange> ageRanges = ageRangeController.listAgeRanges();
        for (AgeRange ageRange : ageRanges) {
            System.out.println(ageRange);
        }
    }
}
