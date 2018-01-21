package myhomework.pokerdeck.model;



/**
 * This class represents a poker-style playing card with a suit (hearts, spades, clubs, diamonds) and a face value
 * (Ace, 2-10, Jack, Queen, and King).
 * PokerCard instances are immutable.
 *
 *  @author Bruno Martinez
 */
public class PokerCard {
	
	/**
	 * Enum representing the different poker card suits
	 */
	public enum Suit {
		HEARTS, SPADES, CLUBS, DIAMONDS
	}
	
	/**
	 * Enum representing the different poker card face values
	 */
	public enum Value {
		TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
	}
		
	private final Suit suit;
	private final Value value;
	
	/**
	 * Builds a new Poker card with the specified suit and face value
	 * @param suit the suit of the card
	 * @param value the face value of the card
	 * @throws NullPointerException if one input parameter is null
	 */
	public PokerCard(Suit suit, Value value) {
		
		if(suit == null || value == null) {
			throw new NullPointerException();
		}
		
		this.suit = suit;
		this.value = value;
	}
	
	/**
	 * Gives the suit of the card
	 * @return a suit (not null)
	 */
	public Suit getSuit() {
		return suit;
	}
	
	/**
	 * Gives the suit face value	
	 * @return the card face value (not null)
	 */
	public Value getValue() {
		return value;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((suit == null) ? 0 : suit.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PokerCard other = (PokerCard) obj;
		if (suit != other.suit)
			return false;
		if (value != other.value)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.value + "_" + this.suit;
	}
}
