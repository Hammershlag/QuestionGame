package server;

import config.ConfigHandler;
import server.core.Server;
import server.database.questionDatabase.QuestionDatabaseHandler;
import server.database.userDatabase.UserDatabaseHandler;

import static help.ConsoleListener.startConsoleListener;
import static help.ConsoleListener.stopConsoleListener;

/**
 * The `ServerMain` class is the entry point for the server application. It initializes the server, user database,
 * and question database and starts the server to listen for client connections.
 *
 * @author Tomasz Zbroszczyk
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

        // Initialize user database
        userDatabaseHandler = new UserDatabaseHandler(configHandler.getString("user_database_dir"));
        questionDatabaseHandler = new QuestionDatabaseHandler(configHandler.getString("question_database_dir"));

        // Initialize and start the server
        Server server = new Server(configHandler);
        server.startServer();

        stopConsoleListener();
    }
}

