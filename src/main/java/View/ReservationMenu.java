package View;

import Controller.BookController;
import Controller.MemberController;
import Controller.ReservationController;

import java.util.Scanner;

/**
 * A classe ReservationMenu representa o menu de reservas.
 */
public class ReservationMenu {
    private Scanner scanner;
    private ReservationController reservationController;
    private CreateReservationView createReservationView;
    private RemoveReservationView removeReservationView;
    private MainMenu mainMenu;

    /**
     * Construtor da classe ReservationMenu.
     * @param mainMenu O menu principal.
     * @param memberController O controlador de membros.
     * @param bookController O controlador de livros.
     * @param reservationController O controlador de reservas.
     */
    public ReservationMenu(MainMenu mainMenu, MemberController memberController, BookController bookController, ReservationController reservationController) {
        this.mainMenu = mainMenu;
        scanner = new Scanner(System.in);
        this.reservationController = reservationController;
        createReservationView = new CreateReservationView(memberController, bookController, reservationController, scanner);
        removeReservationView = new RemoveReservationView(reservationController, scanner);
    }

    /**
     * Inicia o menu de reservas.
     * Exibe as opções disponíveis e processa a escolha do usuário.
     */
    public void start() {
        while (true) {
            System.out.println("Escolha uma opção:");
            System.out.println("1. Criar reserva");
            System.out.println("2. Devolver reserva");
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
                        System.out.println("Voltar...");
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
