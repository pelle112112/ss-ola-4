package soft2.model;

import java.time.Instant;

public class Rental {
    private final int bikeId;
    private final int startPrice;
    private final Instant startedAt;
    private Instant endedAt;

    public Rental(int bikeId, int startPrice) {
        this.bikeId = bikeId;
        this.startPrice = startPrice;
        this.startedAt = Instant.now();
    }

    public int getBikeId() { return bikeId; }
    public int getStartPrice() { return startPrice; }
    public Instant getStartedAt() { return startedAt; }
    public Instant getEndedAt() { return endedAt; }
    public void end() { this.endedAt = Instant.now(); }
}
