package server;

import server.database.userDatabase.UserDatabaseHandler;

public class MessageProcessor {

    private UserDatabaseHandler userDatabaseHandler;

    public MessageProcessor(UserDatabaseHandler userDatabaseHandler) {
        this.userDatabaseHandler = userDatabaseHandler;
    }

    private String[] preProcessMessage(String message) {
        String[] messageParts = message.split(":");
        return messageParts;
    }

    public String processMessage(String message) {
        String[] preProcessedMessage = preProcessMessage(message);

        if (preProcessedMessage[0].equals("newClient")){
            System.out.println("New client connected " + preProcessedMessage[1]);
            return "New client connected " + preProcessedMessage[1];
        } else if (preProcessedMessage[0].equals("newUser")) {
            if(userDatabaseHandler.addUser(preProcessedMessage[1], preProcessedMessage[2])) {
                return "User added successfully";
            } else {
                return "bad_request";
            }
        } else if (preProcessedMessage[0].equals("login")) {
            if (userDatabaseHandler.getUserByUsername(preProcessedMessage[1]) != null) {
                if (userDatabaseHandler.getUserByUsername(preProcessedMessage[1]).getPassword().equals(preProcessedMessage[2])) {
                    return "User logged in";
                } else {
                    return "bad_request";
                }

            } else {
                return "bad_request";
            }
        } else if (preProcessedMessage[0].equals("getUser")) {
            if (preProcessedMessage[1].equals("username")) {
                if (userDatabaseHandler.getUserByUsername(preProcessedMessage[2]) != null) {
                    return userDatabaseHandler.getUserByUsername(preProcessedMessage[2]).toString();
                } else {
                    return "bad_request";
                }
            } else if (preProcessedMessage[1].equals("id")) {
                if (userDatabaseHandler.getUserById(Integer.parseInt(preProcessedMessage[2])) != null) {
                    return userDatabaseHandler.getUserById(Integer.parseInt(preProcessedMessage[2])).toString();
                } else {
                    return "bad_request";
                }
            } else {
                return "bad_request";
            }
        }
        return "bad_message";
    }

}
