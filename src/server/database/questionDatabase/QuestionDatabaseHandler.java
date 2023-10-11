package server.database.questionDatabase;

import server.database.userDatabase.User;

import java.util.LinkedList;

import java.io.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionDatabaseHandler {
    private final LinkedList<Question> questions;
    private final String filename;

    public QuestionDatabaseHandler(String filename) {
        this.filename = filename;
        this.questions = new LinkedList<>();
        loadQuestionsFromFile();
    }

    public void addQuestion(String type, String questionText, String correct_answer, String[] answers) {
        int id = questions.isEmpty() ? 1 : questions.getLast().getId() + 1;
        LinkedList<String> answerList = new LinkedList<>();
        for (String answer : answers) {
            answerList.add(answer);
        }
        Question question = new Question(id, type, questionText ,correct_answer, answerList);
        questions.add(question);
        appendQuestionToFile(question);
    }


    public Question getQuestionById(int id) {
        for (Question question : questions) {
            if (question.getId() == id) {
                return question;
            }
        }
        return null; // Question with the specified id not found
    }

    public void sortQuestionsById() {
        Collections.sort(questions, (q1, q2) -> Integer.compare(q1.getId(), q2.getId()));
    }

    private void loadQuestionsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                int id = Integer.parseInt(parts[0].trim());
                String type = parts[1].trim();
                String questionText = parts[2].trim();
                String correct_answer = parts[3].trim();
                String[] answers = parts[4].split(";");
                LinkedList<String> answerList = new LinkedList<>();
                for (String answer : answers) {
                    answerList.add(answer);
                }
                Question question = new Question(id, type, questionText,correct_answer, answerList);
                questions.add(question);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendQuestionToFile(Question question) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(question.getId() + ":" + question.getType() + ":" + question.getQuestion() + ":" + question.getCorrectAnswer() + ":" + String.join(";", question.getAnswers()));
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LinkedList<Question> getQuestions() {
        return questions;
    }

    @Override
    public String toString() {
        return questions.stream().map(Question::toString).collect(Collectors.joining("\n"));
    }
}

