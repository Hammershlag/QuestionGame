package server.database.userDatabase;

import server.database.DatabaseHandler;

import java.io.*;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;

/**
 * The `UserDatabaseHandler` class manages a database of users stored in a text file. It provides methods to add, retrieve,
 * and manipulate user records.
 *
 * @uses DatabaseHandler
 * @author Tomasz Zbroszczyk
 * @since 12.10.2023
 * @version 1.0
 */
public class UserDatabaseHandler implements DatabaseHandler<User> {
    /**
     * The collection of users.
     */
    private final LinkedList<User> users;
    /**
     * The ID of the last user in the database.
     */
    private int lastUserId;
    /**
     * The filename for user storage.
     */
    private final String filename;

    /**
     * Constructs a new `UserDatabaseHandler` with the specified database directory.
     *
     * @param databaseDirectory The directory where the user database is stored.
     */
    public UserDatabaseHandler(String databaseDirectory) {
        users = new LinkedList<>();
        lastUserId = 0;
        filename = databaseDirectory;
        loadFromFile();
    }

    /**
     * Adds a new user to the database with the given username and password.
     *
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @return `true` if the user is successfully added, `false` if the username already exists or an error occurs.
     */
    public boolean add(String username, String password) {
        return add(username + ":" + password);
    }

    /**
     * Adds a new user to the database from the given string.
     * @uses DatabaseHandler
     * @param str
     * @return true if the user is successfully added, false if an error occurs
     */
    @Override
    public boolean add(String str) {
        // Check if a user with the same username already exists
        String[] parts = str.split(":");
        for (User user : users) {
            if (user.getUsername().equals(parts[0])) {
                System.out.println("User with the same username already exists.");
                return false; // User not added
            }
        }

        lastUserId++; // Increment the user id for the new user
        User newUser = new User(lastUserId, parts[0], parts[1]);
        users.add(newUser);

        // Attempt to append the user to the file
        if (appendToFile(newUser)) {
            return true; // User added and stored in the file successfully
        } else {
            System.out.println("Failed to append user to the file.");
            users.removeLast(); // Rollback the user addition
            return false; // User not added
        }
    }

    /**
     * Retrieves a user by their unique ID.
     * @uses DatabaseHandler
     * @param id The ID of the user to retrieve.
     * @return The `User` object if found, or `null` if the user is not found.
     */
    @Override
    public User getById(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null; // User not found
    }

    /**
     * Retrieves a user by their username.
     * @uses DatabaseHandler
     * @param username The username of the user to retrieve.
     * @return The `User` object if found, or `null` if the user is not found.
     */
    @Override
    public User getByName(String... username) {
        for (User user : users) {
            if (user.getUsername().equals(username[0])) {
                return user;
            }
        }
        return null; // User not found
    }

    /**
     * Sorts the list of users by their unique IDs and updates the user database file.
     * @uses DatabaseHandler
     */
    @Override
    public void sortById() {
        Collections.sort(users, Comparator.comparing(User::getId));
        update();
    }

    /**
     * Loads users from the user database file into the database.
     * @uses DatabaseHandler
     * @return `true` if the users are loaded successfully, `false` if an error occurs.
     */
    @Override
    public boolean loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 3) {
                    int id = Integer.parseInt(parts[0]);
                    String username = parts[1];
                    String password = parts[2];
                    users.add(new User(id, username, password));
                    if (id > lastUserId) {
                        lastUserId = id;
                    }
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Appends a new user to the end of the user database file.
     * @uses DatabaseHandler
     * @param user The user to append to the file.
     * @return `true` if the user is appended successfully, `false` if an error occurs.
     */
    @Override
    public boolean appendToFile(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(user.getId() + ":" + user.getUsername() + ":" + user.getPassword());
            writer.newLine();
            writer.flush();
            return true; // Successful append
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Failed to append
        }
    }

    /**
     * Appends a string to the end of the file. Considers str is in a correct format.
     * @uses DatabaseHandler
     * @param str
     * @return true if the record is successfully appended, false if an error occurs
     */
    @Deprecated
    @Override
    public boolean appendToFile(String str) {
        return false;
    }

    /**
     * Saves the list of users to the user database file.
     * @uses DatabaseHandler
     * @return `true` if the users are saved successfully, `false` if an error occurs.
     */
    @Override
    public boolean update() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (User user : users) {
                writer.write(user.getId() + ":" + user.getUsername() + ":" + user.getPassword());
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
     * Gets a list of all users stored in the database.
     * @uses DatabaseHandler
     * @return A linked list of all users.
     */
    @Override
    public LinkedList<User> getAll() {
        return users;
    }

    /**
     * Prints the details of all users in the database.
     */
    public void printUsers() {
        for (User user : users) {
            System.out.println(user.toString());
        }
    }
}
