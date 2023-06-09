package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reservation {

    private int id;
    private List<Book> books;
    private List<CD> cds;
    private Member member;
    private LocalDate startDate;
    private LocalDate endDate;
    private State state;
    private int satisfactionRating;
    private String additionalComments;

    public Reservation(Member member, Book book, LocalDate startDate, LocalDate endDate) {
        this.member = member;
        this.books = new ArrayList<>();
        this.cds = new ArrayList<>();
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = State.PENDENTE;
        this.satisfactionRating = 0;
        this.additionalComments = "";

        if (book != null) {
            this.books.add(book);
        }
    }
    public Reservation(Member member, LocalDate startDate, LocalDate endDate) {
        this.member = member;
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = State.PENDENTE;
        this.satisfactionRating = 0;
        this.additionalComments = "";
        this.books = new ArrayList<>();
        this.cds = new ArrayList<>();
    }


    public Reservation(Member member, CD cd, LocalDate startDate, LocalDate endDate) {
        this.member = member;
        this.books = new ArrayList<>();
        this.cds = new ArrayList<>();
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = State.PENDENTE;
        this.satisfactionRating = 0;
        this.additionalComments = "";

        if (cd != null) {
            this.cds.add(cd);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public List<CD> getCds() {
        return cds;
    }

    public void setCds(List<CD> cds) {
        this.cds = cds;
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

    public void addBook(Book book) {
        this.books.add(book);
    }

    public void addCD(CD cd) {
        this.cds.add(cd);
    }

    public int getItemsCount() {
        int itemCount = 0;

        if (!books.isEmpty()) {
            itemCount += books.size();
        }

        if (!cds.isEmpty()) {
            itemCount += cds.size();
        }

        return itemCount;
    }

    @Override
    public String toString() {
        StringBuilder reservationInfo = new StringBuilder("Reserva #" + id + ":\n");

        if (!books.isEmpty()) {
            reservationInfo.append("Livros:\n");
            for (Book book : books) {
                reservationInfo.append("  - ").append(book.getTitle()).append(" por ").append(book.getAuthor()).append("\n");
            }
        } else {
            reservationInfo.append("Livros: Nenhum livro associado\n");
        }

        if (!cds.isEmpty()) {
            reservationInfo.append("CDs:\n");
            for (CD cd : cds) {
                reservationInfo.append("  - ").append(cd.getTitle()).append(" artista: ").append(cd.getArtist()).append("\n");
            }
        } else {
            reservationInfo.append("CDs: Nenhum CD associado\n");
        }

        reservationInfo.append("Reservado por: ").append(member.getName()).append("\n")
                .append("Reservado em: ").append(startDate).append("\n")
                .append("Termina em: ").append(endDate).append("\n")
                .append("Estado: ").append(state).append("\n");

        return reservationInfo.toString();
    }
}
