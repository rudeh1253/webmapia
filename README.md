# webmapia
Mapia game run on web

Based on: [https://namu.wiki/w/%EC%9B%B9%EB%A7%88%ED%94%BC%EC%95%84#s-5.1](https://namu.wiki/w/%EC%9B%B9%EB%A7%88%ED%94%BC%EC%95%84#s-5.1)

## Get started
1. Install Git, Node.js, JDK 17
2. Open a terminal (or Git Bash) and run the following command: <br>
   ```git clone https://github.com/rudeh1253/webmapia.git```
3. For fron-end server, first, go to the root directory of the project and run the following command: <br>
   ```cd client && npm start```
4. For back-end server, first, go to the root directory of the project and run the following command: <br>
   ```
   cd server
   ./mvnw package
   cd target
   java -jar webmapia-0.0.1-SNAPSHOT.jar
   ```
Make sure JDK 17 and Node.js are installed on your PC.

When you encounter the following error: <br>
```
GameServiceImplTest.processVote_includeNobility:156 expected: <EXECUTE_BY_VOTE> but was: <INVALID_VOTE>
```
just rerun ```./mvnw package``` repetively until succeeding to package. This error is issued because of the test code. One of the test code outputs the result randomly because I wrote the test code inadequeately. I have to fix it someday (not now).

Default port number for front-end server is 3000, and for back-end server is 8080.

If you want to let external PCs access the web application running, maybe you should do some work such as port forwarding, or deploying it on a host using, for instance, [Amazon Web Service](https://aws.amazon.com/).

If you want to build the react code and deploy independetly, in other words, if you want for your front-end server to run without npm, make use of a web server such as [Nginx](https://www.nginx.com/).

## Tech Stack
### Front-end
- TypeScript
- React
- Redux
- SockJS
### Back-end
- Java (JDK 17)
- Spring Boot
- Spring WebSocket
### Deployement
- AWS EC2
- Nginx (for front-end server)
- Apache Tomcat (for back-end server)

## Development period
2023.06.24 ~

## Member
[@rudeh1253](https://github.com/rudeh1253)
