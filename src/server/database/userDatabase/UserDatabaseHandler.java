package server.database.userDatabase;

import java.io.*;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;

public class UserDatabaseHandler {
    private LinkedList<User> users;
    private int lastUserId;

    private String filename;

    public UserDatabaseHandler(String databaseDirectory) {
        users = new LinkedList<>();
        lastUserId = 0;
        filename = databaseDirectory;// Initialize with a starting id
        loadUsersFromTextFile();
    }

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


    public User getUserById(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null; // User not found
    }

    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null; // User not found
    }

    public void sortUsersById() {
        Collections.sort(users, Comparator.comparing(User::getId));
        saveUsersToTextFile();
    }

    private boolean loadUsersFromTextFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while((line = reader.readLine()) != null) {
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

    private boolean appendUserToTextFile(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(user.getId() + ":" + user.getUsername() + ":" + user.getPassword());
            writer.newLine();
            writer.flush();
            writer.close();
            return true; // Successful append
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Failed to append
        }
    }

    private boolean saveUsersToTextFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (User user : users) {
                writer.write(user.getId() + ":" + user.getUsername() + ":" + user.getPassword());
                writer.newLine();

            }
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void printUsers() {

        for (User user : users) {
            System.out.println(user.toString());
        }
    }
}

