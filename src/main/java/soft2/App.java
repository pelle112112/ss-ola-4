package soft2;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import soft2.routing.Routes;

import java.util.UUID;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        int port = 7000;

        Javalin app = Javalin.create(cfg -> {
            cfg.staticFiles.add(s -> {
                s.hostedPath = "/";
                s.directory = "/public";
                s.location = Location.CLASSPATH;
            });
        });

        // BEFORE: start timer + MDC
        app.before(ctx -> {
            ctx.attribute("startNanos", System.nanoTime());

            String cid = ctx.header("X-Correlation-Id");
            if (cid == null || cid.isBlank()) cid = UUID.randomUUID().toString();
            MDC.put("correlation_id", cid);

            String uid = ctx.header("X-User-Id");
            if (uid != null && !uid.isBlank()) MDC.put("user_id", uid);
        });

        // AFTER: request summary
        app.after(ctx -> {
            Long start = ctx.attribute("startNanos");
            long elapsedMs = (start != null) ? (System.nanoTime() - start) / 1_000_000 : -1;
            log.info("Request handled method={} path={} status={} elapsed_ms={}",
                    ctx.method(), ctx.path(), ctx.status(), elapsedMs);
            MDC.clear();
        });

        app.events(e -> {
            e.serverStarted(() -> log.info("Application startup"));
            e.serverStopped(() -> log.info("Application shutdown"));
        });

        Routes.register(app);
        app.start(port);
        System.out.println("Server running on http://localhost:" + port + "/");
    }
}
