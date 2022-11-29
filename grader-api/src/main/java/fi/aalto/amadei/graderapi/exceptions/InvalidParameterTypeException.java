package fi.aalto.amadei.graderapi.exceptions;

public class InvalidParameterTypeException extends RuntimeException {
    public InvalidParameterTypeException() {
        super();
    }

    public InvalidParameterTypeException(String message) {
        super(message);
    }

    public InvalidParameterTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidParameterTypeException(Throwable cause) {
        super(cause);
    }
}
