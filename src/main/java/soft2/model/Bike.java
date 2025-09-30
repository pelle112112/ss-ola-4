package soft2.model;

public class Bike {
    private final int id;
    private final String type;
    private final boolean available;

    public Bike(int id, String type) {
        this(id, type, true);
    }

    public Bike(int id, String type, boolean available) {
        this.id = id;
        this.type = type;
        this.available = available;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public boolean isAvailable() { return available; }

    public int id() { return id; }
    public String type() { return type; }
    public boolean available() { return available; }

    public Bike withAvailable(boolean a) { return new Bike(id, type, a); }
}
