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
    private List<Book> selectedBooks;
    private List<CD> selectedCDs;

    public CreateReservationView(MemberController memberController, BookController bookController, ReservationController reservationController, Scanner scanner) {
        this.memberController = memberController;
        this.bookController = bookController;
        this.reservationController = reservationController;
        this.scanner = scanner;
        this.selectedBooks = new ArrayList<>();
        this.selectedCDs = new ArrayList<>();
    }

    public void createReservation() {
        List<Book> books = reservationController.getAllBooks();
        List<CD> cds = reservationController.getAllCDs();
        List<Member> members = memberController.listMembers();
        Member selectedMember = selectMember(members);

        if (selectedMember == null) {
            System.out.println("Seleção de membro cancelada.");
            return;
        }

        int totalItemsSelected = 0;

        while (true) {
            System.out.println("Selecione uma opção:");
            System.out.println("1. Listar Livros");
            System.out.println("2. Listar CDs");
            System.out.println("3. Pesquisar Itens");
            System.out.println("4. Finalizar Reserva");
            System.out.println("0. Cancelar e voltar");

            int option = readOptionNumber();

            switch (option) {
                case 1:
                    int booksSelected = selectItems(books, "Livro", 3 - totalItemsSelected);
                    totalItemsSelected += booksSelected;
                    break;
                case 2:
                    int cdsSelected = selectItems(cds, "CD", 3 - totalItemsSelected);
                    totalItemsSelected += cdsSelected;
                    break;
                case 3:
                    totalItemsSelected += searchAndSelectItem(books, cds, selectedBooks, selectedCDs, totalItemsSelected);
                    break;
                case 4:
                    finalizeReservation(selectedMember, LocalDate.now().plusDays(7));

                    return;
                case 0:
                    System.out.println("Seleção cancelada.");
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        }
    }

    private <T> int selectItems(List<T> items, String itemType, int maxItems) {
        int itemsSelected = 0;

        while (maxItems > 0) {
            displayItems(items, itemType, maxItems);
            int optionNumber = readOptionNumber();

            if (optionNumber == 0) {
                break; // Exit the loop and return the selected items
            } else if (optionNumber > items.size()) {
                System.out.println("Opção inválida. Tente novamente.");
                continue;
            }

            T selectedItem = items.get(optionNumber - 1);

            if (itemType.equals("Livro")) {
                selectedBooks.add((Book) selectedItem);
            } else if (itemType.equals("CD")) {
                selectedCDs.add((CD) selectedItem);
            }

            items.remove(selectedItem);
            maxItems--;
            itemsSelected++;

            System.out.println(itemType + " selecionado: " + selectedItem.toString());
        }

        return itemsSelected;
    }



    private void displayItems(List<?> items, String itemType, int itemsRemaining) {
        System.out.println(itemType + "s disponíveis:");

        for (int i = 0; i < items.size(); i++) {
            Object item = items.get(i);
            System.out.println((i + 1) + ".");
            displayItemDetails(item);
        }

        if (itemsRemaining > 0) {
            System.out.println("0. Voltar");
            System.out.println("Itens restantes: " + itemsRemaining);
        } else {
            System.out.println("0. Voltar");
        }
    }


    private void displayItemDetails(Object item) {
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
            System.out.println("   Gênero: " + cd.getCategory().getCategoryName());
            System.out.println("   Quantidade: " + cd.getQuantity());
        }
        System.out.println();
    }

    private int readOptionNumber() {
        while (true) {
            System.out.print("Digite o número da opção: ");
            String optionNumberString = scanner.nextLine();

            try {
                int optionNumber = Integer.parseInt(optionNumberString);
                return optionNumber;
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void finalizeReservation(Member selectedMember, LocalDate endDate) {
        reservationController.addReservation(selectedMember, selectedBooks, selectedCDs, endDate);
    }


    private List<Book> searchBooks(List<Book> books, String searchTerm) {
        List<Book> matchedBooks = new ArrayList<>();

        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(searchTerm.toLowerCase())) {
                matchedBooks.add(book);
            }
        }

        return matchedBooks;
    }

    private List<CD> searchCDs(List<CD> cds, String searchTerm) {
        List<CD> matchedCDs = new ArrayList<>();

        for (CD cd : cds) {
            if (cd.getTitle().toLowerCase().contains(searchTerm.toLowerCase())) {
                matchedCDs.add(cd);
            }
        }

        return matchedCDs;
    }

    private Member selectMember(List<Member> members) {
        displayMembers(members);

        System.out.print("Digite o número do membro: ");
        int memberNumber = readOptionNumber();

        if (memberNumber == 0) {
            return null; // User canceled the selection
        } else if (memberNumber < 1 || memberNumber > members.size()) {
            System.out.println("Número inválido. Tente novamente.");
            return selectMember(members); // Retry the selection
        }

        return members.get(memberNumber - 1);
    }

    private void displayMembers(List<Member> members) {
        System.out.println("Membros disponíveis:");

        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            int memberItemNumber = member.getBorrowedBooks().size() + member.getBorrowedCDs().size();

            System.out.println((i + 1) + ".");
            System.out.println("   Nome: " + member.getName());
            System.out.println("   Email: " + member.getEmail());
            System.out.println("   Itens reservados: " + memberItemNumber);
            System.out.println();
        }

        System.out.println("0. Voltar");
    }

    private int searchAndSelectItem(List<Book> books, List<CD> cds, List<Book> selectedBooks, List<CD> selectedCDs, int totalItemsSelected) {
        System.out.println("Digite o termo de pesquisa:");
        String searchTerm = scanner.nextLine();

        List<Book> matchedBooks = searchBooks(books, searchTerm);
        List<CD> matchedCDs = searchCDs(cds, searchTerm);

        if (matchedBooks.isEmpty() && matchedCDs.isEmpty()) {
            System.out.println("Nenhum item encontrado. Tente novamente.");
            return 0;
        }

        int itemsSelected = 0;
        int maxItems = 3 - totalItemsSelected;

        while (maxItems > 0) {
            System.out.println("Itens encontrados:");
            displayItems(matchedBooks, "Livro", matchedBooks.size());
            displayItems(matchedCDs, "CD", matchedCDs.size());
            System.out.println("0. Voltar");

            int option = readOptionNumber();

            if (option == 0) {
                break;
            } else if (option > 0 && option <= matchedBooks.size()) {
                Book selectedBook = matchedBooks.get(option - 1);
                selectedBooks.add(selectedBook);
                books.remove(selectedBook);
                itemsSelected++;
                maxItems--;
                System.out.println("Livro selecionado: " + selectedBook.toString());
            } else if (option > matchedBooks.size() && option <= (matchedBooks.size() + matchedCDs.size())) {
                CD selectedCD = matchedCDs.get(option - matchedBooks.size() - 1);
                selectedCDs.add(selectedCD);
                cds.remove(selectedCD);
                itemsSelected++;
                maxItems--;
                System.out.println("CD selecionado: " + selectedCD.toString());
            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }

            if (itemsSelected >= 3) {
                break;
            }
        }

        return itemsSelected > 0 ? itemsSelected : 0; // Retorna 0 apenas se nenhum item foi selecionado
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
