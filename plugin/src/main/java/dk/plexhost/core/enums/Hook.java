package dk.plexhost.core.enums;

public enum Hook {
    VAULT(false),
    PLACEHOLDERAPI(false),
    ACTIONBAR(true),
    ESSENTIALS(false)
    ;

    private final boolean isBuiltIn;
    Hook(boolean paramBoolean) {
        this.isBuiltIn = paramBoolean;
    }

    public boolean isBuiltIn() {
        return isBuiltIn;
    }
}
