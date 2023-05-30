package Controller;

import Data.BookData;
import Data.CDData;
import Data.ReservationData;
import Data.MemberData;
import Model.*;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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


    public void addReservation() {
        List<Member> members = memberData.load();
        List<Book> books = bookData.listBooks();
        List<CD> cds = cdData.load();

        Member selectedMember = selectMember(members);
        if (selectedMember == null) {
            System.out.println("Erro: Membro não selecionado.");
            return;
        }

        Book selectedBook = selectBook(books);
        CD selectedCD = selectCD(cds);

        if (selectedBook == null && selectedCD == null) {
            System.out.println("Erro: Nenhum item selecionado.");
            return;
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = getEndDateInput();

        if (endDate == null) {
            System.out.println("Erro: Data final da reserva não fornecida.");
            return;
        }

        Reservation reservation;

        if (selectedBook != null && selectedCD != null) {
            reservation = new Reservation(selectedMember, selectedBook, selectedCD, startDate, endDate);
        } else if (selectedBook != null) {
            reservation = new Reservation(selectedMember, selectedBook, startDate, endDate);
        } else if (selectedCD != null) {
            reservation = new Reservation(selectedMember, selectedCD, startDate, endDate);
        } else {
            System.out.println("Erro: Nenhum item selecionado.");
            return;
        }


        reservation.setState(State.RESERVADO); // estado alterado para 'RESERVADO' em memória
        reservationData.save(reservation); // cria a reserva na base de dados

        // Update quantidade do livro ou CD - 1
        if (selectedBook != null) {
            bookData.updateBookQuantityDecrease(selectedBook.getId());
            memberData.addBookToBorrowedBooks(selectedMember, selectedBook);
        }

        if (selectedCD != null) {
            cdData.updateCDQuantityDecrease(selectedCD.getId());
            memberData.addCDToBorrowedCDs(selectedMember, selectedCD);
        }
    }


    private CD selectCD(List<CD> cds) {
        Scanner scanner = new Scanner(System.in);


        System.out.println("CDs disponíveis:");
        for (CD cd : cds) {
            System.out.println(cd.getId() + ". Título: " + cd.getTitle());
        }

        // Add the "none" option
        System.out.println("0. Nenhum");

        // Prompt user to select a CD by ID
        System.out.print("Selecione o ID do CD: ");
        int cdId = scanner.nextInt();

        if (cdId == 0) {
            return null;
        }

        for (CD cd : cds) {
            if (cd.getId() == cdId) {
                return cd;
            }
        }

        return null;
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

        System.out.println("0. Nenhum");

        int selection = getUserInput("Opção: ");
        if (selection == 0) {
            return null;
        } else if (selection >= 1 && selection <= books.size()) {
            return books.get(selection - 1); // Return the selected book
        } else {
            System.out.println("Opção inválida. Por favor, selecione uma opção válida.");
            return selectBook(books);
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


    public void deliverReservation() {
        try {
            List<Reservation> reservations = reservationData.load();

            List<Reservation> matchingReservations = reservations.stream()
                    .filter(reservation -> reservation.getState() == State.RESERVADO && (reservation.getBook() != null || reservation.getCd() != null))
                    .collect(Collectors.toList());


            if (matchingReservations.isEmpty()) {
                System.out.println("Nenhuma reserva encontrada para o livro ou CD.");
                return;
            }

            // Display a list of reservations
            System.out.println("Reservas encontradas:");
            for (int i = 0; i < matchingReservations.size(); i++) {
                Reservation reservation = matchingReservations.get(i);
                Book book = reservation.getBook();
                CD cd = reservation.getCd();
                Member member = reservation.getMember();

                String itemInfo;
                if (book != null && cd != null) {
                    itemInfo = String.format("%d. Livro: %s - %s\n   CD: %s - %s", (i + 1), book.getTitle(), member.getName(), cd.getTitle(), member.getName());
                } else if (book != null) {
                    itemInfo = String.format("%d. Livro: %s - %s", (i + 1), book.getTitle(), member.getName());
                } else if (cd != null) {
                    itemInfo = String.format("%d. CD: %s - %s", (i + 1), cd.getTitle(), member.getName());
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

            // Update quantity of book or CD + 1
            Book selectedBook = selectedReservation.getBook();
            CD selectedCD = selectedReservation.getCd();

            if (selectedBook != null) {
                updateBookQuantityIncrease(selectedBook);
                memberData.removeBookFromBorrowedBooks(selectedReservation.getMember(), selectedBook);
            }

            if (selectedCD != null) {
                cdData.updateCDQuantityIncrease(selectedCD);
                memberData.removeCDFromBorrowedCDs(selectedReservation.getMember(), selectedCD);
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

}
