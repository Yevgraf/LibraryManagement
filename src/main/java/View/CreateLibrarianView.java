package View;

import java.time.LocalDate;
import java.util.Scanner;

import Controller.LibrarianController;
import Model.Librarian;

public class CreateLibrarianView {
    private Scanner input;
    private LibrarianController controller;

    public CreateLibrarianView(LibrarianController controller) {
        input = new Scanner(System.in);
        this.controller = controller;
    }

    public Librarian createLibrarian() {
        System.out.println("Criar novo utilizador");
        System.out.println("--------------------");

        System.out.print("Nome: ");
        String name = input.nextLine();

        System.out.print("Morada: ");
        String address = input.nextLine();

        System.out.print("Data de nascimento (AAAA-MM-DD): ");
        String birthDateStr = input.nextLine();
        LocalDate birthDate = LocalDate.parse(birthDateStr);

        System.out.print("Telefone: ");
        String phone = input.nextLine();

        System.out.print("Email: ");
        String email = input.nextLine();

        System.out.print("Password: ");
        String password = input.nextLine();

        return new Librarian(name, address, birthDate, phone, email, password);
    }


    public void displayLibrarian(Librarian librarian) {
        System.out.println("Bibliotecário criado:");
        System.out.println(librarian);
    }
}
