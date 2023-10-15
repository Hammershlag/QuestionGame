package client.core.messages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * The MessageReceiver class is responsible for receiving and processing messages from the server.
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 08.10.2023
 * @see java.net.Socket
 */
public class MessageReceiver implements Runnable {
    /**
     * The Socket object representing the client-server connection.
     */
    private final Socket socket;
    /**
     * A unique identifier for the client.
     */
    private final String clientNumber;

    /**
     * Constructs a new MessageReceiver instance.
     *
     * @param socket       The Socket object representing the client-server connection.
     * @param clientNumber A unique identifier for the client.
     */
    public MessageReceiver(Socket socket, String clientNumber) {
        this.socket = socket;
        this.clientNumber = clientNumber;
    }

    /**
     * Run the message receiver, listening for incoming messages from the server.
     */
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
