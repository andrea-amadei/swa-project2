package fi.aalto.amadei.graderapi.grading;

import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.Future;

public class ExecutingSubmission {
    private final Future<?> future;
    private final Instant startTime;

    public ExecutingSubmission(Future<?> future) {
        Objects.requireNonNull(future);
        this.future = future;

        this.startTime = Instant.now();
    }

    public Future<?> getFuture() {
        return future;
    }

    public Instant getStartTime() {
        return startTime;
    }
}
