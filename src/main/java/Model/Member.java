package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Member extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    private int counter = 1;
    private int id;
    private int maxBorrowedBooks;
    private Card card;
    private List<Book> borrowedBooks;

    public Member(String name, String address, LocalDate birthDate, String phone, String email) {
        super(name, address, birthDate, phone, email);
        this.id = counter++;
        this.maxBorrowedBooks = 3;
        this.borrowedBooks = new ArrayList<Book>();
    }

    public int getId() {
        return id;
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

    public void setBorrowedBooks(List<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }
}

