# Homework PockerDeck API project


Build a REST API to deal cards from a PokerDeck

*Author: Bruno Martinez*
*Date : 21/01/2018*
*Git: https://github.com/bmcmartinez/pokerdeck-rest*

## Subject

Your assignment is to code, in an objected-oriented programming language, a set of classes and a REST API that represent a deck of poker-style playing cards. (Fifty-two playing cards in four suits: hearts, spades, clubs, diamonds, with face values of Ace, 2-10, Jack, Queen, and
King.)

Within one of your classes, you must provide two operations:
 
  * shuffle() 
  
Shuffle returns no value, but results in the cards in the deck being randomly permuted. Please do not use library-provided “shuffle” operations to implement this function. You may use library- provided random number generators in your solution.

  * dealOneCard()

This function should return one card from the deck to the caller. Specifically, a call to shuffle followed by 52 calls to dealOneCard should result in the caller being provided all 52 cards of the deck in a random order. If the caller then makes a 53rd call to
dealOneCard, no card is dealt.


After implementing this internal interface, you must expose the functions to create a deck, shuffle a deck, and deal a card from a deck via a REST API. 
The structure of the REST API is up to you but please consider the combination of resources and actions represented by this problem and make appropriate tradeoffs in compliance to strict REST doctrine.


## Building and Launching

### Prerequisite

  * Gradle to build and launch the project
  * Curl tool or similar to do manual test to the standalone local server
  * Java 1.7 or higher

### Building and launching the unit tests

```
gradlew build
```

This will produce a unit test report: build/reports/tests/test/index.html
    
    
### Launching the standalone server and do some manual tests

This section explains how to launch the server to test the REST API.
*Note: in this simple version the server uses in memory data persistence only.*

#### Install the application

This will deploy everything needed in a build/install.
  
```
gradlew installDist
```
 
#### Launch the server with the script: 

To launch the simple local server listening on localhost:8080.  
``` 
build\install\PokerDeckRest\bin\PokerDeckRest
```

#### Test the API 

In another console use curl to test the API
  
  * Create a new Deck  
 
```    
$>curl -i -X POST http://localhost:8080/api/v1/decks/
HTTP/1.1 201 Created
Location: http://localhost:8080/api/v1/decks/10
Date: Sun, 21 Jan 2018 09:54:45 GMT
Content-Length: 0
```
               
  * Get the new deck (it is not shuffled yet) 

```   
$>curl -i -X GET http://localhost:8080/api/v1/decks/10
HTTP/1.1 200 OK
Content-Type: application/json
Date: Sun, 21 Jan 2018 09:55:52 GMT
Content-Length: 116

{"id":10,"link":"http://localhost:8080/api/v1/decks/10","linkDeal":"http://localhost:8080/api/v1/decks/10/dealcard"}     
```

  * Shuffle it
  
```   
$>curl -i -X PUT http://localhost:8080/api/v1/decks/10
HTTP/1.1 200 OK
Content-Type: application/json
Date: Sun, 21 Jan 2018 09:55:52 GMT
Content-Length: 116

{"id":10,"link":"http://localhost:8080/api/v1/decks/10","linkDeal":"http://localhost:8080/api/v1/decks/10/dealcard"}     
```
       
  * Deal some cards

```
curl -i -X PUT http://localhost:8080/api/v1/decks/10/dealcard
HTTP/1.1 200 OK
Content-Type: application/json
Date: Sat, 20 Jan 2018 22:33:58 GMT
Content-Length: 33

{"suit":"HEARTS","value":"SEVEN"}
```

```
curl -i -X PUT http://localhost:8080/api/v1/decks/10/dealcard
HTTP/1.1 200 OK
Content-Type: application/json
Date: Sat, 20 Jan 2018 22:34:34 GMT
Content-Length: 33
        
{"suit":"DIAMONDS","value":"TWO"} 
``` 
      
        
 * Get the last card that was dealt

``` 
curl -i -X GET http://localhost:8080/api/v1/decks/10/dealcard
HTTP/1.1 200 OK
Content-Type: application/json
Date: Sat, 20 Jan 2018 22:34:34 GMT
Content-Length: 33
        
{"suit":"DIAMONDS","value":"TWO"}
```
       
     

### Other: (Javadoc / JAR / WAR .. etc)

Javadoc: 

```
gradlew.bat javadoc 
```
   
Otherwise launch: ``gradlew.bat tasks``
This will give the complete list of possible tasks.


## Implementation details

### Poker Deck model

To shuffle the poker deck I wrote my own shuffle method base on the popular algorithm of Fisher And Yates, in a generic class.
The random number generation uses the standard ``java.util.Random``, which is enough for this project (``java.security.SecureRandom`` may be preferred when security is involved or for implementing real gambling game)

### Poker Deck storage

Instead of storing the full deck (list of cards), I introduced the concept of DeckSnapshot that captures the current state of a Deck and allows to restore it later. DeckSnapshot actually contains a copy of the seed number used to initialize the random number generator in the last shuffling operation. Thus, shuffling a new Deck instance with same seed number will result in obtaining the same card sequence order as in the original Deck instance.

### Java Rest Server

I tried to used common libraries relying of standard concept of Java REST Servers:

  * Jersey library implementing the standard JAX-RS API (RESTful Web Services)
  * Hibernate library implementing the standard JPA API (Persistence API)
  * Additionally I used the popular jackson library for converting object to/from JSON format.

For the actual HTTP server and Databases implementation and how to test it locally with Junit, I took examples from this website: https://technologyconversations.com/2014/03/26/application-development-back-end-solution-with-java/

It uses:
  * HSQLDB for the database (in memory flavor)
  * Grizzly Web Server


### REST API

#### Operations and resources

|    HTTP Action    |    Resource                       |    Description                                                                                     |    Reply Content                                               |    Success status code    |
|-------------------|-----------------------------------|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------|---------------------------|
|    POST           |    /api/v1/decks                  |    Creates a new deck                                                                              |    Body: None, Location: URI of the new created resource       |    201-CREATED          |
|    GET            |    /api/v1/decks/*id*             |    Get an existing deck                                                                            |    Body: JSON describing the deck and link to other actions    |    200-OK               |
|    PUT            |    /api/v1/decks/*id*             |    Reset and Shuffle an existing deck.  After this call, it is possible to deal up to 52 cards.    |    Body: JSON describing the deck and link to other actions    |    200-OK               |
|    PUT            |    /api/v1/decks/*id*/dealcard    |    Deals a new card (if any)                                                                       |    Body: JSON describing the new card                          |    200-OK               |
|    GET            |    /api/v1/decks/*id*/dealcard    |    Get the last card that was dealt from the deck.                                                 |    Body: JSON describing the new card                          |    200-OK               |

#### Functional Errors


|    HTTP Action    |    Resource                       |    HTTP Error code      |     Description                                                                                                                                         |
|-------------------|-----------------------------------|-------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------|
|    GET            |    /api/v1/decks/*id*             |    404-NOT FOUND        |    The deck resource with id <id> does not exists yet.                                                                                                  |
|    PUT            |    /api/v1/decks/*id*             |    404-NOT FOUND        |    The deck resource with id <id> does not exists yet.                                                                                                  |
|    PUT            |    /api/v1/decks/*id*/dealcard    |    404-NOT FOUND        |    The deck resource with id <id> does not exists yet.                                                                                                  |
|    GET            |    /api/v1/decks/*id*/dealcard    |    404-NOT FOUND        |    The deck resource with id <id> does not exists yet.       Or no card was dealt from the deck yet.       Or all the cards were dealt from the deck    |


### Testing

I wrote many unit tests using Junit and I reached a coverage of 85.5% on the main source directory (excluding test files).
To gain time I did not unit tested method such as "toString()" (not critical in the project) or basic setters/getters.

I did manual testing with curl command line tool.


### Conclusion

This was extremely interesting do to this projects as it was the opportunity to have a refresh on the latest Java technology (these last years I rather developed in C++ and python). I took this opportunity to read many documentation and I learned a lot.



