package client;

import config.ConfigHandler;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Client {
    private static String configPath = "C:\\Projects\\TestGame\\TestGameServer\\src\\config\\config.txt";

    private int port;
    private String ip;
    private double maxResponseTime;
    private Map<String, Socket> clients; // Map to store client numbers and their sockets
    private Timer timer;

    public Client(ConfigHandler configHandler) {
        this.port = configHandler.getInt("port");
        this.ip = configHandler.getString("ip");
        this.maxResponseTime = configHandler.getDouble("max_response_time");
        this.clients = new HashMap<>();
        this.timer = new Timer();
    }

    public void startClient() {
        System.out.println("Client started on: " + ip + ":" + port);

        // Implement client logic here
        try {
            // Object of scanner class
            Scanner sc = new Scanner(System.in);
            String line;

            while (true) {
                // Read user input
                System.out.print("Enter a command ('clientNumber:message', 'new:message', or 'clientNumber:exit'): ");
                System.out.println();
                line = sc.nextLine();

                if ("exit".equalsIgnoreCase(line)) {
                    break;
                }

                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    String command = parts[0].trim();
                    String message = parts[1].trim();

                    if ("new".equalsIgnoreCase(command)) {
                        // Create a new client and send the message
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

            // Closing the scanner object
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createAndSendClient(String message) {
        try {
            // Create a new client socket
            Socket socket = new Socket(ip, port);

            // Add the new client to the map
            String newClientNumber = "" + (clients.size() + 1);
            clients.put(newClientNumber, socket);

            // Send the message from the new client
            sendMessage(socket, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(Socket socket, String message) {
        try {
            // Writing to the server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Reading from the server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Sending the user input to the server
            out.println(message);
            out.flush();

            // Schedule a timer task to check for response within max_response_time seconds
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    // Display "Server not responding" if no response within max_response_time seconds
                    System.out.println("Server not responding");
                    System.out.print("Enter a command ('clientNumber:message', 'new:message', or 'clientNumber:exit'): ");
                    System.out.println();
                }
            };
            timer.schedule(timerTask, (int) (maxResponseTime * 1000));

            // Wait for the server response
            String response = in.readLine();

            // Cancel the timer task as a response has been received
            timerTask.cancel();

            // Display the server reply and the number of connected clients
            System.out.println("Server replied: " + response);
            System.out.println("Number of connected clients: " + clients.size());
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

    public static void main(String[] args) {
        ConfigHandler configHandler = new ConfigHandler(configPath, Client.class);
        Client client = new Client(configHandler);
        client.startClient();
    }
}
