package fi.aalto.amadei.graderapi.beans.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@JsonPropertyOrder({ "timestamp", "status", "error", "message" })
public class ErrorBean {
    private final Instant timestamp;
    private final int status;
    private final String error;
    private final String message;

    public ErrorBean(HttpStatus status, String message) {
        this.timestamp = Instant.now();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
