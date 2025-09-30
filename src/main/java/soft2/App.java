package soft2;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import soft2.routing.Routes;

public class App {
    public static void main(String[] args) {
        int port = 7000;

        Javalin app = Javalin.create(cfg -> {
            cfg.staticFiles.add(s -> {
                s.hostedPath = "/";
                s.directory = "/public";
                s.location = Location.CLASSPATH;
            });
        });

        Routes.register(app);

        app.start(port);
        System.out.println("Server running on http://localhost:" + port + "/");
    }
}
