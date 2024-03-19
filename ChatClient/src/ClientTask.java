import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import java.io.*;
import java.net.Socket;

public class ClientTask extends Task<Void> {

    private BufferedReader reader;
    private Socket socket;
    private Client client;

    // default constructor
    public ClientTask(){
    }

    // second constructor
    public ClientTask(Socket socket, Client client){
        this.socket = socket;
        this.client = client;
        
        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // The task implementation
    @Override
    protected Void call(){
    
    	while (true) {
            try {
                String response = reader.readLine();
                System.out.println("\n" + response);
                
                //update message propriety of the task
                this.updateMessage(response);
                
                // prints the user name after displaying the server's message
                if (client.getUserName() != null) {
                    System.out.print("[" + client.getUserName() + "]: ");
                }
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    	return null;
    }

    @Override
    protected void cancelled(){
        super.cancelled();
        updateMessage("The task was cancelled.");
    }
    @Override
    protected void failed(){
        super.failed();
        updateMessage("The task failed.");
    }

    @Override
    public void succeeded(){
    	super.succeeded();
    	updateMessage("The task finished successfully.");
    }
}