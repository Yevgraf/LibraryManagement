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
public class Card  {


    private int id;
    private Member member;
    private String cardNumber;




    public Card(Member member, String cardNumber) {
        this.member = member;
        this.cardNumber = cardNumber;

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

    public void setId(int id) {
        this.id = id;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public String toString() {
        return String.format(
                "Cartão{%n" +
                        "    membro=%s%n" +
                        "    número do cartão='%s'%n" +
                        "}",
                member,
                cardNumber
        );
    }


}
