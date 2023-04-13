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

    public void addReservation(Reservation reservation) {
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
        reservationData.addReservation(reservation);
        updateMember(member);

    }

    public void removeReservation(Reservation reservation) {
        Book book = reservation.getBook();
        Member member = reservation.getMember();

        reservationData.removeReservation(reservation);
        updateBookStatus(book, false);
        updateMember(member);
    }

    public Reservation getReservationByMemberNameOrBookIsbn(String searchTerm) {

        Reservation reservation = reservationData.getReservationByBookNameOrIsbn(searchTerm);

        return reservation;
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
            if (m.getId() == member.getId()) {
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



}
