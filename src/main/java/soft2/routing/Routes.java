package soft2.routing;

import io.javalin.Javalin;
import soft2.controllers.BikeController;

public class Routes {
    public static void register(Javalin app) {
        var ctrl = new BikeController();

        app.get ("/bikes",                 ctrl.getAllBikes());
        app.post("/bikes/{id}/reserve",    ctrl.reserveBike());
        app.post("/bikes/{id}/start",      ctrl.startRental());
        app.post("/bikes/{id}/return",     ctrl.returnBike());


        app.post  ("/admin/inventory",     ctrl.addBike());
        app.delete("/admin/inventory/{id}",ctrl.removeBike());
    }
}
