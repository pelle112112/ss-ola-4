package soft2.model;

import java.time.Duration;
import java.time.Instant;

public class Rental {
    private final String id;
    private final String userId;
    private final int bikeId;
    private final Instant start;
    private Instant end;
    private long feeCents;

    public Rental(String id, String userId, int bikeId) {
        this.id = id;
        this.userId = userId;
        this.bikeId = bikeId;
        this.start = Instant.now();
    }

    public void end(long feeCents) {
        this.end = Instant.now();
        this.feeCents = feeCents;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public int getBikeId() { return bikeId; }
    public Instant getStart() { return start; }
    public Instant getEnd() { return end; }
    public long getFeeCents() { return feeCents; }


    public String id(){ return id; }
    public String userId(){ return userId; }
    public int bikeId(){ return bikeId; }
    public Instant start(){ return start; }
    public Instant endTime(){ return end; }
    public long feeCents(){ return feeCents; }

    public long durationSeconds() {
        Instant e = end != null ? end : Instant.now();
        return Duration.between(start, e).toSeconds();
    }
}
