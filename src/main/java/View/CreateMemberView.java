package View;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Controller.MemberController;
import Model.Member;

public class CreateMemberView {
    private Scanner scanner;
    private MemberController memberController;

    /**
     * Cria uma instância de CreateMemberView.
     *
     * @param controller o controlador de membros associado à view
     */
    public CreateMemberView(MemberController controller) {
        scanner = new Scanner(System.in);
        memberController = controller;
    }

    /**
     * Executa o processo de criação de um novo membro.
     */
    public void createMember() {
        String name = enterMemberName();
        String address = enterMemberAddress();
        LocalDate birthDate = enterMemberBirthDate();
        String phone = enterMemberPhone();
        String email = enterMemberEmail();

        memberController.createMember(name, address, birthDate, phone, email);
    }

    private String enterMemberName() {
        String name = null;
        while (name == null || name.isEmpty()) {
            System.out.print("Nome: ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Nome não pode ser vazio.");
            }
        }
        return name;
    }

    private String enterMemberAddress() {
        String address = null;
        while (address == null || address.isEmpty()) {
            System.out.print("Morada: ");
            address = scanner.nextLine().trim();
            if (address.isEmpty()) {
                System.out.println("Morada não pode ser vazia.");
            }
        }
        return address;
    }

    private LocalDate enterMemberBirthDate() {
        LocalDate birthDate = null;
        while (birthDate == null) {
            System.out.print("Data de nascimento (DD/MM/AAAA): ");
            String birthDateStr = scanner.nextLine().trim();
            try {
                birthDate = LocalDate.parse(birthDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                if (birthDate.isAfter(LocalDate.now())) {
                    System.out.println("Data de nascimento inválida. A data não pode ser posterior à data atual.");
                    birthDate = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data inválido. Utilize o formato DD/MM/AAAA.");
            }
        }
        return birthDate;
    }

    private String enterMemberPhone() {
        String phone = null;
        while (phone == null || !Pattern.matches("^(9[1236]\\d{7})$", phone)) {
            System.out.print("Telefone: ");
            phone = scanner.nextLine().trim();
            if (!Pattern.matches("^(9[1236]\\d{7})$", phone)) {
                System.out.println("Número de telefone inválido. Utilize o formato 9XXXXXXXX.");
            }
        }
        return phone;
    }

    private String enterMemberEmail() {
        String email = null;
        while (email == null || !isValidEmail(email)) {
            System.out.print("Email: ");
            email = scanner.nextLine().trim();
            if (!isValidEmail(email)) {
                System.out.println("Endereço de email inválido.");
            }
        }
        return email;
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Lista os membros cadastrados.
     */
    public void listMembers() {
        List<Member> members = memberController.listMembers();
        for (Member member : members) {
            System.out.println(member);
        }
    }
}
