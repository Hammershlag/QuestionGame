package server.database.questionDatabase;

import java.util.LinkedList;

/**
 * The `Question` class represents a question entity with its associated properties.
 * It includes an ID, type, question text, correct answer, and a list of possible answers.
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 */
public class Question {
    /**
     * The unique identifier for the question.
     */
    private final int id;
    /**
     * The type or category of the question. 0 for question with predefined answers, 1 for question with user input.
     */
    private final String type;
    /**
     * The text of the question.
     */
    private final String question;
    /**
     * The correct answer to the question.
     */
    private final String correct_answer;
    /**
     * A list of possible answers to the question.
     */
    private final LinkedList<String> answers;

    /**
     * Constructs a new Question object with the specified parameters.
     *
     * @param id             The unique identifier for the question.
     * @param type           The type or category of the question.
     * @param question       The text of the question.
     * @param correct_answer The correct answer to the question.
     * @param answers        A list of possible answers to the question.
     */
    Question(int id, String type, String question, String correct_answer, LinkedList<String> answers) {
        this.id = id;
        this.type = type;
        this.question = question;
        this.correct_answer = correct_answer;
        this.answers = answers;
    }

    /**
     * Gets the unique identifier (ID) of the question.
     *
     * @return The ID of the question.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the type or category of the question.
     *
     * @return The type of the question.
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the text of the question.
     *
     * @return The question text.
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Gets an array of possible answers to the question.
     *
     * @return An array of possible answers.
     */
    public String[] getAnswers() {
        return answers.toArray(new String[0]);
    }

    /**
     * Gets the correct answer to the question.
     *
     * @return The correct answer.
     */
    public String getCorrectAnswer() {
        return correct_answer;
    }

    /**
     * Returns a string representation of the question, including its ID, type, correct answer, question text, and answers.
     *
     * @return A string representation of the question.
     */
    @Override
    public String toString() {
        return "Question [id=" + id + ", type=" + type + ", correct_answer=" + correct_answer + ", question=" + question + ", answers=" + answers + "]";
    }

    /**
     * Compares the provided answer with the correct answer to determine if it is correct.
     *
     * @param answer The answer to compare.
     * @return `true` if the answer is correct, `false` otherwise.
     */
    public boolean compareAnswer(String answer) {
        return correct_answer.equals(answer);
    }
}
