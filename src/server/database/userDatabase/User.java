package server.database.userDatabase;

public class User {

    final private int id;
    final private String username;
    final private String password;

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", password=" + password + "]";
    }

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
