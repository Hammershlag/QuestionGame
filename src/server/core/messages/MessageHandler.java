package server.core.messages;

/**
 * The `MessageHandler` class is responsible for handling and representing messages in the server.
 * Each message contains a message content, sender, receiver, and a timestamp.
 *
 * @author Tomasz Zbroszczyk
 * @since 07.10.2023
 * @version 1.0
 */
public class MessageHandler {
    /**
     * The message content.
     */
    private final String message;
    /**
     * The sender of the message.
     */
    private final String sender;
    /**
     * The receiver of the message.
     */
    private final String receiver;
    /**
     * The timestamp when the message was sent.
     */
    private final String timestamp;

    /**
     * Constructs an empty `MessageHandler` with all fields set to empty strings.
     */
    public MessageHandler() {
        message = "";
        sender = "";
        receiver = "";
        timestamp = "";
    }

    /**
     * Constructs a `MessageHandler` with the specified message content, sender, receiver, and timestamp.
     *
     * @param message   The content of the message.
     * @param sender    The sender of the message.
     * @param receiver  The receiver of the message.
     * @param timestamp The timestamp when the message was sent.
     */
    public MessageHandler(String message, String sender, String receiver, String timestamp) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
    }

    /**
     * Returns a string representation of the message.
     *
     * @return A string in the format: "Message: [message]; Sender: [sender]; Receiver: [receiver]; Timestamp: [timestamp]\n"
     */
    public String toString() {
        return "Message: " + message + "; Sender: " + sender + "; Receiver: " + receiver + "; Timestamp: " + timestamp + "\n";
    }

    /**
     * Get the content of the message.
     *
     * @return The message content.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Get the sender of the message.
     *
     * @return The sender's name.
     */
    public String getSender() {
        return sender;
    }

    /**
     * Get the receiver of the message.
     *
     * @return The receiver's name.
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * Get the timestamp when the message was sent.
     *
     * @return The timestamp as a string.
     */
    public String getTimestamp() {
        return timestamp;
    }
}
