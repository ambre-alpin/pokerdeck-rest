package myhomework.pokerdeck.model;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import myhomework.pokerdeck.util.ListShuffle;


/**
 * This class represents a Poker Deck of 52 cards.
 * 
 * This class provides two main operations: 
 *    - shuffle() : that results in the cards in the deck being randomly permuted.
 *    - dealOneCard() : that return the one card (the "next one") from the deck to the caller. 
 * 
 * Specifically, a call to shuffle followed by 52 calls to dealOneCard should result in the caller
 * being provided all 52 cards of the deck in a random order. If the caller then makes a 53rd call to
 * dealOneCard, no card is dealt (exception thrown).
 *
 * The class also gives the possibility to save a given state of the Deck thanks to {@link DeckSnapshot}.
 * 
 * Internally this implementation gathers a list of {@link PokerCard} and a position pointing to the next card.
 * 
 * @author Bruno Martinez
 */
public class PokerDeck {
	
	private ArrayList<PokerCard> cardList;
	private int position;
	private long randomSeed; // 0 if not shuffled
	
	/**
	 * Builds an new PokerDeck that is not shuffled. 
	 * 
	 * You need to shuffle first this deck, otherwise successive call to {@link dealOneCard}
	 * will return a predictive ordered sequence.
	 */
	public PokerDeck() {
		this.cardList = new ArrayList<PokerCard>(64);
		// Reset to initialize the deck
		reset();
	}

	
	/**
	 * Resets the deck to an initial not shuffled state with position at the first position.
	 */
	public void reset() {
		
		this.position = 0;
		this.randomSeed = 0; // 0 if not shuffled
		this.cardList.clear();
		// Fill the deck with the cards (not shuffled)
		for(PokerCard.Suit s : PokerCard.Suit.values()) {
			for(PokerCard.Value v : PokerCard.Value.values()) {
				this.cardList.add(new PokerCard(s, v));
			}
		}	
		
	}
	
	/**
	 * (re)shuffles the deck permuting randomly the cards, and resets the internal position to the beginning 
	 * of the deck.
	 * 
	 * This methods also reset the internal random generator with a new random seed.
	 * 
	 * After calling this method you can call {@link dealOneCard} up to 52 times without exception.
	 */
	public void shuffle() {
		// reset position to 0 and (re)shuffle with a new random seed
		long newSeed = System.nanoTime() ^ ((long) System.identityHashCode(this) << 32);
		shuffle(newSeed, 0);
	}
	
	private void shuffle(long randomSeed, int position) {
		// reset position and (re)shuffle
		this.randomSeed = randomSeed;
		this.position = position;
		
		// If seed == 0, by convention we do not shuffle the deck
		if(this.randomSeed != 0) {
			ListShuffle shuffle = new ListShuffle(randomSeed);
			shuffle.shuffle(this.cardList);
		}
	}
	
	/**
	 * Deals the next card of the deck. 
	 * 
	 * This methods returns the next card and advance the internal position of the deck. 
	 * If the internal position of the deck is already at the end of the deck when calling this 
	 * methods then it throws {@link NoSuchElementException}.
	 * 
	 * @return the next Card (not null)
	 * @throws NoSuchElementException when no more card available
	 */
	public PokerCard dealOneCard() {
		if(hasMoreCard()) {
			return this.cardList.get(this.position++);
		} else {
			// we reach the end of the deck
			throw new NoSuchElementException("End of the deck reached");
		}
	}
	
	/**
	 * Checks if there is more card to dealt.
	 * @return true if more card can be dealt, false if the end of the deck has been reached
	 */
	public boolean hasMoreCard() {
		return (this.position < this.cardList.size());
	}
	
	/**
	 * Gives the last dealed card, if any.
	 * @return null if no card dealed yet.
	 */
	public PokerCard getLastDealedCard(){
		if (this.position < 1) {
			return null;
		} else {
			return this.cardList.get(this.position - 1);
		}
			
	}

	
	/**
	 * Take a "snapshot" of the state this deck, thus it is possible
	 * to restore it later to the current state state.
	 * @return a snapshot corresponding to the current state
	 */
	public DeckSnapshot takeSnapshot() {
		return new DeckSnapshot(this.randomSeed, this.position);
	}
	

	/**
	 * Resets the current deck to the specified snapshot.
	 * If the snapshot is invalid throws an exception.
	 * @param snapshot a previous snapshot 
	 */
	public void resetFromSnapshot(DeckSnapshot snapshot) {		
		assert(snapshot.getPosition() >= 0);
		reset();
		shuffle(snapshot.getRandomSeed(), snapshot.getPosition());
	}

	
	@Override
	public String toString() {
		return "PokerDeck [ position=" + this.position + ", cardList=" + this.cardList + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cardList == null) ? 0 : cardList.hashCode());
		result = prime * result + position;
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
		PokerDeck other = (PokerDeck) obj;
		if (cardList == null) {
			if (other.cardList != null)
				return false;
		} else if (!cardList.equals(other.cardList))
			return false;
		if (position != other.position)
			return false;
		return true;
	}

}
