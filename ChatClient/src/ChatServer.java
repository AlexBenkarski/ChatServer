import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This is the chat server program.
 * Press Ctrl + C to terminate the program.
 */
public class ChatServer {
	private int port;
	private Set<String> userNames = new HashSet<>();
	private Set<UserThread> userThreads = new HashSet<>();

	public ChatServer(int port) {
		this.port = port;
	}

	public void execute() {
        // Creates a server socket that is bound to the specified port number.
		try (ServerSocket serverSocket = new ServerSocket(port)) {

			System.out.println("Chat Server is listening on port " + port);

            // Use a infinite loop to keep listening new client connections
			while (true) {
                // Once a ServerSocket instance is created, call accept() to start listening for incoming client requests
				Socket socket = serverSocket.accept();
				System.out.println("New user connected");
                
                // Create a new UserThread to handle new client.
				UserThread newUser = new UserThread(socket, this);
                // Add the new user to data structure
				userThreads.add(newUser);
                // Start the user thread
				newUser.start();

			}

		} catch (IOException ex) {
			System.out.println("Error in the server: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Syntax: java ChatServer <port-number>");
			System.exit(0);
		}
        // Accept port number from commend arguments
		int port = Integer.parseInt(args[0]);

        // Create CharServer object
		ChatServer server = new ChatServer(port);
		server.execute();
	}
    
    
	/**
	 * Delivers a message from one user to others (broadcasting)
	 */
	void broadcast(String message, UserThread excludeUser) {
		for (UserThread aUser : userThreads) {
			if (aUser != excludeUser) {
				aUser.sendMessage(message);
			}
		}
	}

	/**
	 * Stores username of the newly connected client.
	 */
	void addUserName(String userName) {
		userNames.add(userName);
	}

	/**
	 * When a client is disconneted, removes the associated username and UserThread
	 */
	void removeUser(String userName, UserThread aUser) {
		boolean removed = userNames.remove(userName);
		if (removed) {
			userThreads.remove(aUser);
			System.out.println("The user " + userName + " quitted");
		}
	}

	Set<String> getUserNames() {
		return this.userNames;
	}

	/**
	 * Returns true if there are other users connected (not count the currently connected user)
	 */
	boolean hasUsers() {
		return !this.userNames.isEmpty();
	}
}



/**
 * This thread handles connection for each connected client, so the server
 *    can handle multiple clients at the same time.
 */
class UserThread extends Thread {
    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;

    // UserThread constructor
    public UserThread(Socket socket, ChatServer server) {
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