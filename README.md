# Sweng-Ass2
Assignment two for Software Engineering
Group 15

To run the web-app:
Go to terminal -> navigate to location of the webserver folder within inotes
To build run the command:

./mvnw clean install spring-boot:run

Creating our network with the command:

docker network create our-network

Finally, to run containers:

docker run --name=mongo-container --rm -d --network=our-network mongo
docker build -t inotes .
docker run --name=inotes-container --rm -d -p 8080:8080 --network=our-network inotes
