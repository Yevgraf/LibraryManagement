package View;

import Controller.LibrarianController;

import java.util.Scanner;

/**
 * A classe LoginView representa a interface de login do sistema.
 */
public class LoginView {
    private final LibrarianController librarianController;

    /**
     * Construtor da classe LoginView.
     *
     * @param librarianController o controlador do bibliotecário responsável pelo login.
     */
    public LoginView(LibrarianController librarianController) {
        this.librarianController = librarianController;
    }

    /**
     * Realiza o processo de login.
     */
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
