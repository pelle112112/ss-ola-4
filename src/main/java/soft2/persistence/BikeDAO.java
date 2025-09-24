package soft2.persistence;

import soft2.model.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BikeDAO {

    private final List<Bike> bikes = new ArrayList<>(List.of(
            new Bike(1, "Mountain"),
            new Bike(2, "Road"),
            new Bike(3, "Hybrid"),
            new Bike(4, "Electric"),
            new Bike(5, "BMX"),
            new Bike(6, "Cruiser")
    ));
    private final Shop shop = new Shop(bikes);

    private final PricingService pricing = new PricingService(Map.of(
            "Mountain", 50,
            "Road",     60,
            "Hybrid",   45,
            "Electric", 100,
            "BMX",      25,
            "Cruiser",  30
    ));

    private final ConcurrentHashMap<Integer, Rental> activeRentals = new ConcurrentHashMap<>();

    private static BikeDAO instance = null;
    private BikeDAO() {}
    public static BikeDAO getInstance() {
        if (instance == null) { instance = new BikeDAO(); }
        return instance;
    }

    public List<Bike> getAllBikes() { return shop.getInventory(); }
    public Shop shop() { return shop; }
    public PricingService pricing() { return pricing; }

    public boolean reserveBike(int id) { return shop.reserveBike(id); }
    public boolean returnBike(int id) { return shop.returnBike(id); }

    public Rental startRental(int bikeId, int startPrice) {
        var rental = new Rental(bikeId, startPrice);
        activeRentals.put(bikeId, rental);
        return rental;
    }

    public Rental endRental(int bikeId) {
        var rental = activeRentals.remove(bikeId);
        if (rental != null) {
            rental.end();
        }
        return rental;
    }


}
