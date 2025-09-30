package soft2.controllers;

import io.javalin.http.Handler;
import soft2.model.Reservation;
import soft2.persistence.Store;

import java.util.Map;

public class ReservationController {

    public Handler reserveBike() {
        return ctx -> {
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            String userId = String.valueOf(body.get("user_id"));
            int bikeId = body.get("bike_id") instanceof Number n ? n.intValue()
                    : Integer.parseInt(String.valueOf(body.get("bike_id")));

            try {
                Reservation res = Store.get().createReservation(userId, bikeId);
                ctx.status(201).json(res);

            } catch (Exception e) {
                ctx.status(409).json(Map.of("error", e.getMessage()));
            }
        };
    }
}
