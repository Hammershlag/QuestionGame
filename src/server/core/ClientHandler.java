package server.core;

import server.core.messages.MessageProcessor;
import server.database.questionDatabase.QuestionDatabaseHandler;
import server.database.relationDatabase.RelationDatabaseHandler;
import server.database.userDatabase.UserDatabaseHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The `ClientHandler` class is responsible for handling communication with individual clients connected to the server.
 *
 * @uses Runnable
 * @author Tomasz Zbroszczyk
 * @since 05.10.2023
 * @version 1.0
 */
public class ClientHandler implements Runnable {
    /**
     * The socket for communicating with the client.
     */
    private final Socket clientSocket;
    /**
     * The collection of connected clients.
     */
    private final ConcurrentHashMap<String, Socket> clients;
    /**
     * The handler for user database operations.
     */
    private final UserDatabaseHandler userDatabaseHandler;
    /**
     * The handler for question database operations.
     */
    private final QuestionDatabaseHandler questionDatabaseHandler;

    /**
     * The handler for relation database operations.
     */
    private final RelationDatabaseHandler relationDatabaseHandler;

    /**
     * Constructs a `ClientHandler` for handling communication with a client.
     *
     * @param socket                The socket for communicating with the client.
     * @param clients               The collection of connected clients.
     * @param userDatabaseHandler    The handler for user database operations.
     * @param questionDatabaseHandler The handler for question database operations.
     */
    public ClientHandler(Socket socket, ConcurrentHashMap<String, Socket> clients, UserDatabaseHandler userDatabaseHandler, QuestionDatabaseHandler questionDatabaseHandler, RelationDatabaseHandler relationDatabaseHandler) {
        this.clientSocket = socket;
        this.clients = clients;
        this.userDatabaseHandler = userDatabaseHandler;
        this.questionDatabaseHandler = questionDatabaseHandler;
        this.relationDatabaseHandler = relationDatabaseHandler;
    }

    /**
     * Handles communication with the client. Reads incoming messages, processes them, and sends responses.
     */
    @Override
    public void run() {
        PrintWriter out = null;
        BufferedReader in = null;
        String clientKey = getClientKey(clientSocket);

        try {
            // Get the output stream of the client
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Get the input stream of the client
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String line;

            while ((line = in.readLine()) != null) {
                if ("exit".equals(line)) {
                    // Inform the server that the client is closing and indicate which client is exiting
                    closeClient(clientKey);
                    break;
                } else {
                    // Writing the received message from the client
                    System.out.printf("Received from %s: %s%n", clientKey, line);
                    MessageProcessor messageProcessor = new MessageProcessor(userDatabaseHandler, questionDatabaseHandler, relationDatabaseHandler);
                    String outMessage = messageProcessor.processMessage(line);
                    out.println(outMessage);
                    out.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Closes the client's connection, removes the client from the collection, and displays an exit message.
     *
     * @param clientKey The unique key identifying the client.
     */
    private void closeClient(String clientKey) {
        // Remove the client from the map
        clients.remove(clientKey);

        // Close the client's socket
        try {
            clientSocket.close();

            // Display a message indicating which client is exiting
            System.out.println("Client " + clientKey + " exited.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a unique key for identifying a client based on their IP address and port.
     *
     * @param socket The socket associated with the client.
     * @return A unique key for identifying the client.
     */
    private String getClientKey(Socket socket) {
        return socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
    }
}
