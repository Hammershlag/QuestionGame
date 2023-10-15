package help;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The `ConsoleListener` class provides functionality to redirect console output to log files and manage log files.
 *
 * @author Tomasz Zbroszczyk
 * @since 11.10.2023
 * @version 1.0
 */
public class ConsoleListener {
    /**
     * The prefix for log file names.
     */
    private static String filename;
    /**
     * The directory where log files are stored.
     */
    private static String directory;
    /**
     * The maximum number of log files to keep.
     */
    private static int maxFiles;
    /**
     * The output stream for the log file.
     */
    private static FileOutputStream outStream;
    /**
     * The console output stream.
     */
    private static PrintStream consoleOut;


    /**
     * Starts listening to the console output and redirects it to log files.
     *
     * @param dir      The directory where log files are stored.
     * @param prefix   The prefix for log file names.
     * @param maxFiles_ The maximum number of log files to keep.
     */
    public static void startConsoleListener(String dir, String prefix, int maxFiles_) {
        directory = dir;
        filename = prefix;
        maxFiles = maxFiles_;

        // Create a SimpleDateFormat to generate the date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = dateFormat.format(new Date());

        File logDirectory = new File(directory);

        // Ensure the log directory exists
        if (!logDirectory.exists()) {
            logDirectory.mkdirs();
        }

        // List log files in the directory
        File[] logFiles = logDirectory.listFiles((dir1, name) -> name.startsWith(prefix));

        // If there are too many log files, delete the oldest one
        if (logFiles.length >= maxFiles) {
            File oldestLog = getOldestLog(logFiles);
            if (oldestLog != null) {
                oldestLog.delete();
            }
        }

        try {
            // Create the log file with the timestamp
            String logFileName = prefix + "_" + timestamp + ".log";
            File logFile = new File(logDirectory, logFileName);
            outStream = new FileOutputStream(logFile);
            consoleOut = new PrintStream(new TeeOutputStream(System.out, outStream));
            System.setOut(consoleOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops listening to the console output and restores the original System.out.
     */
    public static void stopConsoleListener() {
        System.setOut(System.out); // Restore the original System.out
        System.setErr(System.err);
        try {
            if (outStream != null) {
                outStream.flush(); // Flush the output stream
                outStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the oldest log file from an array of log files.
     *
     * @param logFiles An array of log files.
     * @return The oldest log file, or null if no files are found.
     */
    private static File getOldestLog(File[] logFiles) {
        File oldestLog = null;
        long oldestTimestamp = Long.MAX_VALUE;

        for (File logFile : logFiles) {
            String fileName = logFile.getName();
            String timestampString = fileName.substring(filename.length() + 1, fileName.length() - 4); // Extract the timestamp part
            long timestamp = Long.parseLong(timestampString);

            if (timestamp < oldestTimestamp) {
                oldestTimestamp = timestamp;
                oldestLog = logFile;
            }
        }

        return oldestLog;
    }
}

/**
 * The `TeeOutputStream` class extends `OutputStream` to duplicate output to two different output streams.
 */
class TeeOutputStream extends OutputStream {
    /**
     * The first output stream.
     */
    private final OutputStream out1;
    /**
     * The second output stream.
     */
    private final OutputStream out2;

    /**
     * Constructs a new `TeeOutputStream` with two output streams.
     *
     * @param out1 The first output stream.
     * @param out2 The second output stream.
     */
    public TeeOutputStream(OutputStream out1, OutputStream out2) {
        this.out1 = out1;
        this.out2 = out2;
    }

    /**
     * Writes a byte to both output streams.
     *
     * @param b The byte to write.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void write(int b) throws IOException {
        out1.write(b);
        out2.write(b);
    }
}
