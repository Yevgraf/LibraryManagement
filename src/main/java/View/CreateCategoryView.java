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

        categoryController.createCategory(categoryName);
        System.out.println("Categoria criada e guardada com sucesso.");
    }
}
