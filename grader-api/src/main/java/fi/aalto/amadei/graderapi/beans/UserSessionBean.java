package fi.aalto.amadei.graderapi.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

public class UserSessionBean {
    @JsonProperty("session_token")
    private String sessionID;

    @JsonProperty("created_at")
    private Instant creationTime;

    @JsonProperty("expires_at")
    private Instant expirationTime;

    @JsonProperty("new_session")
    private boolean newSession;

    @JsonIgnore
    private boolean superUser;

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Instant creationTime) {
        this.creationTime = creationTime;
    }

    public Instant getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Instant expirationTime) {
        this.expirationTime = expirationTime;
    }

    public boolean getNewSession() {
        return newSession;
    }

    public void setNewSession(boolean newSession) {
        this.newSession = newSession;
    }

    public boolean isNewSession() {
        return newSession;
    }

    public boolean getSuperUser() {
        return superUser;
    }

    public void setSuperUser(boolean superUser) {
        this.superUser = superUser;
    }

    public static UserSessionBean getSessionFromRequest(HttpServletRequest request) {
        return (UserSessionBean) request.getAttribute("session");
    }

    public static UserSessionBean getSessionFromRequest(ServletRequest request) {
        return getSessionFromRequest((HttpServletRequest) request);
    }
}
