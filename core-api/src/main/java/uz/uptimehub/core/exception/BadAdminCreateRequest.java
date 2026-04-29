package uz.uptimehub.core.exception;

public class BadAdminCreateRequest extends RuntimeException {
    public BadAdminCreateRequest(String message) {
        super(message);
    }
}
