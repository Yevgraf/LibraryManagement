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

    /**
     * Prompts the user to enter the details of a new author and creates it using the authorController.
     */
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

    /**
     * Prompts the user to enter the name of the author.
     * Keeps prompting until a non-empty name is entered.
     *
     * @return The name of the author.
     */
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

    /**
     * Prompts the user to enter the address of the author.
     * Keeps prompting until a non-empty address is entered.
     *
     * @return The address of the author.
     */
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

    /**
     * Prompts the user to enter the date of birth of the author.
     * Keeps prompting until a valid date in the format "DD/MM/YYYY" is entered.
     *
     * @return The date of birth of the author.
     */
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

    /**
     * Validates the date of birth of the author.
     * Throws an IllegalArgumentException if the date of birth is after the current date.
     *
     * @param dateOfBirth The date of birth to validate.
     */
    private void validateDateOfBirth(LocalDate dateOfBirth) {
        LocalDate currentDate = LocalDate.now();
        if (dateOfBirth.isAfter(currentDate)) {
            throw new IllegalArgumentException("Data de nascimento inválida.");
        }
    }

    /**
     * Lists all the authors using the authorController.
     */
    public void listAuthors() {
        List<Author> authors = authorController.listAuthors();
        for (Author author : authors) {
            System.out.println(author);
        }
    }
}
