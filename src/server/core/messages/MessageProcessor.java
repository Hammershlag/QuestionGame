package server.core.messages;

import server.database.questionDatabase.QuestionDatabaseHandler;
import server.database.relationDatabase.Relation;
import server.database.relationDatabase.RelationDatabaseHandler;
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
     * The handler for relation database operations.
     */
    private static RelationDatabaseHandler relationDatabaseHandler;

    /**
     * Constructs a `MessageProcessor` with the specified user database handler and question database handler.
     *
     * @param userDatabaseHandler       The handler for user database operations.
     * @param questionDatabaseHandler   The handler for question database operations.
     * @param relationDatabaseHandler   The handler for relation database operations.
     */
    public MessageProcessor(UserDatabaseHandler userDatabaseHandler, QuestionDatabaseHandler questionDatabaseHandler, RelationDatabaseHandler relationDatabaseHandler) {
        this.userDatabaseHandler = userDatabaseHandler;
        this.questionDatabaseHandler = questionDatabaseHandler;
        this.relationDatabaseHandler = relationDatabaseHandler;
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
                if (userDatabaseHandler.add(preProcessedMessage[1], preProcessedMessage[2])) {
                    return "User added successfully";
                } else {
                    return "bad_request";
                }
            }
            case "login": {
                if (userDatabaseHandler.getByName(preProcessedMessage[1]) != null) {
                    if (userDatabaseHandler.getByName(preProcessedMessage[1]).getPassword().equals(preProcessedMessage[2])) {
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
                    if (userDatabaseHandler.getByName(preProcessedMessage[2]) != null) {
                        return userDatabaseHandler.getByName(preProcessedMessage[2]).toString();
                    } else {
                        return "bad_request";
                    }
                } else if (preProcessedMessage[1].equals("id")) {
                    if (userDatabaseHandler.getById(Integer.parseInt(preProcessedMessage[2])) != null) {
                        return userDatabaseHandler.getById(Integer.parseInt(preProcessedMessage[2])).toString();
                    } else {
                        return "bad_request";
                    }
                } else {
                    return "bad_request";
                }
            }
            case "getQuestion": {
                if (preProcessedMessage[1].equals("id")) {
                    if (questionDatabaseHandler.getById(Integer.parseInt(preProcessedMessage[2])) != null) {
                        return questionDatabaseHandler.getById(Integer.parseInt(preProcessedMessage[2])).toString();
                    } else {
                        return "bad_request";
                    }
                } else if (preProcessedMessage[1].equals("random")) {
                    return questionDatabaseHandler.getAll().get((int) (Math.random() * questionDatabaseHandler.getAll().size())).toString();
                } else {
                    return "bad_request";
                }
            }
            case "addQuestion": {
                questionDatabaseHandler.add(preProcessedMessage[1], preProcessedMessage[2], preProcessedMessage[3], preProcessedMessage[4].split(";"));
                return "Question added successfully";
            }
            case "answerQuestion": {
                if (preProcessedMessage[1].equals("id")) {
                    if (questionDatabaseHandler.getById(Integer.parseInt(preProcessedMessage[2])) != null) {
                        return questionDatabaseHandler.getById(Integer.parseInt(preProcessedMessage[2])).compareAnswer(preProcessedMessage[3]) ? "Correct answer" : "Wrong answer";
                    }
                }
                return "bad_request";
            }
            case "addRelation": {
                if (relationDatabaseHandler.add(Integer.parseInt(preProcessedMessage[1]), Integer.parseInt(preProcessedMessage[2])))
                    return "Relation added successfully";
                else
                    return "bad_request";
            }
            case "getRelation": {
                if (preProcessedMessage[1].equals("id")) {
                    return relationDatabaseHandler.getById(Integer.parseInt(preProcessedMessage[2])).toString();
                } else if (preProcessedMessage[1].equals("users")) {
                    return relationDatabaseHandler.getByName(preProcessedMessage[2], preProcessedMessage[3]).toString() == null ? "bad_request" : relationDatabaseHandler.getByName(preProcessedMessage[2], preProcessedMessage[3]).toString();
                } else {
                    return "bad_request";
                }

            }
            //TODO updateRelation
            case "updateRelation": {
                if (preProcessedMessage[1].equals("id")) {
                    return "Not yet implemented";
                } else if (preProcessedMessage[1].equals("users")) {
                    return "Not yet implemented";
                } else {
                    return "bad_request";
                }

            }
            case "getRelations": {
                if(preProcessedMessage[1].equals("userId"))
                    return relationDatabaseHandler.getAll().stream().filter(relation -> relation.getUser1Id() == Integer.parseInt(preProcessedMessage[2]) || relation.getUser2Id() == Integer.parseInt(preProcessedMessage[2])).map(Object::toString).reduce("", (s, s2) -> s + s2 + "\n");
                else if(preProcessedMessage[1].equals("all")) {
                    return relationDatabaseHandler.getAll().stream().map(Object::toString).reduce("", (s, s2) -> s + s2 + "\n");
                } else
                    return "bad_request";
            }
            case "getRelationQuestions": {
                if(preProcessedMessage[1].equals("id")) {
                    Relation relation = relationDatabaseHandler.getById(Integer.parseInt(preProcessedMessage[2]));
                    if(preProcessedMessage[5].equals("id") && relation != null && preProcessedMessage[3].equals("userId")) {
                        int question;
                        int count = 0;
                        do {
                        question = questionDatabaseHandler.getAll().get((int) (Math.random() * questionDatabaseHandler.getAll().size())).getId();
                        } while ((relation.getQuestionsAnsweredByUser1().contains(question) && relation.getUser1Id() == Integer.parseInt(preProcessedMessage[4]))
                                || (relation.getQuestionsAnsweredByUser2().contains(question) && relation.getUser2Id() == Integer.parseInt(preProcessedMessage[4]))
                                || (relation.getQuestionsUnansweredByUser1().containsKey(question) && relation.getUser1Id() == Integer.parseInt(preProcessedMessage[4]))
                                || (relation.getQuestionsUnansweredByUser2().containsKey(question) && relation.getUser2Id() == Integer.parseInt(preProcessedMessage[4]))
                                || count++ > relationDatabaseHandler.getRelationsSize());
                        return questionDatabaseHandler.getById(question).toString();
                    } else
                        return "bad_request";
                } else
                    return "bad_request";
            }
            case "relationAddQuestionAnswer": {
                if(preProcessedMessage[1].equals("id")) {
                    Relation relation = relationDatabaseHandler.getById(Integer.parseInt(preProcessedMessage[2]));
                    if(relation != null && preProcessedMessage[3].equals("userId") && preProcessedMessage[5].equals("questionId")) {
                        if(relation.getUser1Id() == Integer.parseInt(preProcessedMessage[4])) {
                            relation.addQuestionAnsweredByUser1(Integer.parseInt(preProcessedMessage[6]), preProcessedMessage[7]);
                            relationDatabaseHandler.update();
                            return "Question answered successfully";
                        } else if(relation.getUser2Id() == Integer.parseInt(preProcessedMessage[4])) {
                            relation.addQuestionAnsweredByUser2(Integer.parseInt(preProcessedMessage[6]), preProcessedMessage[7]);
                            relationDatabaseHandler.update();
                            return "Question answered successfully";
                        } else {
                            return "bad_request";
                        }
                    } else
                        return "bad_request";
                } else
                    return "bad_request";
            }
            case "answerRelationQuestion": {
                //TODO implement answerRelationQuestion
            }
            default:
                return "bad_message";
        }
    }
}
