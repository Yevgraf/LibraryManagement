package Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import Data.CardData;
import Data.MemberData;
import Model.Card;
import Model.Member;

public class MemberController {
    private MemberData memberData;
    private Scanner scanner;

    public MemberController(MemberData memberData, Scanner scanner) {
        this.memberData = memberData;
        this.scanner = scanner;
    }

    public void createMember(String name, String address, LocalDate birthDate, String phone, String email) {
        Scanner scanner = new Scanner(System.in);

        while (name == null || name.isEmpty()) {
            System.out.print("Digite o nome do membro: ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Nome não pode ser vazio");
            }
        }

        while (address == null || address.isEmpty()) {
            System.out.print("Digite o endereço do membro: ");
            address = scanner.nextLine().trim();
            if (address.isEmpty()) {
                System.out.println("Endereço não pode ser vazio");
            }
        }

        while (birthDate == null || birthDate.isAfter(LocalDate.now())) {
            System.out.print("Digite a data de nascimento do membro (no formato DD/MM/AAAA): ");
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String input = scanner.nextLine().trim();
                birthDate = LocalDate.parse(input, formatter);

            } catch (DateTimeParseException e) {
                System.out.println("Data de nascimento inválida");
            }
        }

        while (phone == null || !Pattern.matches("^(9[1236]\\d{7})$", phone)) {
            System.out.print("Digite o telefone do membro (no formato 9XXXXXXXX): ");
            phone = scanner.nextLine().trim();
            if (!Pattern.matches("^(9[1236]\\d{7})$", phone)) {
                System.out.println("Número de telefone inválido");
            }
        }

        while (email == null || !isValidEmail(email)) {
            System.out.print("Digite o email do membro: ");
            email = scanner.nextLine().trim();
            if (!isValidEmail(email)) {
                System.out.println("Endereço de email inválido");
            }
        }

        List<Member> memberList = listMembers();
        Member member = new Member(name, address, birthDate, phone, email);
        CardController cardController = new CardController(new CardData());
        String cardNumber = cardController.generateCardNumber(member.getId());
        Card card = cardController.createCard(member, cardNumber);
        member.setCard(card);
        memberList.add(member);
        memberData.save(memberList);

        System.out.println("Membro registrado com sucesso!");
    }


    // Validation methods
    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public Member getMemberByName(String name) {
        List<Member> members = listMembers();
        List<Member> matchingMembers = members.stream()
                .filter(member -> member.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        if (matchingMembers.isEmpty()) {
            return null;
        }
        if (matchingMembers.size() == 1) {
            return matchingMembers.get(0);
        }
        System.out.println("Há mais de um membro com esse nome, selecione o ID correto:");
        for (Member member : matchingMembers) {
            System.out.println("ID: " + member.getId() + ", Nome: " + member.getName() + ", Telefone: "
                    + member.getPhone() + ", Email: " + member.getEmail());
        }
        int selectedId = scanner.nextInt();
        scanner.nextLine();
        return matchingMembers.stream()
                .filter(member -> member.getId() == selectedId)
                .findFirst()
                .orElse(null);
    }

    public List<Member> listMembers() {
        return memberData.load();
    }

}