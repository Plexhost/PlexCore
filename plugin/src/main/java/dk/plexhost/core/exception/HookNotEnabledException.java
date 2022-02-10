package dk.plexhost.core.exception;

public final class HookNotEnabledException extends RuntimeException {
    public HookNotEnabledException(String message) {
        super(message);
    }
}