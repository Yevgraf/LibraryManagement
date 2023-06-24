package View;

import Controller.ReservationController;

import java.util.Scanner;

public class RemoveReservationView {

    private ReservationController reservationController;
    private Scanner scanner;

    /**
     * Constructs a new instance of the RemoveReservationView class with the specified ReservationController and Scanner.
     * @param reservationController The reservation controller.
     * @param scanner The scanner for user input.
     */
    public RemoveReservationView(ReservationController reservationController, Scanner scanner) {
        this.reservationController = reservationController;
        this.scanner = scanner;
    }

    /**
     * Displays the view for delivering a book/CD and handles user input.
     */
    public void deliverBook() {
        System.out.println("Entregar livro/CD:");
        reservationController.deliverReservation();
    }
}
