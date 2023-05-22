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
        if (isInvalidInput(name, address, birthDate, phone, email)) {
            System.out.println("Os dados de membro são inválidos. Não foi possível criar o membro.");
            return;
        }

        Member member = new Member(name, address, birthDate, phone, email);
        boolean success = saveMember(member);

        if (success) {
            // Retrieve the saved member to ensure it has an ID
            Member savedMember = getMemberByEmail(member.getEmail());

            CardController cardController = new CardController(new CardData());
            String cardNumber = cardController.generateCardNumber(savedMember.getId());
            Card card = cardController.createCard(savedMember, cardNumber);
            savedMember.setCard(card);

            System.out.println("Membro criado e guardado com sucesso.");
        } else {
            System.out.println("Ocorreu um erro ao salvar o membro. Não foi possível criar o cartão.");
        }
    }

    public Member getMemberByEmail(String email) {
        List<Member> members = listMembers();
        for (Member member : members) {
            if (member.getEmail().equalsIgnoreCase(email)) {
                return member;
            }
        }
        return null;
    }


    private boolean isInvalidInput(String name, String address, LocalDate birthDate, String phone, String email) {
        return name.isEmpty() || address.isEmpty() || birthDate == null || phone.isEmpty() || email.isEmpty();
    }

    private boolean saveMember(Member member) {
        List<Member> memberList = listMembers();
        if (memberList.contains(member)) {
            System.out.println("Utilizador já existe: " + member.getEmail());
            return false;
        }

        memberList.add(member);
        memberData.save(memberList);
        return true;
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