package soft2.controllers;

import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import soft2.model.Bike;
import soft2.model.User;
import soft2.persistence.Store;

import java.util.Map;

public class AdminController {
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    private static final Logger audit = LoggerFactory.getLogger("AUDIT");

    public Handler addBike() {
        return ctx -> {
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            String adminId = String.valueOf(body.get("admin_id"));
            String type = body.get("type") == null ? "City" : String.valueOf(body.get("type"));

            var u = Store.get().getUser(adminId);
            if (u == null || !u.isAdmin()) {
                ctx.status(403).json(Map.of("error", "forbidden"));
                return;
            }
            try {
                MDC.put("user_id", adminId);
                Bike b = Store.get().addBike(type);
                audit.info("USER_ACTION: type=ADMIN_INVENTORY_UPDATE admin_id={} action=ADD resource_id={} ip={}",
                        adminId, "bike:" + b.getId(), ip(ctx));
                ctx.status(201).json(b);
            } catch (Exception e) {
                log.error("Admin add bike failed error={}", e.getClass().getSimpleName(), e);
                ctx.status(409).json(Map.of("error", e.getMessage()));
            }
        };
    }

    public Handler removeBike() {
        return ctx -> {
            int bikeId = Integer.parseInt(ctx.pathParam("id"));
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            String adminId = String.valueOf(body.get("admin_id"));

            var u = Store.get().getUser(adminId);
            if (u == null || !u.isAdmin()) {
                ctx.status(403).json(Map.of("error", "forbidden"));
                return;
            }
            try {
                MDC.put("user_id", adminId);
                Store.get().removeBike(bikeId);
                audit.info("USER_ACTION: type=ADMIN_INVENTORY_UPDATE admin_id={} action=REMOVE resource_id={} ip={}",
                        adminId, "bike:" + bikeId, ip(ctx));
                ctx.json(Map.of("ok", true));
            } catch (Exception e) {
                log.error("Admin remove bike failed bike_id={} error={}", bikeId, e.getClass().getSimpleName(), e);
                ctx.status(409).json(Map.of("error", e.getMessage()));
            }
        };
    }

    private static String ip(io.javalin.http.Context ctx) {
        String fwd = ctx.header("X-Forwarded-For");
        return (fwd != null) ? fwd : ctx.req().getRemoteAddr();
    }
}