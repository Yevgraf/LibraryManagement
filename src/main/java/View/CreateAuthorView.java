package View;

import Controller.AuthorController;
import Model.Author;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class CreateAuthorView {
    private Scanner scanner;
    private AuthorController authorController;

    public CreateAuthorView(AuthorController controller) {
        scanner = new Scanner(System.in);
        authorController = controller;
    }

    public void createAuthor() {
        System.out.print("Nome: ");
        String name = scanner.nextLine();

        System.out.print("Morada: ");
        String address = scanner.nextLine();

        System.out.print("Data de nascimento (DD/MM/AAAA): ");
        String birthDateStr = scanner.nextLine();
        LocalDate birthDate = LocalDate.parse(birthDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        authorController.createAuthor(name, address, birthDate);
        System.out.println("Autor criado e guardado com sucesso.");
    }

    public void listAuthors() {
        List<Author> authors = authorController.listAuthors();
        for (Author author : authors) {
            System.out.println(author);
        }
    }
}
