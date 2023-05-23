package View;

import Controller.AuthorController;
import Model.Author;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
        String name = getNameInput();
        String address = getAddressInput();
        LocalDate dateOfBirth = getDateOfBirthInput();

        try {
            validateDateOfBirth(dateOfBirth);

            authorController.createAuthor(name, address, dateOfBirth);
            System.out.println("Autor criado e guardado com sucesso.");
        } catch (IllegalArgumentException e) {
            System.out.println("Entrada inválida: " + e.getMessage());
        }
    }

    private String getNameInput() {
        String name;
        do {
            System.out.print("Nome: ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("O nome não pode estar vazio.");
            }
        } while (name.isEmpty());
        return name;
    }

    private String getAddressInput() {
        String address;
        do {
            System.out.print("Morada: ");
            address = scanner.nextLine().trim();
            if (address.isEmpty()) {
                System.out.println("A morada não pode estar vazia.");
            }
        } while (address.isEmpty());
        return address;
    }

    private LocalDate getDateOfBirthInput() {
        while (true) {
            try {
                System.out.print("Data de Nascimento (DD/MM/AAAA): ");
                String input = scanner.nextLine();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate dateOfBirth = LocalDate.parse(input, formatter);
                return dateOfBirth;
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data inválido. Por favor, insira a data no formato DD/MM/AAAA.");
            }
        }
    }


    private void validateDateOfBirth(LocalDate dateOfBirth) {
        LocalDate currentDate = LocalDate.now();
        if (dateOfBirth.isAfter(currentDate)) {
            throw new IllegalArgumentException("Data de nascimento inválida.");
        }
    }


    public void listAuthors() {
        List<Author> authors = authorController.listAuthors();
        for (Author author : authors) {
            System.out.println(author);
        }
    }
}
