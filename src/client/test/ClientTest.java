package client.test;

import client.core.Client;
import config.ConfigHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import static help.ConsoleListener.stopConsoleListener;

/**
 * @author Tomasz Zbroszczyk on 19.10.2023
 * @version 1.0
 */
public class ClientTest extends Client {

    protected static String test_dir;

    public ClientTest(ConfigHandler configHandler) {
        super(configHandler);
        test_dir = configHandler.getString("test_dir");
    }

    @Override
    public void startClient() {
        System.out.println("Client started on: " + ip + ":" + port);
        System.out.println("All the commands are being run from the tests.t file");

        // Implement client logic here
        // Initialize the client counter
        try {
            // Object of scanner class
            BufferedReader br = new BufferedReader(new FileReader(test_dir));
            String line;
            String regex = ""; //TODO regex for test beginning, test here

            while (true) {
                // Read user input
                line = br.readLine();
                System.out.println(line);
                if(Pattern.compile(regex).matcher(line).find() || line.equals("end") || line.equals('\n'))
                    continue;
                if ("exit".equalsIgnoreCase(line)) {
                    // Exit the program after closing all clients
                    closeAllClients();
                    messageReceiverExecutor.shutdownNow();
                    stopConsoleListener();
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
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
