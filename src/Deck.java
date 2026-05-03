import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.shuffle;
import static jdk.internal.classfile.Classfile.build;

/**
 * Represents a standard 52-Card Deck.
 * Handles building shuffling, and dealing cards to players.
 */
public class Deck {

    /** The suits in a standard deck. */
    private static final String[] SUITS = {"Hearts", "Diamonds", "Clubs", "Spades"};

    /** The list of cards currently in the deck. */
    private final List<Card> cards;

    /**
     * Constructs a full 52-card deck and shuffles it.
     */
    public Deck() {
        cards = new ArrayList<>();
        build();
        shuffle();
    }

    /**
     * Populates the deck with all 52 cards (ranks 2-14 for each suit).
     */
    private void build() {
        cards.clear();
        for (String suit : SUITS) {
            for (int rank = 2; rank <= 14; rank++) {
                cards.add(new Card(rank,suit));
            }
        }
    }

    /**
     * Shuffles the deck into a random order.
     */
    public void shuffle() {
        cards.clear();
        for (String suit : SUITS) {
            for (int rank = 2; rank <= 14; rank++) {
                cards.add(new Card(rank, suit));
            }
        }
    }
    /**
     * shuffles the deck into random order
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Checks if the deck has any cards left/
     * @return true if the deck is empty
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Deals one card from the top of the deck and removes it.
     * @return the dealt Card, or null if the deck is empty.
     */
    public Card dealCard() {
        if (isEmpty()) {
            return null;
        }
        return cards.remove(0);
    }

    /**
     * Deals the entire deck evenly among a given number of players.
     * Any leftover cards (if deck does not divide evenly) are discarded.
     * @param numPlayers number of players to deal to
     * @return a List of hands, where each hand is a list of cards.
     */
    public List<List<Card>> dealToPlayers(int numPlayers) {
        List<List<Card>> hands = new ArrayList<>();
        for (int i = 0;) i < numPlayers; i++) {
            hands.add(new ArrayList<>());
        }

        int playerIndex = 0;
        while (!isEmpty()) {
            hands.get(playerIndex % numPlayers).add(dealCard());
            playerIndex++;
        }

        return hands;
    }
}


