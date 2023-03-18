package View;

import Model.Author;

import java.time.LocalDate;
import java.util.Scanner;

public class CreateAuthorView {
    private Scanner input;

    public CreateAuthorView() {
        input = new Scanner(System.in);
    }

    public Author createAuthor() {
        System.out.println("Criar novo autor");
        System.out.println("--------------------");

        System.out.print("Nome: ");
        String name = input.nextLine();

        System.out.print("Morada: ");
        String address = input.nextLine();

        System.out.print("Data de nascimento (AAAA-MM-DD): ");
        String birthDateStr = input.nextLine();
        LocalDate birthDate = LocalDate.parse(birthDateStr);

        Author author = new Author(name, address, birthDate);
        System.out.println("Autor criado: " + author);

        return author;
    }

    public void displayAuthor(Author author) {
        System.out.println("Autor criado:");
        System.out.println(author);
    }
}
