package soft2.persistence;

import soft2.model.Bike;
import soft2.model.Shop;

import java.util.List;

public class BikeDAO {

    // In memory database

    List <Bike> bikes = List.of(
        new Bike(1, "Mountain", 20),
        new Bike(2, "Road", 15),
        new Bike(3, "Hybrid", 18),
        new Bike(4, "Electric", 25),
        new Bike(5, "BMX", 10),
        new Bike(6, "Cruiser", 12)
    );
    Shop shop = new Shop(bikes);

    private static BikeDAO instance = null;
    private BikeDAO() {}
    public static BikeDAO getInstance() {
        if (instance == null) {
            instance = new BikeDAO();
        }
        return instance;
    }
    public List<Bike> getAllBikes() {
        return shop.getInventory();
    }
    public boolean reserveBike(int id) {
        return shop.reserveBike(id);
    }
    public boolean returnBike(int id) {
        return shop.returnBike(id);
    }

}
