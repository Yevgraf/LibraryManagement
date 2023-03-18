package View;

import Controller.LibrarianController;
import Data.LibrarianData;
import Model.Librarian;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class LibrarianMenu {
    private CreateLibrarianView createLibrarianView;
    private LibrarianController librarianController;

    private LibrarianData librarianData;
    private Scanner scanner;

    public LibrarianMenu() {
        scanner = new Scanner(System.in);
        librarianData = new LibrarianData();
        librarianController = new LibrarianController(librarianData);
        createLibrarianView = new CreateLibrarianView(librarianController);
    }

    public void start() {
        while (true) {
            System.out.println("Escolha uma opção:");
            System.out.println("1. Criar bibliotecário");
            System.out.println("2. Listar bibliotecários");
            System.out.println("3. Sair");

            int option = scanner.nextInt();
            scanner.nextLine(); // consume new line

            switch (option) {
                case 1:
                    createLibrarian();
                    break;
                case 2:
                    listLibrarians();
                    break;
                case 3:
                    System.out.println("Saindo...");
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private void createLibrarian() {
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

    private void listLibrarians() {
        List<Librarian> librarians = librarianController.listLibrarians();
        for (Librarian librarian : librarians) {
            System.out.println(librarian);
        }
    }
}
