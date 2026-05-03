/**
 * Represents a single playing card using a Java Record.
 * Cards have ranks (2-14, where 11 = Jack, 12 = Queen, 13 = King, 14 = Ace)
 * A suit (Hearts, Diamonds, Clubs, Spades).
 */
public record Card(int rank, String suit) implements Comparable<Card> {

    /**
     * Display names for ranks 2-10 are their number, face cards have names.
     */
    private static final String[] RANK_NAMES = {
            "", "", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "Jack", "Queen", "King", "Ace"
    };

    /**
     * Returns a readable name for the card (ex: "Ace of Spades").
     */
    public String getName() {
        return RANK_NAMES[rank] + " of " + suit;
    }

    /**
     * Compares this card to another by rank for War resolution
     *
     * @param other the card to compare against
     * @return negative if this card loses, 0 if tie, positive if this card wins.
     */
    @Override
    public int compareTo(Card other) {
        return Integer.compare(this.rank, other.rank);
    }
}
