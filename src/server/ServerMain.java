package server;

import config.ConfigHandler;
import server.core.Server;
import server.database.questionDatabase.QuestionDatabaseHandler;
import server.database.userDatabase.UserDatabaseHandler;

import static help.ConsoleListener.startConsoleListener;
import static help.ConsoleListener.stopConsoleListener;

public class ServerMain extends Server {

    public static void main(String[] args) {

        configHandler = new ConfigHandler(configPath, Server.class);

        //Check for args
        checkArgs(args, configHandler);

        startConsoleListener(configHandler.getString("log_file_dir"), configHandler.getString("log_file"), configHandler.getInt("max_log_files"));

        //Initialize user database
        userDatabaseHandler = new UserDatabaseHandler(configHandler.getString("user_database_dir"));
        questionDatabaseHandler = new QuestionDatabaseHandler(configHandler.getString("question_database_dir"));

        //Initialize server
        Server server = new Server(configHandler);
        server.startServer();

        stopConsoleListener();
    }

}
