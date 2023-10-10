package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HelpPrint {

    public static void printHelp() {
        String filename = "C:\\Projects\\TestGame\\TestGameServer\\src\\server\\README.md";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
