package myhomework.pokerdeck.model;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Test;

public class PokerDeckTest {

	@Test 
	public void testReset() {
		PokerDeck deck1 = new PokerDeck();
		PokerDeck deck2 = new PokerDeck();
		deck2.dealOneCard();
		deck2.reset();
		
		assertEquals(deck1, deck2);
		
		deck2.shuffle();
		deck2.reset();
		assertEquals(deck1, deck2);	
	}
	
		
	@Test
	public void testDealOnCardMaxCallAfterInit() {
		PokerDeck deck = new PokerDeck();
		doTestDealOnCardMaxCall(deck);
	}
	
	@Test
	public void testDealOnCardMaxCallAterShuffle() {
		PokerDeck deck = new PokerDeck();
		deck.shuffle();
		doTestDealOnCardMaxCall(deck);
	}
	
	@Test
	public void testDealOnCardMaxCallAterReset() {
		PokerDeck deck = new PokerDeck();
		deck.reset();
		doTestDealOnCardMaxCall(deck);
	}
	
	
	private void doTestDealOnCardMaxCall(PokerDeck deck) {
		
		// Check the max number of call we can do to dealOnCard()
		int count = 0;						
		while(deck.hasMoreCard()) {
			deck.dealOneCard();
			count++;
		}		
		assertEquals(52, count);
		boolean hasThrown = false;
		try {
			deck.dealOneCard();
		} catch (NoSuchElementException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
	}
	
	@Test
	public void testEquals() {
		PokerDeck deck2 = new PokerDeck();
		PokerDeck deck1 = new PokerDeck();
		
		assertEquals(deck2, deck1);
		assertEquals(deck2.hashCode(), deck1.hashCode());
		deck1.dealOneCard();
		assertNotEquals(deck2, deck1);
		
		// cannot really test that after shuffle() the deck should be different,
		// there is still the theorical case when shuffle() could give the same result 
		// as initial ordered sequence, which make non deterministic result.. 
	}
	
	@Test
	public void testDealOneCard() {
		PokerDeck deck = new PokerDeck();
		assertNull(deck.getLastDealedCard());
		assertTrue(deck.hasMoreCard());
		PokerCard card = deck.dealOneCard();
		assertEquals(card, deck.getLastDealedCard());
		assertEquals(card, deck.getLastDealedCard());
		
		deck.shuffle();
		card = deck.dealOneCard();
		assertEquals(card, deck.getLastDealedCard());
		
	}
	
	@Test
	public void testDealOneCardReturnAllCardsAfterInit() {
		PokerDeck deck = new PokerDeck();
		doTestDealOneCardReturnAllCards(deck);
	}
	
	@Test
	public void testDealOneCardReturnAllCardsAfterShuffle() {
		PokerDeck deck = new PokerDeck();
		deck.shuffle();
		doTestDealOneCardReturnAllCards(deck);
	}
	
	@Test
	public void testDealOneCardReturnAllCardsAfterReset() {
		PokerDeck deck = new PokerDeck();
		deck.reset();
		doTestDealOneCardReturnAllCards(deck);
	}
	
	private void doTestDealOneCardReturnAllCards(PokerDeck deck) {
		
		Set<PokerCard> cardSet = new HashSet<PokerCard> ();		
		while(deck.hasMoreCard()) {
			assertTrue(cardSet.add(deck.dealOneCard()));
		}
				
		// Ensure we have 52 different cards
		assertEquals(52, cardSet.size());
	}
	
	
	@Test
	public void testTakeSnapshotInit () {
		PokerDeck deck = new PokerDeck();
		DeckSnapshot s = new DeckSnapshot();
		assertEquals(s, deck.takeSnapshot());
	}
	
	@Test
	public void testTakeSnapshotAfterDeal () {
		PokerDeck deck = new PokerDeck();
		deck.shuffle();
		DeckSnapshot s1 = deck.takeSnapshot();
		deck.dealOneCard();
		DeckSnapshot s2 = deck.takeSnapshot();
		assertNotEquals(s1, s2);
	}
	
	@Test
	public void testResetFromSnapshot() {
		PokerDeck referencedeck = new PokerDeck();
		referencedeck.shuffle();
		referencedeck.dealOneCard();
		DeckSnapshot s = referencedeck.takeSnapshot();
		
		PokerDeck deck = new PokerDeck();
		assertNotEquals(referencedeck, deck);
		deck.resetFromSnapshot(s);
		assertEquals(referencedeck, deck);
		deck.shuffle();
		deck.dealOneCard();
		assertNotEquals(referencedeck, deck);
		deck.resetFromSnapshot(s);
		assertEquals(referencedeck, deck);		
	}
	
	@Test
	public void testResetFromDefaultSnapshot() {
		DeckSnapshot s = new PokerDeck().takeSnapshot();
		
		PokerDeck deck = new PokerDeck();
		deck.shuffle();
		deck.dealOneCard();
		assertNotEquals(new PokerDeck(), deck);
		deck.resetFromSnapshot(s);
		assertEquals(new PokerDeck(), deck);
		deck.shuffle();
		deck.dealOneCard();
		assertNotEquals(new PokerDeck(), deck);
		deck.resetFromSnapshot(s);
		assertEquals(new PokerDeck(), deck);
	}
	
}
