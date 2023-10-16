package server.database;

import java.util.LinkedList;

/**
 * @author Tomasz Zbroszczyk on 16.10.2023
 * @version 1.0
 */
public interface DatabaseHandler<T> {

    void loadFromFile();
    void appendToFile(T t);
    boolean add(String str);
    T getById(int id);
    T getByName(int... id);
    void sortById();
    void appendToFile(String str);
    LinkedList<T> getAll();
    void update();
}
