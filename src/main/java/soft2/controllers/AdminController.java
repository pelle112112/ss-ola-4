package soft2.controllers;

import io.javalin.http.Handler;
import soft2.model.Bike;
import soft2.model.User;
import soft2.persistence.Store;

import java.util.Map;

public class AdminController {

    public Handler addBike() {
        return ctx -> {
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            String adminId = String.valueOf(body.get("admin_id"));
            String type = body.get("type") == null ? "City" : String.valueOf(body.get("type"));

            User user = Store.get().getUser(adminId);
            if (user == null || !user.isAdmin()) {
                ctx.status(403).json(Map.of("error", "forbidden"));
                return;
            }

            try {
                Bike bike = Store.get().addBike(type);

                ctx.status(201).json(bike);
            } catch (Exception e) {
                ctx.status(409).json(Map.of("error", e.getMessage()));
            }
        };
    }


    public Handler removeBike() {
        return ctx -> {
            int bikeId = Integer.parseInt(ctx.pathParam("id"));
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            String adminId = String.valueOf(body.get("admin_id"));

            User user = Store.get().getUser(adminId);
            if (user == null || !user.isAdmin()) {
                ctx.status(403).json(Map.of("error", "forbidden"));
                return;
            }

            try {
                Store.get().removeBike(bikeId);
                ctx.json(Map.of("ok", true));

            } catch (Exception e) {
                ctx.status(409).json(Map.of("error", e.getMessage()));
            }
        };
    }
}
