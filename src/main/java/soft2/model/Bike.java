package soft2.model;

public class Bike {
    private int id;
    private String type;
    private int cost;
    private boolean isReserved;

    public Bike(int id, String type, int cost ) {
        this.id = id;
        this.type = type;
        this.cost = cost;
        this.isReserved = false; // Default to not reserved
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }
}
