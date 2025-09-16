package exception;

/**
 * Represents a custom exception specific to the Rotom chatbot.
 * This exception is thrown when an error occurs in command processing,
 * task handling, or storage operations.
 */
public class RotomException extends Exception {
    /**
     * Constructs a {@code RotomException} with the specified detail message.
     * @param message Description of the error.
     */
    public RotomException(String message) {
        super(message);
    }
}
