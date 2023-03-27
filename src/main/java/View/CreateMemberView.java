package View;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import Model.Member;

public class CreateMemberView {
    private Scanner scanner;

    public CreateMemberView() {
        scanner = new Scanner(System.in);
    }

    public Member getMemberInfo() {
        System.out.println("--- Cadastrar Novo Membro ---");
        System.out.print("Nome completo: ");
        String name = capitalizeWords(scanner.nextLine());

        System.out.print("Endereço: ");
        String address = capitalizeWords(scanner.nextLine());

        LocalDate birthDate = null;
        boolean isValidDate = false;
        while (!isValidDate) {
            try {
                System.out.print("Data de Nascimento (yyyy-mm-dd): ");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                birthDate = LocalDate.parse(scanner.nextLine(), formatter);
                isValidDate = true;
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida. Tente novamente.");
            }
        }

        System.out.print("Telefone: ");
        String phone = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        Member member = new Member(name, address, birthDate, phone, email);

        return member;
    }

    private String capitalizeWords(String str) {
        StringBuilder sb = new StringBuilder();
        String[] words = str.split("\\s");
        for (String word : words) {
            if (word.length() > 0) {
                sb.append(Character.toUpperCase(word.charAt(0)));
                sb.append(word.substring(1).toLowerCase());
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }

}
