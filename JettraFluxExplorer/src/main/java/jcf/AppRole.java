package jcf;

public enum AppRole {
    ADMIN,
    MANAGER,
    USER,
    OPERATOR,
    ANALYST;

    public String getValue() {
        return name();
    }
}
