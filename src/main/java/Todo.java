public class Todo extends Task {
    private boolean isDone;

    public Todo(String description) {
        super(description);
        isDone = false;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public boolean isDone() {
        return isDone;
    }
    
    public String toString() {
        String status = "";
        if (isDone) {
            status = "Yes";
        } else {
            status = "No";
        }
        return super.toString() + System.lineSeparator() + "is done? " + status;
    }
}