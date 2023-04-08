package Controller;

import Data.CardData;
import Model.Card;
import Model.Member;

import java.util.ArrayList;
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
        List<Card> cards = cardData.load();
        if (cards == null) {
            cards = new ArrayList<>();
        }
        String cardNumber = "";
        boolean isUnique = false;
        int year = Calendar.getInstance().get(Calendar.YEAR) % 100;

        while (!isUnique) {
            int randomNumber = (int) (Math.random() * 900000) + 100000;
            cardNumber = String.format("%02d%06d", year, randomNumber);
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


    public List<Card> getAllCards() {
        return cardData.load();
    }
}
