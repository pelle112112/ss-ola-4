package soft2.persistence;

import soft2.model.*;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Store {

    private static final Store INSTANCE = new Store();
    public static Store get() { return INSTANCE; }

    private final Map<Integer, Bike> bikes = new ConcurrentHashMap<>();
    private final Map<String, Reservation> reservations = new ConcurrentHashMap<>();
    private final Map<Integer, String> reservedBikeToReservation = new ConcurrentHashMap<>();
    private final Map<Integer, Rental> activeRentals = new ConcurrentHashMap<>();
    private final Map<String, User> users = new ConcurrentHashMap<>();
    private int nextReservationId = 1;
    private int nextRentalId = 1;

    private Store() {
        bikes.put(1, new Bike(1, "City", true));
        bikes.put(2, new Bike(2, "Road", true));
        bikes.put(3, new Bike(3, "E-Bike", true));

        users.put("u1", new User("u1", "pass", false));
        users.put("admin", new User("admin", "admin", true));
    }

    //Bikes
    public Collection<Bike> listBikes(){ return bikes.values(); }

    //Reservations
    public synchronized Reservation createReservation(String userId, int bikeId) {
        Bike b = bikes.get(bikeId);
        if (b == null || !b.available()) throw new IllegalStateException("Bike not available");

        String resId = String.valueOf(nextReservationId++);   // incrementing ID
        Reservation res = new Reservation(resId, bikeId, userId, Instant.now(), true);

        reservations.put(resId, res);
        bikes.put(bikeId, b.withAvailable(false));
        reservedBikeToReservation.put(bikeId, resId);
        return res;
    }


    // Rentals
    public synchronized Rental startRental(String userId, String reservationId) {
        Reservation res = reservations.get(reservationId);
        if (res == null || !res.active()) throw new IllegalStateException("Invalid reservation");
        if (!Objects.equals(res.userId(), userId)) throw new IllegalStateException("Reservation owner mismatch");
        if (activeRentals.containsKey(res.bikeId())) throw new IllegalStateException("Bike already rented");

        String rentalId = String.valueOf(nextRentalId++);   // incrementing ID
        Rental rental = new Rental(rentalId, userId, res.bikeId());

        activeRentals.put(res.bikeId(), rental);
        reservations.put(reservationId, res.deactivate());
        return rental;
    }


    public synchronized Rental endRental(String userId, int bikeId, long feeCents) {
        Rental r = activeRentals.remove(bikeId);
        if (r == null) throw new IllegalStateException("No active rental");
        if (!Objects.equals(r.userId(), userId)) throw new IllegalStateException("Rental owner mismatch");

        r.end(feeCents);
        bikes.put(bikeId, bikes.get(bikeId).withAvailable(true));
        reservedBikeToReservation.remove(bikeId);
        return r;
    }

    //Users
    public User getUser(String id) { return users.get(id); }

    //Admin

    public synchronized Bike addBike(String type) {
        int nextId = bikes.keySet().stream().max(Integer::compareTo).orElse(0) + 1;
        Bike newBike = new Bike(nextId, type, true);
        bikes.put(nextId, newBike);
        return newBike;
    }

    public synchronized void removeBike(int bikeId) {
        Bike current = bikes.get(bikeId);
        if (current != null && current.available()) {
            bikes.remove(bikeId);
        } else {
            throw new IllegalStateException("Cannot remove: not available or missing");
        }
    }
}
