package fi.aalto.amadei.graderapi.exceptions;

public class InvalidUserSessionException extends RuntimeException {
    public InvalidUserSessionException() {
    }

    public InvalidUserSessionException(String message) {
        super(message);
    }

    public InvalidUserSessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidUserSessionException(Throwable cause) {
        super(cause);
    }
}
