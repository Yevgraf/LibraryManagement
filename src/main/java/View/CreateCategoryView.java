package View;

import java.util.Scanner;

import Controller.CategoryController;

public class CreateCategoryView {
    private Scanner scanner;
    private CategoryController categoryController;

    /**
     * Cria uma instância de CreateCategoryView.
     *
     * @param controller o controlador de categoria associado à view
     */
    public CreateCategoryView(CategoryController controller) {
        scanner = new Scanner(System.in);
        categoryController = controller;
    }

    /**
     * Executa o processo de criação de uma nova categoria.
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
     * Valida o nome da categoria fornecido.
     *
     * @param categoryName o nome da categoria a ser validado
     * @throws IllegalArgumentException se o nome da categoria estiver vazio
     */
    private void validateCategoryName(String categoryName) {
        if (categoryName.isEmpty()) {
            throw new IllegalArgumentException("O nome da categoria não pode estar vazio.");
        }
    }

}
