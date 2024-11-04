package tutoringApp.Services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tutoringApp.Controllers.ClientHandler;
import tutoringApp.Models.MessageLog;

public class ServerMain {
    private static final int PORT = 6655;
    private static final Map<String, ClientHandler> clients = new HashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("[Server] ::: is listening on Port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.out.println("Could not listen on Port " + PORT);
        }
    }

    public static synchronized void addClient(String username, ClientHandler clientHandler) {
        clients.put(username, clientHandler);
    }

    public static synchronized void removeClient(String username) {
        clients.remove(username);
    }

    public static synchronized void broadcast(String message, ClientHandler sender) {
        if (clients.containsKey(sender.getUsername())) {
            String senderUsername = sender.getUsername();
            for (ClientHandler client : clients.values()) {
                if (client != sender && clients.containsKey(client.getUsername())) {
                    String recipientUsername = client.getUsername();
                    recordConversation(senderUsername, recipientUsername, message);
                    client.sendMessage("[Received] " + senderUsername + ": " + message);
                }
            }
        }
    }

   
    public static synchronized void sendPrivateMessage(String message,String username) {
        String[] parts = message.split(" ", 2); // Split into two parts: recipientUsername messageContent
        if (parts.length == 2) {
            String recipientUsername = parts[0].substring(1); // Remove '@' from the recipient's username
            String privateMessage = parts[1];
            ClientHandler recipient = clients.get(recipientUsername);
            if (recipient != null) {
                String[] messageParts = message.split(" ", 2); // Split message by space to separate sender's username
                if (messageParts.length > 0) {
                    String senderUsername = username; // Remove '@' from sender's username
                    ClientHandler sender = clients.get(senderUsername);
                    if (sender != null) {
                        recipient.sendMessage("[Private from " + sender.getUsername() + "]: " + privateMessage);
                        sender.sendMessage("[Private to " + recipient.getUsername() + "]: " + privateMessage);
                        recordConversation(senderUsername,recipientUsername,privateMessage); // Record private message in conversation log
                    } else {
                        // Handle case when sender is not found in clients map
                        System.out.println("Sender '" + senderUsername + "' not found in clients map.");
                    }
                } else {
                    // Handle case when message does not contain sender's username
                    System.out.println("Message does not contain sender's username: " + message);
                }
            } else {
                // Handle case when recipient is not online
                System.out.println("User '" + recipientUsername + "' is not online.");
            }
        } else {
            // Handle invalid private message format
            System.out.println("Invalid private message format: " + message);
        }
    }
    public static synchronized File createConversationLogFile(String client1, String client2) {
        String directoryName = "conversationLogs";
        if (!new File(directoryName).exists()) {
            new File(directoryName).mkdir();
        }

        String fileName = generateConversationFileName(client1, client2);
        File conversationLogFile = new File(directoryName, fileName);

        if (!conversationLogFile.exists()) {
            try {
                conversationLogFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return conversationLogFile;
    }

    public static synchronized String generateConversationFileName(String client1, String client2) {
        return client1.compareTo(client2) < 0 ? (client1 + "_" + client2 + "_conversation.log") : (client2 + "_" + client1 + "_conversation.log");
    }
    
    public static synchronized void recordConversation(String client1, String client2, String message) {
        File conversationLogFile = createConversationLogFile(client1, client2);

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(conversationLogFile))) {
            List<MessageLog> messageLogs = (List<MessageLog>) ois.readObject(); // Read existing messages
            MessageLog messageLog = new MessageLog();
            messageLog.setContent(String.format("[%s] [%s to %s]: %s\n", getCurrentTimestamp(), client1, client2, message));
            messageLogs.add(messageLog); // Add new message
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(conversationLogFile))) {
                oos.writeObject(messageLogs); // Write updated list back to file
            }
        } catch (IOException | ClassNotFoundException e) {
            // If file doesn't exist or is empty, create a new list with the current message and write it to the file
            List<MessageLog> messageLogs = new ArrayList<>();
            MessageLog messageLog = new MessageLog();
            messageLog.setContent(String.format("[%s] [%s to %s]: %s\n", getCurrentTimestamp(), client1, client2, message));
            messageLogs.add(messageLog);
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(conversationLogFile))) {
                oos.writeObject(messageLogs);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    public static synchronized String getCurrentTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return LocalDateTime.now().format(formatter);
    }
    
}
