package fi.aalto.amadei.graderapi.beans.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import fi.aalto.amadei.graderapi.beans.UserSessionBean;

import java.time.Instant;

@JsonPropertyOrder({ "timestamp", "session", "response" })
public class SuccessBean<T> {
    private final UserSessionBean session;
    private final Instant timestamp;
    private final T response;

    public SuccessBean(UserSessionBean session, T response) {
        this.session = session;
        this.timestamp = Instant.now();
        this.response = response;
    }

    public UserSessionBean getSession() {
        return session;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public T getResponse() {
        return response;
    }
}
