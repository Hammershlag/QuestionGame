package help;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * The `HelpPrint` class provides methods for printing the content of a text file to the console.
 *
 * @author Tomasz Zbroszczyk
 * @since 10.10.2023
 * @version 1.0
 */
public class HelpPrint {

    /**
     * Prints the content of a text file to the console.
     *
     * @param filename The name of the file to be printed.
     */
    public static void printHelp(String filename) {
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
