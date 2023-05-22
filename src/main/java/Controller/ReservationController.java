package Controller;

import Data.BookData;
import Data.ReservationData;
import Data.MemberData;
import Model.*;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

        // Update the book quantity
        bookData.updateBookQuantityDecrease(selectedBook.getId());

        // Add the book to the member's borrowed books list
        memberData.addBookToBorrowedBooks(selectedMember, selectedBook);

        System.out.println("Reserva adicionada com sucesso.");
    }


    private LocalDate getEndDateInput() {
        System.out.print("Digite a data final da reserva (dd/mm/yyyy): ");
        Scanner scanner = new Scanner(System.in);
        String endDateStr = scanner.nextLine();

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(endDateStr, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Erro: Formato de data inválido. Use o formato dd/mm/yyyy.");
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

    public void deliverReservationByBookNameAndUserName() {
        try {
            List<Reservation> reservations = reservationData.load();

            List<Reservation> matchingReservations = reservations.stream()
                    .filter(reservation -> reservation.getState() == State.RESERVADO)
                    .collect(Collectors.toList());

            if (matchingReservations.isEmpty()) {
                System.out.println("Nenhuma reserva encontrada para o livro com título ou ISBN.");
                return;
            }

            // Display a list of books matching the criteria
            System.out.println("Livros encontrados:");
            for (int i = 0; i < matchingReservations.size(); i++) {
                Reservation reservation = matchingReservations.get(i);
                Book book = reservation.getBook();
                Member member = reservation.getMember();

                String bookInfo = String.format("%d. %s - %s", (i + 1), book.getTitle(), member.getName());
                System.out.println(bookInfo);
                System.out.println("   - Email: " + member.getEmail());
                System.out.println("   - Data de devolução: " + reservation.getEndDate());
            }

            // Prompt for book selection
            System.out.print("Selecione o livro (digite o número correspondente): ");
            Scanner scanner = new Scanner(System.in);
            int bookSelection = scanner.nextInt();

            if (bookSelection < 1 || bookSelection > matchingReservations.size()) {
                System.out.println("Seleção inválida.");
                return;
            }

            Reservation selectedReservation = matchingReservations.get(bookSelection - 1);
            Book selectedBook = selectedReservation.getBook();

            // Update the reservation state to 'ENTREGUE'
            reservationData.updateReservationState(selectedReservation, State.ENTREGUE);

            // Prompt for additional information (satisfaction rating and additional comments)
            int satisfactionRating = getSatisfactionRatingInput();
            String additionalComments = getAdditionalCommentsInput();

            // Update the reservation with additional information using ReservationData's method
            reservationData.updateReservationAdditionalInfo(selectedReservation, satisfactionRating, additionalComments);

            // Update the book quantity
            updateBookQuantityIncrease(selectedBook);

            // Remove the book from the member's borrowed books
            memberData.removeBookFromBorrowedBooks(selectedReservation.getMember(), selectedBook);

            System.out.println("Reserva atualizada para 'ENTREGUE': " + selectedReservation.toString());
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao tentar alterar a reserva para 'ENTREGUE': " + e.getMessage());
        }
    }


    private int getSatisfactionRatingInput() {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Insira a avaliação de satisfação (1-5): ");
        int rating = scanner.nextInt();
        return rating;
    }

    private String getAdditionalCommentsInput() {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Insira os comentários adicionais: ");
        String comments = scanner.nextLine();
        return comments;
    }

    private void updateReservationAdditionalInfo(Reservation reservation, int satisfactionRating, String additionalComments) {
        // Definir a avaliação de satisfação e os comentários adicionais no objeto de reserva
        reservation.setSatisfactionRating(satisfactionRating);
        reservation.setAdditionalComments(additionalComments);
    }


    private void updateBookQuantityIncrease(Book book) {
        // Update the book quantity by increasing it by 1
        bookData.updateBookQuantityIncrease(book.getId());
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


}
