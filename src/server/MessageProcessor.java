package server;

public class MessageProcessor {

    public MessageProcessor() {

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
        }
        return "Message processed";
    }

}
