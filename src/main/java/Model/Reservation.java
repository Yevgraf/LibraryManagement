package Model;

import java.io.Serializable;
import java.time.LocalDate;

public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    private static transient int counter = 0;
    private int id;
    private Book book;
    private Member member;
    private LocalDate date;

    public Reservation(Book book, Member member, LocalDate date) {
        this.id = counter++;
        this.book = book;
        this.member = member;
        this.date = date;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Reservation #" + id + ":\n" +
                "Book: " + book.getTitle() + " by " + book.getAuthor() + "\n" +
                "Reserved by: " + member.getName() + "\n" +
                "Reserved on: " + date + "\n";
    }

}
