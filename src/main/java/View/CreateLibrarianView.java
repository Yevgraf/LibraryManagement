package View;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import Controller.LibrarianController;
import Model.Librarian;

public class CreateLibrarianView {
    private Scanner scanner;
    private LibrarianController librarianController;

    /**
     * Cria uma instância de CreateLibrarianView.
     *
     * @param controller o controlador de bibliotecários associado à view
     */
    public CreateLibrarianView(LibrarianController controller) {
        scanner = new Scanner(System.in);
        librarianController = controller;
    }

    /**
     * Executa o processo de criação de um novo bibliotecário.
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

    private void validateBirthDate(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        if (birthDate.isAfter(currentDate)) {
            throw new IllegalArgumentException("Data de nascimento inválida.");
        }
    }

    private void validatePhone(String phone) {
        if (!Pattern.matches("^(9[1236]\\d{7})$", phone)) {
            throw new IllegalArgumentException("Telefone inválido. Deve ter 9 dígitos começando com 91, 92, 93, 96 ou 96.");
        }
    }

    /**
     * Lista os bibliotecários cadastrados.
     */
    public void listLibrarians() {
        List<Librarian> librarians = librarianController.listLibrarians();
        for (Librarian librarian : librarians) {
            System.out.println(librarian);
        }
    }
}
