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

    /**
     * Creates a new member with the given information and saves it.
     * Generates a random password for the member and sends it to their email address.
     * Additionally, creates a card for the member and associates it with their account.
     * If any error occurs during the process, appropriate error messages are displayed.
     *
     * @param name      The name of the member.
     * @param address   The address of the member.
     * @param birthDate The birth date of the member.
     * @param phone     The phone number of the member.
     * @param email     The email address of the member.
     */
    public void createMember(String name, String address, LocalDate birthDate, String phone, String email) {
        if (isInvalidInput(name, address, birthDate, phone, email)) {
            System.out.println("Os dados de membro são inválidos. Não foi possível criar o membro.");
            return;
        }

        // Generate a random password for the member
        String password = generateRandomPassword();

        Member member = new Member(name, address, birthDate, phone, email, password);
        boolean success = saveMember(member);

        if (success) {
            // Retrieve the saved member to ensure it has an ID
            Member savedMember = getMemberByEmail(member.getEmail());

            CardController cardController = new CardController(new CardData());
            String cardNumber = cardController.generateCardNumber(savedMember.getId());
            Card card = cardController.createCard(savedMember, cardNumber);
            savedMember.setCard(card);

            EmailController.sendEmail(savedMember.getEmail(), "Senha da sua conta", "Prezado(a) membro,\n\nA senha da sua conta é: " + password + "\n\nPor favor, mantenha-a em segurança.\n\nAtenciosamente,\nA BiblioSMF");
            System.out.println("Membro criado e guardado com sucesso. A senha foi enviada para o email do membro.");
        } else {
            System.out.println("Ocorreu um erro ao salvar o membro. Não foi possível criar o cartão.");
        }
    }

    /**
     * Generates a random password with a length of 12 characters.
     *
     * @return The randomly generated password.
     */
    private String generateRandomPassword() {
        // gera password random com tamanho 12
        int length = 12;
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * allowedChars.length());
            password.append(allowedChars.charAt(randomIndex));
        }

        return password.toString();
    }

    /**
     * Retrieves a member with the given email address from the list of members.
     *
     * @param email The email address of the member to retrieve.
     * @return The member object if found, or null if not found.
     */
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



    public List<Member> listMembers() {
        return memberData.load();
    }

}