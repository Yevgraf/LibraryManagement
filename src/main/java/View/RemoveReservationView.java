package View;

import Controller.ReservationController;
import Model.Reservation;

import java.util.Scanner;

public class RemoveReservationView {

    private ReservationController reservationController;
    private Scanner scanner;

    public RemoveReservationView(ReservationController reservationController, Scanner scanner) {
        this.reservationController = reservationController;
        this.scanner = scanner;
    }

    public void removeReservation() {
        System.out.println("Remover reserva:");
        System.out.print("Nome do Livro ou ISBN do livro: ");
        String searchTerm = scanner.nextLine();

        reservationController.removeReservationByBookNameOrIsbn(searchTerm);
    }
}
