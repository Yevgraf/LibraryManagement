package Controller;

import Data.CardData;
import Model.Card;
import Model.Member;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class CardController {
    private CardData cardData;

    public CardController(CardData cardData) {
        this.cardData = cardData;
    }

    /**
     * Creates a new card for the specified member with the given card number.
     *
     * @param member     The member for whom the card is being created.
     * @param cardNumber The card number to assign to the card.
     * @return The newly created card.
     * @throws IllegalArgumentException if the member is null.
     */

    public Card createCard(Member member, String cardNumber) {
        if (member == null) {
            throw new IllegalArgumentException("Membro não pode ser nulo");
        }


        Card card = new Card(member, cardNumber);


        cardData.save(card);


        return card;
    }

    /**
     * Generates a unique card number for a new card based on the member ID and current year.
     *
     * @param memberId The ID of the member.
     * @return A unique card number.
     */
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
