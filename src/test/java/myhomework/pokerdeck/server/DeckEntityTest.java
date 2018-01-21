package myhomework.pokerdeck.server;

import static org.junit.Assert.*;

import org.junit.Test;

import myhomework.pokerdeck.model.DeckSnapshot;

public class DeckEntityTest {

	
	@Test
	public void testNewObject() {
		DeckEntity deckEntity =  new DeckEntity();
		assertNotNull(deckEntity.getSnapshot());
	}
	
	@Test
	public void testEquals() {
		DeckEntity deckEntity1 = new DeckEntity();
		DeckEntity deckEntity2 = new DeckEntity();
					
		assertEquals(deckEntity1, deckEntity2);
		assertEquals(deckEntity1.hashCode(), deckEntity2.hashCode());
			
		deckEntity1.setId(123);
		assertNotEquals(deckEntity1, deckEntity2);
		deckEntity2.setId(123);
		assertEquals(deckEntity1, deckEntity2);
		assertEquals(deckEntity1.hashCode(), deckEntity2.hashCode());
		
		deckEntity1.setSnapshot(new DeckSnapshot(1231, 5));
		assertNotEquals(deckEntity1, deckEntity2);		
		deckEntity2.setSnapshot(new DeckSnapshot(1231, 5));
		assertEquals(deckEntity1, deckEntity2);
		assertEquals(deckEntity1.hashCode(), deckEntity2.hashCode());
	}

}
