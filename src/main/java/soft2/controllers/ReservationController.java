package soft2.controllers;

import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import soft2.model.Reservation;
import soft2.persistence.Store;

import java.util.Map;

public class ReservationController {
    private static final Logger log = LoggerFactory.getLogger(ReservationController.class);
    private static final Logger audit = LoggerFactory.getLogger("AUDIT");

    public Handler reserveBike() {
        return ctx -> {
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            String userId = String.valueOf(body.get("user_id"));
            int bikeId = (int) (body.get("bike_id") instanceof Number n ? n.intValue()
                    : Integer.parseInt(String.valueOf(body.get("bike_id"))));

            try {
                // simulate an external verification call for each reservation
                simulateVerification(log);

                MDC.put("user_id", userId);
                Reservation res = Store.get().createReservation(userId, bikeId);

                audit.info("USER_ACTION: type=RESERVATION_CREATE user_id={} resource_id={} ip={}",
                        userId, "bike:" + bikeId, ip(ctx));

                ctx.status(201).json(res);
            } catch (Exception e) {
                log.error("Reservation failed bike_id={} error={}", bikeId, e.getClass().getSimpleName(), e);
                ctx.status(409).json(Map.of("error", e.getMessage()));
            }
        };
    }

    private static String ip(io.javalin.http.Context ctx) {
        String fwd = ctx.header("X-Forwarded-For");
        return (fwd != null) ? fwd : ctx.req().getRemoteAddr();
    }

    private static void simulateVerification(Logger log) {
        long start = System.currentTimeMillis();
        try {
            // simulate 100–1200ms latency
            long delay = 100 + (long) (Math.random() * 1100);
            Thread.sleep(delay);
            boolean fallback = delay > 800;
            if (fallback) {
                log.warn("Verification slow elapsed_ms={} using_fallback=true", delay);
            } else {
                log.info("Verification ok elapsed_ms={}", delay);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Verification interrupted", e);
        }
    }
}
