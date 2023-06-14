package View;

import Controller.BookController;
import Controller.MemberController;
import Controller.ReservationController;
import Model.Book;
import Model.CD;
import Model.Member;
import Model.Reservation;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;

public class CreateReservationView {

    private MemberController memberController;
    private BookController bookController;
    private ReservationController reservationController;
    private Scanner scanner;

    public CreateReservationView(MemberController memberController, BookController bookController, ReservationController reservationController, Scanner scanner) {
        this.memberController = memberController;
        this.bookController = bookController;
        this.reservationController = reservationController;
        this.scanner = scanner;
    }

    public void createReservation() {
        // Obtain the necessary inputs
        List<Member> members = getMembers();
        List<Book> books = getBooks();
        List<CD> cds = getCDs();

        Member selectedMember = selectMember(members);
        Optional<Book> selectedBookOption = selectBook(books);
        Optional<CD> selectedCDOption = selectCD(cds);
        LocalDate endDate = getEndDateInput();

        // Check if any of the inputs are null or empty
        if (selectedMember == null || (!selectedBookOption.isPresent() && !selectedCDOption.isPresent()) || endDate == null) {
            System.out.println("Erro: Reserva não criada devido a inputs inválidos.");
            return;
        }

        Book selectedBook = selectedBookOption.orElse(null);
        CD selectedCD = selectedCDOption.orElse(null);

        // Pass the inputs to the addReservation() method
        reservationController.addReservation(selectedMember, selectedBook, selectedCD, endDate);
    }


    public Optional<CD> selectCD(List<CD> cds) {
        System.out.println("CDs disponíveis:");
        for (CD cd : cds) {
            System.out.println(cd.getId() + ". Título: " + cd.getTitle());
        }

        // Add the "none" option
        System.out.println("0. Nenhum");

        // Prompt user to select a CD by ID
        int cdId = getUserInput("Selecione o ID do CD: ");

        if (cdId == 0) {
            return Optional.empty();
        }

        for (CD cd : cds) {
            if (cd.getId() == cdId) {
                return Optional.of(cd);
            }
        }

        // Handle the case when no CD is selected
        System.out.println("Erro: CD inválido. Nenhum CD selecionado.");
        return selectCD(cds); // Prompt the user again for CD selection
    }





    public LocalDate getEndDateInput() {
        System.out.print("Digite a data final da reserva (dd/mm/yyyy): ");
        scanner.nextLine(); // Consume the newline character

        String endDateStr = scanner.nextLine();

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(endDateStr, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Erro: Formato de data inválido. Use o formato dd/mm/yyyy.");
            return getEndDateInput(); // Prompt the user again for input
        }
    }







    public Member selectMember(List<Member> members) {
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

    public Optional<Book> selectBook(List<Book> books) {
        System.out.println("Selecione o livro:");

        for (int i = 0; i < books.size(); i++) {
            System.out.println((i + 1) + ". " + books.get(i).getTitle());
        }

        System.out.println("0. Nenhum");

        int selection = getUserInput("Opção: ");
        if (selection == 0) {
            return Optional.empty();
        } else if (selection >= 1 && selection <= books.size()) {
            return Optional.of(books.get(selection - 1));
        } else {
            System.out.println("Opção inválida. Por favor, selecione uma opção válida.");
            return selectBook(books);
        }
    }


    public int getUserInput(String message) {
        System.out.print(message);
        return scanner.nextInt();
    }


    public List<Member> getMembers() {
        return reservationController.getAllMembers();
    }

    public List<Book> getBooks() {
        return reservationController.getAllBooks();
    }

    public List<CD> getCDs() {
        return reservationController.getAllCDs();
    }


    public void listAllReservations(){
        List<Reservation> reservations = reservationController.getAllReservations();

        if (reservations.isEmpty()) {
            System.out.println("Reservas não encotradas.");
        } else {
            System.out.println("Todas as reservas:");
            for (Reservation reservation : reservations) {
                System.out.println(reservation.toString());
            }
        }
    }


}
