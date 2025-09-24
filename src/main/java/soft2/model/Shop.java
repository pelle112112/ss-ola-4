package soft2.model;

import java.util.List;

public class Shop {

    private List<Bike> inventory;

    public Shop(List<Bike> inventory) {
        this.inventory = inventory;
    }

    public List<Bike> getInventory() {
        return inventory;
    }
    public void setInventory(List<Bike> inventory) {
        this.inventory = inventory;
    }

    public Bike findBikeById(int id) {
        for (Bike bike : inventory) {
            if (bike.getId() == id) {
                return bike;
            }
        }
        return null; // Bike not found
    }

    public boolean reserveBike(int id) {
        Bike bike = findBikeById(id);
        if (bike != null && !bike.isReserved()) {
            bike.setReserved(true);
            return true; // Reservation successful
        }
        return false; // Bike not found or already reserved
    }
    public boolean returnBike(int id) {
        Bike bike = findBikeById(id);
        if (bike != null && bike.isReserved()) {
            bike.setReserved(false);
            return true; // Return successful
        }
        return false; // Bike not found or not reserved
    }


    public Bike addBikeToInventory(Bike bike) {
        inventory.add(bike);
        return bike;
    }
    public Bike removeBikeFromInventory(int id) {
        Bike bike = findBikeById(id);
        if (bike != null) {
            inventory.remove(bike);
            return bike;
        }
        return null; // Bike not found
    }


}
