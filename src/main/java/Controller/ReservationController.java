package Controller;

import Data.BookData;
import Data.CDData;
import Data.ReservationData;
import Data.MemberData;
import Model.*;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ReservationController {

    private ReservationData reservationData;
    private MemberData memberData;
    private BookData bookData;
    private CDData cdData;

    public ReservationController(ReservationData reservationData, MemberData memberData, BookData bookData, CDData cdData) {
        this.reservationData = reservationData;
        this.memberData = memberData;
        this.bookData = bookData;
        this.cdData = cdData;
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


    public void addReservation(Member selectedMember, Book selectedBook, CD selectedCD, LocalDate endDate) {
        if (selectedMember == null) {
            System.out.println("Erro: Membro não selecionado.");
            return;
        }

        if (selectedBook == null && selectedCD == null) {
            System.out.println("Erro: Nenhum item selecionado.");
            return;
        }

        LocalDate startDate = LocalDate.now();
        Reservation reservation = new Reservation(selectedMember, startDate, endDate);

        if (selectedBook != null) {
            reservation.addBook(selectedBook);
            bookData.updateBookQuantityDecrease(selectedBook.getId());
        }

        if (selectedCD != null) {
            reservation.addCD(selectedCD);
            cdData.updateCDQuantityDecrease(selectedCD.getId());
        }

        reservation.setState(State.RESERVADO); // Set state to 'RESERVADO'
        reservationData.save(reservation); // Save the reservation in the database
    }



    public List<Reservation> getReservationsForMember(Member member) {
        return reservationData.getReservationsForMember(member);
    }

    public List<Reservation> getAllReservations() {
        return reservationData.load();
    }


    public void deliverReservation() {
        try {
            List<Reservation> reservations = reservationData.load();


            List<Reservation> matchingReservations = reservations.stream()
                    .filter(reservation -> reservation.getState() == State.RESERVADO &&
                            (!reservation.getBooks().isEmpty() || !reservation.getCds().isEmpty()))
                    .collect(Collectors.toList());

            if (matchingReservations.isEmpty()) {
                System.out.println("Nenhuma reserva encontrada para o livro ou CD.");
                return;
            }

            // Display a list of reservations
            System.out.println("Reservas encontradas:");
            for (int i = 0; i < matchingReservations.size(); i++) {
                Reservation reservation = matchingReservations.get(i);
                List<Book> books = reservation.getBooks();
                List<CD> cds = reservation.getCds();
                Member member = reservation.getMember();

                String itemInfo;
                if (!books.isEmpty() && !cds.isEmpty()) {
                    itemInfo = String.format("%d. Livro: %s - %s\n   CD: %s - %s", (i + 1), books.get(0).getTitle(), member.getName(), cds.get(0).getTitle(), member.getName());
                } else if (!books.isEmpty()) {
                    itemInfo = String.format("%d. Livro: %s - %s", (i + 1), books.get(0).getTitle(), member.getName());
                } else if (!cds.isEmpty()) {
                    itemInfo = String.format("%d. CD: %s - %s", (i + 1), cds.get(0).getTitle(), member.getName());
                } else {
                    itemInfo = String.format("%d. Reserva inválida", (i + 1));
                }

                System.out.println(itemInfo);
                System.out.println("   - Email: " + member.getEmail());
                System.out.println("   - Data de devolução: " + reservation.getEndDate());
            }


            // Prompt for item selection
            System.out.print("Selecione o item (digite o número correspondente): ");
            Scanner scanner = new Scanner(System.in);
            int itemSelection = scanner.nextInt();

            if (itemSelection < 1 || itemSelection > matchingReservations.size()) {
                System.out.println("Seleção inválida.");
                return;
            }

            Reservation selectedReservation = matchingReservations.get(itemSelection - 1);

            // Update reservation state to 'ENTREGUE'
            reservationData.updateReservationState(selectedReservation, State.ENTREGUE);

            // Prompt for satisfaction rating
            int satisfactionRating = getSatisfactionRatingInput();
            String additionalComments = getAdditionalCommentsInput();

            // Update reservation with additional information
            reservationData.updateReservationAdditionalInfo(selectedReservation, satisfactionRating, additionalComments);

            // Update quantity of books or CDs + 1
            List<Book> selectedBooks = selectedReservation.getBooks();
            List<CD> selectedCDs = selectedReservation.getCds();

            for (Book selectedBook : selectedBooks) {
                updateBookQuantityIncrease(selectedBook);
                memberData.removeBookFromBorrowedBooks(selectedReservation.getMember(), selectedBook);
            }

            for (CD selectedCD : selectedCDs) {
                cdData.updateCDQuantityIncrease(selectedCD);
            }

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


    private void updateBookQuantityIncrease(Book book) {
        bookData.updateBookQuantityIncrease(book.getId());
    }


    public List<Member> getAllMembers() {
        return memberData.load();
    }

    public List<Book> getAllBooks() {
        return bookData.load();
    }

    public List<CD> getAllCDs() {
        return cdData.load();
    }
}
