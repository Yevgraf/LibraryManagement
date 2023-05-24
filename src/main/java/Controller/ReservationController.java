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
        LocalDate endDate = getEndDateInput();

        if (endDate == null) {
            System.out.println("Erro: Data final da reserva não fornecida.");
            return;
        }

        Reservation reservation = new Reservation(selectedMember, selectedBook, startDate, endDate);
        reservation.setState(State.RESERVADO); // estado alterado para 'RESERVADO' em memoria
        reservationData.save(reservation); // cria a reserva na base dados

        // Update qauntidade livro - 1
        bookData.updateBookQuantityDecrease(selectedBook.getId());

        //adiciona livro a lista de livros emprestados do membro - tabela BorrowedBook
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

        Book selectedBook = null;
        boolean validSelection = false;
        while (!validSelection) {
            int selection = getUserInput("Opção: ");
            if (selection >= 1 && selection <= books.size()) {
                selectedBook = books.get(selection - 1);
                validSelection = true;
            } else {
                System.out.println("Opção inválida. Por favor, selecione uma opção válida.");
            }
        }

        return selectedBook;
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

            // Prompt seleção de livro
            System.out.print("Selecione o livro (digite o número correspondente): ");
            Scanner scanner = new Scanner(System.in);
            int bookSelection = scanner.nextInt();

            if (bookSelection < 1 || bookSelection > matchingReservations.size()) {
                System.out.println("Seleção inválida.");
                return;
            }

            Reservation selectedReservation = matchingReservations.get(bookSelection - 1);
            Book selectedBook = selectedReservation.getBook();

            // Update estado reserva para 'ENTREGUE'
            reservationData.updateReservationState(selectedReservation, State.ENTREGUE);

            // Prompt para formulário statisfação
            int satisfactionRating = getSatisfactionRatingInput();
            String additionalComments = getAdditionalCommentsInput();

            //update reserva com a informação adicional
            reservationData.updateReservationAdditionalInfo(selectedReservation, satisfactionRating, additionalComments);

            // Update quantidade de livro +1
            updateBookQuantityIncrease(selectedBook);

            //remove livro da lista de livros emprestados do membro - remove da tabela BorrowedBook
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



    private void updateBookQuantityIncrease(Book book) {
        bookData.updateBookQuantityIncrease(book.getId());
    }

}
