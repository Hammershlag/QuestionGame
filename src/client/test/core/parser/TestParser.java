package client.test.core.parser;

import client.test.core.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Tomasz Zbroszczyk on 20.10.2023
 * @version 1.0
 */
public class TestParser extends Test {

    protected String test_dir;

    private final  String regex_beg = "(\\d+): ([^:]+): ([^:]+)";
    private final  String regex_end = "end";
    private final String regex_for = "\\s*for ([1-9]\\d*) times:";

    private final String regex_for_end = "\\s*end for";

    protected List<Test> tests = new ArrayList<>();

    public TestParser(String test_dir) {
        this.test_dir = test_dir;
    }

    public void parse() {

        try {
            BufferedReader br = new BufferedReader(new FileReader(test_dir));
            String line;
            Test test = null;
            while((line = br.readLine()) != null) {
                if (Pattern.compile(regex_beg).matcher(line).matches()){
                    String[] split = line.split(": ");
                    test = new Test(Integer.parseInt(split[0]), split[1], split[2]);
                    tests.add(test);
                    System.out.println("BEGGINING - OK");
                } else if(Pattern.compile(regex_for_end).matcher(line).matches()) {
                    System.out.println("END FOR - OK");
                } else if(Pattern.compile(regex_for).matcher(line).matches()) {
                    List<String> commandsBuf = new ArrayList<>();
                    List<String> answersBuf = new ArrayList<>();
                    int n = Integer.parseInt(Arrays.stream(line.split(" ")).filter(s -> s.matches("\\d+")).findFirst().get());
                    line = br.readLine();
                    while(!Pattern.compile(regex_for_end).matcher(line).matches()) {
                        commandsBuf.add(line.trim());
                        line = br.readLine();
                        answersBuf.add(line.trim());
                        line = br.readLine();
                    }
                    for(int i = 0; i < n; i++) {
                        commandsBuf.forEach(tests.get(tests.size()-1)::addCommand);
                        answersBuf.forEach(tests.get(tests.size()-1)::addAnswer);
                    }
                    System.out.println("FOR - OK");
                } else if(Pattern.compile(regex_end).matcher(line).matches()) {
                    tests.get(tests.size()-1).addCommand("all:exit");
                    tests.get(tests.size()-1).addAnswer("NO ANSWER");
                    tests.get(tests.size()-1).addCommand("exit");
                    tests.get(tests.size()-1).addAnswer("NO ANSWER");
                    System.out.println("END - OK");
                    System.out.println();
                } else if(!line.equals('\n') && line.length() > 3) {
                    boolean isExit = line.trim().equals("exit");

                    line = parseCommand(line);

                    tests.get(tests.size()-1).addCommand(line.trim());
                    line = br.readLine();
                    tests.get(tests.size()-1).addAnswer(line.trim());
                    System.out.println("COMMAND - OK");
                    if (isExit)
                        System.out.println();
                }
            }
            System.out.println("------------");
            System.out.println("PARSING - OK");
            System.out.println("------------");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String parseCommand(String input) {
        Pattern pattern = Pattern.compile("\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(input);

        if (!matcher.find()) {
            return input; // No function calls found, return the input as is
        }

        matcher.reset(); // Reset the matcher to the beginning of the input string

        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String functionCall = matcher.group(1);
            String functionOutput = evaluateFunction(functionCall);
            matcher.appendReplacement(result, functionOutput);
        }

        matcher.appendTail(result);

        return result.toString();
    }

    public static String evaluateFunction(String functionCall) {
        String[] parts = functionCall.split("\\(");
        String functionName = parts[0];
        String[] arguments = parts[1].split(",");

        if (functionName.equals("randomNumber")) {
            if (arguments.length == 2) {
                int beg = Integer.parseInt(arguments[0]);
                int end = Integer.parseInt(arguments[1].split("\\)")[0]);
                return String.valueOf(randomNumber(beg, end));
            } else if (arguments.length == 1) {
                int len = Integer.parseInt(arguments[0].split("\\)")[0]);
                return String.valueOf(randomNumber(len));
            }
        } else if (functionName.equals("randomString") && arguments.length == 1) {
            int len = Integer.parseInt(arguments[0].split("\\)")[0]);
            return randomString(len);
        }

        return ""; // Handle other functions or invalid function calls here
    }

    public static int randomNumber(int beg, int end) {
        Random random = new Random();
        return random.nextInt(end - beg + 1) + beg;
    }

    public static int randomNumber(int len) {
        if (len <= 0) {
            return 0;
        }
        int min = (int) Math.pow(10, len - 1);
        int max = (int) Math.pow(10, len) - 1;
        return randomNumber(min, max);
    }

    public static String randomString(int len) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder result = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < len; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }

        return result.toString();
    }

}
