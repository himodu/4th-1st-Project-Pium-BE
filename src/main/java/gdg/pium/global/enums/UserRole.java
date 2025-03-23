package gdg.pium.global.enums;

public enum UserRole {
    USER("USER"),
    ADMIN("ADMIN");

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }
}
