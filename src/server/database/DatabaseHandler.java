package server.database;

import java.util.LinkedList;

/**
 * @author Tomasz Zbroszczyk on 16.10.2023
 * @version 1.0
 */
public interface DatabaseHandler<T> {
    /**
     * Loads the database from a file.
     * @return true if the database is successfully loaded, false if an error occurs
     */

    boolean loadFromFile();

    /**
     * Appends a new record T t to the database file.
     * @param t
     * @return true if the record is successfully appended, false if an error occurs
     */
    boolean appendToFile(T t);

    /**
     * Adds a new record to the database from the given string.
     * @param str
     * @return true if the record is successfully added, false if an error occurs
     */
    boolean add(String str);

    /**
     * Gets record from the database by id.
     * @param id
     * @return record T t
     */
    T getById(int id);

    /**
     * Gets record from the database by name/names.
     * @param str
     * @return record T t
     */
    T getByName(String... str);

    /**
     * Sorts all records from the database by id (can be specified in implementation)
     */
    void sortById();

    /**
     * Appends a record to the database file. Assumes correct string format.
     * @param str
     * @return true if the record is successfully appended, false if an error occurs
     */
    boolean appendToFile(String str);

    /**
     * Gets all records from the database.
     * @return LinkedList<T> all
     */
    LinkedList<T> getAll();

    /**
     * Updates the database file.
     * @return true if the database is successfully updated, false if an error occurs
     */
    boolean update();
}
