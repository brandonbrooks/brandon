package brandon.utils;

import java.util.ArrayList;
import java.util.Random;
import java.io.Serializable;

/** A deck of cards */
public class CardDeck extends ArrayList<Integer> implements Serializable {

    public CardDeck() {}

    /** Add cards to the deck */
    public void addCards(int numcards, int cardType) {
        for (int i = 0; i < numcards; i++) {
            add(cardType);
        }
    }

    /** shuffle the deck */
    public void shuffle() {

        CardDeck oldDeck = (CardDeck) clone();

        this.clear();

        Random r = new Random(System.currentTimeMillis());
        int decksize = oldDeck.size();

        for (int i = 0; i < decksize; i++) {
            int  index = r.nextInt(oldDeck.size());
            int cardNumber = oldDeck.get(index);
            add(cardNumber);
            oldDeck.remove(index);
        }
    }

    /** Returns a card drawn from the deck */
    public int draw() {
        if (size() > 0) {
            int cardNumber = get(0);
            remove(0);

            return cardNumber;
        }
        
        // ERROR! The deck is empty!
        return -1;
    }

    /** Add a card to the deck */
    public void addCard(int cardNumber) {
        add(cardNumber);
    }
    
    /** Sort a collection of cards */
    /*public static Collection<Integer> sort(Collection<Integer> originalCards) {
        final String methodName = "sort(Collection)";
        
        // Clone the cards
        ArrayList cards = new ArrayList();
        for(Integer cardNumber : originalCards) {
            cards.add(cardNumber);
        }

        int numCards = cards.size();
        if (numCards < 2) return cards;

        ArrayList sortedCards = new ArrayList();
        while(cards.size() > 0) {
            // Get the lowest card number
            int lowNumber = 99;
            int removalCard = -1;
            
            for(Integer card : cards) {
                removalCard = card;
                int cardNumber = card.ordinal();
                if (cardNumber < lowNumber) { lowNumber = cardNumber; }
            }

            if ((lowNumber == 99) || (removalCard == null)) {
                Log.error(ResourceCard.class, methodName, "Error while sorting cards");
                break;
            }
            
            boolean removed = true;
            while (removed == true) {
                removed = cards.remove(removalCard);
                sortedCards.add(removalCard);
            }
        }

        return sortedCards;
    }
    */
}
