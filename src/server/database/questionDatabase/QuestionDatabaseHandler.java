package server.database.questionDatabase;

import server.database.userDatabase.User;

import java.util.LinkedList;

import java.io.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The `QuestionDatabaseHandler` class manages a collection of questions and provides methods for adding, retrieving,
 * and managing questions in a database. It also allows loading questions from a file and appending new questions to it.
 *
 * @author Tomasz Zbroszczyk
 * @since 12.10.2023
 * @version 1.0
 */
public class QuestionDatabaseHandler {
    /**
     * The collection of questions.
     */
    private final LinkedList<Question> questions;
    /**
     * The filename for question storage.
     */
    private final String filename;

    /**
     * Constructs a `QuestionDatabaseHandler` with the specified filename for question storage.
     *
     * @param filename The filename for question storage.
     */
    public QuestionDatabaseHandler(String filename) {
        this.filename = filename;
        this.questions = new LinkedList<>();
        loadQuestionsFromFile();
    }

    /**
     * Adds a new question to the database with the specified type, question text, correct answer, and a list of answers.
     *
     * @param type          The type or category of the question.
     * @param questionText  The text of the question.
     * @param correct_answer The correct answer to the question.
     * @param answers       An array of possible answers to the question.
     */
    public void addQuestion(String type, String questionText, String correct_answer, String[] answers) {
        // Generate a unique ID for the new question
        int id = questions.isEmpty() ? 1 : questions.getLast().getId() + 1;
        LinkedList<String> answerList = new LinkedList<>();
        Collections.addAll(answerList, answers);
        Question question = new Question(id, type, questionText, correct_answer, answerList);
        questions.add(question);
        appendQuestionToFile(question);
    }

    /**
     * Retrieves a question by its unique ID.
     *
     * @param id The ID of the question to retrieve.
     * @return The question with the specified ID, or `null` if not found.
     */
    public Question getQuestionById(int id) {
        for (Question question : questions) {
            if (question.getId() == id) {
                return question;
            }
        }
        return null; // Question with the specified ID not found
    }

    /**
     * Sorts the questions in the database by their unique ID in ascending order.
     */
    public void sortQuestionsById() {
        Collections.sort(questions, (q1, q2) -> Integer.compare(q1.getId(), q2.getId()));
    }

    /**
     * Loads questions from the question storage file into the database.
     */
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
                Collections.addAll(answerList, answers);
                Question question = new Question(id, type, questionText, correct_answer, answerList);
                questions.add(question);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Appends a new question to the end of the question storage file.
     *
     * @param question The question to append to the file.
     */
    private void appendQuestionToFile(Question question) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(question.getId() + ":" + question.getType() + ":" + question.getQuestion() + ":" + question.getCorrectAnswer() + ":" + String.join(";", question.getAnswers()));
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a list of all questions stored in the database.
     *
     * @return A linked list of all questions.
     */
    public LinkedList<Question> getQuestions() {
        return questions;
    }

    /**
     * Returns a string representation of all questions in the database, joined with newline characters.
     *
     * @return A string containing all questions, each separated by a newline character.
     */
    @Override
    public String toString() {
        return questions.stream().map(Question::toString).collect(Collectors.joining("\n"));
    }
}


