package server.database.questionDatabase;

import server.database.DatabaseHandler;
import server.database.userDatabase.User;

import java.util.LinkedList;

import java.io.*;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * The `QuestionDatabaseHandler` class manages a collection of questions and provides methods for adding, retrieving,
 * and managing questions in a database. It also allows loading questions from a file and appending new questions to it.
 *
 * @uses DatabaseHandler
 * @author Tomasz Zbroszczyk
 * @since 12.10.2023
 * @version 1.0
 */
public class QuestionDatabaseHandler implements DatabaseHandler<Question> {
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
        loadFromFile();
    }

    /**
     * Adds a new question to the database with the given type, question text, correct answer, and answer options by using add(String str)
     * @param type
     * @param questionText
     * @param correct_answer
     * @param answers
     * @return
     */
    public boolean add(String type, String questionText, String correct_answer, String[] answers) {
        return add(type + ":" + questionText + ":" + correct_answer + ":" + String.join(";", answers));
    }

    /**
     * Adds a new question to the database from the given string
     * @uses DatabaseHandler
     * @param str
     * @return true if the question is successfully added, false if an error occurs
     */
    @Override
    public boolean add(String str) {
        String[] parts = str.split(":");
        String[] ans = parts[3].split(";");
        // Generate a unique ID for the new question
        int id = questions.isEmpty() ? 1 : questions.getLast().getId() + 1;
        LinkedList<String> answerList = new LinkedList<>();
        Collections.addAll(answerList, ans);
        Question question = new Question(id, parts[0], parts[1], parts[2], answerList);
        questions.add(question);
        appendToFile(question);
        return true;
    }

    /**
     * Retrieves a question by its unique ID.
     * @uses DatabaseHandler
     * @param id The ID of the question to retrieve.
     * @return The question with the specified ID, or `null` if not found.
     */
    @Override
    public Question getById(int id) {
        for (Question question : questions) {
            if (question.getId() == id) {
                return question;
            }
        }
        return null; // Question with the specified ID not found
    }

    /**
     * Sorts the questions in the database by their unique ID in ascending order.
     * @uses DatabaseHandler
     */
    @Override
    public void sortById() {
        Collections.sort(questions, (q1, q2) -> Integer.compare(q1.getId(), q2.getId()));
    }

    /**
     * Loads questions from the question storage file into the database.
     * @uses DatabaseHandler
     * @return `true` if the questions are loaded successfully, `false` if an error occurs.
     */
    @Override
    public boolean loadFromFile() {
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
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Appends a new question to the end of the question storage file.
     * @uses DatabaseHandler
     * @param question The question to append to the file.
     * @return `true` if the question is successfully appended, `false` if an error occurs.
     */
    @Override
    public boolean appendToFile(Question question) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(question.getId() + ":" + question.getType() + ":" + question.getQuestion() + ":" + question.getCorrectAnswer() + ":" + String.join(";", question.getAnswers()));
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Appends a string to the end of the file. Considers str is in a correct format. Deprecated
     * @uses DatabaseHandler
     * @param str
     * @return false
     */
    @Deprecated
    @Override
    public boolean appendToFile(String str) {
        return false;
    }

    /**
     * Gets a question by its name.
     * @uses DatabaseHandler
     * @param str
     * @return The question with the specified name, or `null` if not found.
     */

    @Override
    public Question getByName(String... str) {
        return questions.stream().filter(question -> question.getQuestion().equals(str[0])).collect(Collectors.toList()).get(0);
    }

    /**
     * Gets a list of all questions stored in the database.
     * @uses DatabaseHandler
     * @return A linked list of all questions.
     */
    public LinkedList<Question> getAll() {
        return questions;
    }

    /**
     * Saves the list of questions to the question storage file.
     * @uses DatabaseHandler
     * @return `true` if the questions are saved successfully, `false` if an error occurs.
     */
    @Override
    public boolean update() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Question question : questions) {
                writer.write(question.getId() + ":" + question.getType() + ":" + question.getQuestion() + ":" + question.getCorrectAnswer() + ":" + String.join(";", question.getAnswers()));
                writer.newLine();
            }
            writer.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns a string representation of all questions in the database, joined with newline characters.
     * @uses DatabaseHandler
     * @return A string containing all questions, each separated by a newline character.
     */
    @Override
    public String toString() {
        return questions.stream().map(Question::toString).collect(Collectors.joining("\n"));
    }
}


