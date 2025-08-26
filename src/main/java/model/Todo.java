package model;

public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String[] getFileInput() {
        String[] s = super.getFileInput();
        s[0] = "T";
        return s;
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
