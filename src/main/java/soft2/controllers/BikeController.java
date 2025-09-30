package soft2.controllers;

import io.javalin.http.Handler;
import soft2.persistence.Store;

public class BikeController {
    public Handler getAllBikes() {
        return ctx -> {
            ctx.json(Store.get().listBikes());
        };
    }
}
