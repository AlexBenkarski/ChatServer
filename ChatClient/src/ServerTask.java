import java.net.*;
import javafx.concurrent.Task;
import java.io.*;

public class ServerTask extends Task<Void> {
	private int port;
	private Server server;
	
	public ServerTask(Server server, int port) {
		this.port = port;
		this.server = server;
	}
	
	@Override
	protected Void call() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {

			System.out.println("Chat Server is listening on port " + port);

            // Use a infinite loop to keep listening new client connections
			while (true) {
                // Once a ServerSocket instance is created, call accept() to start listening for incoming client requests
				Socket socket = serverSocket.accept();
				System.out.println("New user connected");
                
                // Create a new UserThread to handle new client.
				UserThread newUser = new UserThread(socket, this.server);
				
                // Add the new user to data structure
				server.addUserThread(newUser);
                // Start the user thread
				newUser.start();
				

			}

		} catch (IOException ex) {
			System.out.println("Error in the server: " + ex.getMessage());
			ex.printStackTrace();
		}
		return null;
	}
	
}