package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int counter = 0;
    private int id;
    private Book book;
    private Member member;
    private LocalDate startDate;
    private LocalDate endDate;

    public Reservation(Book book, Member member, LocalDate startDate, LocalDate endDate) {
        this.id = counter++;
        this.book = book;
        this.member = member;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Reservation #" + id + ":\n" +
                "Book: " + book.getTitle() + " by " + book.getAuthor() + "\n" +
                "Reserved by: " + member.getName() + "\n" +
                "Reserved on: " + startDate + "\n" +
                "Ends on: " + endDate + "\n";
    }

    public static void resetIdCounter(List<Reservation> reservationList) {
        int maxId = reservationList.stream().mapToInt(Reservation::getId).max().orElse(0);
        counter = maxId;
    }


}
