package Controller;

import Data.CardData;
import Model.Card;
import Model.Member;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class CardController {
    private CardData cardData;

    public CardController(CardData cardData) {
        this.cardData = cardData;
    }

    public void saveCards(List<Card> cards) {
        cardData.save(cards);
    }

    public Card createCard(Member member, String cardNumber) {
        Card card = new Card(member, cardNumber);
        List<Card> cards = cardData.load();
        cards.add(card);
        saveCards(cards);
        return card;
    }
    public String generateCardNumber(int memberId) {
        List<Card> cards = CardData.load();
        String cardNumber = "";
        boolean isUnique = false;
        int year = Calendar.getInstance().get(Calendar.YEAR) % 100;

        while (!isUnique) {
            int randomNumber = (int) (Math.random() * 900000) + 100000; // generate a random number between 100000 and 999999
            cardNumber = String.format("%02d%06d", year, randomNumber); // concatenate year and random number
            isUnique = true;

            for (Card card : cards) {
                if (card.getCardNumber().equals(cardNumber)) {
                    isUnique = false;
                    break;
                }
            }
        }

        return cardNumber;
    }



    public void borrowBook(Card card) {
        if (card.canBorrow()) {
            card.borrowBook();
            List<Card> cards = cardData.load();
            saveCards(cards);
        } else {
            throw new IllegalStateException("Membro já tem o máximo de livros reservados");
        }
    }

    public void returnBook(Card card) {
        card.returnBook();
        List<Card> cards = cardData.load();
        saveCards(cards);
    }

    public List<Card> getAllCards() {
        return cardData.load();
    }
}
