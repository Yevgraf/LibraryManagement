package View;

import Controller.BookController;
import Controller.MemberController;
import Controller.ReservationController;
import Model.Book;
import Model.Member;

import java.util.Scanner;

public class ReservationMenu {
    private Scanner scanner;
    private ReservationController reservationController;
    private CreateReservationView createReservationView;
    private MainMenu mainMenu;

    public ReservationMenu(MainMenu mainMenu, MemberController memberController, BookController bookController, ReservationController reservationController) {
        this.mainMenu = mainMenu;
        scanner = new Scanner(System.in);
        this.reservationController = reservationController;
        createReservationView = new CreateReservationView(memberController, bookController, reservationController, scanner);
    }

    public void start() {
        while (true) {
            System.out.println("Escolha uma opção:");
            System.out.println("1. Criar reserva");
            System.out.println("2. Cancelar reserva");
            System.out.println("3. Listar reservas");
            System.out.println("4. Voltar");

            int option = scanner.nextInt();
            scanner.nextLine(); // consume new line

            try {
                switch (option) {
                    case 1:
                        createReservationView.createReservation();
                        break;
                    case 2:
                      // createReservationView.cancelReservation();
                        break;
                    case 3:
                        createReservationView.listAllReservations();
                        break;
                    case 4:
                        System.out.println("Voltando...");
                        mainMenu.displayMenu();
                        return;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
