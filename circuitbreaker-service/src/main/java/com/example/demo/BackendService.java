package com.example.demo;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BackendService {
    private static final int FAILURE_THRESHOLD = 3;
    private static final long OPEN_TIMEOUT_MS = 10000;

    private final AtomicInteger failureCount = new AtomicInteger(0);
    private Instant circuitOpenedAt = null;
    private final Random random = new Random();

    public String fetchData() {
        if (isCircuitOpen()) {
            return "Circuit is open. Request blocked.";
        }

        try {
            if (random.nextBoolean()) {
                throw new RuntimeException("Simulated failure");
            }
            resetCircuit();
            return "Success response";
        } catch (Exception e) {
            if (failureCount.incrementAndGet() >= FAILURE_THRESHOLD) {
                circuitOpenedAt = Instant.now();
            }
            return "Fallback response due to error: " + e.getMessage();
        }
    }

    private boolean isCircuitOpen() {
        if (circuitOpenedAt == null) return false;
        long elapsed = Instant.now().toEpochMilli() - circuitOpenedAt.toEpochMilli();
        if (elapsed > OPEN_TIMEOUT_MS) {
            circuitOpenedAt = null;
            failureCount.set(0);
            return false;
        }
        return true;
    }

    private void resetCircuit() {
        failureCount.set(0);
        circuitOpenedAt = null;
    }
}
