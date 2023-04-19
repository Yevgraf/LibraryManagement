/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import Data.LibrarianData;
import Model.Librarian;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author franc
 */
public class Authentication {

    public boolean login() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o seu e-mail:");
        String Email = scanner.nextLine();
        System.out.println("Digite a sua password:");
        String password = scanner.nextLine();

        List<Librarian> librarians = (List<Librarian>) LibrarianData.load();

        for (Librarian librarian : librarians) {
            if (librarian.getEmail().equals(Email) && librarian.getPassword().equals(password)) {
                System.out.println("Login bem-sucedido!");
                return true;
            }
        }

        System.out.println("Credenciais inválidas. Tente novamente.");
        return false;
    }
}
