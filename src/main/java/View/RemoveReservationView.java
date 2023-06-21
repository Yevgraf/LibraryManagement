package View;

import Controller.ReservationController;

import java.util.Scanner;

/**
 * A classe RemoveReservationView representa a visualização para remoção de reservas.
 */
public class RemoveReservationView {

    private ReservationController reservationController;
    private Scanner scanner;

    /**
     * Construtor da classe RemoveReservationView.
     * @param reservationController O controlador de reservas.
     * @param scanner O scanner para entrada de dados.
     */
    public RemoveReservationView(ReservationController reservationController, Scanner scanner) {
        this.reservationController = reservationController;
        this.scanner = scanner;
    }

    /**
     * Realiza a entrega de um livro ou CD.
     * Chama o método do controlador de reservas responsável por entregar a reserva.
     */
    public void deliverBook() {
        System.out.println("Entregar livro/CD:");
        reservationController.deliverReservation();
    }


}
