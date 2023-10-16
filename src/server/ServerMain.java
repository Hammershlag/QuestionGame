package server;

import config.ConfigHandler;
import server.core.Server;
import server.database.questionDatabase.QuestionDatabaseHandler;
import server.database.relationDatabase.RelationDatabaseHandler;
import server.database.userDatabase.UserDatabaseHandler;

import static help.ConsoleListener.startConsoleListener;
import static help.ConsoleListener.stopConsoleListener;

/**
 * The `ServerMain` class is the entry point for the server application. It initializes the server, user database,
 * and question database and starts the server to listen for client connections.
 *
 * @author Tomasz Zbroszczyk
 * @since 14.10.2023
 * @version 1.0
 */
public class ServerMain extends Server {

    /**
     * The main entry point of the server application.
     *
     * @param args Command-line arguments to configure the server (e.g., --port, --max-clients, --ping-interval).
     */
    public static void main(String[] args) {
        configHandler = new ConfigHandler(configPath, Server.class);

        // Check for command-line arguments
        checkArgs(args, configHandler);

        startConsoleListener(configHandler.getString("log_file_dir"), configHandler.getString("log_file"), configHandler.getInt("max_log_files"));

        // Initialize databases
        userDatabaseHandler = new UserDatabaseHandler(configHandler.getString("user_database_dir"));
        questionDatabaseHandler = new QuestionDatabaseHandler(configHandler.getString("question_database_dir"));
        relationDatabaseHandler = new RelationDatabaseHandler(configHandler.getString("relation_database_dir")); //TODO: add relation database inside Server class

        // Initialize and start the server
        Server server = new Server(configHandler);
        server.startServer();

        stopConsoleListener();
    }
}

