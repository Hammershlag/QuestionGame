package server;

import config.ConfigHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

public class Server {
    private static String configPath = "C:\\Projects\\TestGame\\TestGameServer\\src\\config\\config.txt";
    private int port;
    private String ip;
    private int maxClients;
    private ConcurrentHashMap<String, Socket> clients;
    private int pingInterval;
    private long lastPingTime;

    public Server(ConfigHandler configHandler) {
        this.port = configHandler.getInt("port");
        this.ip = configHandler.getString("ip");
        this.maxClients = configHandler.getInt("max_clients");
        this.clients = new ConcurrentHashMap<>();
        this.pingInterval = configHandler.getInt("ping_interval"); // in milliseconds
        this.lastPingTime = System.currentTimeMillis();
    }

    public void startServer() {
        System.out.println("Server started on: " + ip + ":" + port);
        System.out.println("Max Clients: " + maxClients);

        ExecutorService executorService = Executors.newFixedThreadPool(maxClients);
        ServerSocket server = null;

        try {
            server = new ServerSocket(port);
            server.setReuseAddress(true);

            // Create a separate thread for pinging clients
            Thread pingThread = new Thread(this::pingClientsInBackground);
            pingThread.setDaemon(true); // Mark the thread as daemon so it won't prevent the JVM from exiting
            pingThread.start();

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

    private void pingClientsInBackground() {
        while (true) {
            // Calculate the time elapsed since the last ping
            long currentTime = System.currentTimeMillis();
            long timeElapsed = currentTime - lastPingTime;

            // Check if it's time to ping clients based on the pingInterval (500 milliseconds)
            if (timeElapsed >= pingInterval) {
                pingClients();
                lastPingTime = currentTime;
            }

            // Sleep for a short duration to avoid busy-waiting
            try {
                Thread.sleep(50); // Sleep for 50 milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void pingClients() {
        // Iterate through clients and send ping messages
        Iterator<Map.Entry<String, Socket>> iterator = clients.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Socket> entry = iterator.next();
            String clientKey = entry.getKey();
            Socket client = entry.getValue();
            try {
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                // Send a ping message (you can define your ping message here)
                out.println("Ping");
                out.flush();
            } catch (IOException e) {
                // Client is not responding, remove it from the clients map
                iterator.remove();
                try {
                    client.close();
                    System.out.println("Client " + clientKey + " removed due to unresponsiveness.");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ConfigHandler configHandler = new ConfigHandler(configPath, Server.class);
        Server server = new Server(configHandler);
        server.startServer();
    }
}
