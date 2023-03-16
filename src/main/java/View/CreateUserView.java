package View;

import java.time.LocalDate;
import java.util.Scanner;

import Model.User;

public class CreateUserView {
    private Scanner input;

    public CreateUserView() {
        input = new Scanner(System.in);
    }

    public User createUser() {
        System.out.println("Criar novo utilizador");
        System.out.println("--------------------");

        System.out.print("Nome: ");
        String name = input.nextLine();

        System.out.print("Morada: ");
        String address = input.nextLine();

        System.out.print("Data de nascimento (AAAA-MM-DD): ");
        String birthDateStr = input.nextLine();
        LocalDate birthDate = LocalDate.parse(birthDateStr);

        System.out.print("Telefone: ");
        String phone = input.nextLine();

        System.out.print("Email: ");
        String email = input.nextLine();

        System.out.print("Password: ");
        String password = input.nextLine();

        User user = new User(name, address, birthDate, phone, email, password);
        System.out.println("Utilizador criado: " + user);



        return user;
    }
    public void displayUser(User user) {
        System.out.println("Utilizador criado:");
        System.out.println(user);
    }
}
