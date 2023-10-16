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

    public int getRelationsSize() {
        return relations.size();
    }

    @Override
    public void loadFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                relations.add(new Relation(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void appendToFile(Relation relation) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(relation.toString());
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Appends a string to the end of the file. Considers str is in a correct format.
     * @param str
     */
    @Override
    public void appendToFile(String str) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(str);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO ?????????
    @Override
    public boolean add(String str) {
        String s2 = ++maxId + str;
        relations.add(new Relation(s2));
        appendToFile(relations.getLast());
        return true;
    }

    public boolean add(int id1, int id2) {
        if (checkIfExists(id1, id2)) {
            return false;
        }
        String str = (relations.getLast().getId()+1) + ":" + id1 + ":" + id2 + ":0:0:;:;:;:;";
        relations.add(new Relation(str));
        appendToFile(relations.getLast());
        return true;
    }

    @Override
    public Relation getById(int id) {
        for (Relation relation : relations) {
            if (relation.getId() == id) {
                return relation;
            }
        }
        return null;
    }

    @Override
    public Relation getByName(int... id) {
        for (Relation relation : relations) {
            if (relation.getUser1Id() == id[0] && relation.getUser2Id() == id[1]) {
                return relation;
            }
        }
        return null;
    }

    @Override
    public void sortById() {
        Collections.sort(relations, (r1, r2) -> Integer.compare(r1.getId(), r2.getId()));
    }

    @Override
    public void update() {
        try (FileWriter writer = new FileWriter(filename, false)) {
            LinkedList<Relation> relations_c = (LinkedList<Relation>) relations.clone();
            while(!relations_c.isEmpty()) {
                writer.write(relations_c.removeFirst().toString());
                writer.write("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public LinkedList<Relation> getAll() {
        return relations;
    }

    public boolean checkIfExists(int user1Id, int user2Id) {
        for (Relation relation : relations) {
            if (relation.getUser1Id() == user1Id && relation.getUser2Id() == user2Id) {
                return true;
            }
        }
        return false;
    }
}
