package soft2;

import io.javalin.Javalin;
import soft2.routing.Routes;

public class App {
    public static void main(String[] args) {
        int port = 7000;
        Javalin app = Javalin.create();
        ServerConfig.startJavalinServer(app, port);
        Routes.register(app);
        app.start(port);
        System.out.println("Server running on http://localhost:" + port + "/api");
    }
}
