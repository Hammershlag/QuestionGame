package server.database.relationDatabase;

import server.database.DatabaseHandler;
import server.database.questionDatabase.Question;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * The `RelationDatabaseHandler` class manages a collection of relations and provides methods for adding, retrieving,
 * and managing relations in a database. It also allows loading relations from a file and appending new relations to it.
 *
 * @uses DatabaseHandler
 * @author Tomasz Zbroszczyk on 16.10.2023
 * @version 1.0
 */
public class RelationDatabaseHandler implements DatabaseHandler<Relation> {

    /**
     * The collection of relations.
     */
    private LinkedList<Relation> relations;
    /**
     * The maximum ID of a relation in the database.
     */
    private int maxId;
    /**
     * The filename for relation storage.
     */
    private String filename;

    /**
     * Constructs a `RelationDatabaseHandler` with the specified filename for relation storage.
     *
     * @param filename The filename for relation storage.
     */
    public RelationDatabaseHandler(String filename) {
        this.filename = filename;
        this.relations = new LinkedList<>();
        this.maxId = relations.isEmpty() ? 0 : relations.getLast().getId();
        loadFromFile();
    }

    /**
     * Gets the number of relations in the database.
     *
     * @return The number of relations in the database.
     */
    public int getRelationsSize() {
        return relations.size();
    }

    /**
     * Loads the database from a file.
     * @uses DatabaseHandler
     * @return true if the database is successfully loaded, false if an error occurs
     */
    @Override
    public boolean loadFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                relations.add(new Relation(line));
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Appends a new record Relation relation to the database file.
     * @uses DatabaseHandler
     * @param relation
     * @return true if the record is successfully appended, false if an error occurs
     */

    @Override
    public boolean appendToFile(Relation relation) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(relation.toString());
            bw.newLine();
            bw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Appends a string to the end of the file. Considers str is in a correct format.
     * @uses DatabaseHandler
     * @param str
     * @return true if the record is successfully appended, false if an error occurs
     */
    @Override
    public boolean appendToFile(String str) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(str);
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Adds a new record to the database from the given string.
     * @uses DatabaseHandler
     * @param str
     * @return true if the record is successfully added, false if an error occurs
     */
    @Deprecated
    @Override
    public boolean add(String str) {
        String s2 = ++maxId + str;
        relations.add(new Relation(s2));
        appendToFile(relations.getLast());
        return true;
    }

    /**
     * Adds a new relation to the database for the given users.
     * @uses DatabaseHandler
     * @param id1 Id of the first user
     * @param id2 Id of the second user
     * @return true if the record is successfully added, false if an error occurs
     */
    public boolean add(int id1, int id2) {
        if (checkIfExists(id1, id2)) {
            return false;
        }
        String str = (relations.getLast().getId()+1) + ":" + id1 + ":" + id2 + ":0:0:;:;:;:;";
        relations.add(new Relation(str));
        appendToFile(relations.getLast());
        return true;
    }

    /**
     * Gets record from the database by id.
     * @uses DatabaseHandler
     * @param id
     * @return record Relation relation, null if not found
     */
    @Override
    public Relation getById(int id) {
        for (Relation relation : relations) {
            if (relation.getId() == id) {
                return relation;
            }
        }
        return null;
    }

    /**
     * Gets record from the database by name/names.
     * @uses DatabaseHandler
     * @param str
     * @return record Relation relation, null if not found
     */
    @Override
    public Relation getByName(String... str) {
        for (Relation relation : relations) {
            if (relation.getUser1Id() == Integer.parseInt(str[0]) && relation.getUser2Id() == Integer.parseInt(str[1])) {
                return relation;
            }
        }
        return null;
    }

    /**
     * Sorts all records from the database by ids of the relations
     * @uses DatabaseHandler
     */
    @Override
    public void sortById() {
        Collections.sort(relations, (r1, r2) -> Integer.compare(r1.getId(), r2.getId()));
    }

    /**
     * Updates the database file.
     * @uses DatabaseHandler
     * @return true if the database is successfully updated, false if an error occurs
     */
    @Override
    public boolean update() {
        try (FileWriter writer = new FileWriter(filename, false)) {
            LinkedList<Relation> relations_c = (LinkedList<Relation>) relations.clone();
            while(!relations_c.isEmpty()) {
                writer.write(relations_c.removeFirst().toString());
                writer.write("\n");
            }
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets all records from the database.
     * @uses DatabaseHandler
     * @return LinkedList<Relation> all
     */
    @Override
    public LinkedList<Relation> getAll() {
        return relations;
    }

    /**
     * Check if the relation between two users exists
     * @param user1Id Id of the first user
     * @param user2Id Id of the second user
     * @return true if the relation exists, false if not
     */
    public boolean checkIfExists(int user1Id, int user2Id) {
        for (Relation relation : relations) {
            if (relation.getUser1Id() == user1Id && relation.getUser2Id() == user2Id) {
                return true;
            }
        }
        return false;
    }
}
