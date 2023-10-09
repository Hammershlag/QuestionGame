package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final ConcurrentHashMap<String, Socket> clients;

    public ClientHandler(Socket socket, ConcurrentHashMap<String, Socket> clients) {
        this.clientSocket = socket;
        this.clients = clients;
    }

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
                    MessageProcessor messageProcessor = new MessageProcessor();
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

    private String getClientKey(Socket socket) {
        return socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
    }
}
