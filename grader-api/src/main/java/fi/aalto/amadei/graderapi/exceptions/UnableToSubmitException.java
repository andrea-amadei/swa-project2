package fi.aalto.amadei.graderapi.exceptions;

public class UnableToSubmitException extends RuntimeException {
    public UnableToSubmitException() {
        super();
    }

    public UnableToSubmitException(String message) {
        super(message);
    }

    public UnableToSubmitException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableToSubmitException(Throwable cause) {
        super(cause);
    }
}
