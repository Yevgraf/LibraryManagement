/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import Controller.*;
import View.*;
import java.util.Scanner;

/**
 *
 * @author franc
 */
public class Authentication {

    private String username;
    private String password;

    public boolean login() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o seu username:");
        username = scanner.nextLine();
        System.out.println("Digite a sua password:");
        password = scanner.nextLine();

        if (username.equals("bibliotecario") && password.equals("livro")) {
            System.out.println("Login bem-sucedido!");
            return true;
        } else {
            System.out.println("Credenciais inválidas. Tente novamente.");
            return false;
        }
    }
}
