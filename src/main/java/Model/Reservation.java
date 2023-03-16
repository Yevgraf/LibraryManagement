/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.time.LocalDate;

/**
 * @author franc
 */

public class Reservation {
    private static int counter = 0;
    private int id;
    private Book book;
    private Card card;
    private LocalDate startDate;
    private LocalDate endDate;

    public Reservation(Book book, Card card, LocalDate startDate, LocalDate endDate) {
        this.id = counter++;
        this.book = book;
        this.card = card;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public Card getCard() {
        return card;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
