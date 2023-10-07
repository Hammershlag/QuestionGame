package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MessageReceiver implements Runnable {
    private Socket socket;
    private String clientNumber;

    public MessageReceiver(Socket socket, String clientNumber) {
        this.socket = socket;
        this.clientNumber = clientNumber;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message;
            while ((message = in.readLine()) != null) {
                if (!"Ping".equalsIgnoreCase(message)) {
                    // Display messages from the server as soon as they are received (excluding "Ping")
                    System.out.println("Client " + clientNumber + " received: " + message);
                }
            }
        } catch (IOException e) {
            // Handle exceptions, e.g., connection closed
            System.out.println("Client " + clientNumber + " disconnected.");
        }
    }
}