/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import java.util.Scanner;

public class LogInView {
    private final Scanner input;

    public LogInView() {
        input = new Scanner(System.in);
    }

    public void displayLogin() {
        System.out.println("LOGIN");
        System.out.println("-----");

        System.out.print("Utilizador: ");
        String user = input.nextLine();

        System.out.print("Password: ");
        String password = input.nextLine();

        if (checkCredentials(user, password)) {
            System.out.println("Login realizado com sucesso!");
        } else {
            System.out.println("Utilizador ou Password incorretos.");
        }
    }

    private boolean checkCredentials(String user, String password) {
        
        return true;
    }

    public static void main(String[] args) {
        LogInView loginView = new LogInView();
        loginView.displayLogin();
    }
}
