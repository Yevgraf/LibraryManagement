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
        System.out.println("--- Register New Member ---");
        System.out.print("Full Name: ");
        String name = capitalizeWords(scanner.nextLine());

        System.out.print("Address: ");
        String address = capitalizeWords(scanner.nextLine());

        LocalDate birthDate = null;
        boolean isValidDate = false;
        while (!isValidDate) {
            try {
                System.out.print("Date of Birth (dd/MM/yyyy): ");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                birthDate = LocalDate.parse(scanner.nextLine(), formatter);

                // Check that the member is at least 6 years old
                LocalDate sixYearsAgo = LocalDate.now().minusYears(6);
                if (birthDate.isBefore(sixYearsAgo)) {
                    isValidDate = true;
                } else {
                    System.out.println("The member must be at least 6 years old.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please try again.");
            }
        }

        System.out.print("Phone: ");
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
