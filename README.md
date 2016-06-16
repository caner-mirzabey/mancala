# Kalah (Mancala) Game
Board Game written in Java

## Requirements
JDK 1.7+

## Frameworks
Spring Boot Platform (Embedded Tomcat)
Atmosphere WebSocket (Client & Server)
Spring Core & Web
AngularJS (Client)

## Run
Either
    - Run from terminal directly:
        mvnw* install spring-boot:run
    - Or 
        - build first by running
            mvnw* install
        then 
            java -jar target/myproject-0.0.1-SNAPSHOT.jar

* mvnw is an portable maven installment runner. Use mvnw.bat for Windows systems.


## Solution
    - Keeping all the data in ConcurrentHashMaps
    - To provide Real-Time solution for multiplayer, implemented with WebSocket
    - Session management is handled via WebSocket channels (Login channel subscription with provided username)
    - 
    
## Potential Additions
    - Game Engine transformation to State Machine
    - Security
    - Session Management
    - Persistence Layer & Cache,
    - User Inteface is not complete. Implementing game board design
    
    