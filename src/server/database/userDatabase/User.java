package server.database.userDatabase;

/**
 * The `User` class represents a user entity with an ID, username, and password.
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 */
public class User {
    /**
     * The unique ID of the user.
     */
    private final int id;
    /**
     * The username of the user.
     */
    private final String username;
    /**
     * The password of the user.
     */
    private final String password;

    /**
     * Constructs a new `User` with the specified ID, username, and password.
     *
     * @param id       The unique ID of the user.
     * @param username The username of the user.
     * @param password The password of the user.
     */
    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the ID of the user.
     *
     * @return The unique ID of the user.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the username of the user.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password of the user.
     *
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns a string representation of the user containing its ID, username, and password.
     *
     * @return A string representation of the user.
     */
    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", password=" + password + "]";
    }

    /**
     * Parses a formatted user string and creates a `User` object.
     *
     * @param userString A formatted user string containing ID, username, and password.
     * @return A `User` object if the parsing is successful, or `null` if the format is invalid.
     */
    public static User parseUserString(String userString) {
        String[] parts = userString.split("[,=]");
        if (parts.length == 6) {
            int id = Integer.parseInt(parts[1].trim());
            String username = parts[3].trim();
            String password = parts[5].trim();
            return new User(id, username, password);
        }
        return null; // Invalid userString format
    }
}
