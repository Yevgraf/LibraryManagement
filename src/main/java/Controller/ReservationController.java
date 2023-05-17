package Controller;

import Data.BookData;
import Data.ReservationData;
import Data.MemberData;
import Model.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        List<Book> books = bookData.listBooks();
        List<Member> members = memberData.load();
        boolean success = true;


        if (reservation.getBook() == null) {
            System.out.println("Erro: Livro não pode ser nulo");
            success = false;
        }

        if (reservation.getMember() == null) {
            System.out.println("Erro: Membro não pode ser nulo");
            success = false;
        }

        if (reservation.getMember().getBorrowedBooks().contains(reservation.getBook())) {
            System.out.println("Erro: Membro já possui uma reserva deste livro");
            success = false;
        }

        if (reservation.getMember().getBorrowedBooks().size() + reservation.getMember().getBorrowedBooks().size() >= reservation.getMember().getMaxBorrowedBooks() + reservation.getMember().getMaxBorrowedBooks()) {
            System.out.println("Erro: Número máximo de livros reservados e emprestados atingido para este membro");
            success = false;
        }

        if (reservationData.loadReservedReservation().stream()
                .filter(r -> r.getMember().equals(reservation.getMember()))
                .anyMatch(r -> r.getBook().equals(reservation.getBook()) && (r.getState() == State.RESERVADO || r.getState() == State.PENDENTE))) {
            System.out.println("Erro: Membro já possui uma reserva ou empréstimo deste livro");
            success = false;
        }

        if (endDate == null) {
            System.out.println("Erro: Data final da reserva não pode ser nula");
            success = false;
        }

        Book book = books.stream()
                .filter(b -> b.getId() == reservation.getBook().getId())
                .findFirst()
                .orElse(null);

        if (book == null) {
            System.out.println("Erro: Livro não encontrado");
            success = false;
        }

        Member member = members.stream()
                .filter(m -> m.getName().equals(reservation.getMember().getName()))
                .findFirst()
                .orElse(null);

        if (member == null) {
            System.out.println("Erro: Membro não encontrado");
            success = false;
        }

        if (success) {
            reservation.setState(State.PENDENTE);

            member.getBorrowedBooks().add(book);
            List<Book> booksFromReservation = new ArrayList<>();
            booksFromReservation.add(book);

            reservation.setEndDate(endDate);
            reservationData.addReservation(reservation);

            updateBookQuantityReduce(book);

            updateMember(member, booksFromReservation);

            updateReservationReserved();

            // verifica se a reserva foi adiciona com sucesso
            List<Reservation> reservations = reservationData.loadReservedReservation();
            if (reservations.contains(reservation)) {
                System.out.println("Reserva adicionada com sucesso!");
            } else {
                System.out.println("Erro ao adicionar reserva");
            }
        }

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

    private void updateMember(Member member, List<Book> booksFromReservation) {
        List<Member> members = memberData.load();
        for (Member m : members) {
            if (m.getEmail().equals(member.getEmail())) {
                m.setBorrowedBooks(member.getBorrowedBooks());
                for (Book book : booksFromReservation) {
                    if (!m.getBorrowedBooks().contains(book)) {
                        m.getBorrowedBooks().add(book);
                    }
                }
                break;
            }
        }
        memberData.save(members);
    }


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



    public void deliverReservationByBookNameAndUserName(String bookNameOrIsbn, String userName) {
        try {
            List<Reservation> reservations = reservationData.load();
            Optional<Reservation> matchingReservation = findReservationByBookNameAndUserName(bookNameOrIsbn, userName, reservations);
            if (matchingReservation.isPresent()) {
                Reservation reservationToUpdate = matchingReservation.get();
                if (!isBookBorrowedByMember(reservationToUpdate.getBook(), reservationToUpdate.getMember())) {
                    System.out.println("A reserva não pode ser entregue, pois o livro não foi emprestado para o membro que fez a reserva.");
                    return;
                }
                reservationToUpdate.setState(State.ENTREGUE);
                reservationData.save(reservations);
                updateBookQuantityIncrease(reservationToUpdate.getBook());
                removeBookFromMember(reservationToUpdate.getMember(), reservationToUpdate.getBook());
                System.out.println("Reserva atualizada para 'ENTREGUE': " + reservationToUpdate.toString());
            } else {
                System.out.println("Nenhuma reserva encontrada para o livro com título ou ISBN: " + bookNameOrIsbn + " e o utilizador: " + userName);
            }
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao tentar alterar a reserva para 'ENTREGUE': " + e.getMessage());
        }
    }

    private boolean isBookBorrowedByMember(Book book, Member member) {
        List<Member> members = memberData.load();
        Optional<Member> matchingMember = members.stream()
                .filter(m -> m.getName().equalsIgnoreCase(member.getName()))
                .findFirst();
        if (matchingMember.isPresent()) {
            return matchingMember.get().getBorrowedBooks().stream()
                    .anyMatch(b -> b.getTitle().equalsIgnoreCase(book.getTitle()) && b.getIsbn().equalsIgnoreCase(book.getIsbn()));
        } else {
            return false;
        }
    }




    private Optional<Reservation> findReservationByBookNameAndUserName(String bookNameOrIsbn, String userName, List<Reservation> reservations) {
        String searchInput = bookNameOrIsbn.trim().toLowerCase();
        return reservations.stream()
                .filter(reservation -> {
                    String bookTitle = reservation.getBook().getTitle().trim().toLowerCase();
                    String bookISBN = reservation.getBook().getIsbn().trim().toLowerCase();
                    return bookTitle.equalsIgnoreCase(searchInput) || bookISBN.equalsIgnoreCase(searchInput);
                })
                .filter(reservation -> reservation.getMember().getName().equalsIgnoreCase(userName))
                .findFirst();
    }


    private void removeBookFromMember(Member member, Book book) {
        List<Member> members = memberData.load();
        members.stream()
                .filter(m -> m.getName().equalsIgnoreCase(member.getName()))
                .findFirst()
                .ifPresent(m -> m.getBorrowedBooks().removeIf(b -> b.getTitle().equals(book.getTitle())));
        memberData.save(members);
    }





}
