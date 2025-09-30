package soft2.model;

public class User {
    private final String id;
    private final String passwordHash;
    private final boolean admin;

    public User(String id, String passwordHash, boolean admin) {
        this.id = id;
        this.passwordHash = passwordHash;
        this.admin = admin;
    }

    public String getId() { return id; }
    public String getPasswordHash() { return passwordHash; }
    public boolean isAdmin() { return admin; }

    public String id() { return id; }
    public String passwordHash() { return passwordHash; }
    public boolean admin() { return admin; }
}
