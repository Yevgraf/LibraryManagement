package Controller;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
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
        List<Member> memberList = listMembers();
        Member member = new Member(name, address, birthDate, phone, email);
        CardController cardController = new CardController(new CardData());
        String cardNumber = cardController.generateCardNumber(member.getId());
        Card card = cardController.createCard(member, cardNumber);
        member.setCard(card);
        memberList.add(member);
        memberData.save(memberList);
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
