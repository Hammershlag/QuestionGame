package server.core.messages;

import server.database.questionDatabase.QuestionDatabaseHandler;
import server.database.userDatabase.UserDatabaseHandler;

/**
 * The `MessageProcessor` class is responsible for processing incoming messages and executing various server tasks based on message content.
 *
 * @author Tomasz Zbroszczyk
 * @since 09.10.2023
 * @version 1.0
 */
public class MessageProcessor {
    /**
     * The handler for user database operations.
     */
    private final UserDatabaseHandler userDatabaseHandler;
    /**
     * The handler for question database operations.
     */
    private static QuestionDatabaseHandler questionDatabaseHandler;

    /**
     * Constructs a `MessageProcessor` with the specified user database handler and question database handler.
     *
     * @param userDatabaseHandler    The handler for user database operations.
     * @param questionDatabaseHandler The handler for question database operations.
     */
    public MessageProcessor(UserDatabaseHandler userDatabaseHandler, QuestionDatabaseHandler questionDatabaseHandler) {
        this.userDatabaseHandler = userDatabaseHandler;
        MessageProcessor.questionDatabaseHandler = questionDatabaseHandler;
    }

    /**
     * Pre-processes an incoming message by splitting it into parts.
     *
     * @param message The incoming message to be pre-processed.
     * @return An array of message parts after pre-processing.
     */
    private String[] preProcessMessage(String message) {
        String[] messageParts = message.split(":");
        return messageParts;
    }

    /**
     * Processes an incoming message and performs appropriate actions based on the message content.
     *
     * @param message The incoming message to be processed.
     * @return A response message based on the processing result.
     */
    public String processMessage(String message) {
        String[] preProcessedMessage = preProcessMessage(message);
        switch (preProcessedMessage[0]) {
            case "newClient": {
                System.out.println("New client connected " + preProcessedMessage[1]);
                return "New client connected " + preProcessedMessage[1];
            }
            case "newUser": {
                if (userDatabaseHandler.addUser(preProcessedMessage[1], preProcessedMessage[2])) {
                    return "User added successfully";
                } else {
                    return "bad_request";
                }
            }
            case "login": {
                if (userDatabaseHandler.getUserByUsername(preProcessedMessage[1]) != null) {
                    if (userDatabaseHandler.getUserByUsername(preProcessedMessage[1]).getPassword().equals(preProcessedMessage[2])) {
                        return "User logged in";
                    } else {
                        return "bad_request";
                    }
                } else {
                    return "bad_request";
                }
            }
            case "getUser": {
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
            case "getQuestion": {
                if (preProcessedMessage[1].equals("id")) {
                    if (questionDatabaseHandler.getQuestionById(Integer.parseInt(preProcessedMessage[2])) != null) {
                        return questionDatabaseHandler.getQuestionById(Integer.parseInt(preProcessedMessage[2])).toString();
                    } else {
                        return "bad_request";
                    }
                } else if (preProcessedMessage[1].equals("random")) {
                    return questionDatabaseHandler.getQuestions().get((int) (Math.random() * questionDatabaseHandler.getQuestions().size())).toString();
                } else {
                    return "bad_request";
                }
            }
            case "addQuestion": {
                questionDatabaseHandler.addQuestion(preProcessedMessage[1], preProcessedMessage[2], preProcessedMessage[3], preProcessedMessage[4].split(";"));
                return "Question added successfully";
            }
            case "answerQuestion": {
                if (preProcessedMessage[1].equals("id")) {
                    if (questionDatabaseHandler.getQuestionById(Integer.parseInt(preProcessedMessage[2])) != null) {
                        return questionDatabaseHandler.getQuestionById(Integer.parseInt(preProcessedMessage[2])).compareAnswer(preProcessedMessage[3]) ? "Correct answer" : "Wrong answer";
                    }
                }
                return "bad_request";
            }
            default:
                return "bad_message";
        }
    }
}
