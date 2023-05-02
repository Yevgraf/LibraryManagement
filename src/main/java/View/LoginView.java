package View;

import Controller.LibrarianController;

import java.util.Scanner;

public class LoginView {
    private final LibrarianController librarianController;

    public LoginView(LibrarianController librarianController) {
        this.librarianController = librarianController;
    }

    public void login() {
        Scanner scanner = new Scanner(System.in);
        boolean success = false;

        while (!success) {
            System.out.println("Por favor, insira seu email: ");
            String email = scanner.nextLine();

            System.out.println("Por favor, insira sua senha: ");
            String password = scanner.nextLine();

            success = librarianController.login(email, password);

            if (success) {
                System.out.println("Login bem sucedido!");
                MainMenu mainMenu = new MainMenu();
                mainMenu.displayMenu();
            } else {
                System.out.println("Email ou senha incorretos. Por favor, tente novamente.");
            }
        }
    }

}
