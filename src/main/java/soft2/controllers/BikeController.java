package soft2.controllers;

import io.javalin.http.Handler;
import soft2.model.Bike;
import soft2.model.Rental;
import soft2.persistence.BikeDAO;

import java.util.Map;

public class BikeController {

    public Handler getAllBikes() {
        return ctx -> ctx.json(BikeDAO.getInstance().getAllBikes());
    }

    public Handler reserveBike() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            BikeDAO dao = BikeDAO.getInstance();
            boolean ok = dao.reserveBike(id);
            if (!ok) {
                ctx.status(409).json(Map.of("error", "Bike not found or already reserved"));
            } else {
                ctx.json(Map.of("reserved", true, "bikeId", id));
            }
        };
    }

    public Handler startRental() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            BikeDAO dao  = BikeDAO.getInstance();
            Bike bike = dao.shop().findBikeById(id);
            if (bike == null) { ctx.status(404).json(Map.of("error", "Bike not found")); return; }

            if (!dao.shop().reserveBike(id)) {
                ctx.status(409).json(Map.of("error", "Bike already reserved"));
                return;
            }

            Integer price = dao.pricing().getCurrentPrice(bike.getType());
            Rental rental = dao.startRental(id, price);

            ctx.json(Map.of(
                    "bikeId", id,
                    "type", bike.getType(),
                    "startPrice", price,
                    "startedAt", rental.getStartedAt().toString()
            ));
        };
    }

    public Handler returnBike() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            BikeDAO dao = BikeDAO.getInstance();
            Rental rental = dao.endRental(id);
            if (rental == null) {
                ctx.status(409).json(Map.of("error", "No active rental for this bike"));
                return;
            }

            dao.shop().returnBike(id);
            ctx.json(Map.of(
                    "bikeId", id,
                    "startPrice", rental.getStartPrice(),
                    "startedAt", rental.getStartedAt().toString(),
                    "endedAt", rental.getEndedAt().toString()
            ));
        };
    }

    public Handler getCurrentPrices() {
        return ctx -> ctx.json(BikeDAO.getInstance().pricing().currentAsMap());
    }

    public Handler changePrice() {
        return ctx -> {
            //
        };
    }

    public Handler addBike() {
        return ctx -> {
            //
        };
    }

    public Handler removeBike() {
        return ctx -> {
            //
        };
    }



}
