package server.database.questionDatabase;

import java.util.LinkedList;

public class Question {

    final private int id;
    final private String type;
    final private String question;
    final private String correct_answer;
    final private LinkedList<String> answers;

    Question(int id, String type, String question, String correct_answer, LinkedList<String> answers) {
        this.id = id;
        this.type = type;
        this.question = question;
        this.correct_answer = correct_answer;
        this.answers = answers;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getAnswers() {
        return answers.toArray(new String[0]);
    }

    public String getCorrectAnswer() {
        return correct_answer;
    }

    @Override
    public String toString() {
        return "Question [id=" + id + ", type=" + type + ", correct_answer=" + correct_answer + ", question=" + question + ", answers=" + answers + "]";
    }
}
