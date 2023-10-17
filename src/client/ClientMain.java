package client;

import client.core.Client;
import config.ConfigHandler;

import static help.ConsoleListener.startConsoleListener;
import static help.ConsoleListener.stopConsoleListener;

/**
 * The `ClientMain` class serves as the entry point for running a client for the TestGameServer project.
 * It configures and starts the client application.
 *
 * @uses Client
 * @author Tomasz Zbroszczyk
 * @since 14.10.2023
 * @version 1.0
 */
public class ClientMain extends Client {

    /**
     * Main method for running the client application.
     *
     * @param args Command-line arguments (not used in this implementation).
     */
    public static void main(String[] args) {
        ConfigHandler configHandler = new ConfigHandler(configPath, Client.class);

        checkArgs(args, configHandler);

        startConsoleListener(configHandler.getString("log_file_dir"), configHandler.getString("log_file"), configHandler.getInt("max_log_files"));

        Client client = new Client(configHandler);
        client.startClient();

        stopConsoleListener();
    }
}
