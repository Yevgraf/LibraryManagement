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

    /**
     * Cria uma instância de CreateReservationView.
     *
     * @param memberController      o controlador de membros associado à view
     * @param bookController        o controlador de livros associado à view
     * @param reservationController o controlador de reservas associado à view
     * @param scanner               o scanner utilizado para entrada de dados
     */
    public CreateReservationView(MemberController memberController, BookController bookController, ReservationController reservationController, Scanner scanner) {
        this.memberController = memberController;
        this.bookController = bookController;
        this.reservationController = reservationController;
        this.scanner = scanner;
    }

    /**
     * Executa o processo de criação de uma nova reserva.
     */
    public void createReservation() {
        // Obter as entradas necessárias
        List<Member> members = reservationController.getAllMembers();
        List<Book> books = reservationController.getAllBooks();
        List<CD> cds = reservationController.getAllCDs();

        Member selectedMember = selectMember(members);
        List<Book> selectedBooks = selectItems("livro", books, 3);
        List<CD> selectedCDs = selectItems("CD", cds, 3);
        LocalDate endDate = getEndDateInput();

        // Verificar se alguma das entradas é nula ou vazia
        if (selectedMember == null || (selectedBooks.isEmpty() && selectedCDs.isEmpty()) || endDate == null) {
            System.out.println("Erro: Reserva não criada devido a entradas inválidas.");
            return;
        }

        // Passar as entradas para o método addReservation()
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

    /**
     * Seleciona os itens (livros ou CDs) para a reserva.
     *
     * @param itemType   o tipo de item a ser selecionado (livro ou CD)
     * @param items      a lista de itens disponíveis para seleção
     * @param maxItems   o número máximo de itens que podem ser selecionados
     * @param <T>        o tipo genérico do item
     * @return a lista de itens selecionados
     */
    private <T> List<T> selectItems(String itemType, List<T> items, int maxItems) {
        List<T> selectedItems = new ArrayList<>();

        System.out.println("Selecione os " + itemType + "s (até " + maxItems + "):");
        int itemsRemaining = maxItems;

        while (itemsRemaining > 0 && !items.isEmpty()) {
            System.out.println("Itens restantes: " + itemsRemaining);
            displayItems(items);
            System.out.println("0. Nenhum " + itemType);

            System.out.print("Digite o número do " + itemType + " ou 0 para encerrar a seleção: ");
            int itemNumber = scanner.nextInt();

            if (itemNumber == 0) {
                break;
            }

            if (itemNumber < 1 || itemNumber > items.size()) {
                System.out.println("Número inválido. Tente novamente.");
                continue;
            }

            T selectedItem = items.get(itemNumber - 1);
            selectedItems.add(selectedItem);
            items.remove(selectedItem);
            itemsRemaining--;
        }

        return selectedItems;
    }

    /**
     * Obtém a entrada da data de término da reserva.
     *
     * @return a data de término da reserva
     */
    private LocalDate getEndDateInput() {
        System.out.print("Digite a data de término (DD/MM/AAAA): ");
        String endDateString = scanner.next();

        try {
            return LocalDate.parse(endDateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            System.out.println("Data inválida. Tente novamente.");
            return null;
        }
    }

    /**
     * Exibe na saída padrão a lista de membros disponíveis para seleção.
     *
     * @param members a lista de membros
     */
    private void displayMembers(List<Member> members) {
        for (Member member : members) {
            System.out.println(member.getId() + ". " + member.getName());
        }
    }

    /**
     * Exibe na saída padrão a lista de itens disponíveis para seleção.
     *
     * @param items a lista de itens
     * @param <T>   o tipo genérico do item
     */
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

    /**
     * Lista todas as reservas.
     */
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
