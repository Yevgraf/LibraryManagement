package Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import Data.LibrarianData;
import Model.Librarian;

public class LibrarianController {
    private LibrarianData librarianData;

    public LibrarianController(LibrarianData librarianData) {
        this.librarianData = librarianData;
    }

    public void createLibrarian(String name, String address, LocalDate birthDate, String phone, String email, String password) {
        Scanner scanner = new Scanner(System.in);

        while (name == null || name.isEmpty()) {
            System.out.print("Digite o nome do bibliotecário: ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Nome não pode ser vazio");
            }
        }

        while (address == null || address.isEmpty()) {
            System.out.print("Digite o endereço do bibliotecário: ");
            address = scanner.nextLine().trim();
            if (address.isEmpty()) {
                System.out.println("Endereço não pode ser vazio");
            }
        }

        while (birthDate == null || birthDate.isAfter(LocalDate.now())) {
            System.out.print("Digite a data de nascimento do bibliotecário (no formato DD/MM/AAAA): ");
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String input = scanner.nextLine().trim();
                birthDate = LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Data de nascimento inválida");
            }
        }

        while (phone == null || !Pattern.matches("^(9[1236]\\d{7})$", phone)) {
            System.out.print("Digite o telefone do bibliotecário (no formato 9XXXXXXXX): ");
            phone = scanner.nextLine().trim();
            if (!Pattern.matches("^(9[1236]\\d{7})$", phone)) {
                System.out.println("Telefone inválido");
            }
        }


        while (email == null || !Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", email)) {
            System.out.print("Digite o email do bibliotecário: ");
            email = scanner.nextLine().trim();
            if (!Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", email)) {
                System.out.println("Email inválido");
            }
        }

        while (password == null || !Pattern.matches("^[^\\s]{0,16}$", password)) {
            System.out.print("Digite a senha do bibliotecário (máximo 16 caracteres e sem espaços): ");
            password = scanner.nextLine().trim();
            if (!Pattern.matches("^[^\\s]{0,16}$", password)) {
                System.out.println("Senha inválida");
            }
        }

        // Verificar se já existe um bibliotecário com o mesmo email antes de criar um novo bibliotecário com esse email
        List<Librarian> librarianList = librarianData.load();
        final String emailToCheck = email;
        if (librarianList.stream().anyMatch(librarian -> librarian.getEmail().equals(emailToCheck))) {
            System.out.println("Já existe um bibliotecário com esse email");
            return;
        }


        Librarian librarian = new Librarian(name, address, birthDate, phone, email, password);
        librarianList.add(librarian);
        librarianData.save(librarianList);

        System.out.println("Bibliotecário registrado com sucesso!");
    }

    public static boolean login(String email, String password) {
        List<Librarian> userList = LibrarianData.load();

        for (Librarian user : userList) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return true;
            }
        }

        return false;
    }

    public void deleteLibrarian() {
        List<Librarian> librarianList = librarianData.load();

        System.out.print("Digite o email do bibliotecário: ");
        Scanner scanner = new Scanner(System.in);
        String email = scanner.nextLine().trim();

        List<Librarian> matchingLibrarians = librarianList.stream()
                .filter(librarian -> librarian.getEmail().equalsIgnoreCase(email))
                .collect(Collectors.toList());

        if (matchingLibrarians.isEmpty()) {
            System.out.println("Não foi encontrado nenhum bibliotecário com esse email.");
            return;
        }

        System.out.println("Bibliotecários encontrados: ");
        for (int i = 0; i < matchingLibrarians.size(); i++) {
            System.out.println((i + 1) + ". " + matchingLibrarians.get(i).getName());
        }

        System.out.print("Digite o número do bibliotecário que deseja remover: ");
        int selection = scanner.nextInt();

        if (selection < 1 || selection > matchingLibrarians.size()) {
            System.out.println("Seleção inválida.");
            return;
        }

        Librarian librarianToRemove = matchingLibrarians.get(selection - 1);
        librarianData.deleteLibrarian(librarianToRemove.getEmail());

    }


    public List<Librarian> listLibrarians() {
        return librarianData.load();
    }
}
