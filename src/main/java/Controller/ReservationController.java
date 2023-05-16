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

        reservation.setState(State.PENDENTE);
        // adiciona livros ao arraylist nos membros para verificar quantas reservas tem e qual os livros reservaram
        member.getBorrowedBooks().add(book);
        // data de final de reserva, currentdate = inicio
        reservation.setEndDate(endDate);
        reservationData.addReservation(reservation);
        // remove 1 livro da quantidade
        updateBookQuantityReduce(book);
        // update ficheiro membros com o arraylist atualizado
        updateMember(member);
        System.out.println("Reserva adicionada com sucesso!");
        updateReservationReserved();
    }

    public void updateReservationReserved() {
        List<Reservation> reservations = reservationData.load();
        boolean updated = false;
        for (Reservation r : reservations) {
            if (r.getState() == State.PENDENTE) {
                r.setState(State.RESERVADO);
                updated = true;
            }
        }
        if (updated) {
            reservationData.save(reservations);
            System.out.println("Reservas depois do update: " + reservations);
        } else {
            System.out.println("Não há reservas pendentes para atualizar.");
        }
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
    private void updateBookQuantityReduce(Book book) {
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

    private void updateBookQuantityIncrease(Book book) {
        List<Book> books = bookData.load();
        for (Book b : books) {
            if (b.getId() == book.getId()) {
                int currentQuantity = b.getQuantity();
                b.setQuantity(currentQuantity + 1);
                break;
            }
        }
        bookData.save(books);
    }


    public void updateReservationDelivered(String bookName, String userName) {
        List<Reservation> reservations = reservationData.load();
        for (Reservation r : reservations) {
            if (r.getBook().getTitle().equalsIgnoreCase(bookName) && r.getMember().getName().equalsIgnoreCase(userName)) {
                r.setState(State.ENTREGUE);
                break;
            }
        }
        reservationData.save(reservations);
    }




    public void updateReservationCanceled(Reservation reservation) {
        List<Reservation> reservations = reservationData.load();
        for (Reservation r : reservations) {
            if (r.getId() == reservation.getId()) {
                r.setState(State.CANCELADA);
                break;
            }
        }
        reservationData.save(reservations);
    }

    public void deliverReservationByBookNameAndUserName(String bookNameOrIsbn, String userName) {
        try {
            Reservation matchingReservation = findReservationByBookNameAndUserName(bookNameOrIsbn, userName);
            if (matchingReservation != null) {
                if (matchingReservation.getState() != State.RESERVADO) {
                    System.out.println("A reserva não pode ser entregue, pois não está no estado 'RESERVADA'");
                    return;
                }
                if (!isBookBorrowedByMember(matchingReservation.getBook(), matchingReservation.getMember())) {
                    System.out.println("A reserva não pode ser entregue, pois o livro não foi emprestado para o membro que fez a reserva.");
                    return;
                }
                updateReservationDelivered(matchingReservation.getBook().getTitle(),matchingReservation.getMember().getName());
                reservationData.save(reservationData.load());
                updateBookQuantityIncrease(matchingReservation.getBook());
                System.out.println("Reserva atualizada para 'ENTREGUE': " + matchingReservation.toString());
            } else {
                System.out.println("Nenhuma reserva encontrada para o livro com título ou ISBN: " + bookNameOrIsbn + " e o usuário: " + userName);
            }
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao tentar alterar a reserva para 'ENTREGUE': " + e.getMessage());
        }
    }

    private Reservation findReservationByBookNameAndUserName(String bookNameOrIsbn, String userName) {
        List<Reservation> reservations = reservationData.load();
        String searchInput = bookNameOrIsbn.trim().toLowerCase();
        for (Reservation reservation : reservations) {
            String bookTitle = reservation.getBook().getTitle().trim().toLowerCase();
            String bookISBN = reservation.getBook().getIsbn().trim().toLowerCase();
            if (bookTitle.equalsIgnoreCase(searchInput) || bookISBN.equalsIgnoreCase(searchInput)) {
                if (reservation.getMember().getName().equalsIgnoreCase(userName)) {
                    return reservation;
                }
            }
        }
        return null;
    }

    private boolean isBookBorrowedByMember(Book book, Member member) {
        for (Book borrowedBook : member.getBorrowedBooks()) {
            if (borrowedBook.equals(book)) {
                return true;
            }
        }
        return false;
    }


}
