package Controller;

import Data.BookData;
import Data.ReservationData;
import Data.MemberData;
import Model.*;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
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

    public void addReservation() {
        List<Member> members = memberData.load();
        List<Book> books = bookData.listBooks();

        Member selectedMember = selectMember(members);
        if (selectedMember == null) {
            System.out.println("Erro: Membro não selecionado.");
            return;
        }

        Book selectedBook = selectBook(books);
        if (selectedBook == null) {
            System.out.println("Erro: Livro não selecionado.");
            return;
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = getEndDateInput(); // Prompt the librarian to enter the end date

        if (endDate == null) {
            System.out.println("Erro: Data final da reserva não fornecida.");
            return;
        }

        Reservation reservation = new Reservation(selectedMember, selectedBook, startDate, endDate);
        reservation.setState(State.RESERVADO); // Set the state to "RESERVADO"
        reservationData.save(reservation); // Save the reservation

        System.out.println("Reserva adicionada com sucesso.");
    }

    private LocalDate getEndDateInput() {
        System.out.print("Digite a data final da reserva (yyyy-mm-dd): ");
        Scanner scanner = new Scanner(System.in);
        String endDateStr = scanner.nextLine();

        try {
            return LocalDate.parse(endDateStr);
        } catch (DateTimeParseException e) {
            System.out.println("Erro: Formato de data inválido. Use o formato yyyy-mm-dd.");
            return null;
        }
    }


    private Member selectMember(List<Member> members) {
        System.out.println("Selecione o membro:");
        for (int i = 0; i < members.size(); i++) {
            System.out.println((i + 1) + ". " + members.get(i).getName());
        }

        int selection = getUserInput("Opção: ");
        if (selection >= 1 && selection <= members.size()) {
            return members.get(selection - 1);
        } else {
            return null;
        }
    }

    private Book selectBook(List<Book> books) {
        System.out.println("Selecione o livro:");
        for (int i = 0; i < books.size(); i++) {
            System.out.println((i + 1) + ". " + books.get(i).getTitle());
        }

        int selection = getUserInput("Opção: ");
        if (selection >= 1 && selection <= books.size()) {
            return books.get(selection - 1);
        } else {
            return null;
        }
    }

    private int getUserInput(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(prompt);
        return scanner.nextInt();
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


//    private void updateBookQuantityReduce(Book book) {
//        List<Book> books = bookData.load();
//        for (Book b : books) {
//            if (b.getId() == book.getId()) {
//                int currentQuantity = b.getQuantity();
//                if (currentQuantity == 0) {
//                    System.out.println("Erro: Não há mais cópias disponíveis deste livro");
//                    return;
//                }
//                b.setQuantity(currentQuantity - 1);
//                break;
//            }
//        }
//        bookData.save(books);
//    }

//    private void updateBookQuantityIncrease(Book book) {
//        List<Book> books = bookData.load();
//        for (Book b : books) {
//            if (b.getId() == book.getId()) {
//                int currentQuantity = b.getQuantity();
//                b.setQuantity(currentQuantity + 1);
//                break;
//            }
//        }
//        bookData.save(books);
//    }



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
              //  updateBookQuantityIncrease(reservationToUpdate.getBook());
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
                .ifPresent(m -> {
                    List<Book> borrowedBooks = m.getBorrowedBooks();
                    boolean bookRemoved = false;
                    for (int i = 0; i < borrowedBooks.size(); i++) {
                        if (borrowedBooks.get(i).getTitle().equals(book.getTitle())) {
                            borrowedBooks.remove(i);
                            bookRemoved = true;
                            break;
                        }
                    }
                    if (bookRemoved) {
                        m.setBorrowedBooks(borrowedBooks);
                    }
                });
        memberData.save(members);
    }

    public boolean isBookBorrowed(String title) {
        return reservationData.load().stream()
                .anyMatch(r -> r.getBook().getTitle().equalsIgnoreCase(title) && r.getState().equals(State.RESERVADO));
    }



}
