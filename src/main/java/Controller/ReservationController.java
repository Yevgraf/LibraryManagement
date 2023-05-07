package Controller;

import Data.BookData;
import Data.ReservationData;
import Data.MemberData;
import Model.*;



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
        if (book == null) {
            System.out.println("Erro: Livro não pode ser nulo");
            return;
        }

        if (member == null) {
            System.out.println("Erro: Membro não pode ser nulo");
            return;
        }

        if (member.getBorrowedBooks().contains(book)) {
            System.out.println("Erro: Membro já possui uma reserva deste livro");
            return;
        }
        if (member.getBorrowedBooks().size() >= 3) {
            System.out.println("Erro: Número máximo de livros reservados atingido para este membro");
            return;
        }
        if (endDate == null) {
            System.out.println("Erro: Data final da reserva não pode ser nula");
            return;
        }
        //adiciona livros ao arraylist nos membros para verificar quantas reservas tem e qual os livros reservaram
        member.getBorrowedBooks().add(book);
        //data de final de reserva, currentdate = inicio
        reservation.setEndDate(endDate);
        //update no estado da reserva
        updateReservationReserved(reservation);
        //adicionar reserva ao ficheiro
        reservationData.addReservation(reservation);
        //update ficheiro membros com o arraylist atualizado
        updateMember(member);
        //remove 1 livro da quantidade
        updateBookQuantity(book);
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

    //remove 1 na quantidade do livro escolhido, se quantidade atual for igual a 0 print mensagem;
    private void updateBookQuantity(Book book) {
        List<Book> books = bookData.load();
        for (Book b : books) {
            if (b.getId() == book.getId()) {
                int currentQuantity = b.getQuantity();
                if (currentQuantity == 0) {
                    System.out.println("Erro: Não há mais cópias disponíveis deste livro");
                    return;
                }
                b.setQuantity(currentQuantity - 1);
                break;
            }
        }
        bookData.save(books);
    }


    public void updateReservationReserved(Reservation reservation) {
        List<Reservation> reservations = reservationData.load();
        for (Reservation r : reservations) {
            if (r.getId() == reservation.getId()) {
                r.setState(State.RESERVED);
                break;
            }
        }
        reservationData.save(reservations);
    }

    public void updateReservationDelivered(Reservation reservation) {
        List<Reservation> reservations = reservationData.load();
        for (Reservation r : reservations) {
            if (r.getId() == reservation.getId()) {
                r.setState(State.DELIVERED);
                break;
            }
        }
        reservationData.save(reservations);
    }


    public void updateReservationCanceled(Reservation reservation) {
        List<Reservation> reservations = reservationData.load();
        for (Reservation r : reservations) {
            if (r.getId() == reservation.getId()) {
                r.setState(State.CANCELED);
                break;
            }
        }
        reservationData.save(reservations);
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
            updateBookQuantity(reservationToRemove.getBook());
            Member member = reservationToRemove.getMember();
            member.getBorrowedBooks().remove(reservationToRemove.getBook());
            updateMember(member);
        } else {
            System.out.println("Reserva não encontrada para remoção.");
        }
    }

}
