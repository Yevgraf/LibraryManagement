package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Member extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    private int maxBorrowedBooks;
    private Card card;
    private List<Book> borrowedBooks;
    private List<Book> reservedBooks;

    public Member(String name, String address, LocalDate birthDate, String phone, String email) {
        super(name, address, birthDate, phone, email);
        this.maxBorrowedBooks = 3;
        this.borrowedBooks = new ArrayList<Book>();
        this.reservedBooks = new ArrayList<Book>();
    }

    public int getMaxBorrowedBooks() {
        return maxBorrowedBooks;
    }

    public void setMaxBorrowedBooks(int maxBorrowedBooks) {
        this.maxBorrowedBooks = maxBorrowedBooks;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public List<Book> getReservedBooks() {
        return reservedBooks;
    }

    public void borrowBook(Book book) throws Exception {
        if (borrowedBooks.size() == maxBorrowedBooks) {
            throw new Exception("O número máximo de livros emprestados foi atingido.");
        }
        borrowedBooks.add(book);
    }

    public void returnBook(Book book) {
        borrowedBooks.remove(book);
    }

    public void addReservation(Reservation reservation) throws Exception {
        if (reservedBooks.contains(reservation.getBook())) {
            throw new Exception("O livro já foi reservado pelo membro.");
        }
        reservedBooks.add(reservation.getBook());
    }

    public void removeReservation(Reservation reservation) {
        reservedBooks.remove(reservation.getBook());
    }

    public void addReservedBook(Book book) throws Exception {
        if (reservedBooks.contains(book)) {
            throw new Exception("O livro já foi reservado pelo membro.");
        }
        reservedBooks.add(book);
    }

    public void removeReservedBook(Book book) {
        reservedBooks.remove(book);
    }

}
