package server.database.userDatabase;

import java.io.*;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;

/**
 * The `UserDatabaseHandler` class manages a database of users stored in a text file. It provides methods to add, retrieve,
 * and manipulate user records.
 *
 * @author Tomasz Zbroszczyk
 * @since 12.10.2023
 * @version 1.0
 */
public class UserDatabaseHandler {
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
        loadUsersFromTextFile();
    }

    /**
     * Adds a new user to the database with the given username and password.
     *
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @return `true` if the user is successfully added, `false` if the username already exists or an error occurs.
     */
    public boolean addUser(String username, String password) {
        // Check if a user with the same username already exists
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.println("User with the same username already exists.");
                return false; // User not added
            }
        }

        lastUserId++; // Increment the user id for the new user
        User newUser = new User(lastUserId, username, password);
        users.add(newUser);

        // Attempt to append the user to the file
        if (appendUserToTextFile(newUser)) {
            return true; // User added and stored in the file successfully
        } else {
            System.out.println("Failed to append user to the file.");
            users.removeLast(); // Rollback the user addition
            return false; // User not added
        }
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The `User` object if found, or `null` if the user is not found.
     */
    public User getUserById(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null; // User not found
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user to retrieve.
     * @return The `User` object if found, or `null` if the user is not found.
     */
    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null; // User not found
    }

    /**
     * Sorts the list of users by their unique IDs and updates the user database file.
     */
    public void sortUsersById() {
        Collections.sort(users, Comparator.comparing(User::getId));
        saveUsersToTextFile();
    }

    /**
     * Loads users from the user database file into the database.
     *
     * @return `true` if the users are loaded successfully, `false` if an error occurs.
     */
    private boolean loadUsersFromTextFile() {
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
     *
     * @param user The user to append to the file.
     * @return `true` if the user is appended successfully, `false` if an error occurs.
     */
    private boolean appendUserToTextFile(User user) {
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
     * Saves the list of users to the user database file.
     *
     * @return `true` if the users are saved successfully, `false` if an error occurs.
     */
    private boolean saveUsersToTextFile() {
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
     * Prints the details of all users in the database.
     */
    public void printUsers() {
        for (User user : users) {
            System.out.println(user.toString());
        }
    }
}
