package uz.uptimehub.coreapi.exception;

public class InvalidSortRule extends RuntimeException {
    public InvalidSortRule() {
        super("Invalid sort rule provided.");
    }
}
