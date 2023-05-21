package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Member extends User  {

    private int id;
    private int maxBorrowedBooks;
    private Card card;
    private List<Book> borrowedBooks;

    public Member(String name, String address, LocalDate birthDate, String phone, String email) {
        super(name, address, birthDate, phone, email);
        this.maxBorrowedBooks = 3;
        this.borrowedBooks = new ArrayList<Book>();
    }
    public Member(int id, String name, String address, LocalDate birthDate, String phone, String email, int maxBorrowedBooks) {
        super(name, address, birthDate, phone, email);
        this.id = id;
        this.maxBorrowedBooks = maxBorrowedBooks;
        this.borrowedBooks = new ArrayList<>();
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

    @Override
    public String toString() {
        return "Membro:\n" +
                "  ID: " + id + "\n" +
                "  Nome: " + getName() + "\n" +
                "  Email: " + getEmail() + "\n" +
                "  Livros emprestados: " + borrowedBooks.stream()
                .map(Book::getTitle)
                .collect(Collectors.joining(", "));
    }



}

