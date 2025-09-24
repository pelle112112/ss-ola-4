package soft2;

import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.bundled.RouteOverviewPlugin;

public class ServerConfig {

    public static void startJavalinServer(Javalin app, int port){
        app.updateConfig(ServerConfig::javalinConfiguration);
        // Routing

    }

    private static void javalinConfiguration(JavalinConfig config){
        config.routing.contextPath = "/api";
        config.http.defaultContentType = "application/json";
        config.plugins.register(new RouteOverviewPlugin("/"));
    }
}
