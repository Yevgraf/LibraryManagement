package View;

import java.util.Scanner;

import Controller.CategoryController;

/**
 * The CreateCategoryView class represents the view for creating a new category.
 */
public class CreateCategoryView {
    private Scanner scanner;
    private CategoryController categoryController;

    /**
     * Constructs a CreateCategoryView object.
     *
     * @param controller the CategoryController object
     */
    public CreateCategoryView(CategoryController controller) {
        scanner = new Scanner(System.in);
        categoryController = controller;
    }

    /**
     * Prompts the user to enter the name of the category and creates it.
     * Displays appropriate messages based on the outcome.
     */
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

    /**
     * Validates the name of the category.
     *
     * @param categoryName the name of the category to validate
     * @throws IllegalArgumentException if the category name is empty
     */
    private void validateCategoryName(String categoryName) {
        if (categoryName.isEmpty()) {
            throw new IllegalArgumentException("O nome da categoria não pode estar vazio.");
        }
    }

}
