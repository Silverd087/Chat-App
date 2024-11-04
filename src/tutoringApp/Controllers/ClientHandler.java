package tutoringApp.Controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import tutoringApp.Services.ServerMain;

public class ClientHandler extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void run() {
        try {
            System.out.println("[Client] connected");
            out.println("Enter your username:");
            username = in.readLine();
            ServerMain.addClient(username, this);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("[Client] message: " + inputLine);
                if (inputLine.startsWith("@")) {
                    ServerMain.sendPrivateMessage(inputLine,username);
                } else {
                    ServerMain.broadcast(username + ": " + inputLine, this);
                }
            }
        } catch (IOException e) {
            System.out.println("Error handling: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            ServerMain.removeClient(username);
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String getUsername() {
        return username;
    }
}