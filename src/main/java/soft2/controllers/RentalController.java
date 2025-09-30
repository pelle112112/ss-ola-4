package soft2.controllers;

import io.javalin.http.Handler;
import soft2.model.Rental;
import soft2.persistence.Store;

import java.util.Map;

public class RentalController {

    public Handler startRental() {
        return ctx -> {
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            String userId = String.valueOf(body.get("user_id"));
            String reservationId = String.valueOf(body.get("reservation_id"));

            try {
                Rental r = Store.get().startRental(userId, reservationId);

                ctx.status(201).json(Map.of(
                        "rental_id", r.getId(),
                        "bike_id", r.getBikeId(),
                        "start", r.getStart().toString()
                ));
            } catch (Exception e) {
                ctx.status(409).json(Map.of("error", e.getMessage()));
            }
        };
    }

    public Handler endRental() {
        return ctx -> {
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            String userId = String.valueOf(body.get("user_id"));
            int bikeId = body.get("bike_id") instanceof Number n ? n.intValue()
                    : Integer.parseInt(String.valueOf(body.get("bike_id")));

            long feeCents = 1000L;

            try {
                Rental r = Store.get().endRental(userId, bikeId, feeCents);
                ctx.json(Map.of(
                        "rental_id", r.getId(),
                        "duration_s", r.durationSeconds(),
                        "fees_charged_cents", r.getFeeCents()
                ));

            } catch (Exception e) {
                ctx.status(409).json(Map.of("error", e.getMessage()));
            }
        };
    }
}
