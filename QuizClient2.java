
import java.io.*;
import java.net.*;

public class QuizClient2 {
	private static String SERVER_ADDRESS = "localhost";// Server address to connect to
	private static int SERVER_PORT = 1234;// Port number of the server

	public static void main(String[] args) {
		loadServerConfig(); // Load server configuration from file
	
		BufferedReader in = null;
		BufferedReader stin = null;
		BufferedWriter out = null;
	
		Socket socket = null;
	
		try {
			socket = new Socket(SERVER_ADDRESS, SERVER_PORT); // Create a socket to connect to the server
	
			in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Input stream to receive messages from the server
			stin = new BufferedReader(new InputStreamReader(System.in)); // Input stream to read user input from the console
	
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // Output stream to send messages to the server
	
			String outputMessage;
			boolean flag = true;
			while (flag) {
				// Receive question from the server
				String inputMessage = in.readLine();
	
				if (inputMessage.equals("Quiz complete")) { // Check if the quiz is complete
					inputMessage = in.readLine(); // Receive the final score
					System.out.println(inputMessage); // Print the final score
					break; // Exit the loop
				}
				System.out.println(inputMessage); // Print the question received from the server
	
				// User inputs their answer
				System.out.print("Client> ");
				outputMessage = stin.readLine(); // Read user answer from the console
				out.write(outputMessage + "\n"); // Send the answer to the server
				out.flush();
	
				// Receive response from the server
				String responseMessage = in.readLine();
				System.out.println(responseMessage); // Print the response (correct or incorrect)
	
				// User decides whether to continue or end the quiz
				System.out.print("Client> ");
	
				while (true) {
					outputMessage = stin.readLine(); // Read user input for 'next' or 'end'
					out.write(outputMessage + "\n"); // Send the command to the server
					out.flush();
					if (outputMessage.equalsIgnoreCase("end")) { // If user chooses to end the quiz
						flag = false;
						break;
					} else if (outputMessage.equalsIgnoreCase("next")) { // If user chooses to continue to the next question
						break;
					} else {
						responseMessage = in.readLine(); // Receive prompt from server to enter a valid command
						System.out.println(responseMessage); // Print the prompt
					}
				}
			}
	
		} catch (IOException e) {
			System.out.println(e.getMessage()); // Print any IO exception messages
		} finally {
			try {
				socket.close(); // Close the socket when done
			} catch (IOException e) {
				System.out.println("Disconnected"); // Print message when disconnected
			}
		}
	}
	
	// Method to load server configuration from file
	private static void loadServerConfig() {
		File configFile = new File("server_info.dat");
	
		if (configFile.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
				String line;
				while ((line = br.readLine()) != null) {
					if (line.startsWith("SERVER_ADDRESS=")) {
						SERVER_ADDRESS = line.split("=")[1].trim(); // Set the server address from the configuration file
					} else if (line.startsWith("SERVER_PORT=")) {
						SERVER_PORT = Integer.parseInt(line.split("=")[1].trim()); // Set the port number from the configuration file
					}
				}
			} catch (IOException e) {
				System.out.println("Error reading configuration file: " + e.getMessage()); // Print error if reading fails
			} catch (NumberFormatException e) {
				System.out.println("Invalid port number in configuration file: " + e.getMessage()); // Print error if port number is invalid
			}
		} else {
			System.out.println("Configuration file not found. Using default values."); // Print message if configuration file is not found
		}
	}
}	