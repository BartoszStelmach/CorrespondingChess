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
$\textcolor{Aquamarine}{\text{Core:}}$ Java 14, SpringBoot, Lombok  
$\textcolor{Aquamarine}{\text{DB:}}$ MySQL, Hibernate  
$\textcolor{Aquamarine}{\text{API:}}$ REST, Swagger  
$\textcolor{Aquamarine}{\text{Logging:}}$ Slf4j  
$\textcolor{Aquamarine}{\text{Exception:}}$ Spring ControllerAdvice  
$\textcolor{Aquamarine}{\text{Build:}}$ Gradle  
$\textcolor{Aquamarine}{\text{Deployment:}}$ Docker  
$\textcolor{Aquamarine}{\text{Version control system:}}$ Git


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


## Docker Deployment
cd <PROJECT_PATH>  
docker build --tag java-docker-chess .  

docker volume create mysql_data  
docker volume create mysql_config  
docker network create mysqlnet  

docker run -it --rm  -v mysql_data:/var/lib/mysql -v mysql_config:/etc/mysql/conf.d --network mysqlnet --name mysqlserver -e MYSQL_USER=user -e MYSQL_PASSWORD=password -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=chess -p 3307:3306 mysql:8.0  
docker run --rm --name springboot-server-chess --network mysqlnet -e MYSQL_URL=jdbc:mysql://mysqlserver/chess -p 8080:8080 java-docker-chess  

### Docker maintenance
docker exec -it mysqlserver mysql -p -e "SELECT * FROM game" chess  

docker exec -t -i mycontainer /bin/bash  
cat logs/chess-app.log

### <font color="Red">Not yet implemented</font>
1. Castling
2. Verifying check (stopping checks, pins, discovered checks, checks after promotion) 
3. Verifying mate




