package View;

import java.util.Scanner;

public class MainMenu {
    private Scanner scanner;
    private AuthorMenu authorMenu;
    private LibrarianMenu librarianMenu;

    public MainMenu() {
        scanner = new Scanner(System.in);
        authorMenu = new AuthorMenu(this);
        librarianMenu = new LibrarianMenu(this);
    }

    public void displayMenu() {
        int option = 0;
        do {
            System.out.println("Bem-vindo à biblioteca!");
            System.out.println("Selecione uma opção:");
            System.out.println("1 - Gestão de autores");
            System.out.println("2 - Gestão de bibliotecários");
            System.out.println("3 - Gestão de editoras");
            System.out.println("4 - Gestão de categorias");
            System.out.println("5 - Reserva de livros");
            System.out.println("0 - Sair");

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    authorMenu.start();
                    break;
                case 2:
                    librarianMenu.start();
                    break;
                case 3:
                    // chamar gestao de editoras
                    break;
                case 4:
                    // chamar gestao categorias
                    break;
                case 5:
                    // chamar gestao reserva
                    break;
                case 0:
                    System.out.println("Até logo!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }

        } while (option != 0);
    }
}
