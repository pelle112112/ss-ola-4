package soft2.model;

import java.util.HashMap;
import java.util.Map;

public class PricingService {
    private final Map<String, Integer> current = new HashMap<>();

    public PricingService(Map<String, Integer> amount) {
        current.putAll(amount);
    }

    public Integer getCurrentPrice(String type) {
        return current.get(type);
    }

    public Map<String,Integer> currentAsMap() {
        return Map.copyOf(current);
    }
}
