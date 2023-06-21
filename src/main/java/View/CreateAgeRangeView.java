package View;

import java.util.List;
import java.util.Scanner;

import Controller.AgeRangeController;
import Model.AgeRange;

public class CreateAgeRangeView {
    private Scanner scanner;
    private AgeRangeController ageRangeController;

    /**
     * Cria uma instância da classe CreateAgeRangeView.
     *
     * @param controller O controlador de faixas etárias.
     */
    public CreateAgeRangeView(AgeRangeController controller) {
        scanner = new Scanner(System.in);
        ageRangeController = controller;
    }

    /**
     * Permite a criação de uma nova faixa etária.
     * Solicita a descrição da faixa etária ao usuário e chama o controlador para criar a faixa etária correspondente.
     * Exibe uma mensagem de sucesso ou de erro, caso ocorra alguma exceção.
     */
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

    /**
     * Lista todas as faixas etárias existentes.
     * Obtém a lista de faixas etárias do controlador e as exibe no console.
     */
    public void listAgeRanges() {
        List<AgeRange> ageRanges = ageRangeController.listAgeRanges();
        for (AgeRange ageRange : ageRanges) {
            System.out.println(ageRange);
        }
    }
}
