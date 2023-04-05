package View;

import Controller.BookController;
import Controller.MemberController;
import Controller.ReservationController;
import Model.Book;
import Model.Member;
import Model.Reservation;
import java.util.List;
import java.time.LocalDate;
import java.util.Scanner;

public class CreateReservationView {

    private MemberController memberController;
    private BookController bookController;
    private ReservationController reservationController;
    private Scanner scanner;

    public CreateReservationView(MemberController memberController, BookController bookController, ReservationController reservationController, Scanner scanner) {
        this.memberController = memberController;
        this.bookController = bookController;
        this.reservationController = reservationController;
        this.scanner = scanner;
    }

    public void createReservation() {
        System.out.println("Adicionar reserva:");
        System.out.print("Nome do membro: ");
        String memberName = scanner.nextLine();
        Member member = memberController.getMemberByName(memberName);
        if (member == null) {
            System.out.println("Membro não encontrado.");
            return;
        }
        System.out.print("Nome do livro ou ISBN: ");
        String searchTerm = scanner.nextLine();
        Book book = bookController.findBookByIdOrName(searchTerm);
        if (book == null) {
            System.out.println("Livro não encontrado.");
            return;
        }
        Reservation reservation = new Reservation(book, member, LocalDate.now());
        reservationController.addReservation(reservation);
        System.out.println("Reserva adicionada com sucesso!");
    }

    public void listAllReservations(){
        List<Reservation> reservations = reservationController.getAllReservations();

        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            System.out.println("All reservations:");
            for (Reservation reservation : reservations) {
                System.out.println(reservation.toString());
            }
        }
    }
}
