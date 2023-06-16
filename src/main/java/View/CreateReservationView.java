package View;

import Controller.BookController;
import Controller.MemberController;
import Controller.ReservationController;
import Model.Book;
import Model.CD;
import Model.Member;
import Model.Reservation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
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
        List<Member> members = reservationController.getAllMembers();
        List<Book> books = reservationController.getAllBooks();
        List<CD> cds = reservationController.getAllCDs();

        Member selectedMember = selectMember(members);
        List<Book> selectedBooks = selectItems("Livro", books, 3);
        List<CD> selectedCDs = selectItems("CD", cds, 3);
        LocalDate endDate = getEndDateInput();

        // Check if any of the inputs are null or empty
        if (selectedMember == null || (selectedBooks.isEmpty() && selectedCDs.isEmpty()) || endDate == null) {
            System.out.println("Erro: Reserva não criada devido a inputs inválidos.");
            return;
        }

        // Pass the inputs to the addReservation() method
        reservationController.addReservation(selectedMember, selectedBooks, selectedCDs, endDate);
    }

    private Member selectMember(List<Member> members) {
        System.out.println("Selecione um membro:");
        displayMembers(members);

        System.out.print("Digite o número do membro: ");
        int memberNumber = scanner.nextInt();

        if (memberNumber == 0) {
            return null; // User canceled the selection
        } else if (memberNumber < 1 || memberNumber > members.size()) {
            System.out.println("Número inválido. Tente novamente.");
            return selectMember(members); // Retry the selection
        }

        return members.get(memberNumber - 1);
    }

    private <T> List<T> selectItems(String itemType, List<T> items, int maxItems) {
        List<T> selectedItems = new ArrayList<>();

        int itemsRemaining = maxItems;

        while (itemsRemaining > 0) {
            System.out.println("Itens restantes: " + itemsRemaining);
            System.out.println("0. Cancelar e voltar");
            System.out.println("1. Listar Livros");
            System.out.println("2. Listar CDs");
            System.out.println("3. Pesquisar Itens (Livros e CDs)");
            System.out.println("4. Finalizar reserva");

            System.out.print("Digite o número da opção: ");
            int optionNumber = scanner.nextInt();

            if (optionNumber == 0) {
                break; // Exit the loop and return the selected items so far
            } else if (optionNumber == 1 && itemType.equals("Livro")) {
                displayBooks((List<Book>) items, itemsRemaining);
            } else if (optionNumber == 2 && itemType.equals("CD")) {
                displayCDs((List<CD>) items, itemsRemaining);
            } else if (optionNumber == 3) {
                if (itemType.equals("Livro")) {
                    displayBooks((List<Book>) items, itemsRemaining);
                } else if (itemType.equals("CD")) {
                    displayCDs((List<CD>) items, itemsRemaining);
                }
            } else if (optionNumber == 4) {
                break; // Finalize the selection
            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }

            if (items.isEmpty()) {
                System.out.println("Nenhum item disponível.");
                break; // Exit the loop and return the selected items so far
            }

            System.out.print("Digite o número do item: ");
            int itemNumber = scanner.nextInt();

            if (itemNumber == 0) {
                break; // Exit the loop and return the selected items so far
            } else if (itemNumber < 1 || itemNumber > items.size()) {
                System.out.println("Número inválido. Tente novamente.");
            } else {
                T selectedItem = items.get(itemNumber - 1);
                selectedItems.add(selectedItem);
                items.remove(selectedItem);
                itemsRemaining--;
            }
        }

        return selectedItems;
    }



    private void displayItems(List<?> items, String itemType, int itemsRemaining) {
        System.out.println(itemType + "s disponíveis:");

        for (int i = 0; i < items.size(); i++) {
            Object item = items.get(i);
            System.out.println((i + 1) + ".");

            if (item instanceof Book) {
                Book book = (Book) item;
                System.out.println("   Título: " + book.getTitle());
                System.out.println("   Autor: " + book.getAuthor().getName());
                System.out.println("   ISBN: " + book.getIsbn());
                System.out.println("   Quantidade: " + book.getQuantity());
            } else if (item instanceof CD) {
                CD cd = (CD) item;
                System.out.println("   Título: " + cd.getTitle());
                System.out.println("   Artista: " + cd.getArtist().getName());
                System.out.println("   Género: " + cd.getCategory().getCategoryName());
                System.out.println("   Quantidade: " + cd.getQuantity());
            }
            System.out.println();
        }

        System.out.println("0. Voltar");
        System.out.println("Itens restantes: " + itemsRemaining);
    }



    private void displayBooks(List<Book> books, int itemsRemaining) {
        displayItems(books, "Livro", itemsRemaining);
    }

    private void displayCDs(List<CD> cds, int itemsRemaining) {
        displayItems(cds, "CD", itemsRemaining);
    }



    private LocalDate getEndDateInput() {
        System.out.print("Digite a data de término (DD/MM/AAAA): ");
        String endDateString = scanner.next();

        try {
            return LocalDate.parse(endDateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            System.out.println("Data inválida. Tente novamente.");
            return getEndDateInput(); // Retry getting the end date
        }
    }

    private void displayMembers(List<Member> members) {
        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            int memberItemNumber = member.getBorrowedBooks().size() + member.getBorrowedCDs().size();
            System.out.println((i + 1) + ". Name: " + member.getName() + ", Email: " + member.getEmail() + ", Itens reservados: " + memberItemNumber);
        }
    }

    public void listAllReservations() {
        List<Reservation> reservations = reservationController.getAllReservations();

        if (reservations.isEmpty()) {
            System.out.println("Reservas não encontradas.");
        } else {
            System.out.println("Todas as reservas:");
            for (Reservation reservation : reservations) {
                System.out.println(reservation.toString());
            }
        }
    }

}
