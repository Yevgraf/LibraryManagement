package View;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import Controller.MemberController;
import Model.Member;

public class CreateMemberView {
    private Scanner scanner;
    private MemberController memberController;

    public CreateMemberView(MemberController controller) {
        scanner = new Scanner(System.in);
        memberController = controller;
    }

    public void createMember() {
        System.out.print("Nome: ");
        String name = scanner.nextLine();

        System.out.print("Morada: ");
        String address = scanner.nextLine();

        System.out.print("Data de nascimento (AAAA-MM-DD): ");
        String birthDateStr = scanner.nextLine();
        LocalDate birthDate = LocalDate.parse(birthDateStr);

        System.out.print("Telefone: ");
        String phone = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        memberController.createMember(name, address, birthDate, phone, email);
        System.out.println("Membro criado e guardado com sucesso.");
    }

    public void listMembers() {
        List<Member> members = memberController.listMembers();
        for (Member member : members) {
            System.out.println(member);
        }
    }
}
