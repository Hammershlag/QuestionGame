package server.core;

import config.ConfigHandler;
import server.database.questionDatabase.QuestionDatabaseHandler;
import server.database.relationDatabase.RelationDatabaseHandler;
import server.database.userDatabase.UserDatabaseHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.*;

import static help.ConsoleListener.stopConsoleListener;
import static help.HelpPrint.printHelp;

/**
 * The `Server` class represents a basic server application for handling client connections and processing messages.
 *
 * @author Tomasz Zbroszczyk
 * @since 05.10.2023
 * @version 1.0
 */
public class Server {
    /**
     * The path to the configuration file.
     */
    protected static String configPath = "C:\\Projects\\TestGame\\TestGameServer\\src\\config\\config.ch";
    /**
     * The port on which the server listens for incoming connections.
     */
    private int port;
    /**
     * The IP address on which the server listens for incoming connections.
     */
    private String ip;
    /**
     * The maximum number of clients that can be connected to the server at the same time.
     */
    private int maxClients;
    /**
     * The collection of connected clients.
     */
    private ConcurrentHashMap<String, Socket> clients;
    /**
     * The interval between pings to check for client responsiveness.
     */
    private int pingInterval;
    /**
     * The minimum interval between pings to check for client responsiveness.
     */
    private int minPingInterval;
    /**
     * The time of the last ping.
     */
    private long lastPingTime;
    /**
     * The handler for user database operations.
     */
    protected static UserDatabaseHandler userDatabaseHandler;
    /**
     * The handler for question database operations.
     */
    protected static QuestionDatabaseHandler questionDatabaseHandler;
    /**
     * The handler for relation database operations.
     */
    protected static RelationDatabaseHandler relationDatabaseHandler;
    /**
     * The configuration handler for server configuration.
     */
    protected static ConfigHandler configHandler;

    /**
     * Constructs a `Server` instance with the specified configuration handler.
     *
     * @param configHandler The configuration handler to use for server configuration.
     */
    public Server(ConfigHandler configHandler) {
        this.port = configHandler.getInt("port");
        this.ip = configHandler.getBoolean("outgoing") ? configHandler.getString("ip") : configHandler.getString("iploc");
        this.maxClients = configHandler.getInt("max_clients");
        this.clients = new ConcurrentHashMap<>();
        this.pingInterval = configHandler.getInt("ping_interval"); // in milliseconds
        this.minPingInterval = configHandler.getInt("min_ping_interval"); // in milliseconds
        this.lastPingTime = System.currentTimeMillis();
    }

    /**
     * Default constructor for the `Server` class.
     */
    public Server(){}

    /**
     * Starts the server, accepts client connections, and handles incoming messages.
     */
    public void startServer() {
        System.out.println("Server started on: " + ip + ":" + port);
        System.out.println("Max Clients: " + maxClients);
        System.out.println("Ping Interval: " + pingInterval);

        ExecutorService executorService = Executors.newFixedThreadPool(maxClients);
        ServerSocket server = null;

        try {
            if(!configHandler.getBoolean("outgoing"))
                server = new ServerSocket(port);
            else {
                server = new ServerSocket();
                server.bind(new InetSocketAddress(ip, port));
            }
            server.setReuseAddress(true);

            // Create a separate thread for pinging clients
            Thread pingThread = new Thread(this::pingClientsInBackground);
            pingThread.setDaemon(true); // Mark the thread as daemon, so it won't prevent the JVM from exiting
            pingThread.start();

            // Create a separate thread for asynchronous input listening
            Thread inputThread = new Thread(this::listenForInputInBackground);
            inputThread.setDaemon(true);
            inputThread.start();

            while (true) {
                // Accept client connections until reaching the maximum
                if (clients.size() < maxClients) {
                    Socket client = server.accept();
                    String clientKey = getClientKey(client);
                    clients.put(clientKey, client);

                    // Display that a new client is connected to the server
                    System.out.println("New client connected " + clientKey);

                    // Create a new thread to handle the client
                    ClientHandler clientHandler = new ClientHandler(client, clients,userDatabaseHandler, questionDatabaseHandler, relationDatabaseHandler);
                    executorService.execute(clientHandler);
                } else {
                    // Handle messages from existing clients but do not accept new connections
                    Socket client = server.accept();
                    String clientKey = getClientKey(client);
                    if (clients.containsKey(clientKey)) {
                        // Create a new thread to handle the client
                        ClientHandler clientHandler = new ClientHandler(client, clients,userDatabaseHandler, questionDatabaseHandler, relationDatabaseHandler);
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
    /**
     * Listens for user input in the background. It allows the server operator to enter commands through the console.
     * The available commands are "exit" (to gracefully stop the server), "ping" (to ping all clients), and "clear"
     * (to clear the terminal screen).
     */
    private void listenForInputInBackground() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine().trim();

            if ("exit".equalsIgnoreCase(input)) {
                // Handle 'exit' command
                closeAllClientConnections();
                stopConsoleListener();
                System.exit(0); // Exit the server
            } else if ("ping".equalsIgnoreCase(input)) {
                // Handle the "ping" command to ping all client connections
                pingAllClients();
            } else if ("clear".equalsIgnoreCase(input)) {
                // Handle the "clear" command to clear the terminal
                clearTerminal();
            } else {
                System.out.println("Unknown command: " + input);
            }
        }
    }

    /**
     * Clears the terminal window where the server is running. The method detects the operating system and runs the
     * appropriate command to clear the terminal screen. It supports Windows, Unix/Linux, and macOS.
     */
    private void clearTerminal() {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                // For Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
                // For Unix/Linux or macOS
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Pings all connected clients to check for responsiveness.
     */
    private void pingAllClients() {
        // Iterate through clients and send ping messages
        for (Map.Entry<String, Socket> entry : clients.entrySet()) {
            String clientKey = entry.getKey();
            Socket client = entry.getValue();
            try {
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                // Send a ping message to the client
                out.println("Forced ping");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Closes all client connections and stops the server.
     */
    private void closeAllClientConnections() {
        Iterator<Map.Entry<String, Socket>> iterator = clients.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Socket> entry = iterator.next();
            Socket client = entry.getValue();
            try {
                client.close();
                iterator.remove();
                System.out.println("Connection with client " + entry.getKey() + " closed.");
            } catch (IOException e) {
                // Handle any exceptions as needed
                e.printStackTrace();
            }
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

    /**
     * Pings clients at regular intervals in the background.
     */
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
                Thread.sleep(minPingInterval); // Sleep for 50 milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Pings all connected clients to check for responsiveness. This method iterates through connected clients and sends
     * ping messages to each client. If a client doesn't respond within the specified time, it is considered unresponsive
     * and removed from the list of clients.
     */
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

    /**
     * Handles command-line arguments and overrides configuration properties.
     *
     * @param args          An array of command-line arguments.
     * @param configHandler The configuration handler to override properties.
     */
    public static void checkArgs(String[] args, ConfigHandler configHandler) {
        for(int i = 0; i < args.length; i += 2) {
            if(args[i].equals("--help") || args[i].equals("-h")) {
                printHelp("C:\\Projects\\TestGame\\TestGameServer\\src\\server\\README.md"); //Print README.md as help
                System.exit(0);
            } else if(args[i].equals("--port") || args[i].equals("-p")) {
                configHandler.overrideProperty("port", args[i + 1]);
            } else if(args[i].equals("--max-clients") || args[i].equals("-c")) {
                configHandler.overrideProperty("max_clients", args[i + 1]);
            } else if(args[i].equals("--ping-interval") || args[i].equals("-i")) {
                configHandler.overrideProperty("ping_interval", args[i + 1]);
            } else if(args[i].equals("--outgoing") || args[i].equals("-o")) {
                configHandler.overrideProperty("outgoing", args[i + 1]);
            } else if(args[i].equals("--log-file") || args[i].equals("-l")) {
                configHandler.overrideProperty("log_file", args[i + 1]);
            } else if(args[i].equals("--max-log-files") || args[i].equals("-m")) {
                configHandler.overrideProperty("max_log_files", args[i + 1]);
            } else if(args[i].equals("--log-file-dir") || args[i].equals("-d")) {
                configHandler.overrideProperty("log_file_dir", args[i + 1]);
            } else if(args[i].equals("--user-database-dir") || args[i].equals("-u")){
                configHandler.overrideProperty("user_database_file", args[i + 1]);
            } else if(args[i].equals("--question-database-dir") || args[i].equals("-q")){
                configHandler.overrideProperty("question_database_dir", args[i + 1]);
            } else if(args[i].equals("--relation-database-dir") || args[i].equals("-r")) {
                configHandler.overrideProperty("relation_database_dir", args[i + 1]);
            } else {
                System.out.println("Unknown argument: " + args[i]);
            }
        }
    }
}
