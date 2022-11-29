package fi.aalto.amadei.graderapi.exceptions;

public class InvalidDatabaseInteractionException extends RuntimeException {
    public InvalidDatabaseInteractionException() {
        super();
    }

    public InvalidDatabaseInteractionException(String message) {
        super(message);
    }

    public InvalidDatabaseInteractionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDatabaseInteractionException(Throwable cause) {
        super(cause);
    }
}
