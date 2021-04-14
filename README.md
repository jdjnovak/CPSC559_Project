# CPSC559_Project
A maven generated project for the CPSC559 winter 2021 term project  

## About
This project was to create a distributed peer system that would:
1. Connect via TCP to a central registry and get an initial list of peers
2. Spin up three threads  
  a. A process that handles UDP messages to and from peers in the system  
  b. A Peer process which sends, via the UDP socket and at a set interval, a "keep-alive" message to a randomly selected peer  
  c. A CLI procedss that handles users input for sending a typed message (called 'snips') to all peers (can also see current peers and all received messages)  
3. When the UDP process receives a 'stop' datagram, the process then replies with an ack and signals the rest of the processes to shutdown. 

## Prerequisites
- OpenJDK 11.0.10 
- Apache Maven 3.6.3

## Installation
- Clone the repository 
`git clone https://github.com/jdjnovak/CPSC559_Project.git`
- Compile to .jar
`mvn package`
- Run
`java -jar target/CPSC559_Project-1.0-SNAPSHOT.jar`

## Group members
- Simone Mendonca
- Joshua Novak

