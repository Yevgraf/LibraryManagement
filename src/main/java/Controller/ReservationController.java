package Controller;

import Data.BookData;
import Data.CardData;
import Data.ReservationData;
import Data.MemberData;
import Model.Book;
import Model.Card;
import Model.Member;
import Model.Reservation;

import java.time.LocalDate;
import java.util.List;

public class ReservationController {

    private ReservationData reservationData;
    private MemberData memberData;
    private BookData bookData;


    public ReservationController(ReservationData reservationData, MemberData memberData, BookData bookData) {
        this.reservationData = reservationData;
        this.memberData = memberData;
        this.bookData = bookData;
    }

    public void setReservationData(ReservationData reservationData) {
        this.reservationData = reservationData;
    }

    public void setMemberData(MemberData memberData) {
        this.memberData = memberData;
    }

    public void setBookData(BookData bookData) {
        this.bookData = bookData;
    }

    public void addReservation(Reservation reservation, LocalDate endDate) {
        Book book = reservation.getBook();
        Member member = reservation.getMember();

        if (book.isBorrowed()) {
            System.out.println("Erro: Livro já está emprestado e não disponível para reserva");
            return;
        }

        if (member.getBorrowedBooks().size() >= 3) {
            System.out.println("Erro: Número máximo de livros reservados atingido para este membro");
            return;
        }

        member.getBorrowedBooks().add(book);

        updateBookStatus(book, true);
        reservation.setEndDate(endDate);
        reservationData.addReservation(reservation);
        updateMember(member);
    }







    public List<Reservation> getReservationsForMember(Member member) {
        return reservationData.getReservationsForMember(member);
    }

    public List<Reservation> getAllReservations() {
        return reservationData.load();
    }

    private void updateMember(Member member) {
        List<Member> members = memberData.load();
        for (Member m : members) {
            if (m.getEmail() == member.getEmail()) {
                m.setBorrowedBooks(member.getBorrowedBooks());
                break;
            }
        }
        memberData.save(members);
    }

    private void updateBookStatus(Book book, boolean borrowed) {
        List<Book> books = bookData.load();
        for (Book b : books) {
            if (b.getId() == book.getId()) {
                b.setBorrowed(borrowed);
                break;
            }
        }
        bookData.save(books);
    }

    public void removeReservationByBookNameOrIsbn(String bookNameOrIsbn) {
        ReservationData reservationData = new ReservationData();
        BookData bookData = new BookData();
        MemberData memberData = new MemberData();
        List<Reservation> reservations = reservationData.load();
        String searchInput = bookNameOrIsbn.trim().toLowerCase();

        Reservation reservationToRemove = null;
        for (Reservation reservation : reservations) {
            String bookTitle = reservation.getBook().getTitle().trim().toLowerCase();
            String bookISBN = reservation.getBook().getIsbn().trim().toLowerCase();
            if (bookTitle.equalsIgnoreCase(searchInput) || bookISBN.equalsIgnoreCase(searchInput)) {
                reservationToRemove = reservation;
                System.out.println("Reserva removida: " + reservation.toString());
                break;
            }
        }

        if (reservationToRemove != null) {
            reservations.remove(reservationToRemove);
            reservationData.save(reservations);
            updateBookStatus(reservationToRemove.getBook(), false);
            Member member = reservationToRemove.getMember();
            member.getBorrowedBooks().remove(reservationToRemove.getBook());
            updateMember(member);
        } else {
            System.out.println("Reserva não encontrada para remoção.");
        }
    }

}
