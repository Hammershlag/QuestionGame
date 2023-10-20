package client.test.core.parser;

import client.test.core.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Tomasz Zbroszczyk on 20.10.2023
 * @version 1.0
 */
public class TestParser extends Test {

    protected String test_dir;

    private final  String regex_beg = "(\\d+): ([^:]+): ([^:]+)"; //OK
    private final  String regex_end = "end"; //OK
    private final String regex_for = "\\s*for ([1-9]\\d*) times:"; //OK

    private final String regex_for_end = "\\s*end for"; //OK

    protected List<Test> tests = new ArrayList<>();

    TestParser(){}
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

}
