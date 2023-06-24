package View;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import Controller.LibrarianController;
import Model.Librarian;

/**
 * The CreateLibrarianView class represents the view for creating a new librarian.
 */
public class CreateLibrarianView {
    private Scanner scanner;
    private LibrarianController librarianController;

    /**
     * Constructs a CreateLibrarianView object.
     *
     * @param controller the LibrarianController object
     */
    public CreateLibrarianView(LibrarianController controller) {
        scanner = new Scanner(System.in);
        librarianController = controller;
    }

    /**
     * Prompts the user to enter the details of a new librarian and creates it.
     * Displays appropriate messages based on the outcome.
     */
    public void createLibrarian() {
        String name = getNameInput();
        String address = getAddressInput();
        LocalDate birthDate = getBirthDateInput();
        String phone = getPhoneInput();
        String email = getEmailInput();
        String password = getPasswordInput();

        librarianController.createLibrarian(name, address, birthDate, phone, email, password);
        System.out.println("Bibliotecário criado e guardado com sucesso.");
    }

    /**
     * Retrieves the name input from the user.
     *
     * @return the name input
     */
    private String getNameInput() {
        String name = null;
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Nome: ");
            name = scanner.nextLine();
            if (!name.isEmpty()) {
                validInput = true;
            } else {
                System.out.println("O nome não pode estar vazio.");
            }
        }
        return name;
    }

    /**
     * Retrieves the address input from the user.
     *
     * @return the address input
     */
    private String getAddressInput() {
        String address = null;
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Morada: ");
            address = scanner.nextLine();
            if (!address.isEmpty()) {
                validInput = true;
            } else {
                System.out.println("A morada não pode estar vazia.");
            }
        }
        return address;
    }


    /**
     * Retrieves the birth date input from the user.
     *
     * @return the birth date input
     */
    private LocalDate getBirthDateInput() {
        LocalDate birthDate = null;
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Digite a data de nascimento do bibliotecário (no formato DD/MM/AAAA): ");
            String birthDateStr = scanner.nextLine();
            try {
                birthDate = LocalDate.parse(birthDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                validateBirthDate(birthDate);
                validInput = true;
            } catch (DateTimeParseException e) {
                System.out.println("Data de nascimento inválida. Digite no formato DD/MM/AAAA.");
            } catch (IllegalArgumentException e) {
                System.out.println("Data de nascimento inválida: " + e.getMessage());
            }
        }
        return birthDate;
    }

    /**
     * Retrieves the phone input from the user.
     *
     * @return the phone input
     */
    private String getPhoneInput() {
        String phone = null;
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Telefone: ");
            phone = scanner.nextLine().trim();
            if (Pattern.matches("^(9[1236]\\d{7})$", phone)) {
                validInput = true;
            } else {
                System.out.println("Telefone inválido. Deve ter 9 dígitos começam com 91, 92, 93, 96 ou 96.");
            }
        }
        return phone;
    }

    /**
     * Retrieves the email input from the user.
     *
     * @return the email input
     */
    private String getEmailInput() {
        String email = null;
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Email: ");
            email = scanner.nextLine().trim();
            if (Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", email)) {
                validInput = true;
            } else {
                System.out.println("Email inválido");
            }
        }
        return email;
    }

    /**
     * Retrieves the password input from the user.
     *
     * @return the password input
     */
    private String getPasswordInput() {
        String password = null;
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Password: ");
            password = scanner.nextLine();
            if (!password.isEmpty()) {
                validInput = true;
            } else {
                System.out.println("A password não pode estar vazia.");
            }
        }
        return password;
    }


    /**
     * Validates the birth date input.
     *
     * @param birthDate the birth date to validate
     * @throws IllegalArgumentException if the birth date is invalid
     */
    private void validateBirthDate(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        if (birthDate.isAfter(currentDate)) {
            throw new IllegalArgumentException("Data de nascimento inválida.");
        }
    }

    /**
     * Lists all librarians.
     */
    public void listLibrarians() {
        List<Librarian> librarians = librarianController.listLibrarians();
        for (Librarian librarian : librarians) {
            System.out.println(librarian);
        }
    }
}

