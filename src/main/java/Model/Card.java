/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.Date;

/**
 * @author franc
 */
public class Card {
    private static int counter = 0;
    private int id;
    private Member member;
    private String cardNumber;
    private int borrowedBooks;

    public Card(Member member, String cardNumber) {
        this.id = counter++;
        this.member = member;
        this.cardNumber = cardNumber;
        this.borrowedBooks = 0;
    }

    public int getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(int borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public boolean canBorrow() {
        return borrowedBooks < member.getMaxBorrowedBooks();
    }

    public void borrowBook() {

        if (canBorrow()) {
            borrowedBooks++;
        } else {
            throw new IllegalStateException("Membro já tem o máximo de livros reservados");
        }
    }

    public void returnBook() {
        borrowedBooks--;
    }
}
