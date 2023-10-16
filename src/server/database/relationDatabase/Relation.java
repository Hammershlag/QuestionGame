package server.database.relationDatabase;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * The `Relation` class represents a relation entity with its associated properties
 * It is used to store relation between two users (questions answered by both users, questions answered correctly by both users, questions answered by one user and not answered by second user)
 *
 * @author Tomasz Zbroszczyk on 16.10.2023
 * @version 1.0
 */
public class Relation {

    /**
     * The unique identifier for the relation.
     */
    private int id;
    /**
     * The ID of the first user.
     */
    private int user1Id;
    /**
     * The ID of the second user.
     */
    private int user2Id;
    /**
     * The number of questions answered by both users.
     */
    private int totalQuestionsAnswered;
    /**
     * The number of questions answered correctly by both users.
     */
    private int correctQuestionsAnswered;
    /**
     * The list of questions answered by the first user.
     */
    private LinkedList<Integer> questionsAnsweredByUser1;
    /**
     * The list of questions answered by the second user.
     */
    private LinkedList<Integer> questionsAnsweredByUser2;
    /**
     * The list of questions answered by the first user and not answered by second user.
     */
    private Map<Integer, String> questionsUnansweredByUser2;
    /**
     * The list of questions answered by the second user and not answered by first user.
     */
    private Map<Integer, String> questionsUnansweredByUser1;

    /**
     * Default constructor of the Relation class.
     */
    public Relation() {}

    /**
     * Constructor of the Relation class.
     * @param id
     * @param user1Id
     * @param user2Id
     * @param totalQuestionsAnswered
     * @param correctQuestionsAnswered
     * @param questionsAnsweredByUser1
     * @param questionsAnsweredByUser2
     * @param questionsUnansweredByUser2
     * @param questionsUnansweredByUser1
     */
    public Relation(int id, int user1Id, int user2Id, int totalQuestionsAnswered, int correctQuestionsAnswered, LinkedList<Integer> questionsAnsweredByUser1, LinkedList<Integer> questionsAnsweredByUser2, Map<Integer, String> questionsUnansweredByUser2, Map<Integer, String> questionsUnansweredByUser1) {
        this.id = id;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.totalQuestionsAnswered = totalQuestionsAnswered;
        this.correctQuestionsAnswered = correctQuestionsAnswered;
        this.questionsAnsweredByUser1 = questionsAnsweredByUser1;
        this.questionsAnsweredByUser2 = questionsAnsweredByUser2;
        this.questionsUnansweredByUser2 = questionsUnansweredByUser2;
        this.questionsUnansweredByUser1 = questionsUnansweredByUser1;
    }

    /**
     * Constructor of the Relation class from a string (database format).
     * @param str
     */
    public Relation(String str) {
        Relation relation = fromString(str);
        this.id = relation.getId();
        this.user1Id = relation.getUser1Id();
        this.user2Id = relation.getUser2Id();
        this.totalQuestionsAnswered = relation.getTotalQuestionsAnswered();
        this.correctQuestionsAnswered = relation.getCorrectQuestionsAnswered();
        this.questionsAnsweredByUser1 = relation.getQuestionsAnsweredByUser1();
        this.questionsAnsweredByUser2 = relation.getQuestionsAnsweredByUser2();
        this.questionsUnansweredByUser2 = relation.getQuestionsUnansweredByUser2();
        this.questionsUnansweredByUser1 = relation.getQuestionsUnansweredByUser1();
    }

    /**
     * Gets the unique identifier (ID) of the relation.
     *
     * @return The ID of the relation.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the ID of the first user.
     *
     * @return The ID of the first user.
     */
    public int getUser1Id() {
        return user1Id;
    }

    /**
     * Gets the ID of the second user.
     *
     * @return The ID of the second user.
     */
    public int getUser2Id() {
        return user2Id;
    }

    /**
     * Gets the total number of questions answered by both users.
     *
     * @return The total number of questions answered by both users.
     */
    public int getTotalQuestionsAnswered() {
        return totalQuestionsAnswered;
    }

    /**
     * Gets the total number of questions answered correctly by both users.
     *
     * @return The total number of questions answered correctly by both users.
     */
    public int getCorrectQuestionsAnswered() {
        return correctQuestionsAnswered;
    }

    /**
     * Gets the list of questions answered by the first user.
     *
     * @return The list of questions answered by the first user.
     */
    public LinkedList<Integer> getQuestionsAnsweredByUser1() {
        return questionsAnsweredByUser1;
    }

    /**
     * Gets the list of questions answered by the second user.
     *
     * @return The list of questions answered by the second user.
     */
    public LinkedList<Integer> getQuestionsAnsweredByUser2() {
        return questionsAnsweredByUser2;
    }

    /**
     * Gets the list of questions answered by the first user and not answered by second user.
     *
     * @return The list of questions answered by the first user and not answered by second user.
     */
    public Map<Integer, String> getQuestionsUnansweredByUser2() {
        return questionsUnansweredByUser2;
    }

    /**
     * Gets the list of questions answered by the second user and not answered by first user.
     *
     * @return The list of questions answered by the second user and not answered by first user.
     */
    public Map<Integer, String> getQuestionsUnansweredByUser1() {
        return questionsUnansweredByUser1;
    }

    public void addQuestionAnsweredByUser1(int questionId, String answer) {
        this.questionsUnansweredByUser1.put(questionId, answer);
    }

    public void addQuestionAnsweredByUser2(int questionId, String answer) {
        this.questionsUnansweredByUser2.put(questionId, answer);
    }

    /**
     * toString method of the Relation class.
     *
     * @return String representation of the Relation object.
     */
    @Override
    public String toString() {
        String questionsAnsweredByUser1Str = "";
        questionsAnsweredByUser1Str = this.questionsAnsweredByUser1.stream().map(Object::toString).reduce(questionsAnsweredByUser1Str, (s, s2) -> s + ";" + s2);
        if (questionsAnsweredByUser1Str.length() == 0) {
            questionsAnsweredByUser1Str = ";";
        }
        String questionsAnsweredByUser2Str = "";
        questionsAnsweredByUser2Str = this.questionsAnsweredByUser2.stream().map(Object::toString).reduce(questionsAnsweredByUser2Str, (s, s2) -> s + ";" + s2);
        if (questionsAnsweredByUser2Str.length() == 0) {
            questionsAnsweredByUser2Str = ";";
        }
        String questionsUnansweredByUser1Str = "";
        for (Map.Entry<Integer, String> entry : questionsUnansweredByUser1.entrySet()) {
            questionsUnansweredByUser1Str += entry.getKey() + "-" + entry.getValue() + ";";
        }
        if (questionsUnansweredByUser1Str.length() == 0) {
            questionsUnansweredByUser1Str = ";";
        }
        String questionsUnansweredByUser2Str = "";
        for (Map.Entry<Integer, String> entry : questionsUnansweredByUser2.entrySet()) {
            questionsUnansweredByUser2Str += entry.getKey() + "-" + entry.getValue() + ";";
        }
        if (questionsUnansweredByUser2Str.length() == 0) {
            questionsUnansweredByUser2Str = ";";
        }
        return id +
                ":" + user1Id +
                ":" + user2Id +
                ":" + totalQuestionsAnswered +
                ":" + correctQuestionsAnswered +
                ":" + questionsAnsweredByUser1Str +
                ":" + questionsAnsweredByUser2Str +
                ":" + questionsUnansweredByUser1Str +
                ":" + questionsUnansweredByUser2Str;
    }

    public Relation fromString(String str) {
        String[] parts = str.split(":");
        int id = Integer.parseInt(parts[0].trim());
        int user1Id = Integer.parseInt(parts[1].trim());
        int user2Id = Integer.parseInt(parts[2].trim());
        int totalQuestionsAnswered = Integer.parseInt(parts[3].trim());
        int correctQuestionsAnswered = Integer.parseInt(parts[4].trim());
        LinkedList<Integer> questionsAnsweredByUser1 = new LinkedList<>();
        String[] questionsAnsweredByUser1String = parts[5].trim().split(";");
        for (String questionId : questionsAnsweredByUser1String) {
            questionsAnsweredByUser1.add(Integer.parseInt(questionId));
        }
        LinkedList<Integer> questionsAnsweredByUser2 = new LinkedList<>();
        String[] questionsAnsweredByUser2String = parts[6].trim().split(";");
        for (String questionId : questionsAnsweredByUser2String) {
            questionsAnsweredByUser2.add(Integer.parseInt(questionId));
        }
        Map<Integer, String> questionsUnansweredByUser1 = new HashMap<>();
        String[] questionsUnansweredByUser1String = parts[7].trim().split(";");
        for (String questionId : questionsUnansweredByUser1String) {
            String[] questionParts = questionId.split("-");
            questionsUnansweredByUser1.put(Integer.parseInt(questionParts[0]), questionParts[1]);
        }
        Map<Integer, String> questionsUnansweredByUser2 = new HashMap<>();
        String[] questionsUnansweredByUser2String = parts[8].trim().split(";");
        for (String questionId : questionsUnansweredByUser2String) {
            String[] questionParts = questionId.split("-");
            questionsUnansweredByUser2.put(Integer.parseInt(questionParts[0]), questionParts[1]);
        }
        return new Relation(id, user1Id, user2Id, totalQuestionsAnswered, correctQuestionsAnswered, questionsAnsweredByUser1, questionsAnsweredByUser2, questionsUnansweredByUser1, questionsUnansweredByUser2);
    }



}
