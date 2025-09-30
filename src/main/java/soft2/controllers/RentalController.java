package soft2.controllers;

import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import soft2.model.Rental;
import soft2.persistence.Store;

import java.util.Map;
public class RentalController {
    private static final Logger log = LoggerFactory.getLogger(RentalController.class);
    private static final Logger audit = LoggerFactory.getLogger("AUDIT");

    public Handler startRental() {
        return ctx -> {
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            String userId = String.valueOf(body.get("user_id"));
            String reservationId = String.valueOf(body.get("reservation_id"));
            try {
                MDC.put("user_id", userId);
                Rental r = Store.get().startRental(userId, reservationId);
                audit.info("USER_ACTION: type=RENTAL_START user_id={} resource_id={} ip={}",
                        userId, "rental:" + r.getId(), ip(ctx));
                ctx.status(201).json(Map.of(
                        "rental_id", r.getId(),
                        "bike_id", r.getBikeId(),
                        "start", r.getStart().toString()
                ));
            } catch (Exception e) {
                log.error("Rental start failed reservation_id={} error={}", reservationId, e.getClass().getSimpleName(), e);
                ctx.status(409).json(Map.of("error", e.getMessage()));
            }
        };
    }

    public Handler endRental() {
        return ctx -> {
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            String userId = String.valueOf(body.get("user_id"));
            int bikeId = (int) (body.get("bike_id") instanceof Number n ? n.intValue()
                    : Integer.parseInt(String.valueOf(body.get("bike_id"))));

            long feeCents = 1000L; // keep simple

            try {
                MDC.put("user_id", userId);
                Rental r = Store.get().endRental(userId, bikeId, feeCents);
                audit.info("USER_ACTION: type=RENTAL_END user_id={} resource_id={} duration_s={} fees_charged_cents={} ip={}",
                        userId, "rental:" + r.getId(), r.durationSeconds(), r.getFeeCents(), ip(ctx));
                ctx.json(Map.of(
                        "rental_id", r.getId(),
                        "duration_s", r.durationSeconds(),
                        "fees_charged_cents", r.getFeeCents()
                ));
            } catch (Exception e) {
                log.error("Rental end failed bike_id={} error={}", bikeId, e.getClass().getSimpleName(), e);
                ctx.status(409).json(Map.of("error", e.getMessage()));
            }
        };
    }

    private static String ip(io.javalin.http.Context ctx) {
        String fwd = ctx.header("X-Forwarded-For");
        return (fwd != null) ? fwd : ctx.req().getRemoteAddr();
    }
}