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

    /**
     * Cria uma instância da classe CreateAuthorView.
     *
     * @param controller O controlador de autores.
     */
    public CreateAuthorView(AuthorController controller) {
        scanner = new Scanner(System.in);
        authorController = controller;
    }

    /**
     * Permite a criação de um novo autor.
     * Solicita o nome, endereço e data de nascimento do autor ao usuário.
     * Chama o controlador para criar o autor correspondente.
     * Exibe uma mensagem de sucesso ou de erro, caso ocorra uma exceção.
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
     * Obtém a entrada do usuário para o nome do autor.
     * Valida se o nome não está vazio e solicita novamente caso esteja.
     *
     * @return O nome do autor inserido pelo usuário.
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
     * Obtém a entrada do usuário para o endereço do autor.
     * Valida se o endereço não está vazio e solicita novamente caso esteja.
     *
     * @return O endereço do autor inserido pelo usuário.
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
     * Obtém a entrada do usuário para a data de nascimento do autor.
     * Valida se o formato da data está correto (DD/MM/AAAA) e solicita novamente caso esteja incorreto.
     *
     * @return A data de nascimento do autor inserida pelo usuário.
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
     * Valida se a data de nascimento é anterior à data atual.
     * Caso seja posterior, lança uma exceção com uma mensagem de erro.
     *
     * @param dateOfBirth A data de nascimento a ser validada.
     * @throws IllegalArgumentException Se a data de nascimento for inválida.
     */
    private void validateDateOfBirth(LocalDate dateOfBirth) {
        LocalDate currentDate = LocalDate.now();
        if (dateOfBirth.isAfter(currentDate)) {
            throw new IllegalArgumentException("Data de nascimento inválida.");
        }
    }

    /**
     * Lista todos os autores existentes.
     * Obtém a lista de autores do controlador e os exibe no console.
     */
    public void listAuthors() {
        List<Author> authors = authorController.listAuthors();
        for (Author author : authors) {
            System.out.println(author);
        }
    }
}
