package myhomework.pokerdeck.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class PokerCardTest {

	
	@Test
	public void testAccessors() {
		PokerCard card1 = new PokerCard(PokerCard.Suit.DIAMONDS, PokerCard.Value.JACK);
		PokerCard card2 = new PokerCard(PokerCard.Suit.SPADES, PokerCard.Value.ACE);
		
		assertEquals(PokerCard.Suit.DIAMONDS, card1.getSuit());
		assertEquals(PokerCard.Suit.SPADES, card2.getSuit());
		assertEquals(PokerCard.Value.JACK, card1.getValue());
		assertEquals(PokerCard.Value.ACE, card2.getValue());
	}
	
	@Test
	public void testToString() {
		PokerCard card1 = new PokerCard(PokerCard.Suit.DIAMONDS, PokerCard.Value.JACK);
		PokerCard card2 = new PokerCard(PokerCard.Suit.SPADES, PokerCard.Value.ACE);
		
		assertEquals("JACK_DIAMONDS", card1.toString());
		assertEquals("ACE_SPADES", card2.toString());
		
	}
	
	@Test
	public void testEquals() {
		PokerCard card1 = new PokerCard(PokerCard.Suit.HEARTS, PokerCard.Value.EIGHT);
		PokerCard card2 = new PokerCard(PokerCard.Suit.HEARTS, PokerCard.Value.EIGHT);
		PokerCard card3 = new PokerCard(PokerCard.Suit.HEARTS, PokerCard.Value.FIVE);
		
		assertEquals(card1, card2);
		assertEquals(card1.hashCode(), card2.hashCode());
		assertNotEquals(card1, card3);
		assertNotEquals(card1.hashCode(), card3.hashCode());
	}

}
