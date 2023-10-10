package help;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsoleListener {
    private static String filename;
    private static String directory;
    private static int maxFiles;
    private static FileOutputStream outStream;
    private static PrintStream consoleOut;


    public static void startConsoleListener(String dir, String prefix, int maxFiles) {
        directory = dir;
        filename = prefix;
        maxFiles = maxFiles;

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

class TeeOutputStream extends OutputStream {
    private final OutputStream out1;
    private final OutputStream out2;

    public TeeOutputStream(OutputStream out1, OutputStream out2) {
        this.out1 = out1;
        this.out2 = out2;
    }

    @Override
    public void write(int b) throws IOException {
        out1.write(b);
        out2.write(b);
    }
}
