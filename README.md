# Quiz Game Application

## Overview

The **Quiz Game Application** is a simple client-server application developed in Java that allows multiple clients to connect to a server and participate in a quiz. The server hosts a set of questions, and each client can interact with the server to answer the questions in real time.

## Features

- **Multithreaded Server**: The server can handle multiple clients concurrently using a thread pool.
- **Quiz Questions**: The server has a predefined list of quiz questions that it sends to clients one by one.
- **Client Interaction**: Clients can connect to the server, receive questions, submit answers, and see if they were correct or incorrect.
- **Score Tracking**: The server tracks each client's score and sends the final result at the end of the quiz.

## Technologies Used

- **Java**: The application is developed using Java.
- **Socket Programming**: The server and client communicate over a network using sockets.

## Getting Started

### Prerequisites

- **Java Development Kit (JDK)** version 8 or higher must be installed on your machine.
- **Git** for version control (optional).

### Installation

1. **Clone the Repository**
   
   ```sh
   git clone https://github.com/purplestrawberrywatermelon/Quiz_game_application.git
   cd Quiz_game_application
   ```

2. **Compile the Code**

   - To compile the server and client Java files, run:
     ```sh
     javac QuizServer.java
     javac QuizClient.java
     ```

### Running the Application

1. **Modify Configuration File**

   - Modify the configuration file named `server_info.dat` to specify the server address and port to match your server setup.

2. **Start the Server**
   
   - Run the following command to start the server:
     ```sh
     java QuizServer
     ```
   - The server will start and wait for clients to connect.

3. **Run the Client**
   
   - Open another terminal and run the client to connect to the server:
     ```sh
     java QuizClient
     ```
   - You can run multiple clients to connect to the server simultaneously.

## How to Play

1. When the client connects to the server, it receives quiz questions from the server.
2. The client can type their answer and send it to the server.
3. The server evaluates the answer and provides feedback (correct/incorrect).
4. The client can type 'next' to continue to answer questions or type `end` to stop the quiz.
5. At the end of the quiz, the server sends the client's score.

## Configuration

- The server and client use a configuration file named `server_info.dat` to determine the server address and port.
- If `server_info.dat` is not found, the default values are used (`localhost` for the server address and `1234` for the port).

## Contributing

If you'd like to contribute to this project, feel free to fork the repository and submit a pull request. Any suggestions or improvements are welcome!


