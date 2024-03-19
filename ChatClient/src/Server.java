import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
	private int port;
	private Set<String> userNames = new HashSet<>();
	private Set<UserThread> userThreads = new HashSet<>();
	
	public Server() {
		
	}
	
	public Server(int port) {
		this.port = port;
	}
	
	//setter for the port method
	public void setPort(int port) {
		this.port = port;
	}
	
	//getter for port
	public int getPort() {
		return this.port;
	}
	
	public void addUserThread(UserThread userthread) {
		userThreads.add(userthread);
	}
	
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
