package client;

import config.ConfigHandler;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

public class Client {
    private static String configPath = "C:\\Projects\\TestGame\\TestGameServer\\src\\config\\config.txt";

    private int port;
    private String ip;
    private double maxResponseTime;
    private Map<String, Socket> clients; // Map to store client numbers and their sockets
    private Timer timer;
    private int firstClientIndex;
    private int connectionTries;
    private ExecutorService messageReceiverExecutor;

    public Client(ConfigHandler configHandler) {
        this.port = configHandler.getInt("port");
        this.ip = configHandler.getString("ip");
        this.maxResponseTime = configHandler.getDouble("max_response_time");
        this.firstClientIndex = configHandler.getInt("first_client_index");
        this.connectionTries = configHandler.getInt("connection_tries");
        this.clients = new HashMap<>();
        this.timer = new Timer();
        this.messageReceiverExecutor = Executors.newCachedThreadPool();
    }

    public void startClient() {
        System.out.println("Client started on: " + ip + ":" + port);
        System.out.println("All the commands are available in the README.md file");
        System.out.println();

        // Implement client logic here
        // Initialize the client counter
        try {
            // Object of scanner class
            Scanner sc = new Scanner(System.in);
            String line;

            while (true) {
                // Read user input
                System.out.print("Enter a command: ");
                System.out.println();
                line = sc.nextLine();

                if ("exit".equalsIgnoreCase(line)) {
                    // Exit the program after closing all clients
                    closeAllClients();
                    messageReceiverExecutor.shutdownNow();
                    System.exit(0);
                    break;
                } else if ("all:exit".equalsIgnoreCase(line)) {
                    // Send exit messages to all clients, closing all connections
                    sendExitToAllClients();
                } else if ("clear".equalsIgnoreCase(line)) {
                    // Handle the "clear" command to clear the terminal
                    clearTerminal();
                } else {
                    processCommand(line);
                }

                // Display the list of connected clients
                System.out.print("Number of connected clients: " + clients.size() + " ");
                if (!clients.isEmpty()) {
                    System.out.print(clients.keySet());
                    System.out.println();
                }
                System.out.println();
            }

            // Closing the scanner object
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    private void processCommand(String line) {
        String[] parts = line.split(":", 2);
        if (parts.length == 2) {
            String command = parts[0].trim();
            String message = parts[1].trim();

            if ("new".equalsIgnoreCase(command)) {
                // Increment the client counter and create a new client with the incremented number
                createAndSendClient(message);
            } else if (clients.containsKey(command)) {
                // Send the message from the specified client
                if ("exit".equalsIgnoreCase(message)) {
                    // Close the client's socket and remove it from the map
                    closeClient(clients.get(command), command);
                } else {
                    sendMessage(clients.get(command), message);
                }
            } else {
                System.out.println("Client number invalid");
            }
        } else {
            System.out.println("Invalid command format");
        }
    }

    private void createAndSendClient(String message) {
        // Try to connect to the server n times
        for (int i = 0; i < connectionTries; i++) {
            try {
                // Create a new client socket
                Socket socket = new Socket(ip, port);

                // Send the message from the new client
                sendMessage(socket, message);

                // Wait for the server response
                String response = receiveMessage(socket);

                // Check if the server replied with null
                if (response != null) {
                    // Increment the client counter and add the new client to the map
                    String newClientNumber = "" + (firstClientIndex++);
                    clients.put(newClientNumber, socket);

                    // Start a message receiver thread for the new client
                    startMessageReceiver(socket, newClientNumber);

                    break;
                } else {
                    // Server replied with null, close the client socket
                    socket.close();
                    System.out.println("Server did not respond. Client creation failed.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String receiveMessage(Socket socket) {
        try {
            // Reading from the server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Wait for the server response
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendMessage(Socket socket, String message) {
        try {
            // Writing to the server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Sending the user input to the server
            out.println(message);
            out.flush();

            // Schedule a timer task to check for response within max_response_time seconds
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    // Display "Server not responding" if no response within max_response_time seconds
                    System.out.println("Server not responding");
                    System.out.print("Enter a command: ");
                    System.out.println();
                }
            };
            timer.schedule(timerTask, (int) (maxResponseTime * 1000));

            // Wait for the server response
            String response = receiveMessage(socket);

            // Cancel the timer task as a response has been received
            timerTask.cancel();

            // Display the server reply and the number of connected clients
            System.out.println("Server replied: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeClient(Socket socket, String clientNumber) {
        try {
            // Close the client socket
            socket.close();

            // Remove the client from the map
            clients.remove(clientNumber);

            System.out.println("Client " + clientNumber + " closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendExitToAllClients() {
        // Create a copy of client numbers to avoid ConcurrentModificationException
        List<String> clientNumbers = new ArrayList<>(clients.keySet());

        for (String clientNumber : clientNumbers) {
            sendMessage(clients.get(clientNumber), "exit");
            closeClient(clients.get(clientNumber), clientNumber);
        }
    }

    private void startMessageReceiver(Socket socket, String clientNumber) {
        // Create a new message receiver thread for the client
        messageReceiverExecutor.execute(new MessageReceiver(socket, clientNumber));
    }

    private void closeAllClients() {
        sendExitToAllClients();
        clients.clear();
    }

    public static void main(String[] args) {
        ConfigHandler configHandler = new ConfigHandler(configPath, Client.class);
        Client client = new Client(configHandler);
        client.startClient();
    }
}

