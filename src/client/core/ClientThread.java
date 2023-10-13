package client.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private String ip;
    private int port;
    private String message;

    public ClientThread(String ip, int port, String message) {
        this.ip = ip;
        this.port = port;
        this.message = message;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(ip, port)) {
            // Writing to server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Reading from server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send the user input to the server
            out.println(message);
            out.flush();

            // Display server reply
            System.out.println("Server replied: " + in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

