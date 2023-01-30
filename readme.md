# Corresponding Chess
http://localhost:8080/swagger-ui/index.html  
Application that allows people to play corresponding chess by sending HTTP requests.
Game can be started just by sending players names. ID returned from that call will serve as a reference to the game when sending the move that player wants to perform.

## Moves notation
Moves have to be written in accordance with the rules of commonly used algebraic chess notation:
https://en.wikipedia.org/wiki/Algebraic_notation_(chess)

## Sample calls
### 1. Start game
POST request to /startGame will begin the game. Colours of the players may be randomized by passing 'true' as the 'areColoursRandom' parameter. Otherwise, 'firstPlayerName' will be assigned to the white colour.

Request example:  
   curl -X 'POST' \
   'http://localhost:8080/startGame?firstPlayerName=Magnus%20Carlsen&secondPlayerName=Hikaru%20Nakamura&areColoursRandom=false' \
   -H 'accept: */*' \
   -d ''

Response example:  
   {
      "whiteName": "Magnus Carlsen",
      "blackName": "Hikaru Nakamura",
      "moves": [],
      "id": 35
   }
  

### 2. Play move
POST request to /playMove will try to perform the move. If specified move can be played in the current state of the game 
referenced by the given ID for the specified colour it will be persisted and the simple text graphical representation will be returned, 
where pieces symbols are prefixed with either 'W' for white or 'B' for black.

Request example:  
   curl -X 'POST' \
   'http://localhost:8080/playMove?id=35&colour=WHITE&move=e4' \
   -H 'accept: */*' \
   -d ''


## Technologies
<font color="Aquamarine">Core:</font> Java 14, SpringBoot, Lombok  
<font color="Aquamarine">DB:</font> MySQL, Hibernate  
<font color="Aquamarine">API:</font> REST, Swagger  
<font color="Aquamarine">Logging:</font> Slf4j  
<font color="Aquamarine">Exception:</font> Spring ControllerAdvice  
<font color="Aquamarine">Build:</font> Gradle  
<font color="Aquamarine">Deployment:</font> <font color="Red">Docker //Not yet done  </font>  
<font color="Aquamarine">Version control system:</font> Git


## Successful flow for move request
1. HTTP request is picked by REST Controller.
2. Objects for the given game are loaded from the database.
3. Move syntax is verified.
4. Which colour should move is verified 
5. Move is parsed and converted into objects. 
6. Move is verified against the current state of the board and the rules of chess.
7. Board and complementary objects are changed according to the move. 
8. Objects are persisted in the database. 
9. Simple text graphical representation is returned to the caller.


### <font color="Red">Not yet implemented</font>
1. Castling
2. Verifying check (stopping checks, pins, discovered checks, checks after promotion) 
3. Verifying mate




