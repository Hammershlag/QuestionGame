package server;

public class MessageHandler {

    private final String message;
    private final String sender;
    private final String receiver;
    private final String timestamp;

    public MessageHandler() {
        message = "";
        sender = "";
        receiver = "";
        timestamp = "";
    }

    public MessageHandler(String message, String sender, String receiver, String timestamp) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
    }
    public String toString() {
        return "Message: " + message + "; Sender: " + sender + "; Receiver: " + receiver + "; Timestamp: " + timestamp + "\n";
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
