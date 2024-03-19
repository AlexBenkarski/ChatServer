import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.shape.Line;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;

import java.net.*;
import java.util.*;
import java.io.*;
import javafx.concurrent.Worker;

public class ServerGUI extends Application{
	private int port;
	private Set<String> userNames = new HashSet<>();
	
	//port number
	private Label portLabel = new Label("Port:");
	private TextField portTextField = new TextField();
	//launch button
	private Button launchBtn = new Button("Launch");
	//shut down button
	private Button shutDownBtn = new Button("Shut Down");

	
	// server info
	private Label incomingText = new Label();
	private TextArea serverInfo = new TextArea();
	
	//server object
	private Server server= new Server();
	
	@Override //Override the start method in the Application method
	public void start(Stage primaryStage) {
		HBox hbox1 = new HBox();
		hbox1.setPadding(new Insets(15, 12, 15, 12));
		hbox1.setSpacing(10);
		hbox1.setAlignment(Pos.CENTER);
		hbox1.getChildren().addAll(portLabel, portTextField, launchBtn, shutDownBtn);
		
		VBox vbox1 = new VBox();
		vbox1.setPadding(new Insets(0, 12, 15, 12));
		vbox1.getChildren().addAll(hbox1, serverInfo);
		
		// launch button event handler
		launchBtn.setOnAction(e  -> {
			//TODO: launch server and start listen for connections...
			
			//get port number
			port = Integer.valueOf(portTextField.getText());//String -> Integer
			
			server.setPort(port);
			
			//Creates a server task
			ServerTask servertask = new ServerTask(server, port);
			
			startServerTask(servertask);
		});	
		shutDownBtn.setOnAction(e -> {
			primaryStage.close();
		});
		
		// Create a scene and place it in the stage
		Scene scene = new Scene(vbox1);
		primaryStage.setTitle("Chat Room Server"); //The Stage Title
		primaryStage.setResizable(false); //disable resize
		primaryStage.setScene(scene);; // Place the scene in the stage
		primaryStage.show(); //Display the stage
		
		
		//Makes the text box not editable
		serverInfo.setEditable(false);
		
		//view serverinfo
		System.out.println("");
		
		//Creates color
		shutDownBtn.setStyle("-fx-background-color: #FF6347");
		launchBtn.setStyle("-fx-background-color: #00FF7F");
		vbox1.setStyle("-fx-background-color: #00BFFF");
		
		//change color and font size
		portTextField.setStyle("-fx-text-inner-color: green;");
		portTextField.setStyle("-fx-font-size: 18");
		
		
		
		//change the font of port label
		portLabel.setFont(Font.font("Comic Sans MS", 20));
		
		
	}
	
	public void startServerTask(ServerTask task) {
		//Schedule the task on a background task
		Thread backgroundReadThread = new Thread(task);
		backgroundReadThread.setDaemon(true);
		backgroundReadThread.start();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}