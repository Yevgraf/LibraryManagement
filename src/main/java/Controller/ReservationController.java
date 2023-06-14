package Controller;

import Data.BookData;
import Data.CDData;
import Data.ReservationData;
import Data.MemberData;
import Model.*;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
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


    public void addReservation(Member selectedMember, List<Book> selectedBooks, List<CD> selectedCDs, LocalDate endDate) {
        if (selectedMember == null) {
            System.out.println("Erro: Membro não selecionado.");
            return;
        }

        if ((selectedBooks == null || selectedBooks.isEmpty()) && (selectedCDs == null || selectedCDs.isEmpty())) {
            System.out.println("Erro: Nenhum item selecionado.");
            return;
        }

        LocalDate startDate = LocalDate.now();
        Reservation reservation = new Reservation(selectedMember, startDate, endDate);

        if (selectedBooks != null) {
            for (Book selectedBook : selectedBooks) {
                reservation.addBook(selectedBook);
                bookData.updateBookQuantityDecrease(selectedBook.getId());
            }
        }

        if (selectedCDs != null) {
            for (CD selectedCD : selectedCDs) {
                reservation.addCD(selectedCD);
                cdData.updateCDQuantityDecrease(selectedCD.getId());
            }
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


    private List<Reservation> filterMatchingReservations(List<Reservation> reservations) {
        return reservations.stream()
                .filter(reservation -> reservation.getState() == State.RESERVADO &&
                        (!reservation.getBooks().isEmpty() || !reservation.getCds().isEmpty()))
                .collect(Collectors.toList());
    }

    public void deliverReservation() {
        try {
            List<Reservation> reservations = reservationData.load();

            List<Reservation> matchingReservations = filterMatchingReservations(reservations);

            if (matchingReservations.isEmpty()) {
                System.out.println("Nenhuma reserva encontrada para o livro ou CD.");
                return;
            }

            displayReservations(matchingReservations);

            int itemSelection = getItemSelection(matchingReservations.size());

            if (itemSelection < 1 || itemSelection > matchingReservations.size()) {
                System.out.println("Seleção inválida.");
                return;
            }

            Reservation selectedReservation = matchingReservations.get(itemSelection - 1);

            updateReservationState(selectedReservation, State.ENTREGUE);
            int satisfactionRating = getSatisfactionRatingInput();
            String additionalComments = getAdditionalCommentsInput();

            updateReservationAdditionalInfo(selectedReservation, satisfactionRating, additionalComments);
            updateItemQuantities(selectedReservation);

            System.out.println("Reserva atualizada para 'ENTREGUE': " + selectedReservation.toString());
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao tentar alterar a reserva para 'ENTREGUE': " + e.getMessage());
        }
    }

    private void displayReservations(List<Reservation> reservations) {
        System.out.println("Reservas encontradas:");

        // Mapa para armazenar os itens de reserva agrupados pelo ID da reserva
        Map<Integer, List<String>> reservationItems = new HashMap<>();

        for (Reservation reservation : reservations) {
            List<Book> books = reservation.getBooks();
            List<CD> cds = reservation.getCds();
            Member member = reservation.getMember();

            String itemInfo;
            if (!books.isEmpty() && !cds.isEmpty()) {
                itemInfo = String.format("Livro: %s - %s\n   CD: %s - %s",
                        books.get(0).getTitle(), member.getName(), cds.get(0).getTitle(), member.getName());
            } else if (!books.isEmpty()) {
                itemInfo = String.format("Livro: %s - %s", books.get(0).getTitle(), member.getName());
            } else if (!cds.isEmpty()) {
                itemInfo = String.format("CD: %s - %s", cds.get(0).getTitle(), member.getName());
            } else {
                itemInfo = "Reserva inválida";
            }

            // Informações da reserva formatadas
            String reservationInfo = String.format("%d. %s\n   - Email: %s\n   - Data de devolução: %s",
                    reservations.indexOf(reservation) + 1, itemInfo, member.getEmail(),
                    reservation.getEndDate() != null ? reservation.getEndDate() : "N/A");

            // Verificar se a lista de itens para o ID da reserva já existe no mapa, caso contrário, criar uma nova lista
            List<String> items = reservationItems.getOrDefault(reservation.getId(), new ArrayList<>());
            items.add(reservationInfo);
            reservationItems.put(reservation.getId(), items);
        }

        // Iterar sobre os itens de reserva agrupados por ID e imprimir na tela
        for (List<String> items : reservationItems.values()) {
            items.forEach(System.out::println);
        }
    }




    private int getItemSelection(int maxSelection) {
        System.out.print("Selecione o item (digite o número correspondente): ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    private void updateReservationState(Reservation reservation, State newState) {
        reservationData.updateReservationState(reservation, newState);
    }

    private int getSatisfactionRatingInput() {
        Scanner scanner = new Scanner(System.in);
        int rating;
        while (true) {
            System.out.print("Insira a avaliação de satisfação (1-5): ");
            rating = scanner.nextInt();
            if (rating >= 1 && rating <= 5) {
                break;
            }
            System.out.println("Avaliação inválida. Insira um número entre 1 e 5.");
        }
        return rating;
    }

    private String getAdditionalCommentsInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Insira comentários adicionais: ");
        return scanner.nextLine();
    }

    private void updateReservationAdditionalInfo(Reservation reservation, int satisfactionRating, String additionalComments) {
        reservationData.updateReservationAdditionalInfo(reservation, satisfactionRating, additionalComments);
    }

    private void updateItemQuantities(Reservation reservation) {
        List<Book> books = reservation.getBooks();
        List<CD> cds = reservation.getCds();

        BookData bookData = new BookData();
        CDData cdData = new CDData();

        for (Book book : books) {
            bookData.updateBookQuantityIncrease(book.getId());
        }

        for (CD cd : cds) {
            cdData.updateCDQuantityIncrease(cd.getId());
        }
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
