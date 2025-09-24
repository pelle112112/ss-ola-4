package soft2.model;

public class Bike {
    private int id;
    private String type;
    private boolean reserved;

    public Bike(int id, String type) {
        this.id = id;
        this.type = type;
        this.reserved = false;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public boolean isReserved() { return reserved; }
    public void setReserved(boolean reserved) { this.reserved = reserved; }
}
