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

    private List<CD> borrowedCDs;

    public Member(int id, int maxBorrowedBooks, Card card) {
        this.id = id;
        this.maxBorrowedBooks = maxBorrowedBooks;
        this.card = card;
        this.borrowedBooks = new ArrayList<>();
        this.borrowedCDs = new ArrayList<>();
    }

    public Member(String name, String address, LocalDate birthDate, String phone, String email) {
        super(name, address, birthDate, phone, email);
        this.maxBorrowedBooks = 3;
        this.borrowedBooks = new ArrayList<>();
    }


    public Member(int id, String name, String address, LocalDate birthDate, String phone, String email, int maxBorrowedBooks) {
        super(name, address, birthDate, phone, email);
        this.id = id;
        this.maxBorrowedBooks = maxBorrowedBooks;
        this.borrowedBooks = new ArrayList<>();
        this.borrowedCDs = new ArrayList<>();
    }

    public Member(String name, String address, LocalDate birthDate, String phone, String email, int id, int maxBorrowedBooks, Card card, List<Book> borrowedBooks) {
        super(name, address, birthDate, phone, email);
        this.id = id;
        this.maxBorrowedBooks = maxBorrowedBooks;
        this.card = card;
        this.borrowedBooks = borrowedBooks;
        this.borrowedCDs = new ArrayList<>();
    }
    public Member(int id, User user, String name, String address, LocalDate birthDate, String phone, String email, int maxBorrowedBooks) {
        super(user.getId(), name, address, birthDate, phone, email);
        this.id = id;
        this.maxBorrowedBooks = maxBorrowedBooks;
        this.borrowedBooks = new ArrayList<>();
        this.borrowedCDs = new ArrayList<>();
    }

    public Member(int id, String name, String address, LocalDate birthDate, String phone, String email, int id1, int maxBorrowedBooks, Card card, List<Book> borrowedBooks) {
        super(id, name, address, birthDate, phone, email);
        this.id = id1;
        this.maxBorrowedBooks = maxBorrowedBooks;
        this.card = card;
        this.borrowedBooks = borrowedBooks;
        this.borrowedCDs = new ArrayList<>();
    }

    public Member(int id, int maxBorrowedBooks, Card card, List<Book> borrowedBooks) {
        this.id = id;
        this.maxBorrowedBooks = maxBorrowedBooks;
        this.card = card;
        this.borrowedBooks = borrowedBooks;
        this.borrowedCDs = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setMemberId(int id){
        this.id = id;
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

    public List<CD> getBorrowedCDs() {
        return borrowedCDs;
    }

    public void setBorrowedCDs(List<CD> borrowedCDs) {
        this.borrowedCDs = borrowedCDs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Membro:\n");
        sb.append("  ID: ").append(id).append("\n");
        sb.append("  Nome: ").append(getName()).append("\n");
        sb.append("  Email: ").append(getEmail()).append("\n");
        sb.append("  Livros emprestados: ").append(borrowedBooks.stream()
                .map(Book::getTitle)
                .collect(Collectors.joining(", "))).append("\n");
        sb.append("  CDs emprestados: ").append(borrowedCDs.stream()
                .map(CD::getTitle)
                .collect(Collectors.joining(", ")));
        return sb.toString();
    }


    public void addBorrowedBook(Book book) {
        borrowedBooks.add(book);
    }

    public void addBorrowedCD(CD cd){
        borrowedCDs.add(cd);
    }
}

