package View;

import java.util.List;
import java.util.Scanner;

import Controller.CategoryController;
import Data.CategoryData;
import Model.Category;

public class CategoryMenu {
    private Scanner scanner;
    private CategoryController categoryController;
    private CreateCategoryView createCategoryView;
    private MainMenu mainMenu;

    /**
     * Cria um menu para categorias.
     *
     * @param mainMenu O menu principal do programa.
     */
    public CategoryMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        scanner = new Scanner(System.in);
        CategoryData categoryData = new CategoryData();
        categoryController = new CategoryController(categoryData);
        createCategoryView = new CreateCategoryView(categoryController);
    }

    /**
     * Inicia o menu de categorias.
     * Permanece em um loop contínuo até que o usuário escolha a opção de voltar.
     */
    public void start() {
        while (true) {
            System.out.println("Escolha uma opção:");
            System.out.println("1. Criar categoria");
            System.out.println("2. Listar categorias");
            System.out.println("3. Voltar");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    createCategoryView.createCategory();
                    break;
                case 2:
                    listCategories();
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

    /**
     * Lista todas as categorias.
     */
    private void listCategories() {
        List<Category> categories = categoryController.listCategories();
        System.out.println("Lista de categorias:");
        for (Category category : categories) {
            System.out.println(category.toString());
        }
    }
}
