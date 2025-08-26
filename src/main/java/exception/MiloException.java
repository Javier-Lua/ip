package exception;

/**
 * Represents a custom exception specific to the Milo chatbot.
 * This exception is thrown when an error occurs in command processing,
 * task handling, or storage operations.
 */
public class MiloException extends Exception {
    /**
     * Constructs a {@code MiloException} with the specified detail message.
     * @param message
     */
    public MiloException(String message) {
        super(message);
    }
}
