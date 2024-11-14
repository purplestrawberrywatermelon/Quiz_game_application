import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuizServer {
    private static int PORT = 1234; // Port number for the server
    private static List<Question> questions = new ArrayList<>(); // List to store questions
    private static ExecutorService threadPool;
    public static void main(String[] args) {
        threadPool = Executors.newFixedThreadPool(10); // Create a thread pool with a maximum of 10 threads to handle clients
        loadServerConfig(); // Load server configuration from file
        loadQuestions(); // Load quiz questions
    
        try (ServerSocket listener = new ServerSocket(PORT)) {
            System.out.println("Start Server...");
            System.out.println("Quiz Server is running on port " + PORT);
            System.out.println("Waiting for clients");
    
            while (true) {
                Socket socket = listener.accept(); // Accept a new client connection
                System.out.println("A new connection has been established!");
                System.out.println("New client connected: " + socket.getInetAddress());
    
                // Create a Runnable task to handle the client
                Runnable clientHandler = new ClientHandler(socket, listener);
                threadPool.execute(clientHandler); // Submit the task to the thread pool
            }
        } catch (IOException e) {
            System.out.println(e.getMessage()); // Print any IO exception messages
        } finally {
            threadPool.shutdown(); // Shut down the thread pool when the server stops
        }
    }
    
    // Method to load server configuration from file
    private static void loadServerConfig() {
        File configFile = new File("server_info.dat");
        if (configFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("SERVER_PORT=")) {
                        PORT = Integer.parseInt(line.split("=")[1].trim()); // Set the port number from the configuration file
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading configuration file: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Invalid port number in configuration file: " + e.getMessage());
            }
        } else {
            System.out.println("Configuration file not found. Using default values.");
        }
    }
    
    // Method to load quiz questions into the list
    private static void loadQuestions() {
        questions.add(new Question("What is the capital of France?", "Paris"));
        questions.add(new Question("What is 2 + 2?", "4"));
        questions.add(new Question("What is the color of the sky?", "blue"));
        questions.add(new Question("What is the largest ocean on Earth?", "Pacific"));
        questions.add(new Question("What year did the Titanic sink?", "1912"));
    }
    
    // Inner class to handle client connections
    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private ServerSocket listener;
    
        // Constructor to initialize the client socket and server socket
        public ClientHandler(Socket socket, ServerSocket listener) {
            this.clientSocket = socket;
            this.listener = listener;
        }
    
        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {
    
                int score = 0;
                boolean flag = true;
    
                // Iterate over each question and send it to the client
                for (Question question : questions) {
                    out.write("Server> Question: " + question.getQuestion() + "\n"); // Send the question to the client
                    out.flush();
    
                    // Receive the client's answer
                    String answer = in.readLine();
                    if (answer != null && answer.equalsIgnoreCase(question.getAnswer())) {
                        score++; // Increase score if the answer is correct
                        out.write("RESPONSE: Correct! type 'next' to continue, or type 'end' to stop.\n");
                    } else {
                        out.write("RESPONSE: Incorrect! The correct answer is: " + question.getAnswer() + ". type 'next' to continue, or type 'end' to stop.\n");
                    }
                    out.flush();
    
                    // Loop to determine whether to continue or stop the quiz
                    while (true) {
                        String answer2 = in.readLine();
                        if (answer2 != null && answer2.equalsIgnoreCase("end")) {
                            System.out.println("Quiz ended by client."); // Log that the client ended the quiz
                            flag = false;
                            break;
                        } else if (answer2 != null && answer2.equalsIgnoreCase("next")) {
                            break; // Continue to the next question
                        } else {
                            out.write("Type 'next' to continue, or type 'end' to stop.\n"); // Prompt client for valid input
                            out.flush();
                        }
                    }
    
                    if (!flag) break; // Exit loop if the client chooses to end the quiz
                }
    
                // Send the final score to the client
                out.write("Quiz complete\n");
                out.flush();
                out.write("Quiz complete: Your score is " + score + " out of " + questions.size() + "\n");
                out.flush();
    
            } catch (IOException e) {
                System.out.println("Error handling client: " + e.getMessage()); // Print error if an issue occurs while handling the client
            } finally {
                try {
                    clientSocket.close(); // Close the client socket
                    listener.close(); // Close the server socket
                } catch (IOException e) {
                    System.out.println("Disconnected."); // Log disconnection
                }
            }
        }
    }
    
    // Inner class to represent a quiz question
    private static class Question {
        private String question;
        private String answer;
    
        // Constructor to initialize the question and answer
        public Question(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }
    
        // Getter for the question text
        public String getQuestion() {
            return question;
        }
    
        // Getter for the answer text
        public String getAnswer() {
            return answer;
        }
    }
}
