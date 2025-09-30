package soft2.model;

import java.time.Instant;

public class Reservation {
    private final String id;
    private final int bikeId;
    private final String userId;
    private final Instant reservedAt;
    private final boolean active;

    public Reservation(String id, int bikeId, String userId, Instant reservedAt, boolean active) {
        this.id = id;
        this.bikeId = bikeId;
        this.userId = userId;
        this.reservedAt = reservedAt;
        this.active = active;
    }

    public String getId() { return id; }
    public int getBikeId() { return bikeId; }
    public String getUserId() { return userId; }
    public Instant getReservedAt() { return reservedAt; }
    public boolean isActive() { return active; }

    public String id() { return id; }
    public int bikeId() { return bikeId; }
    public String userId() { return userId; }
    public Instant reservedAt() { return reservedAt; }
    public boolean active() { return active; }

    public Reservation deactivate() {
        return new Reservation(id, bikeId, userId, reservedAt, false);
    }
}
