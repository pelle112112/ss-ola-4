package soft2.model;

import java.util.ArrayList;
import java.util.List;

public class Shop {
    private final List<Bike> inventory;

    public Shop(List<Bike> initial) {
        this.inventory = new ArrayList<>(initial); // ensure mutable
    }

    public List<Bike> getInventory() { return inventory; }

    public Bike findBikeById(int id) {
        for (Bike b : inventory) if (b.getId() == id) return b;
        return null;
    }

    public boolean reserveBike(int id) {
        Bike b = findBikeById(id);
        if (b != null && !b.isReserved()) { b.setReserved(true); return true; }
        return false;
    }

    public boolean returnBike(int id) {
        Bike b = findBikeById(id);
        if (b != null && b.isReserved()) {
            b.setReserved(false);
            return true;
        }
        return false;
    }


}
