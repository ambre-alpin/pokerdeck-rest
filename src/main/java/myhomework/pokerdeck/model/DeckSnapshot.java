package myhomework.pokerdeck.model;

import javax.persistence.Embeddable;

/**
 * This class is used to take a "snapshot" of the state of an instance of {@link PokerDeck}. Thus it is possible
 * to restore the PokerDeck to a previous state.
 * 
 * @author Bruno Martinez
 */
@Embeddable
public class DeckSnapshot {

	
	/**
	 * The last seed used to initialize the Random generator of the Deck (last call to {@link PokerDeck#shuffle()}) 
	 */
	private long randomSeed = 0;
	
	/**
	 * Current position in the Deck
	 */
	private int position = 0;

	
	/**
	 * Creates a snapshot corresponding to a newly initialized deck (deck was not shuffled).
	 * (randomSeed and position are set to 0).
	 */
	public DeckSnapshot() {}
	
	/**
	 * Creates a new instance with the last randomSeed and the current position of the deck.
	 * @param randomSeed the last random seed used  (last call to {@link PokerDeck#shuffle()})
	 * @param position the current position of the deck (should be greater than or equal to 0).
	 * @throws IllegalArgumentException if position negative
	 */
	public DeckSnapshot(long randomSeed, int position) {
		setRandomSeed(randomSeed);
		setPosition(position);
	}
	
	/**
	 * @return the last random seed of the deck or zero if no the deck was not shuffled
	 */
	public long getRandomSeed() {
		return randomSeed;
	}

	/**
	 * @param randomSeed the last random seed of the deck or zero if no the deck was not shuffled
	 */
	public void setRandomSeed(long randomSeed) {
		this.randomSeed = randomSeed;
	}
	
	/**
	 * @return the last position of the deck
	 */
	public int getPosition() {
		return position;
	}

	/**
     * Set the last positions of the deck. 
	 * @param position should be greater than or equal to 0
	 * @throws IllegalArgumentException if position negative
	 */
	public void setPosition(int position) {
		if (position < 0) {
			throw new IllegalArgumentException("negative position");
		}
		this.position = position;
	}

	@Override
	public String toString() {
		return "DeckSnapshot [randomSeed=" + randomSeed + ", position=" + position + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + position;
		result = prime * result + (int) (randomSeed ^ (randomSeed >>> 32));
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
		DeckSnapshot other = (DeckSnapshot) obj;
		if (position != other.position)
			return false;
		if (randomSeed != other.randomSeed)
			return false;
		return true;
	}
}
