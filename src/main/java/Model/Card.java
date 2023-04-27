/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author franc
 */
public class Card implements Serializable {

    private static int counter = 0;
    private int id;
    private Member member;
    private String cardNumber;
    private int borrowedBooks;
    private static final long serialVersionUID = 1L;


    public Card(Member member, String cardNumber) {
        this.id = counter++;
        this.member = member;
        this.cardNumber = cardNumber;
        this.borrowedBooks = 0;
    }

    public static void resetIdCounter(List<Card> cardList) {
        int maxId = cardList.stream().mapToInt(Card::getId).max().orElse(0);
        counter = maxId;
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


    @Override
    public String toString() {
        return String.format(
                "Cartão{%n" +
                        "    membro=%s%n" +
                        "    número do cartão='%s'%n" +
                        "    livros emprestados=%s%n" +
                        "}",
                member,
                cardNumber,
                borrowedBooks
        );
    }


}
