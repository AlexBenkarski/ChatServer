import java.io.*;
import java.net.*;
import java.util.*;

public class UserThread extends Thread{
	    private Socket socket;
	    private Server server;
	    private PrintWriter writer;

	    // UserThread constructor
	    public UserThread(Socket socket, Server server) {
	        this.socket = socket;
	        this.server = server;
	    }

	    public void run() {
	        try {
	            // Once a Socket object is returned, use its InputStream to read data sent from the client
	            InputStream input = socket.getInputStream();
	            // Wrap the InputStream in a BufferedReader to read data as String
	            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	            
	            // Use the OutputStream associated with the Socket to send data to the client
	            OutputStream output = socket.getOutputStream();
	            // As the OutputStream provides only low-level methods (writing data as a byte array), you can wrap it in a PrintWriter to send data in text format
	            writer = new PrintWriter(output, true);

	            printUsers();

	            // read user name from commend line
	            String userName = reader.readLine();
	            server.addUserName(userName);

	            String serverMessage = "New user connected: " + userName;
	            // broadcast "New user connected: " message to every client
	            server.broadcast(serverMessage, this);

	            String clientMessage;
	            
	            // keep accepting user message until user says 'bye'
	            do {
	                // read user message, broadcast it to everyone
	                clientMessage = reader.readLine();
	                serverMessage = "[" + userName + "]: " + clientMessage;
	                server.broadcast(serverMessage, this);

	            } while (!clientMessage.equals("bye"));

	            // after user exits, remove it
	            server.removeUser(userName, this);
	            socket.close();

	            serverMessage = userName + " has quitted.";
	            server.broadcast(serverMessage, this);

	        } catch (IOException ex) {
	            System.out.println("Error in UserThread: " + ex.getMessage());
	            ex.printStackTrace();
	        }
	    }

	    /**
	     * Sends a list of online users to the newly connected user.
	     */
	    void printUsers() {
	        if (server.hasUsers()) {
	            writer.println("Connected users: " + server.getUserNames());
	        } else {
	            writer.println("No other users connected");
	        }
	    }

	    /**
	     * Sends a message to the client.
	     */
	    void sendMessage(String message) {
	        writer.println(message);
	    }

		
	}