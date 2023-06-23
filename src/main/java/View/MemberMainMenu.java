package View;

import Controller.LibrarianController;
import Data.LibrarianData;
import Data.MemberData;
import Model.Member;
import Model.Reservation;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static Data.MemberData.*;

public class MemberMainMenu {
    private Member member;

    public MemberMainMenu(Member member) {
        this.member = member;
    }
    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("---- Member Main Menu ----");
            System.out.println("1. Visualizar histórico de reservas");
            System.out.println("2. Ver respostas aos formulários de satisfação");
            System.out.println("0. Sair");
            System.out.println("-------------------------");
            System.out.println("Escolha uma opção:");

            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (option) {
                case 1:
                    displayReservationHistory(member);
                    break;
                case 2:
                    displaySatisfactionFormResponses(member);
                    break;
                case 0:
                    LoginView login = new LoginView(new LibrarianController(new LibrarianData()));
                    login.login();
                    break;
                default:
                    System.out.println("Opção inválida. Por favor, tente novamente.");
            }
        }

        displayMainMenu();
    }

    private void displayReservationHistory(Member member) {
        System.out.println("Histórico de Reservas para o Membro: " + member.getName());

        List<String> currentlyReservedItems = loadReservedItemsByMemberId(member.getId());
        List<String> previouslyReservedItems = loadPreviousReservedItemsByMemberId(member.getId());

        System.out.println("Itens Atualmente Reservados:");
        for (String item : currentlyReservedItems) {
            System.out.println(item);
        }

        System.out.println();
        System.out.println("Itens Reservados Anteriormente:");
        for (String item : previouslyReservedItems) {
            System.out.println(item);
        }
    }

    private void displaySatisfactionFormResponses(Member member) {
        System.out.println("Formulários de Satisfação para o Membro: " + member.getName());

        List<Reservation> reservations = loadSatisfactionFormResponsesByMemberId(member.getId());

        for (Reservation reservation : reservations) {
            System.out.println("ID da Reserva: " + reservation.getId());
            System.out.println("Data de Início: " + reservation.getStartDate());
            System.out.println("Data de Término: " + reservation.getEndDate());
            System.out.println("Classificação de Satisfação: " + reservation.getSatisfactionRating());
            System.out.println("Comentários Adicionais: " + reservation.getAdditionalComments());
            System.out.println("-------------------------");
        }
    }

    private void displayMainMenu() {
        System.out.println("---- Menu Principal do Membro ----");
        System.out.println("1. Visualizar Histórico de Reservas");
        System.out.println("2. Ver Respostas aos Formulários de Satisfação");
        System.out.println("0. Sair");
        System.out.println("-------------------------");
        System.out.println("Escolha uma opção:");
    }

}
