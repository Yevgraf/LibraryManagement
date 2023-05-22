package View;

import Controller.BookController;
import Controller.MemberController;
import Controller.ReservationController;

import java.util.Scanner;

public class ReservationMenu {
    private Scanner scanner;
    private ReservationController reservationController;
    private CreateReservationView createReservationView;
    private RemoveReservationView removeReservationView;
    private MainMenu mainMenu;

    public ReservationMenu(MainMenu mainMenu, MemberController memberController, BookController bookController, ReservationController reservationController) {
        this.mainMenu = mainMenu;
        scanner = new Scanner(System.in);
        this.reservationController = reservationController;
        createReservationView = new CreateReservationView(memberController, bookController, reservationController, scanner);
        removeReservationView = new RemoveReservationView(reservationController, scanner);
    }

    public void start() {
        while (true) {
            System.out.println("Escolha uma opção:");
            System.out.println("1. Criar reserva");
            System.out.println("2. Devolver livro");
            System.out.println("3. Listar reservas");
            System.out.println("4. Voltar");

            int option = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (option) {
                    case 1:
                        createReservationView.createReservation();
                        break;
                    case 2:
                        removeReservationView.deliverBook();
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
