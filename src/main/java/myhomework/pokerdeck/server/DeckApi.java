package myhomework.pokerdeck.server;


import org.codehaus.jackson.map.ObjectMapper;


import myhomework.pokerdeck.model.PokerCard;
import myhomework.pokerdeck.model.PokerDeck;
import myhomework.pokerdeck.model.DeckSnapshot;

import java.net.URI;
import java.util.NoSuchElementException;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;


/**
 * This class implements the callbacks for the REST API, using JAX-RS REST annotation
 * (Jersey framework beside) 
 * 
 * @author Bruno Martinez
 */
@Path("v1/decks")
@Produces(MediaType.APPLICATION_JSON)
public class DeckApi {

	
	@Context
	private UriInfo uriInfo; // request context information
	
	/**
	 * Callback to create a new Deck (POST). No input.
	 * @return Json of the new created deck (including its new ID) + error code 200
	 */
	@POST
	public Response postDeck() throws Exception {

		DeckEntity deckEntity = new DeckEntity();
		PokerDeck deck = new PokerDeck();

		// Take a new snapshot and store it in the entity
		deckEntity.setSnapshot(deck.takeSnapshot());

		// Save the entity in database
		DeckDao.getInstance().saveDeck(deckEntity); 
		
		// Build the new resource URI	
		UriBuilder builder = UriBuilder.fromUri(uriInfo.getAbsolutePath());
		builder.path(Integer.toString(deckEntity.getId()));
		URI newResourceUri = builder.build();	
		
		// Returns 201 status (OK Created) and the location of the created Deck Resource				
		return Response.created(newResourceUri).build();	
	}
	
	/**
	 * Callback to shuffle a existing Deck (PUT). 
	 * @param id the deck resource ID 
	 * @return Json of the deck + code 200
	 * @throws NoSuchElementException if the deck with the given ID is not found (code 404). 
	 */
	@PUT
	@Path("/{id}")   
	public String putDeck(@PathParam("id") int id, @Context UriInfo uriInfo) throws Exception {

		// restore the entity form persistent storage
		DeckEntity deckEntity = DeckDao.getInstance().getDeck(id);
		if(deckEntity == null) {
			throw new NotFoundException();
		}

		// Restore a PokerDeck from the last snapshot
		PokerDeck deck = new PokerDeck();
		DeckSnapshot snapshot = deckEntity.getSnapshot(); 	
		deck.resetFromSnapshot(snapshot);

		// Shuffle
		deck.shuffle();

		// Take a new snapshot and store it in the entity
		deckEntity.setSnapshot(deck.takeSnapshot());

		// Save the entity in database
		DeckDao.getInstance().saveDeck(deckEntity); 

		// update transient fields (links)
		deckEntity.setLink(uriInfo.getAbsolutePath().toString());
		deckEntity.setLinkDeal(getUriToDealcardResource(deckEntity).toString());

		// write and return JSON response
		return new ObjectMapper().writeValueAsString(deckEntity);
	}
	
	
	/**
	 * Callback to get a Deck by ID.
	 * @param id the deck resource ID
	 * @return Json of the deck + code 200
	 * @throws Exception NotFoundException if the deck with the given ID is not found (code 404). 
	 */
	@GET
	@Path("/{id}")   
	public String getDeck(@PathParam("id") int id, @Context UriInfo uriInfo) throws Exception {

		// restore the entity form persistent storage
		DeckEntity deckEntity = DeckDao.getInstance().getDeck(id);
		if(deckEntity == null) {
			throw new NotFoundException();
		}

		// restore the deck
		//PokerDeck deck = new PokerDeck();
		//DeckSnapshot snapshot = deckEntity.getSnapshot();
		//deck.resetFromSnapshot(snapshot);

		// update transient fields (links)
		deckEntity.setLink(uriInfo.getAbsolutePath().toString());
		deckEntity.setLinkDeal(getUriToDealcardResource(deckEntity).toString());

		// write and return JSON response
		return new ObjectMapper().writeValueAsString(deckEntity);
	}

	
	/**
	 * Callback to get the last card that was dealt. As prerequisite a first deal operation (PUT) must have been
	 * done already, otherwise this method will throw an error.
	 * 
	 * @param id the deck resource ID
	 * @return Json of the card + code 200, if such card exists.
	 * @throws Exception NotFoundException if no card was dealt already or if the deck does not exist 
	 */
	@GET
	@Path("/{id}/dealcard")   
	public String getDealCard(@PathParam("id") int id) throws Exception {

		// restore the entity form persistent storage
		DeckEntity deckEntity = DeckDao.getInstance().getDeck(id);
		if(deckEntity == null) {
			throw new NotFoundException();
		}

		// restore the deck from the last snapshot
		PokerDeck deck = new PokerDeck();
		DeckSnapshot snapshot = deckEntity.getSnapshot();
		deck.resetFromSnapshot(snapshot);

		// Result
		PokerCard card = deck.getLastDealedCard();
		if (card == null) {
			throw new NotFoundException();
		}
		
		return new ObjectMapper().writeValueAsString(card);
	}


	/**
	 * Callback to deal a new card from the deck. 
	 * 
	 * @param id the deck resource ID
	 * @return Json of the new card + code 200.
	 * @throws Exception NotFoundException if the deck does not exist or no more card can be dealt from this deck
	 */
	@PUT
	@Path("/{id}/dealcard")   
	public String putDealCard(@PathParam("id") int id) throws Exception {

		// restore the entity form persistent storage
		DeckEntity deckEntity = DeckDao.getInstance().getDeck(id);
		if(deckEntity == null) {
			throw new NotFoundException();
		}

		// Restore a PokerDeck from the last snapshot
		PokerDeck deck = new PokerDeck();
		DeckSnapshot snapshot = deckEntity.getSnapshot();
		deck.resetFromSnapshot(snapshot);

		// Deal new card (if any) and store it in the entity, otherwise send 404 error
		if(!deck.hasMoreCard()) {
			throw new NotFoundException();
		}
		PokerCard cardToDeal = deck.dealOneCard();

		// Take a new snapshot and store it in the entity
		deckEntity.setSnapshot(deck.takeSnapshot());

		// Save the entity in database
		DeckDao.getInstance().saveDeck(deckEntity); 

		// display the new card to Deal
		return new ObjectMapper().writeValueAsString(cardToDeal);
	}


	
	private URI getUriToDealcardResource(DeckEntity entity) {
		// Return the location of the created Deck Resource
		UriBuilder builder = UriBuilder.fromUri(uriInfo.getAbsolutePath());
		builder.path("dealcard");
		return builder.build();	
	}
}
