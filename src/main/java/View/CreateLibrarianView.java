package View;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import Controller.LibrarianController;
import Model.Librarian;

public class CreateLibrarianView {
    private Scanner scanner;
    private LibrarianController librarianController;

    public CreateLibrarianView(LibrarianController controller) {
        scanner = new Scanner(System.in);
        librarianController = controller;
    }

    public void createLibrarian() {
        System.out.print("Nome: ");
        String name = scanner.nextLine();

        System.out.print("Morada: ");
        String address = scanner.nextLine();

        System.out.print("Data de nascimento (AAAA-MM-DD): ");
        String birthDateStr = scanner.nextLine();
        LocalDate birthDate = LocalDate.parse(birthDateStr);

        System.out.print("Telefone: ");
        String phone = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        librarianController.createLibrarian(name, address, birthDate, phone, email, password);
        System.out.println("Bibliotecário criado e guardado com sucesso.");
    }

    public void listLibrarians() {
        List<Librarian> librarians = librarianController.listLibrarians();
        for (Librarian librarian : librarians) {
            System.out.println(librarian);
        }
    }
}
