package View;

import Controller.BookController;
import Controller.MemberController;
import Controller.ReservationController;
import Model.Book;
import Model.CD;
import Model.Member;
import Model.Reservation;

import java.time.LocalDate;
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
        List<Book> selectedBooks = selectItems("livro", books, 3);
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

        System.out.print("Digite o ID do membro: ");
        int memberId = scanner.nextInt();

        for (Member member : members) {
            if (member.getId() == memberId) {
                return member;
            }
        }

        System.out.println("Membro não encontrado. Tente novamente.");
        return null;
    }
    private <T> List<T> selectItems(String itemType, List<T> items, int maxItems) {
        List<T> selectedItems = new ArrayList<>();

        System.out.println("Selecione os " + itemType + "s (até " + maxItems + "):");
        int itemsRemaining = maxItems;

        if (items.size() == 1) {
            System.out.println("1. " + items.get(0).toString());
            System.out.println("0. Nenhum " + itemType);
            int choice;
            while (true) {
                System.out.print("Digite o número correspondente à opção desejada: ");
                choice = scanner.nextInt();

                if (choice == 1 || choice == 0) {
                    break;
                }

                System.out.println("Opção inválida. Tente novamente.");
            }

            if (choice == 1) {
                T selectedItem = items.get(0);
                selectedItems.add(selectedItem);
                items.remove(selectedItem);
                itemsRemaining--;
            } else if (choice == 0) {
                return selectedItems; // No item selected
            }
        } else {
            while (itemsRemaining > 0) {
                System.out.println("Itens restantes: " + itemsRemaining);
                displayItems(items);
                System.out.println("0. Nenhum " + itemType);

                System.out.print("Digite o ID do " + itemType + " ou 0 para encerrar a seleção: ");
                int itemId = scanner.nextInt();

                if (itemId == 0) {
                    break;
                }

                T selectedItem = findItemById(items, itemId);

                if (selectedItem == null) {
                    System.out.println(itemType + " não encontrado. Tente novamente.");
                    continue;
                }

                selectedItems.add(selectedItem);
                items.remove(selectedItem);
                itemsRemaining--;
            }
        }

        return selectedItems;
    }


    private <T> T findItemById(List<T> items, int itemId) {
        for (T item : items) {
            if (item instanceof Book && ((Book) item).getId() == itemId) {
                return item;
            } else if (item instanceof CD && ((CD) item).getId() == itemId) {
                return item;
            }
        }
        return null;
    }

    private LocalDate getEndDateInput() {
        System.out.print("Digite a data de término (AAAA-MM-DD): ");
        String endDateString = scanner.next();

        try {
            return LocalDate.parse(endDateString);
        } catch (Exception e) {
            System.out.println("Data inválida. Tente novamente.");
            return null;
        }
    }

    private void displayMembers(List<Member> members) {
        for (Member member : members) {
            System.out.println(member.getId() + ". " + member.getName());
        }
    }

    private <T> void displayItems(List<T> items) {
        for (int i = 0; i < items.size(); i++) {
            T item = items.get(i);
            if (item instanceof Book) {
                Book book = (Book) item;
                System.out.println((i + 1) + ". " + book.getTitle());
            } else if (item instanceof CD) {
                CD cd = (CD) item;
                System.out.println((i + 1) + ". " + cd.getTitle());
            }
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

