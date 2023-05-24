package View;

import java.util.Scanner;

import Controller.CategoryController;

public class CreateCategoryView {
    private Scanner scanner;
    private CategoryController categoryController;

    public CreateCategoryView(CategoryController controller) {
        scanner = new Scanner(System.in);
        categoryController = controller;
    }

    public void createCategory() {
        System.out.print("Nome da categoria: ");
        String categoryName = scanner.nextLine();

        try {
            validateCategoryName(categoryName);

            categoryController.createCategory(categoryName);
            System.out.println("Categoria criada e guardada com sucesso.");
        } catch (IllegalArgumentException e) {
            System.out.println("Entrada inválida: " + e.getMessage());
        }
    }

    private void validateCategoryName(String categoryName) {
        if (categoryName.isEmpty()) {
            throw new IllegalArgumentException("O nome da categoria não pode estar vazio.");
        }
    }

}
