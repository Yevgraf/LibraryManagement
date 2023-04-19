package Controller;
import java.time.LocalDate;
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
         // Validations
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do membro inválido.");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Endereço do membro inválido.");
        }
        if (birthDate == null) {
            throw new IllegalArgumentException("Data de nascimento inválida.");
        }
        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de nascimento inválida.");
        }
        if (phone == null || !phone.matches("\\d{10,11}")) {
            throw new IllegalArgumentException("Número de telefone inválido.");
        }
        if (email == null || !isValidEmail(email)) {
            throw new IllegalArgumentException("Endereço de email inválido.");
        }

        List<Member> memberList = listMembers();
        Member member = new Member(name, address, birthDate, phone, email);
        CardController cardController = new CardController(new CardData());
        String cardNumber = cardController.generateCardNumber(member.getId());
        Card card = cardController.createCard(member, cardNumber);
        member.setCard(card);
        memberList.add(member);
        memberData.save(memberList);
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
            System.out.println("ID: " + member.getId() + ", Nome: " + member.getName() + ", Telefone: " + member.getPhone() + ", Email: " + member.getEmail());
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
