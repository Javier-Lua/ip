package command;

import java.util.Stack;

/**
 * Manages the history of executed commands.
 */
public class CommandHistory {
    private final Stack<Command> history = new Stack<>();

    /**
     * Pushes a command into the history stack.
     * @param command The command to push.
     */
    public void push(Command command) {
        history.push(command);
    }

    /**
     * Pops the last command from the history stack.
     * @return The last command or null if empty.
     */
    public Command pop() {
        if (history.isEmpty()) {
            return null;
        }
        return history.pop();
    }

    /**
     * Checks if the history is empty.
     * @return True if empty, false otherwise.
     */
    public boolean isEmpty() {
        return history.isEmpty();
    }
}
