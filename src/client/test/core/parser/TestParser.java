package client.test.core.parser;

import client.test.core.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
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
    private static Map<String, String> varString = new HashMap<>();
    private static Map<String, Integer> varInt = new HashMap<>();
    private static Map<String, Double> varDouble = new HashMap<>();
    private static Map<String, Boolean> varBool = new HashMap<>();

    private static Map<String, String> names = new HashMap<>();

    public TestParser(String test_dir) {
        this.test_dir = test_dir;
    }

    //TODO add ability to use variables inside commands
    public void parse() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(test_dir));
            String line;
            Test test = null;
            while((line = br.readLine()) != null) {
                if (Pattern.compile(regex_beg).matcher(line).matches()){
                    String[] split = line.split(": ");
                    if (varDouble.size() > 0){
                        System.out.println();
                        System.out.println("Double variables: ");
                        System.out.println();
                        varDouble.entrySet().forEach(e -> System.out.println(e.getKey() + " -> " + e.getValue()));
                        System.out.println();
                    }
                    if (varInt.size() > 0) {
                        System.out.println();
                        System.out.println("Integer variables: ");
                        System.out.println();
                        varInt.entrySet().forEach(e -> System.out.println(e.getKey() + " -> " + e.getValue()));
                        System.out.println();
                    }
                    if (varString.size() > 0) {
                        System.out.println();
                        System.out.println("String variables: ");
                        System.out.println();
                        varString.entrySet().forEach(e -> System.out.println(e.getKey() + " -> " + e.getValue()));
                        System.out.println();
                    }
                    if (varBool.size() > 0) {
                        System.out.println();
                        System.out.println("Boolean variables: ");
                        System.out.println();
                        varBool.entrySet().forEach(e -> System.out.println(e.getKey() + " -> " + e.getValue()));
                        System.out.println();
                    }
                    varInt = new HashMap<>();
                    varDouble = new HashMap<>();
                    varString = new HashMap<>();
                    varBool = new HashMap<>();
                    names = new HashMap<>();
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

                    line = parseCommand(line.trim()).split("}")[0].trim();
                    if (line.contains("."))
                        line = line.split("\\.")[0].trim();
                    if (line.equals("casting_error")) {
                        System.out.println("CASTING ERROR");
                        tests.get(tests.size()-1).isCorrect = false;
                        continue;
                    } else if(line.equals("var initialized"))
                        continue;

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
            if (functionOutput.equals("err")) {
                return "casting_error";
            }
            matcher.appendReplacement(result, functionOutput);
        }

        matcher.appendTail(result);

        return result.toString();
    }

    public static String evaluateFunction(String functionCall) {
        String[] parts = functionCall.split("\\(");
        String functionName = parts[0];
        String[] arguments = {};
        if (parts.length > 1)
            arguments = parts[1].split(",");
        String intPattern = "int [a-z]+ = (\\d+|\\{[^}]+)";
        String doublePattern = "double [a-z]+ = (\\d+\\.\\d+|\\{[^}]+)";
        String stringPattern = "string [a-z]+ = (\\w+|\\{[^}]+)";
        String boolPattern = "bool [a-z]+ = (true|false|\\{[^}]+)";
        if(functionCall.matches(intPattern)) {
            String[] split = functionCall.split(" ");
            String varName = split[1];
            if (names.get(varName) != null) {
                return "err";

            } else {

            } //TODO test if vars of this name already exists - check also other types

            int varValue;
            try {
                varValue = Integer.parseInt(parseCommand(split[3]+"}").split("}")[0]);
            } catch (NumberFormatException e) {
                return "casting_error";
            }
            varInt.put(varName, varValue);
            names.put(varName, "int");
            return "var initialized";
        } else if(functionCall.matches(stringPattern)) {
            String[] split = functionCall.split(" ");
            String varName = split[1];
            String varValue;
            try {
                varValue = String.valueOf(parseCommand(split[3]+"}").split("}")[0]);
            } catch (NumberFormatException e) {
                return "casting_error";
            }
            varString.put(varName, varValue);
            names.put(varName, "string");
            return "var initialized";
        } else if(functionCall.matches(doublePattern)) {
            String[] split = functionCall.split(" ");
            String varName = split[1];
            double varValue;
            try {
                varValue = Double.valueOf(parseCommand(split[3]+"}").split("}")[0]);
            } catch (NumberFormatException e) {
                return "casting_error";
            }
            varDouble.put(varName, varValue);
            names.put(varName, "double");
            return "var initialized";
        } else if(functionCall.matches(boolPattern)) {
            String[] split = functionCall.split(" ");
            String varName = split[1];
            boolean varValue;
            try {
                varValue = Boolean.valueOf(parseCommand(split[3]+"}").split("}")[0]);
            } catch (NumberFormatException e) {
                return "casting_error";
            }
            varBool.put(varName, varValue);
            names.put(varName, "bool");
            return "var initialized";
        }
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
