package client.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * The `ClientThread` class represents a client thread that connects to a server and sends a message.
 * It provides a way to interact with a server in a separate thread.
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 */
public class ClientThread extends Thread {
    /**
     * The IP address of the server to connect to.
     */
    private final String ip;
    /**
     * The port number of the server to connect to.
     */
    private final int port;
    /**
     * The message to send to the server.
     */
    private final String message;

    /**
     * Constructs a new `ClientThread` with the specified server IP, port, and message.
     *
     * @param ip      The IP address of the server.
     * @param port    The port number to connect to.
     * @param message The message to send to the server.
     */
    public ClientThread(String ip, int port, String message) {
        this.ip = ip;
        this.port = port;
        this.message = message;
    }

    /**
     * Starts the client thread. It connects to the server, sends a message, and displays the server's reply.
     */
    @Override
    public void run() {
        try (Socket socket = new Socket(ip, port)) {
            // Writing to server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Reading from server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send the user input to the server
            out.println(message);
            out.flush();

            // Display server reply
            System.out.println("Server replied: " + in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
