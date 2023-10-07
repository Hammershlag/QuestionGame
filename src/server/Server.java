package server;

import config.ConfigHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class Server {
    private static String configPath = "C:\\Projects\\TestGame\\TestGameServer\\src\\config\\config.txt";
    private int port;
    private String ip;
    private int maxClients;
    private ConcurrentHashMap<String, Socket> clients;

    public Server(ConfigHandler configHandler) {
        this.port = configHandler.getInt("port");
        this.ip = configHandler.getString("ip");
        this.maxClients = configHandler.getInt("max_clients");
        this.clients = new ConcurrentHashMap<>();
    }

    public void startServer() {
        System.out.println("Server started on: " + ip + ":" + port);
        System.out.println("Max Clients: " + maxClients);

        ExecutorService executorService = Executors.newFixedThreadPool(maxClients);
        ServerSocket server = null;

        try {
            server = new ServerSocket(port);
            server.setReuseAddress(true);

            while (true) {
                // Accept client connections until reaching the maximum
                if (clients.size() < maxClients) {
                    Socket client = server.accept();
                    String clientKey = getClientKey(client);
                    clients.put(clientKey, client);

                    // Display that a new client is connected to the server
                    System.out.println("New client connected " + clientKey);

                    // Create a new thread to handle the client
                    ClientHandler clientHandler = new ClientHandler(client, clients);
                    executorService.execute(clientHandler);
                } else {
                    // Handle messages from existing clients but do not accept new connections
                    Socket client = server.accept();
                    String clientKey = getClientKey(client);
                    if (clients.containsKey(clientKey)) {
                        // Create a new thread to handle the client
                        ClientHandler clientHandler = new ClientHandler(client, clients);
                        executorService.execute(clientHandler);
                    } else {
                        // Close the socket for new clients trying to connect
                        client.close();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getClientKey(Socket socket) {
        return socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
    }

    public static void main(String[] args) {
        ConfigHandler configHandler = new ConfigHandler(configPath, Server.class);
        Server server = new Server(configHandler);
        server.startServer();
    }
}
