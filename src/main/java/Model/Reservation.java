package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Reservation {

    private int id;
    private Book book;
    private Member member;
    private LocalDate startDate;
    private LocalDate endDate;
    private State state;
    private int satisfactionRating;
    private String additionalComments;
    private CD cd;

    public Reservation(Member member, Book book, LocalDate startDate, LocalDate endDate) {
        this.member = member;
        this.book = book;
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = State.PENDENTE;
        this.satisfactionRating = 0;
        this.additionalComments = "";
    }

    public Reservation(Member member, CD cd, LocalDate startDate, LocalDate endDate) {
        this.member = member;
        this.cd = cd;
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = State.PENDENTE;
        this.satisfactionRating = 0;
        this.additionalComments = "";
    }

    public Reservation(int id, Member member, Book book, LocalDate startDate, LocalDate endDate, State state, int satisfactionRating, String additionalComments) {
        this.id = id;
        this.member = member;
        this.book = book;
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = state;
        this.satisfactionRating = satisfactionRating;
        this.additionalComments = additionalComments;
    }

    public Reservation(int id, Member member, CD cd, LocalDate startDate, LocalDate endDate, State state, int satisfactionRating, String additionalComments) {
        this.id = id;
        this.member = member;
        this.cd = cd;
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = state;
        this.satisfactionRating = satisfactionRating;
        this.additionalComments = additionalComments;
    }

    public Reservation(int id, Book book, CD cd, Member member, LocalDate startDate, LocalDate endDate, State state, int satisfactionRating, String additionalComments) {
        this.id = id;
        this.book = book;
        this.cd = cd;
        this.member = member;
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = state;
        this.satisfactionRating = satisfactionRating;
        this.additionalComments = additionalComments;
    }

    public Reservation(Member member, Book book, CD cd, LocalDate startDate, LocalDate endDate) {
        this.member = member;
        this.book = book;
        this.cd = cd;
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = State.RESERVADO;
        this.satisfactionRating = 0;
        this.additionalComments = "";
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getSatisfactionRating() {
        return satisfactionRating;
    }

    public void setSatisfactionRating(int satisfactionRating) {
        this.satisfactionRating = satisfactionRating;
    }

    public String getAdditionalComments() {
        return additionalComments;
    }

    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }

    public CD getCd() {
        return cd;
    }

    public void setCd(CD cd) {
        this.cd = cd;
    }

    public int getItemsCount() {
        int itemCount = 0;

        if (book != null) {
            itemCount++;
        }

        if (cd != null) {
            itemCount++;
        }

        return itemCount;
    }


    public Reservation(int id, Book book, Member member, LocalDate startDate, LocalDate endDate, State state, int satisfactionRating, String additionalComments) {
        this.id = id;
        this.book = book;
        this.member = member;
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = state;
        this.satisfactionRating = satisfactionRating;
        this.additionalComments = additionalComments;
    }
    public Reservation(int id, CD cd, Member member, LocalDate startDate, LocalDate endDate, State state, int satisfactionRating, String additionalComments) {
        this.id = id;
        this.cd = cd;
        this.member = member;
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = state;
        this.satisfactionRating = satisfactionRating;
        this.additionalComments = additionalComments;
    }

    @Override
    public String toString() {
        String reservationInfo = "Reserva #" + id + ":\n";

        if (book != null) {
            reservationInfo += "Livro: " + book.getTitle() + " por " + book.getAuthor() + "\n";
        } else {
            reservationInfo += "Livro: Nenhum livro associado\n";
        }

        if (cd != null) {
            reservationInfo += "CD: " + cd.getTitle() + " artista: " + cd.getArtist() + "\n";
        } else {
            reservationInfo += "CD: Nenhum CD associado\n";
        }

        reservationInfo +=
                "Reservado por: " + member.getName() + "\n" +
                        "Reservado em: " + startDate + "\n" +
                        "Termina em: " + endDate + "\n" +
                        "Estado: " + state + "\n";

        return reservationInfo;
    }

}
