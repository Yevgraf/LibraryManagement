package View;

import Controller.ReservationController;

import java.util.Scanner;

public class RemoveReservationView {

    private ReservationController reservationController;
    private Scanner scanner;

    public RemoveReservationView(ReservationController reservationController, Scanner scanner) {
        this.reservationController = reservationController;
        this.scanner = scanner;
    }

    public void deliverBook() {
        System.out.println("Entregar livro/CD:");
        reservationController.deliverReservation();
    }


}
