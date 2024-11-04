package tutoringApp.Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class WClient {
	String hostName = "localhost";
	int portNumber = 6655;
	
	Socket socket = null;
	PrintWriter out;
	BufferedReader in;
	BufferedReader stdin;
	
	private WClientListener listener;
	
	public WClient() {
		try {
			System.out.println("Connected to server");
			socket = new Socket(hostName,portNumber);
		}catch(IOException e) {
			System.err.println("Couldn't get I/O connection to "+hostName);
		}
	}
	public void start(WClientListener listener) {
		 try {
			 this.listener = listener;
			 out = new PrintWriter(socket.getOutputStream(), true);
			 in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			 stdin = new BufferedReader(new InputStreamReader(System.in));

			 receivingMessages();
		 }catch (UnknownHostException e) {
		 System.err.println("Unknown host: " + hostName);
		 } catch (IOException e) {
		 System.err.println("Couldn't get I/O for the connection to " +hostName);
		 }
		 }

		 public void receivingMessages() {
		 Thread receiveMessagesThread = new Thread(() -> {
		 try {
		 String serverResponse;
		 while ((serverResponse = in.readLine()) != null) 
		 {
			 listener.onMessageRecieved(serverResponse);
			 System.out.println("Server: " + serverResponse);
		 }
		 }catch (IOException e) {
			 System.err.println("Error receiving messages: " + e.getMessage());
		 }
		 });
		 receiveMessagesThread.start();
		 }
		 public void sendMessage(String message) {
			 out.println(message);
		 }
}