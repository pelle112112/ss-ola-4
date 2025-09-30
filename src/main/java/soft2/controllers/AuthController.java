package soft2.controllers;

import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import soft2.model.User;
import soft2.persistence.Store;

import java.util.Map;

public class AuthController {
    private static final Logger audit = LoggerFactory.getLogger("AUDIT");

    public Handler login() {
        return ctx -> {
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            String userId = String.valueOf(body.get("user_id"));
            String password = String.valueOf(body.get("password"));

            var u = Store.get().getUser(userId);
            boolean ok = (u != null && u.getPasswordHash().equals(password));

            if (ok) {
                MDC.put("user_id", userId);
                audit.info("USER_ACTION: type=LOGIN_SUCCESS ip={}", ip(ctx));
                ctx.json(Map.of("ok", true, "user_id", u.getId(), "admin", u.isAdmin()));
            } else {
                audit.info("USER_ACTION: type=LOGIN_FAILURE ip={}", ip(ctx));
                ctx.status(401).json(Map.of("ok", false, "error", "invalid_credentials"));
            }
        };
    }

    private static String ip(io.javalin.http.Context ctx) {
        String fwd = ctx.header("X-Forwarded-For");
        return (fwd != null) ? fwd : ctx.req().getRemoteAddr();
    }
}