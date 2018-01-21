package myhomework.pokerdeck.server;


import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.*;

import static org.junit.Assert.*;

import myhomework.pokerdeck.model.DeckSnapshot;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;



public class DeckApiTest   {

	private static HttpServer server;
	private WebTarget baseTarget;
	private Client client;
	

	@BeforeClass
	public static void beforeBookApiTestClass() {
		server = new Server().startServer();
		
		for(int i = 0; i < 10; i++) {
			DeckEntity deck = new DeckEntity();
			deck.setSnapshot(new DeckSnapshot());
			DeckDao.getInstance().saveDeck(deck);
		}
	}

	@Before
	public void setUp() throws Exception {
		client = ClientBuilder.newClient();
		baseTarget = client.target("http://localhost:8080/api/v1/decks");	
	}

	@AfterClass
	public static void afterUserResourceTestClass() {
		server.shutdown();
	}

	

	@Test
	public void testGetDeckReturnTheCorrectJson() throws Exception {
	
		WebTarget target = baseTarget.path("/10");
		
		Response response = target.request().get();
		assertEquals(200, response.getStatus());
		
		String json = response.readEntity(String.class);		
		assertEquals(
				"{\"id\":10,\"link\":\"http://localhost:8080/api/v1/decks/10\",\"linkDeal\":\"http://localhost:8080/api/v1/decks/10/dealcard\"}",
				json);
	}
	
	@Test
	public void testGetDeckInvalidId() throws Exception {
	
		WebTarget target = baseTarget.path("/1000097");
		Response response = target.request().get();
		assertEquals(404, response.getStatus());
	}
	
	@Test
	public void testPostDeck() throws Exception {
				
		Response response = baseTarget.request().post(Entity.text(""));
		assertEquals(201, response.getStatus());	
		assertNotNull(response.getLocation());
		
		// check new resource 
		WebTarget deckTarget = client.target(response.getLocation());
		Response r = deckTarget.request().put(Entity.text(""));
		assertEquals(200, r.getStatus());		
	}
	
	@Test
	public void testPutDeck() throws Exception {
	
		WebTarget target = baseTarget.path("/10");		
		String json = target.request().put(Entity.text(""), String.class);
		assertEquals(
				"{\"id\":10,\"link\":\"http://localhost:8080/api/v1/decks/10\",\"linkDeal\":\"http://localhost:8080/api/v1/decks/10/dealcard\"}",
				json);		
	}
	
	@Test
	public void testPutDeckInvalidId() throws Exception {
	
		WebTarget target = baseTarget.path("/1000097");
		Response response = target.request().put(Entity.text(""));
		assertEquals(404, response.getStatus());
	}
	
	@Test
	public void testPutDealCardAll() throws Exception {
		
		WebTarget target = baseTarget.path("/10/dealcard");
		
		// Shuffle
		WebTarget targetDeck = baseTarget.path("/10");		
		Response response = targetDeck.request().put(Entity.text(""));
		assertEquals(200, response.getStatus());
		
		for(int i=0; i<52; i++) {					
			response = target.request().put(Entity.text(""));
			assertEquals(200, response.getStatus());
			String json = response.readEntity(String.class);		
			System.out.println(json);		
			assertTrue(json.toString().length() > 6);
		}
		
		// No more card
		response = target.request().put(Entity.text(""));
		assertEquals(404, response.getStatus());
		
		// Reshuffle 
		targetDeck = baseTarget.path("/10");		
		response = targetDeck.request().put(Entity.text(""));
		assertEquals(200, response.getStatus());
		
		// card again
		response = target.request().put(Entity.text(""));
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void testGetDealCard() throws Exception {
				
		// Make first a deal
		WebTarget target = baseTarget.path("/10/dealcard");
		Response response = target.request().put(Entity.text(""));
		String json = response.readEntity(String.class);	
		assertEquals(200, response.getStatus());
		
		// Call a get retrieve the same value
		response = target.request().get();
		String jsonGET = response.readEntity(String.class);	
		assertEquals(200, response.getStatus());
		
		assertEquals(json, jsonGET); 
	}
	
	@Test
	public void testGetDealCardNoCard() throws Exception {
			
		// Call a get retrieve the same value
		WebTarget target = baseTarget.path("/10/dealcard");
		Response response = target.request().get();
		String jsonGET = response.readEntity(String.class);	
		assertEquals(404, response.getStatus());

	}
	
	
	
	@Test
	public void testGetInvalid() throws Exception {
			
		// Make first a deal
		WebTarget target = baseTarget.path("/126698/dealcard");
		Response response = target.request().put(Entity.text(""));
		assertEquals(404, response.getStatus());	
	}
}

