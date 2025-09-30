package soft2.controllers;

import io.javalin.http.Handler;
import soft2.model.User;
import soft2.persistence.Store;

import java.util.Map;

public class AuthController {

    public Handler login() {
        return ctx -> {
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            String userId = String.valueOf(body.get("user_id"));
            String password = String.valueOf(body.get("password"));

            User user = Store.get().getUser(userId);
            boolean ok = (user != null && user.getPasswordHash().equals(password));

            if (ok) {
                ctx.json(Map.of("ok", true, "user_id", user.getId(), "admin", user.isAdmin()));
            } else {
                ctx.status(401).json(Map.of("ok", false, "error", "invalid_credentials"));
            }
        };
    }
}
