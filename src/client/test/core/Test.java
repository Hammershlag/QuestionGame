package client.test.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tomasz Zbroszczyk on 20.10.2023
 * @version 1.0
 */
public class Test {

    protected int testId;
    protected String testName;
    protected String testDescription;

    protected List<String> commands = new ArrayList<>();
    protected List<String> answers = new ArrayList<>();

    protected int commandPtr = 0;
    protected int answerPtr = 0;

    public Test(int testId, String testName, String testDescription, List<String> commands, List<String> answers) {
        this.testId = testId;
        this.testName = testName;
        this.testDescription = testDescription;
        this.commands.addAll(commands);
        this.answers.addAll(answers);
    }

    public Test(int testId, String testName, String testDescription) {
        this.testId = testId;
        this.testName = testName;
        this.testDescription = testDescription;
    }
    public Test() {}

    public String getNextCommand() {
        if(commandPtr < commands.size()) {
            return commands.get(commandPtr++);
        } else {
            return null;
        }
    }

    public boolean hasNextCommand() {
        return commandPtr < commands.size();
    }

    public void previousCommand() {
        if(commandPtr > 0) {
            commandPtr--;
        }
    }

    public String getNextAnswer() {
        if(answerPtr < answers.size()) {
            return answers.get(answerPtr++);
        } else {
            return null;
        }
    }

    public boolean hasNextAnswer() {
        return answerPtr < answers.size();
    }

    public void previousAnswer() {
        if(answerPtr > 0) {
            answerPtr--;
        }
    }
    public void reset() {
        commandPtr = 0;
        answerPtr = 0;
    }

    public String getTestName() {
        return testName;
    }

    public String getTestDescription() {
        return testDescription;
    }

    public int getTestId() {
        return testId;
    }

    public void addCommand(String command) {
        commands.add(command);
    }
    public void addAnswer(String answer) {
        answers.add(answer);
    }
    protected void setTestName(String testName) {
        this.testName = testName;
    }
    protected void setTestDescription(String testDescription) {
        this.testDescription = testDescription;
    }
    protected void setTestId(int testId) {
        this.testId = testId;
    }

}
