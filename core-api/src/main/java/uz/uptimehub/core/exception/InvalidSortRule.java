package uz.uptimehub.core.exception;

public class InvalidSortRule extends RuntimeException {
    public InvalidSortRule() {
        super("Invalid sort rule provided.");
    }
}
