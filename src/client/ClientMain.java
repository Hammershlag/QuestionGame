package client;

import client.core.Client;
import config.ConfigHandler;

import static help.ConsoleListener.startConsoleListener;
import static help.ConsoleListener.stopConsoleListener;

public class ClientMain extends Client{

    //Used only to run client
    public static void main(String[] args) {
        ConfigHandler configHandler = new ConfigHandler(configPath, Client.class);

        checkArgs(args, configHandler);

        startConsoleListener(configHandler.getString("log_file_dir"), configHandler.getString("log_file"), configHandler.getInt("max_log_files"));

        Client client = new Client(configHandler);
        client.startClient();

        stopConsoleListener();
    }

}
