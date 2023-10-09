package server;

public class MessageProcessor {

    public MessageProcessor() {

    }

    private String[] preProcessMessage(String message) {
        String[] messageParts = message.split(":");
        return messageParts;
    }

    private void processMessage(String[] preProcessedMessage) {

        if (preProcessedMessage[0].equals("newClient")){
            System.out.println("New client connected " + preProcessedMessage[1]);
        }
    }

}
