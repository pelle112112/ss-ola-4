package soft2.routing;

import io.javalin.Javalin;
import soft2.controllers.*;

public class Routes {
    public static void register(Javalin app) {
        var bikes = new BikeController();
        var auth = new AuthController();
        var reservations = new ReservationController();
        var rentals = new RentalController();
        var admin = new AdminController();

        app.get("/api/bikes", bikes.getAllBikes());
        app.post("/api/auth/login", auth.login());
        app.post("/api/reservations", reservations.reserveBike());
        app.post("/api/rentals/start", rentals.startRental());
        app.post("/api/rentals/end", rentals.endRental());

        app.post("/api/admin/add-bike", admin.addBike());
        app.post("/api/admin/remove-bike/{id}", admin.removeBike());
    }
}
